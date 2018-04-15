package ch.adv.ui.core.presentation.sessionviewmodel;

import ch.adv.ui.core.presentation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Timer;

/**
 * Handles replay logic for the {@link ch.adv.ui.core.presentation.SessionView}.
 */
public class ReplayViewModel {
    private static final Logger logger = LoggerFactory.getLogger(
            ReplayViewModel.class);
    private static final long HALF_SECOND_MS = 500;
    private final SessionReplayFactory sessionReplayFactory;
    private final ReplayController replayController;
    private StateViewModel stateViewModel;
    private SteppingViewModel steppingViewModel;
    private SessionReplay currentReplayThread;

    @Inject
    public ReplayViewModel(
            SessionReplayFactory sessionReplayFactory,
            ReplayController replayController) {
        logger.debug("init replayViewModel");
        this.sessionReplayFactory = sessionReplayFactory;
        this.replayController = replayController;
    }


    public void setStateViewModel(StateViewModel stateViewModel) {
        this.stateViewModel = stateViewModel;
    }

    public void setSteppingViewModel(SteppingViewModel steppingViewModel) {
        this.steppingViewModel = steppingViewModel;
    }

    /**
     * Stop the current replay and stay at the current snapshot
     */
    public void pauseReplay() {
        if (stateViewModel == null || steppingViewModel == null) {
            throw new IllegalArgumentException("StateViewModel or "
                    + "SteppingViewModel not set");
        }
        stateViewModel.getReplayingProperty().set(false);
        if (this.currentReplayThread != null) {
            currentReplayThread.cancel();
            currentReplayThread = null;
        }
    }

    /**
     * Start replaying the snapshots of this session.
     */
    public void replay() {
        if (stateViewModel == null || steppingViewModel == null) {
            throw new IllegalArgumentException("StateViewModel or "
                    + "SteppingViewModel not set");
        }
        logger.info("Replaying current session...");
        stateViewModel.getReplayingProperty().set(true);

        if (currentReplayThread == null || currentReplayThread.isCanceled()) {
            this.currentReplayThread = sessionReplayFactory
                    .create(stateViewModel, steppingViewModel);
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
        if (stateViewModel == null || steppingViewModel == null) {
            throw new IllegalArgumentException("StateViewModel or "
                    + "SteppingViewModel not set");
        }
        pauseReplay();
        steppingViewModel.navigateSnapshot(Navigate.FIRST);
    }
}
