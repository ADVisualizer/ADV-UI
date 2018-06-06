package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.events.ADVEvent;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private final EventManager eventManager;
    private final DeleteSessionChangeListener listener;
    private final long sessionId;
    private boolean isCanceled;

    @Inject
    public SessionReplay(@Assisted StateViewModel stateViewModel,
                         @Assisted SteppingViewModel steppingViewModel,
                         EventManager eventManager) {
        logger.debug("Initialize RessionReplay");
        this.stateViewModel = stateViewModel;
        this.steppingViewModel = steppingViewModel;
        this.eventManager = eventManager;
        this.listener = new SessionReplay.DeleteSessionChangeListener();
        this.sessionId = stateViewModel.getSessionId();
        eventManager.subscribe(listener,
                ADVEvent.SESSION_REMOVED, sessionId + "");
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
     * Cancels the current thread and resets UI button states
     *
     * @return returns true if it prevents one or more scheduled executions from
     * taking place.
     */
    private boolean cancelReplay() {
        eventManager.unsubscribe(
                listener, ADVEvent.SESSION_REMOVED, sessionId + "");
        Platform.runLater(
                () -> stateViewModel.getReplayingProperty().set(false)
        );
        return super.cancel();
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    /**
     * Listens for the deletion of its associated session.
     */
    private class DeleteSessionChangeListener implements
            PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            cancelReplay();
        }
    }

}

