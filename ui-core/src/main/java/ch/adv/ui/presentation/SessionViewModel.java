package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.model.Snapshot;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;

/**
 * Handles presentation logic for the {@link SessionView}. Delegates tasks to
 * the business logic layer.
 */
public class SessionViewModel {

    private static final Logger logger = LoggerFactory.getLogger(
            SessionViewModel.class);
    private static final long HALF_SECOND_MS = 500;

    private final ObservableList<Pane> availableSnapshotPanes;
    private final ObjectProperty<Pane> currentSnapshotPaneProperty;
    private final ObjectProperty<String> currentSnapshotDescriptionProperty;
    private final SnapshotStore snapshotStore;
    private final BooleanProperty stepFirstBtnDisableProperty;
    private final BooleanProperty stepBackwardBtnDisableProperty;
    private final BooleanProperty stepForwardBtnDisableProperty;
    private final BooleanProperty stepLastBtnDisableProperty;
    private final BooleanProperty speedsliderDisableProperty;
    private final BooleanProperty replayingProperty;
    private final SessionReplayFactory sessionReplayFactory;
    private final ReplayController replayController;


    private int currentSnapshotIndex;
    private int maxSnapshotIndex;
    private Session session;
    private SessionReplay currentReplayThread;

    @Inject
    public SessionViewModel(RootViewModel rootViewModel, SnapshotStore
            snapshotStore, SessionReplayFactory sessionReplayFactory,
                            ReplayController replayController) {
        logger.debug("init sessionViewModel");
        this.sessionReplayFactory = sessionReplayFactory;
        this.replayController = replayController;
        this.snapshotStore = snapshotStore;
        this.session = rootViewModel.getCurrentSessionPropertyProperty().get();

        //instantiate property instances
        this.availableSnapshotPanes = FXCollections.observableArrayList();
        this.currentSnapshotPaneProperty = new SimpleObjectProperty<>();
        this.currentSnapshotDescriptionProperty = new SimpleObjectProperty<>();
        this.stepFirstBtnDisableProperty = new SimpleBooleanProperty();
        this.stepBackwardBtnDisableProperty = new SimpleBooleanProperty();
        this.stepForwardBtnDisableProperty = new SimpleBooleanProperty();
        this.stepLastBtnDisableProperty = new SimpleBooleanProperty();
        this.speedsliderDisableProperty = new SimpleBooleanProperty();
        this.replayingProperty = new SimpleBooleanProperty();

        //initialize properties
        snapshotStore.addPropertyChangeListener(session.getSessionId(), new
                SnapshotPropertyChangeListener());

        this.availableSnapshotPanes.addAll(snapshotStore.getSnapshotPanes(
                session.getSessionId()));
        this.currentSnapshotPaneProperty.set(availableSnapshotPanes.get(0));
        String snapshotDescription = snapshotStore.getSnapshots(session
                .getSessionId()).get(0).getSnapshotDescription();
        this.currentSnapshotDescriptionProperty.set(snapshotDescription);

        replayingProperty.addListener((e, oldV, newV) -> {
            updateStepButtonDisabilities();
            if (newV) {
                speedsliderDisableProperty.set(true);
            } else {
                speedsliderDisableProperty.set(false);
            }
        });

    }

    public ObservableList<Pane> getAvailableSnapshotPanes() {
        return availableSnapshotPanes;
    }

    public ObjectProperty<Pane> getCurrentSnapshotPaneProperty() {
        return currentSnapshotPaneProperty;
    }

    public ObjectProperty<String> getCurrentSnapshotDescriptionProperty() {
        return currentSnapshotDescriptionProperty;
    }

    public int getCurrentSnapshotIndex() {
        return currentSnapshotIndex;
    }

    public int getMaxSnapshotIndex() {
        return maxSnapshotIndex;
    }

    public void setSession(final Session session) {
        this.session = session;
    }

