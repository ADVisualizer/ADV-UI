package ch.adv.ui.presentation;

import ch.adv.ui.ADVModule;
import ch.adv.ui.access.DatastoreAccess;
import ch.adv.ui.access.FileDatastoreAccess;
import ch.adv.ui.logic.ModuleStore;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.logic.model.Session;
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

@Singleton
public class RootViewModel {

    private final ObservableList<Session> availableSessions;
    private final ObjectProperty<Session> currentSession;
    private final DatastoreAccess fileAccess;
    private final SessionStore sessionStore;
    private final ModuleStore moduleStore;
    private final SnapshotStore snapshotStore;

    private static final Logger logger = LoggerFactory.getLogger
            (RootViewModel.class);

    @Inject
    public RootViewModel(SessionStore sessionStore, ModuleStore moduleStore,
                         FileDatastoreAccess fileAccess, SnapshotStore
                                     snapshotStore) {
        this.sessionStore = sessionStore;
        this.moduleStore = moduleStore;
        this.fileAccess = fileAccess;
        this.snapshotStore = snapshotStore;

        this.availableSessions = FXCollections.observableArrayList();
        this.currentSession = new SimpleObjectProperty<>();

        sessionStore.addPropertyChangeListener(new
                SessionPropertyChangeListener());
    }

    public ObservableList<Session> getAvailableSessions() {
        return availableSessions;
    }

    public ObjectProperty<Session> currentSessionProperty() {
        return currentSession;
    }

    /**
     * Delegates removing sessions to the business logic
     *
     * @param session to be removed
     */
    public void removeSession(final Session session) {
        sessionStore.deleteSession(session);
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

    public void loadSession(File file) {
        String json = fileAccess.read(file);
        ADVModule module = moduleStore.parseModule(json);
        Session loadedSession = module.getParser().parse(json);
        loadedSession.setModule(module);
        sessionStore.addSession(loadedSession, true);

        long sessionId = loadedSession.getSessionId();
        Layouter layouter = module.getLayouter();

        snapshotStore.addSnapshotPane(sessionId, layouter.layout(loadedSession
                .getFirstSnapshot()));
    }

    private class SessionPropertyChangeListener implements
            PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            logger.debug("SessionStore has updated. Update ListView");
            List<Session> sessions = sessionStore.getSessions();

            Platform.runLater(() -> {
                currentSession.setValue(sessionStore.getCurrentSession());
                availableSessions.clear();
                availableSessions.addAll(sessions);
            });
        }
    }
}
