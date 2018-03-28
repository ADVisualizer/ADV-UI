package ch.adv.ui.presentation;

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
    private final SessionViewModel sessionViewModel;
    private boolean isCanceled;

    /**
     * GraphSessionReplay.
     *
     * @param sessionViewModel sessionViewModel
     */
    @Inject
    public SessionReplay(@Assisted SessionViewModel sessionViewModel) {
        this.sessionViewModel = sessionViewModel;
    }

    /**
     * Executes the timer task.
     */
    public void run() {
        logger.debug("Session replay tick");
        if (sessionViewModel.getCurrentSnapshotIndex() < sessionViewModel
                .getMaxSnapshotIndex()) {
            Platform.runLater(
                    () -> sessionViewModel.navigateSnapshot(Navigate.FORWARD)
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
    public boolean cancelReplay() {
        Platform.runLater(
                () -> sessionViewModel.isReplayingProperty().set(false)
        );
        return super.cancel();
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}

