package ch.adv.ui.core.presentation;

import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import ch.adv.ui.core.logic.LayoutedSnapshotStore;
import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles stepping logic for the
 * {@link ch.adv.ui.core.presentation.SessionView}.
 */
public class SteppingViewModel {
    private static final Logger logger = LoggerFactory
            .getLogger(SteppingViewModel.class);
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
     * Navigate through a session by stepping forward, backward or to the
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
        logger.debug(layoutedSnapshotStore + "");
        logger.debug(stateViewModel + "");
        LayoutedSnapshot oldLayoutedSnapshot = layoutedSnapshotStore
                .getAll(stateViewModel.getSessionId())
                .get(oldIndex);

        LayoutedSnapshot newLayoutedSnapshot = layoutedSnapshotStore
                .getAll(stateViewModel.getSessionId())
                .get(newIndex);

        eventManager.fire(event, oldLayoutedSnapshot, newLayoutedSnapshot);
    }

}
