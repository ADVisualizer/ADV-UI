package ch.adv.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RootViewModel {

    private ObservableList<String> availableSessions;

    private final SessionStore sessionStore;
    private static final Logger logger = LoggerFactory.getLogger(RootViewModel.class);

    @Inject
    public  RootViewModel(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
        this.availableSessions = FXCollections.observableArrayList();

        sessionStore.addPropertyChangeListener(new SessionPropertyChangeListener());
    }

    public ObservableList<String> getAvailableSessions() {
        return availableSessions;
    }

    private class SessionPropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("SessionStore has updated. Update ListView");

            List<String> availableSessionNames = sessionStore.getSessions().stream().map(s -> {
                String time =  String.format("%tT", s.getSessionId() - TimeZone.getDefault().getRawOffset());
                return s.getSessionName() + " - " + time;
            }).collect(Collectors.toList());

            Platform.runLater(() -> {
                availableSessions.clear();
                availableSessions.addAll(availableSessionNames);
            });
        }
    }
}
