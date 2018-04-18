package ch.adv.ui.core.presentation;

import ch.adv.ui.core.access.DatastoreAccess;
import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.domain.Session;
import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import ch.adv.ui.core.logic.SessionStore;
import ch.adv.ui.core.service.ADVFlowControl;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles presentation logic for the {@link RootView}. Delegates tasks to
 * the business logic layer.
 */
@Singleton
public class RootViewModel {

    private static final Logger logger = LoggerFactory.getLogger(
            RootViewModel.class);

    private final ObservableList<Session> availableSessions = FXCollections
            .observableArrayList();
    private final ObjectProperty<Session> currentSessionProperty = new
            SimpleObjectProperty<>();
    private final BooleanProperty noSessionsProperty = new
            SimpleBooleanProperty(true);
    private final DatastoreAccess fileAccess;
    private final SessionStore sessionStore;
    private final ADVFlowControl flowControl;
    private final LayoutedSnapshotStore layoutedSnapshotStore;

    @Inject
    public RootViewModel(SessionStore sessionStore, ADVFlowControl flowControl,
                         FileDatastoreAccess fileAccess,
                         LayoutedSnapshotStore layoutedSnapshotStore,
                         EventManager eventManager) {

        this.sessionStore = sessionStore;
        this.flowControl = flowControl;
        this.fileAccess = fileAccess;
        this.layoutedSnapshotStore = layoutedSnapshotStore;

        eventManager.subscribe(new SessionStoreListener(), List.of(ADVEvent
                .SESSION_ADDED, ADVEvent.SESSION_REMOVED)
        );
    }

    public ObservableList<Session> getAvailableSessions() {
        return availableSessions;
    }


    public ObjectProperty<Session> getCurrentSessionProperty() {
        return currentSessionProperty;
    }

    public BooleanProperty getNoSessionsProperty() {
        return noSessionsProperty;
    }

    /**
     * Delegates removing current session to the business logic
     */
    public void removeCurrentSession() {
        Session current = currentSessionProperty.get();
        if (current != null) {
            logger.info("Removing session {} ({})", current.getSessionName(),
                    current.getSessionId());
            removeSession(current);
        }
    }

    private void removeSession(Session session) {
        sessionStore.deleteSession(session);
        layoutedSnapshotStore.deleteSession(session.getSessionId());
    }

    /**
     * Delegates removing sessions to the business logic
     */
    public void clearAllSessions() {
        Set<Session> set = new HashSet<>(availableSessions);
        set.forEach(session -> removeSession(session));
    }

    /**
     * Delegates saving sessions to the access layer
     *
     * @param file to be saved to
     */
    public void saveSession(final File file) {
        Session session = currentSessionProperty.get();
        if (session != null) {
            layoutedSnapshotStore.getLayoutedSnapshots(session.getSessionId())
                    .forEach(element -> {
                        String description = element.getSnapshotDescription();
                        long id = element.getSnapshotId();
                        session.getSnapshotById(id)
                                .setSnapshotDescription(description);
                    });
            String json = session.getModule().getStringifyer()
                    .stringify(session);
            fileAccess.write(file, json);
        }
    }

    /**
     * Loads a existing session from filesystem.
     *
     * @param file file to open
     */
    public void loadSession(File file) {
        String json = fileAccess.read(file);
        flowControl.process(json);
    }


    /**
     * Update ui if session store has changed.
     */
    private class SessionStoreListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            if (event.getPropertyName().equals(ADVEvent
                    .SESSION_ADDED.toString())) {
                logger.debug("SessionStore has updated: Session was added. "
                        + "Update ListView");
                Platform.runLater(() -> {
                    // current needs to be set first! Otherwise NullPointer when
                    // creating SessionView
                    Session newSession = (Session) event.getNewValue();
                    currentSessionProperty.setValue(newSession);
                    availableSessions.add(newSession);
                    if (noSessionsProperty.get()){
                        noSessionsProperty.set(false);
                    }
                });
            } else {
                logger.debug("SessionStore has updated: Session was removed. "
                        + "Update ListView");
                Platform.runLater(() -> {
                    Session removedSession = (Session) event.getOldValue();
                    availableSessions.remove(removedSession);
                    if (availableSessions.isEmpty()) {
                        currentSessionProperty.setValue(null);
                        noSessionsProperty.set(true);
                    } else {
                        int size = availableSessions.size();
                        currentSessionProperty
                                .setValue(availableSessions.get(size - 1));
                    }
                });
            }

        }
    }
}
