package ch.hsr.adv.ui.core.presentation;

import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Timer;

/**
 * Handles replay logic for the
 * {@link ch.hsr.adv.ui.core.presentation.SessionView}.
 *
 * @author mtrentini
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
    public ReplayViewModel(SessionReplayFactory sessionReplayFactory,
                           ReplayController replayController,
                           @Assisted StateViewModel stateViewModel,
                           @Assisted SteppingViewModel steppingViewModel) {

        this.sessionReplayFactory = sessionReplayFactory;
        this.replayController = replayController;
        this.stateViewModel = stateViewModel;
        this.steppingViewModel = steppingViewModel;
    }

    /**
     * Stops the current replay and stays at the current snapshot
     */
    void pauseReplay() {
        stateViewModel.getReplayingProperty().set(false);
        if (this.currentReplayThread != null) {
            currentReplayThread.cancel();
            currentReplayThread = null;
        }
    }

    /**
     * Starts replaying the snapshots of this session.
     */
    void replay() {
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
     * Cancels the running replay and steps to the first snapshot.
     */
    void cancelReplay() {
        pauseReplay();
        steppingViewModel.navigateSnapshot(Navigate.FIRST);
    }
}
