package ch.adv.ui.core.presentation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;


/**
 * TimerTask, responsible for showing replays with a defined timeout
 *
 * @author mwieland
 */
public class SessionReplay extends TimerTask {

    private static final Logger logger = LoggerFactory
            .getLogger(SessionReplay.class);
    private final StateViewModel stateViewModel;
    private final SteppingViewModel steppingViewModel;
    private boolean isCanceled;

    @Inject
    public SessionReplay(@Assisted StateViewModel stateViewModel,
                         @Assisted SteppingViewModel steppingViewModel) {

        this.stateViewModel = stateViewModel;
        this.steppingViewModel = steppingViewModel;
        if (stateViewModel.getCurrentSnapshotIndex() == stateViewModel
                .getMaxSnapshotIndex()) {
            steppingViewModel.navigateSnapshot(Navigate.FIRST);
        }
    }


    /**
     * Executes the timer task.
     */
    public void run() {
        logger.debug("Session replay tick");
        if (stateViewModel.getCurrentSnapshotIndex() < stateViewModel
                .getMaxSnapshotIndex()) {
            Platform.runLater(
                    () -> steppingViewModel.navigateSnapshot(Navigate.FORWARD)
            );
        } else {
            logger.info("Replay finished");
            isCanceled = cancelReplay();
        }
    }

    /**
     * Cancel current thread and reset UI button states
     *
     * @return returns true if it prevents one or more scheduled executions from
     * taking place.
     */
    private boolean cancelReplay() {
        Platform.runLater(
                () -> stateViewModel.getReplayingProperty().set(false)
        );
        return super.cancel();
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}

