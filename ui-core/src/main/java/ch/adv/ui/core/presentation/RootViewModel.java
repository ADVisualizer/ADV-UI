package ch.adv.ui.core.presentation;

import ch.adv.ui.core.access.DatastoreAccess;
import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.domain.Session;
import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import ch.adv.ui.core.logic.SessionStore;
import ch.adv.ui.core.service.ADVFlowControl;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
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
import java.util.List;

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
    private final DatastoreAccess fileAccess;
    private final SessionStore sessionStore;
    private final ADVFlowControl flowControl;
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private final EventManager eventManager;

    @Inject
    public RootViewModel(SessionStore sessionStore, ADVFlowControl flowControl,
                         FileDatastoreAccess fileAccess,
                         LayoutedSnapshotStore layoutedSnapshotStore,
                         EventManager eventManager) {

        this.sessionStore = sessionStore;
        this.flowControl = flowControl;
        this.fileAccess = fileAccess;
        this.layoutedSnapshotStore = layoutedSnapshotStore;
        this.eventManager = eventManager;

        eventManager.subscribe(new SessionStoreListener(),
                ADVEvent.CURRENT_SESSION_CHANGED);
    }

    public ObservableList<Session> getAvailableSessions() {
        return availableSessions;
    }


    public ObjectProperty<Session> getCurrentSessionPropertyProperty() {
        return currentSessionProperty;
    }

    /**
     * Delegates removing sessions to the business logic
     *
     * @param session to be removed
     */
    public void removeSession(final Session session) {
        sessionStore.deleteSession(session);
        layoutedSnapshotStore.deleteSession(session.getSessionId());
    }

    /**
     * Delegates saving sessions to the business logic
     *
     * @param file    to be saved to
     * @param session to be saved
     */
    public void saveSession(final File file, final Session session) {
        String json = session.getModule().getStringifyer().stringify(session);
        fileAccess.write(file, json);
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
     * Update listener if session store has changed.
     */
    private class SessionStoreListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            logger.debug("SessionStore has updated. Update ListView");
            List<Session> sessions = sessionStore.getSessions();

            Platform.runLater(() -> {
                currentSessionProperty.setValue(sessionStore
                        .getCurrentSession());
                availableSessions.clear();
                availableSessions.addAll(sessions);
            });
        }
    }
}
