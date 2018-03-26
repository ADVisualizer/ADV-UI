package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Session;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SessionViewModel {

    private Session session;

    private final ObservableList<Pane> availableSnapshotPanes;
    private final ObjectProperty<Pane> currentSnapshotPane;
    private final SnapshotStore snapshotStore;


    private static final Logger logger = LoggerFactory.getLogger
            (SessionViewModel.class);

    @Inject
    public SessionViewModel(RootViewModel rootViewModel, SnapshotStore
            snapshotStore) {
        logger.debug("init sessionViewModel");
        this.availableSnapshotPanes = FXCollections.observableArrayList();
        this.currentSnapshotPane = new SimpleObjectProperty<>();
        this.snapshotStore = snapshotStore;
        this.session = rootViewModel.currentSessionProperty().get();

        snapshotStore.addPropertyChangeListener(session.getSessionId(), new
                SnapshotPropertyChangeListener());

        this.availableSnapshotPanes.addAll(snapshotStore.getSnapshotPanes
                (session.getSessionId()));
        this.currentSnapshotPane.set(availableSnapshotPanes.get(0));
    }

    public ObservableList<Pane> getAvailableSnapshotPanes() {
        return availableSnapshotPanes;
    }

    public ObjectProperty<Pane> currentSnapshotPaneProperty() {
        return currentSnapshotPane;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private class SnapshotPropertyChangeListener implements
            PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            Pane newSnapshot = (Pane) event.getNewValue();

            logger.debug("SnapshotStore for session {} has updated. Add {} to" +
                            " available snapshots", session.getSessionId(),
                    newSnapshot);

            Platform.runLater(() -> {
                availableSnapshotPanes.add(newSnapshot);
            });
        }
    }
}
