package ch.adv.ui.core.presentation.sessionviewmodel;

import ch.adv.ui.core.domain.Session;
import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import ch.adv.ui.core.presentation.LayoutedSnapshotStore;
import ch.adv.ui.core.presentation.RootViewModel;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
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

/**
 * Holds state for the {@link ch.adv.ui.core.presentation.SessionView}.
 */
public class StateViewModel {

    private static final Logger logger = LoggerFactory.getLogger(
            StateViewModel.class);
    private final ObservableList<Pane> availableSnapshotPanes;
    private final ObjectProperty<Pane> currentSnapshotPaneProperty = new
            SimpleObjectProperty<>();
    private final ObjectProperty<String> currentSnapshotDescriptionProperty =
            new SimpleObjectProperty<>();
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private final BooleanProperty speedsliderDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty replayingProperty = new
            SimpleBooleanProperty();
    private final FloatProperty progressProperty = new
            SimpleFloatProperty();
    private final StringProperty currentIndexStringProperty = new
            SimpleStringProperty();
    private final StringProperty maxIndexStringProperty = new
            SimpleStringProperty();
    private final StepButtonState stepButtonState;
    private int currentSnapshotIndex;
    private int maxSnapshotIndex;
    private Session session;
    private long sessionId;


    @Inject
    public StateViewModel(RootViewModel rootViewModel, LayoutedSnapshotStore
            layoutedSnapshotStore, EventManager eventManager, StepButtonState
                                  stepButtonState) {
        logger.debug("init stateViewModel");
        this.layoutedSnapshotStore = layoutedSnapshotStore;
        this.stepButtonState = stepButtonState;
        this.session = rootViewModel.getCurrentSessionProperty().get();

        this.availableSnapshotPanes = FXCollections.observableArrayList();

        //initialize properties
        sessionId = session.getSessionId();
        eventManager.subscribe(
                new StateViewModel.SnapshotPropertyChangeListener(), ADVEvent
                        .SNAPSHOT_ADDED, sessionId + "");

        this.availableSnapshotPanes
                .addAll(layoutedSnapshotStore.getSnapshotPanes(sessionId));
        this.currentSnapshotPaneProperty.set(availableSnapshotPanes.get(0));

        String snapshotDescription = layoutedSnapshotStore
                .getLayoutedSnapshots(sessionId).get(0)
                .getSnapshotDescription();
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

        currentSnapshotDescriptionProperty.addListener((e, oldV, newV) -> {
            LayoutedSnapshot s = layoutedSnapshotStore
                    .getLayoutedSnapshots(session.getSessionId())
                    .get(currentSnapshotIndex);
            String domainDescription = s.getSnapshotDescription();
            if (!newV.equals(domainDescription)) {
                s.setSnapshotDescription(newV);
            }
        });

    }

    private void updateStepButtonDisabilities() {
        if (getMaxSnapshotIndex() == 0 || getReplayingProperty().get()) {
            stepButtonState.disableStepButtons(true, true, true, true);
        } else {
            if (getCurrentSnapshotIndex() == 0) {
                stepButtonState.disableStepButtons(true, true, false, false);
            } else if (getCurrentSnapshotIndex() == getMaxSnapshotIndex()) {
                stepButtonState.disableStepButtons(false, false, true, true);
            } else {
                stepButtonState.disableStepButtons(false, false, false, false);
            }
        }
    }


    /**
     * updates State after a step
     */
    public void handleNavigationStep() {
        updateProgress();
        updateStepButtonDisabilities();
        updateSnapshotDescription();
        currentSnapshotPaneProperty.set(availableSnapshotPanes
                .get(currentSnapshotIndex));
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

    public void setCurrentSnapshotIndex(int currentSnapshotIndex) {
        this.currentSnapshotIndex = currentSnapshotIndex;
    }

    public int getMaxSnapshotIndex() {
        return maxSnapshotIndex;
    }

    public void setSession(final Session session) {
        this.session = session;
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
        String description = layoutedSnapshotStore
                .getLayoutedSnapshots(session.getSessionId())
                .get(currentSnapshotIndex).getSnapshotDescription();
        currentSnapshotDescriptionProperty.set(description);
    }

    public StepButtonState getStepButtonState() {
        return stepButtonState;
    }

    public BooleanProperty getReplayingProperty() {
        return replayingProperty;
    }

    public BooleanProperty getSpeedsliderDisableProperty() {
        return speedsliderDisableProperty;
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

    public ObservableList<Pane> getAvailableSnapshotPanes() {
        return availableSnapshotPanes;
    }

    public long getSessionId() {
        return sessionId;
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
                availableSnapshotPanes.addAll(layoutedSnapshotStore
                        .getSnapshotPanes(session.getSessionId()));
                updateProgress();
                updateStepButtonDisabilities();
            });
        }
    }
}