    public void navigateSnapshot(Navigate navigate) {
        switch (navigate) {
            case FIRST:
                currentSnapshotIndex = 0;
                updateStepButtonDisabilities();
                updateSnapshotDescription();
                currentSnapshotPaneProperty.set(availableSnapshotPanes.get
                        (currentSnapshotIndex));
                break;
            case BACKWARD:
                currentSnapshotIndex--;
                updateStepButtonDisabilities();
                updateSnapshotDescription();
                currentSnapshotPaneProperty.set(availableSnapshotPanes.get
                        (currentSnapshotIndex));
                break;
            case FORWARD:
                currentSnapshotIndex++;
                updateStepButtonDisabilities();
                updateSnapshotDescription();
                currentSnapshotPaneProperty.set(availableSnapshotPanes.get
                        (currentSnapshotIndex));
                break;
            case LAST:
                currentSnapshotIndex = availableSnapshotPanes.size() - 1;
                updateStepButtonDisabilities();
                updateSnapshotDescription();
                currentSnapshotPaneProperty.set(availableSnapshotPanes.get
                        (currentSnapshotIndex));
                break;
        }
    }

    private void updateSnapshotDescription() {
        Snapshot s = snapshotStore.getSnapshots(
                session.getSessionId()).get(currentSnapshotIndex);
        currentSnapshotDescriptionProperty.set(s.getSnapshotDescription());
    }

    private void updateStepButtonDisabilities() {
        maxSnapshotIndex = availableSnapshotPanes.size() - 1;
        if (maxSnapshotIndex <= 1 || replayingProperty.get()) {
            disableStepButtons(true, true, true, true);
        } else {
            if (currentSnapshotIndex == 0) {
                disableStepButtons(true, true, false, false);
            } else if (currentSnapshotIndex == maxSnapshotIndex) {
                disableStepButtons(false, false, true, true);
            } else {
                disableStepButtons(false, false, false, false);
            }
        }
    }

    private void disableStepButtons(boolean first, boolean backward, boolean
            forward, boolean last) {
        stepFirstBtnDisableProperty.set(first);
        stepBackwardBtnDisableProperty.set(backward);
        stepForwardBtnDisableProperty.set(forward);
        stepLastBtnDisableProperty.set(last);
    }

    public BooleanProperty stepFirstBtnDisableProperty() {
        return stepFirstBtnDisableProperty;
    }

    public BooleanProperty stepBackwardBtnDisableProperty() {
        return stepBackwardBtnDisableProperty;
    }

    public BooleanProperty stepForwardBtnDisableProperty() {
        return stepForwardBtnDisableProperty;
    }

    public BooleanProperty stepLastBtnDisableProperty() {
        return stepLastBtnDisableProperty;
    }

    public BooleanProperty isReplayingProperty() {
        return replayingProperty;
    }

    public BooleanProperty speedsliderDisableProperty() {
        return speedsliderDisableProperty;
    }

    public void pauseReplay() {
        replayingProperty.set(false);
        if (this.currentReplayThread != null) {
            currentReplayThread.cancel();
            currentReplayThread = null;
        }
    }

    public void replay() {
        logger.info("Replaying current session...");
        replayingProperty.set(true);

        if (currentReplayThread == null || currentReplayThread.isCanceled()) {
            if (currentSnapshotIndex == maxSnapshotIndex) {
                navigateSnapshot(Navigate.FIRST);
            }

            this.currentReplayThread = sessionReplayFactory.create(this);
            Timer timer = new Timer();
            long timeout = replayController.getReplaySpeed() * HALF_SECOND_MS;
            timer.schedule(currentReplayThread, timeout, timeout);
        } else {
            pauseReplay();
        }
    }

    public void cancelReplay() {
        pauseReplay();
        navigateSnapshot(Navigate.FIRST);
    }


    /**
     * Change listener if a new snapshot was added to the snapshot store
     */
    private class SnapshotPropertyChangeListener implements
            PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            Pane newSnapshot = (Pane) event.getNewValue();

            logger.debug("SnapshotStore for session {} has updated. Add {} to"
                            + " available snapshots",
                    session.getSessionId(),
                    newSnapshot);

            Platform.runLater(() -> {
                availableSnapshotPanes.add(newSnapshot);
                updateStepButtonDisabilities();
            });
        }
    }
}
