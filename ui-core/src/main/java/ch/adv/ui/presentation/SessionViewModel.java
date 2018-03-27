package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.model.Snapshot;
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

/**
 * Handles presentation logic for the {@link SessionView}. Delegates tasks to
 * the business logic layer.
 */
public class SessionViewModel {

    private Session session;

    private final ObservableList<Pane> availableSnapshotPanes;
    private final ObjectProperty<Pane> currentSnapshotPaneProperty;
    private final ObjectProperty<String> currentSnapshotDescriptionProperty;
    private final SnapshotStore snapshotStore;


    private static final Logger logger = LoggerFactory.getLogger(
            SessionViewModel.class);

    @Inject
    public SessionViewModel(final RootViewModel rootViewModel, SnapshotStore
            snapshotStore) {
        logger.debug("init sessionViewModel");
        this.availableSnapshotPanes = FXCollections.observableArrayList();
        this.currentSnapshotPaneProperty = new SimpleObjectProperty<>();
        this.currentSnapshotDescriptionProperty = new SimpleObjectProperty<>();
        this.snapshotStore = snapshotStore;
        this.session = rootViewModel.currentSessionProperty().get();

        snapshotStore.addPropertyChangeListener(session.getSessionId(), new
                SnapshotPropertyChangeListener());

        this.availableSnapshotPanes.addAll(snapshotStore.getSnapshotPanes(
                session.getSessionId()));
        this.currentSnapshotPaneProperty.set(availableSnapshotPanes.get(0));
        String snapshotDescription = snapshotStore.getSnapshots(session
                .getSessionId()).get(0).getSnapshotDescription();
        this.currentSnapshotDescriptionProperty.set(snapshotDescription);
    }

    public ObservableList<Pane> getAvailableSnapshotPanes() {
        return availableSnapshotPanes;
    }

    public ObjectProperty<Pane> currentSnapshotPaneProperty() {
        return currentSnapshotPaneProperty;
    }

    public ObjectProperty<String> currentSnapshotDescriptionProperty() {
        return currentSnapshotDescriptionProperty;
    }

    public void setSession(final Session session) {
        this.session = session;
    }

    private class SnapshotPropertyChangeListener implements
            PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            Pane newSnapshot = (Pane) event.getNewValue();

            logger.debug("SnapshotStore for session {} has updated. Add {} to"
                            + " available snapshots", session.getSessionId(),
                    newSnapshot);


            Snapshot s = snapshotStore.getNewestSnapshot(
                    session.getSessionId());

            Platform.runLater(() -> {
                availableSnapshotPanes.add(newSnapshot);
                currentSnapshotDescriptionProperty.set(s
                        .getSnapshotDescription());
            });
        }
    }
}
