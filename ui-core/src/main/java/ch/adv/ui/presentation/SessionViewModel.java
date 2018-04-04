package ch.adv.ui.presentation;

import ch.adv.ui.domain.Session;
import ch.adv.ui.domain.Snapshot;
import javafx.application.Platform;
import javafx.beans.property.*;
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
    private final ObjectProperty<Pane> currentSnapshotPaneProperty = new
            SimpleObjectProperty<>();
    private final ObjectProperty<String> currentSnapshotDescriptionProperty =
            new SimpleObjectProperty<>();
    private final SnapshotStore snapshotStore;
    private final BooleanProperty stepFirstBtnDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty stepBackwardBtnDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty stepForwardBtnDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty stepLastBtnDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty speedsliderDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty replayingProperty = new
            SimpleBooleanProperty();
    private final FloatProperty progressProperty = new
            SimpleFloatProperty();
    private final SessionReplayFactory sessionReplayFactory;
    private final ReplayController replayController;
    private final StringProperty currentIndexStringProperty = new
            SimpleStringProperty();
    private final StringProperty maxIndexStringProperty = new
            SimpleStringProperty();
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

        //initialize properties
        snapshotStore.addPropertyChangeListener(session.getSessionId(), new
                SnapshotPropertyChangeListener());

        this.availableSnapshotPanes.addAll(snapshotStore.getSnapshotPanes(
                session.getSessionId()));
        this.currentSnapshotPaneProperty.set(availableSnapshotPanes.get(0));
        String snapshotDescription = snapshotStore.getWrappers(session
                .getSessionId()).get(0).getSnapshot().getSnapshotDescription();
        this.currentSnapshotDescriptionProperty.set(snapshotDescription);

        updateProgress();
        updateStepButtonDisabilities();

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

    /**
     * Navigate through a session by stepping forward, backward or to the
     * first or last snapshot.
     *
     * @param navigate direction to navigate
     */
    public void navigateSnapshot(Navigate navigate) {
        switch (navigate) {
            case FIRST:
                currentSnapshotIndex = 0;
                handleNavigationStep();
                currentSnapshotPaneProperty.set(availableSnapshotPanes
                        .get(currentSnapshotIndex));
                break;
            case BACKWARD:
                currentSnapshotIndex--;
                handleNavigationStep();
                currentSnapshotPaneProperty.set(availableSnapshotPanes
                        .get(currentSnapshotIndex));
                break;
            case FORWARD:
                currentSnapshotIndex++;
                handleNavigationStep();
                currentSnapshotPaneProperty.set(availableSnapshotPanes
                        .get(currentSnapshotIndex));
                break;
            case LAST:
                currentSnapshotIndex = availableSnapshotPanes.size() - 1;
                handleNavigationStep();
                currentSnapshotPaneProperty.set(availableSnapshotPanes
                        .get(currentSnapshotIndex));
                break;
            default:
                break;
        }
    }

    private void handleNavigationStep() {
        updateProgress();
        updateStepButtonDisabilities();
        updateSnapshotDescription();
    }

    private void updateProgress() {
        maxSnapshotIndex = availableSnapshotPanes.size() - 1;
        progressProperty.set(
                (1 + (float) currentSnapshotIndex) / (1 + maxSnapshotIndex)
        );
        currentIndexStringProperty.set(currentSnapshotIndex + 1 + "");
        maxIndexStringProperty.set(maxSnapshotIndex + 1 + "");
    }

    private void updateSnapshotDescription() {
        Snapshot s = snapshotStore.getWrappers(
                session.getSessionId()).get(currentSnapshotIndex).getSnapshot();
        currentSnapshotDescriptionProperty.set(s.getSnapshotDescription());
    }

    private void updateStepButtonDisabilities() {
        if (maxSnapshotIndex == 0 || replayingProperty.get()) {
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

    public BooleanProperty getStepFirstBtnDisableProperty() {
        return stepFirstBtnDisableProperty;
    }

    public BooleanProperty getStepBackwardBtnDisableProperty() {
        return stepBackwardBtnDisableProperty;
    }

    public BooleanProperty getStepForwardBtnDisableProperty() {
        return stepForwardBtnDisableProperty;
    }

    public BooleanProperty getStepLastBtnDisableProperty() {
        return stepLastBtnDisableProperty;
    }

    public BooleanProperty isReplayingProperty() {
        return replayingProperty;
    }

    public BooleanProperty getSpeedsliderDisableProperty() {
        return speedsliderDisableProperty;
    }

    /**
     * Stop the current replay and stay at the current snapshot
     */
    public void pauseReplay() {
        replayingProperty.set(false);
        if (this.currentReplayThread != null) {
            currentReplayThread.cancel();
            currentReplayThread = null;
        }
    }

    /**
     * Start replaying the snapshots of this session.
     */
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

    /**
     * Cancel the running replay and step to the first snapshot.
     */
    public void cancelReplay() {
        pauseReplay();
        navigateSnapshot(Navigate.FIRST);
    }

    public FloatProperty getProgressProperty() {
        return progressProperty;
    }

    public StringProperty getCurrentIndexStringProperty() {
        return currentIndexStringProperty;
    }

    public StringProperty getMaxIndexStringProperty() {
        return maxIndexStringProperty;
    }


    /**
     * Change listener if a new snapshot was added to the snapshot store
     */
    private class SnapshotPropertyChangeListener implements
            PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            Platform.runLater(() -> {
                availableSnapshotPanes.clear();
                availableSnapshotPanes.addAll(snapshotStore
                        .getSnapshotPanes(session.getSessionId()));
                updateProgress();
                updateStepButtonDisabilities();
            });
        }
    }
}
