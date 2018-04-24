package ch.adv.ui.array.logic;

import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Listener for all Step Events within the Framework core
 */
@Singleton
public class StepEventController implements PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(
            StepEventController.class);

    private static final List<ADVEvent> NAVIGATION_EVENTS = List.of(
            ADVEvent.STEP_FIRST, ADVEvent.STEP_BACKWARD,
            ADVEvent.STEP_FORWARD, ADVEvent.STEP_LAST);

    private final EventManager eventManager;

    @Inject
    public StepEventController(EventManager eventManager) {
        this.eventManager = eventManager;

        eventManager.subscribe(this, NAVIGATION_EVENTS);
    }

    /**
     * Receives the update as the subscribed event happend.
     * @param event event
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        logger.info("Received property name: {}", event.getPropertyName());
        logger.info("Received old value: {}", event.getOldValue());
        logger.info("Received new value: {}", event.getNewValue());
    }
}
