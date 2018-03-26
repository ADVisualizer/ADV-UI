package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Session;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class SessionViewModel {

    private final ObservableList<Pane> availableSnapshotPanes;
    private final ObjectProperty<Pane> currentSnapshotPane;
    private Session session;

    private static final Logger logger = LoggerFactory.getLogger
            (SessionViewModel.class);

    @Inject
    public SessionViewModel(RootViewModel rootViewModel, SnapshotStore
            snapshotStore) {
        logger.debug("init sessionViewModel");
        this.availableSnapshotPanes = FXCollections.observableArrayList();
        this.currentSnapshotPane = new SimpleObjectProperty<>();

        this.session = rootViewModel.currentSessionProperty().get();

        //TODO: add proper bindings and changelisteners ect
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
}
