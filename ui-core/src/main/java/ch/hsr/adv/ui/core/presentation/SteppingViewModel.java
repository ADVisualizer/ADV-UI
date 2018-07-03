package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.events.ADVEvent;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Handles stepping logic for the
 * {@link ch.hsr.adv.ui.core.presentation.SessionView}.
 *
 * @author mtrentini
 */
public class SteppingViewModel {

    private final EventManager eventManager;
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private StateViewModel stateViewModel;

    @Inject
    public SteppingViewModel(LayoutedSnapshotStore layoutedSnapshotStore,
                             EventManager eventManager,
                             @Assisted StateViewModel stateViewModel) {

        this.layoutedSnapshotStore = layoutedSnapshotStore;
        this.eventManager = eventManager;
        this.stateViewModel = stateViewModel;
    }


    /**
     * Navigates through a session by stepping forward, backward or to the
     * first or last snapshot.
     *
     * @param navigate direction to navigate
     */
    void navigateSnapshot(Navigate navigate) {
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
        if (stateViewModel.isAllowedIndex(oldIndex) && stateViewModel
                .isAllowedIndex(newIndex)) {
            LayoutedSnapshot oldLayoutedSnapshot = layoutedSnapshotStore
                    .getAll(stateViewModel.getSessionId())
                    .get(oldIndex);

            LayoutedSnapshot newLayoutedSnapshot = layoutedSnapshotStore
                    .getAll(stateViewModel.getSessionId())
                    .get(newIndex);

            eventManager.fire(event, oldLayoutedSnapshot, newLayoutedSnapshot);
        }
    }

}
