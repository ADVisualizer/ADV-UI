package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.access.DatastoreAccess;
import ch.hsr.adv.ui.core.logic.*;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.presentation.util.I18n;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles presentation logic for the {@link RootView}. Delegates tasks to
 * the business logic layer.
 */
@Singleton
class RootViewModel {

    private static final int NOTIFICATION_FADE_DELAY = 3;
    private static final Logger logger = LoggerFactory.getLogger(
            RootViewModel.class);
    private final ObservableList<Session> availableSessions = FXCollections
            .observableArrayList();
    private final ObjectProperty<Session> currentSessionProperty = new
            SimpleObjectProperty<>();
    private final BooleanProperty noSessionsProperty = new
            SimpleBooleanProperty(true);
    private final StringProperty notificationMessageProperty = new
            SimpleStringProperty("");

    private final CoreStringifyer coreStringifyer;
    private final ServiceProvider serviceProvider;
    private final DatastoreAccess fileAccess;
    private final SessionStore sessionStore;
    private final FlowControl flowControl;
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private final ScheduledExecutorService notificationResetTimer =
            Executors.newScheduledThreadPool(4);

    private final TimerTask resetTask = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> notificationMessageProperty.set(""));
        }
    };


    @Inject
    RootViewModel(SessionStore sessionStore,
                  FlowControl flowControl,
                  DatastoreAccess fileAccess,
                  LayoutedSnapshotStore layoutedSnapshotStore,
                  EventManager eventManager,
                  ServiceProvider serviceProvider,
                  CoreStringifyer coreStringifyer1) {

        this.sessionStore = sessionStore;
        this.flowControl = flowControl;
        this.fileAccess = fileAccess;
        this.serviceProvider = serviceProvider;
        this.layoutedSnapshotStore = layoutedSnapshotStore;
        this.coreStringifyer = coreStringifyer1;

        eventManager.subscribe(new SessionStoreListener(),
                List.of(ADVEvent.SESSION_ADDED)
        );

        eventManager.subscribe((e) -> {
            String i18nKey = (String) e.getNewValue();
            showNotification(i18nKey);
        }, ADVEvent.NOTIFICATION);
    }

    ObservableList<Session> getAvailableSessions() {
        return availableSessions;
    }

    ObjectProperty<Session> getCurrentSessionProperty() {
        return currentSessionProperty;
    }

    BooleanProperty getNoSessionsProperty() {
        return noSessionsProperty;
    }

    StringProperty getNotificationMessageProperty() {
        return notificationMessageProperty;
    }

    /**
     * Delegates removing current session to the business logic
     */
    void removeCurrentSession() {
        Session current = currentSessionProperty.get();
        if (current != null) {
            logger.info("Removing session {} ({})", current.getSessionName(),
                    current.getSessionId());
            removeSession(current);
        }
    }

    private void removeSession(Session session) {
        try {
            sessionStore.delete(session.getSessionId());
            layoutedSnapshotStore.deleteAll(session.getSessionId());
            logger.info("Deleting session {} ({})", session
                    .getSessionName(), session.getSessionId());
            availableSessions.remove(session);
            if (availableSessions.isEmpty()) {
                currentSessionProperty.setValue(null);
                noSessionsProperty.set(true);
            } else {
                int size = availableSessions.size();
                currentSessionProperty
                        .setValue(availableSessions.get(size - 1));
            }
            showNotification(I18n.NOTIFICATION_SESSION_CLOSE_SUCCESSFUL);
        } catch (Exception e) {
            showNotification(I18n.NOTIFICATION_SESSION_CLOSE_UNSUCCESSFUL);
        }
    }

    /**
     * Delegates removing sessions to the business logic
     */
    void clearAllSessions() {
        List<Session> sessionsToRemove = new ArrayList<>(availableSessions);
        sessionsToRemove.forEach(this::removeSession);
        showNotification(I18n.NOTIFICATION_SESSION_CLOSE_ALL);
    }

    /**
     * Delegates saving sessions to the access layer
     *
     * @param file to be saved to
     */
    void saveSession(final File file) {
        try {
            Session session = currentSessionProperty.get();
            if (session != null) {
                layoutedSnapshotStore
                        .getAll(session.getSessionId())
                        .forEach(element -> {
                            String description = element
                                    .getSnapshotDescription();
                            long id = element.getSnapshotId();
                            session.getSnapshotById(id)
                                    .setSnapshotDescription(description);
                        });

                String json = coreStringifyer.stringify(session);
                fileAccess.write(file, json);
            }
            showNotification(I18n.NOTIFICATION_SESSION_SAVE_SUCCESSFUL);
        } catch (IOException e) {
            showNotification(I18n.NOTIFICATION_SESSION_SAVE_UNSUCCESSFUL);
        }
    }

    /**
     * Loads a existing session from filesystem.
     *
     * @param file file to open
     */
    void loadSession(File file) {
        try {
            String json = fileAccess.read(file);
            flowControl.process(json);
        } catch (IOException e) {
            showNotification(I18n.NOTIFICATION_SESSION_LOAD_UNSUCCESSFUL);
        }
    }

    private void showNotification(String i18nKey) {
        Platform.runLater(() -> {
            notificationMessageProperty.set(I18n.get(i18nKey));
            startNotificationResetTimer();
        });
    }

    private void startNotificationResetTimer() {
        notificationResetTimer
                .schedule(resetTask, NOTIFICATION_FADE_DELAY, TimeUnit.SECONDS);
    }

    /**
     * Update ui if session store has changed.
     */
    private class SessionStoreListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            logger.debug("SessionStore has updated: Session was added. "
                    + "Update ListView");
            Platform.runLater(() -> {
                // current needs to be set first! Otherwise NullPointer when
                // creating SessionView
                Session newSession = (Session) event.getNewValue();
                currentSessionProperty.setValue(newSession);
                availableSessions.add(newSession);
                if (noSessionsProperty.get()) {
                    noSessionsProperty.set(false);
                }
            });
        }
    }
}

