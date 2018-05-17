package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.events.ADVEvent;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds state for the {@link ch.hsr.adv.ui.core.presentation.SessionView}.
 */
class StateViewModel {

    private static final Logger logger = LoggerFactory.getLogger(
            StateViewModel.class);
    private final ObservableList<Region> availableSnapshotPanes;
    private final ObjectProperty<Region> currentSnapshotPaneProperty = new
            SimpleObjectProperty<>();
    private final ObjectProperty<String> currentSnapshotDescriptionProperty =
            new SimpleObjectProperty<>();
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private final BooleanProperty speedSliderDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty replayingProperty = new
            SimpleBooleanProperty();
    private final FloatProperty progressProperty = new
            SimpleFloatProperty();
    private final StringProperty currentIndexStringProperty = new
            SimpleStringProperty();
    private final StringProperty maxIndexStringProperty = new
            SimpleStringProperty();
    private final List<DoubleProperty> dividerPositions = new ArrayList<>();
    private final StepButtonState stepButtonState;
    private int currentSnapshotIndex;
    private int maxSnapshotIndex;
    private Session session;
    private long sessionId;


    @Inject
    StateViewModel(RootViewModel rootViewModel, LayoutedSnapshotStore
            layoutedSnapshotStore, EventManager eventManager, StepButtonState
                           stepButtonState) {
        logger.debug("Construct StateViewModel");
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
                .addAll(layoutedSnapshotStore.getAllPanes(sessionId));
        this.currentSnapshotPaneProperty.set(availableSnapshotPanes.get(0));

        layoutedSnapshotStore.getAll(sessionId)
                .forEach(snapshot ->
                        bindDividerPositions(snapshot.getDividers()));


        String snapshotDescription = layoutedSnapshotStore
                .getAll(sessionId).get(0)
                .getSnapshotDescription();
        this.currentSnapshotDescriptionProperty.set(snapshotDescription);

        updateProgress();
        updateStepButtonDisabilities();

        replayingProperty.addListener((e, oldV, newV) -> {
            updateStepButtonDisabilities();
            if (newV) {
                speedSliderDisableProperty.set(true);
            } else {
                speedSliderDisableProperty.set(false);
            }
        });

        currentSnapshotDescriptionProperty.addListener((e, oldV, newV) -> {
            if (newV != null) {
                LayoutedSnapshot s = layoutedSnapshotStore
                        .getAll(session.getSessionId())
                        .get(currentSnapshotIndex);
                String domainDescription = s.getSnapshotDescription();
                if (!newV.equals(domainDescription)) {
                    s.setSnapshotDescription(newV);
                }
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
    void handleNavigationStep() {
        updateProgress();
        updateStepButtonDisabilities();
        updateSnapshotDescription();
        currentSnapshotPaneProperty.set(availableSnapshotPanes
                .get(currentSnapshotIndex));
    }


    ObjectProperty<Region> getCurrentSnapshotPaneProperty() {
        return currentSnapshotPaneProperty;
    }

    ObjectProperty<String> getCurrentSnapshotDescriptionProperty() {
        return currentSnapshotDescriptionProperty;
    }

    int getCurrentSnapshotIndex() {
        return currentSnapshotIndex;
    }

    void setCurrentSnapshotIndex(int currentSnapshotIndex) {
        this.currentSnapshotIndex = currentSnapshotIndex;
    }

    int getMaxSnapshotIndex() {
        return maxSnapshotIndex;
    }

    void setSession(final Session session) {
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
                .getAll(session.getSessionId())
                .get(currentSnapshotIndex).getSnapshotDescription();
        currentSnapshotDescriptionProperty.set(description);
    }

    StepButtonState getStepButtonState() {
        return stepButtonState;
    }

    BooleanProperty getReplayingProperty() {
        return replayingProperty;
    }

    BooleanProperty getSpeedSliderDisableProperty() {
        return speedSliderDisableProperty;
    }

    FloatProperty getProgressProperty() {
        return progressProperty;
    }

    StringProperty getCurrentIndexStringProperty() {
        return currentIndexStringProperty;
    }

    StringProperty getMaxIndexStringProperty() {
        return maxIndexStringProperty;
    }

    long getSessionId() {
        return sessionId;
    }

    private void bindDividerPositions(List<Divider> dividers) {
        if (dividerPositions.isEmpty()) {
            dividers.forEach(d -> dividerPositions
                    .add(new SimpleDoubleProperty(d.getPosition()))
            );
        }
        for (int i = 0; i < dividers.size(); i++) {
            dividers.get(i).positionProperty()
                    .bindBidirectional(dividerPositions.get(i));
        }
    }

    /**
     * Change listener if a new snapshot was added to the snapshot store
     */
    private class SnapshotPropertyChangeListener implements
            PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            Platform.runLater(() -> {
                LayoutedSnapshot newSnapshot = (LayoutedSnapshot) event
                        .getNewValue();
                Region newPane = newSnapshot.getPane();
                availableSnapshotPanes.add(newPane);
                bindDividerPositions(newSnapshot.getDividers());
                updateProgress();
                updateStepButtonDisabilities();
            });
        }
    }
}
