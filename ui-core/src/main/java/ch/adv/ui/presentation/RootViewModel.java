package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.SessionStore;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class RootViewModel {

    private ObservableList<Session> availableSessions;

    private final SessionStore sessionStore;
    private static final Logger logger = LoggerFactory.getLogger
            (RootViewModel.class);

    @Inject
    public RootViewModel(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
        this.availableSessions = FXCollections.observableArrayList();

        sessionStore.addPropertyChangeListener(new
                SessionPropertyChangeListener());
    }

    public ObservableList<Session> getAvailableSessions() {
        return availableSessions;
    }

    public void deleteSession(Session session) {
        sessionStore.deleteSession(session);
    }

    private class SessionPropertyChangeListener implements
            PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("SessionStore has updated. Update ListView");

            List<Session> sessions = sessionStore.getSessions();

            Platform.runLater(() -> {
                availableSessions.clear();
                availableSessions.addAll(sessions);
            });
        }
    }
}
