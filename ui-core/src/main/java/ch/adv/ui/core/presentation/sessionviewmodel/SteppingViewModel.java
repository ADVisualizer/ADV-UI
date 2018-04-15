package ch.adv.ui.core.presentation.sessionviewmodel;

import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import ch.adv.ui.core.presentation.LayoutedSnapshotStore;
import ch.adv.ui.core.presentation.Navigate;
import ch.adv.ui.core.presentation.SessionView;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import com.google.inject.Inject;

/**
 * Handles stepping logic for the {@link SessionView}.
 */
public class SteppingViewModel {
    private final EventManager eventManager;
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private StateViewModel stateViewModel;

    @Inject
    public SteppingViewModel(LayoutedSnapshotStore
                                     layoutedSnapshotStore, EventManager
            eventManager) {
        this.layoutedSnapshotStore = layoutedSnapshotStore;
        this.eventManager = eventManager;
    }

    public void setStateViewModel(StateViewModel stateViewModel) {
        this.stateViewModel = stateViewModel;
    }

    /**
     * Navigate through a session by stepping forward, backward or to the
     * first or last snapshot.
     *
     * @param navigate direction to navigate
     */
    public void navigateSnapshot(Navigate navigate) {
        if (stateViewModel == null) {
            throw new IllegalArgumentException("StateViewModel not set");
        }
        int oldIndex = stateViewModel.getCurrentSnapshotIndex();
        int newIndex;

        switch (navigate) {
            case FIRST:
                newIndex = 0;
                stateViewModel.setCurrentSnapshotIndex(newIndex);

                fireEvent(ADVEvent.STEP_FIRST, oldIndex,
                        newIndex);
                break;
            case BACKWARD:
                newIndex = stateViewModel.getCurrentSnapshotIndex();
                newIndex--;
                stateViewModel.setCurrentSnapshotIndex(newIndex);

                fireEvent(ADVEvent.STEP_BACKWARD, oldIndex,
                        newIndex);
                break;
            case FORWARD:
                newIndex = stateViewModel.getCurrentSnapshotIndex();
                newIndex++;
                stateViewModel.setCurrentSnapshotIndex(newIndex);

                fireEvent(ADVEvent.STEP_FORWARD, oldIndex,
                        newIndex);
                break;
            case LAST:
                newIndex = stateViewModel.getMaxSnapshotIndex();
                stateViewModel.setCurrentSnapshotIndex(newIndex);
                fireEvent(ADVEvent.STEP_LAST, oldIndex,
                        newIndex);
                break;
            default:
                break;
        }
        stateViewModel.handleNavigationStep();
    }


    private void fireEvent(ADVEvent event, int oldIndex, int newIndex) {
        LayoutedSnapshot oldLayoutedSnapshot = layoutedSnapshotStore
                .getLayoutedSnapshots(stateViewModel.getSessionId())
                .get(oldIndex);

        LayoutedSnapshot newLayoutedSnapshot = layoutedSnapshotStore
                .getLayoutedSnapshots(stateViewModel.getSessionId())
                .get(newIndex);

        eventManager.fire(event, oldLayoutedSnapshot, newLayoutedSnapshot);
    }

}
