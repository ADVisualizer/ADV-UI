package ch.adv.ui.core.logic;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * This class is responsible for the event dispatching of all ADVEvents
 * It uses the {@link java.beans.PropertyChangeSupport} internally.
 * <p>
 * Modules can register {@link java.beans.PropertyChangeListener} to listen
 * for specific Events which are called by the ADV framework.
 */
@Singleton
public class EventManager {

    private static final Logger logger = LoggerFactory.getLogger(
            EventManager.class);

    private final PropertyChangeSupport eventStore = new
            PropertyChangeSupport(this);

    /**
     * Registers a listener for a specific event
     *
     * @param listener listener
     * @param event    ADV event
     */
    public void subscribe(PropertyChangeListener listener, ADVEvent event) {
        eventStore.addPropertyChangeListener(event.toString(), listener);
    }

    /**
     * Registers a listener for multiple events
     *
     * @param listener listener
     * @param events   ADV events
     */
    public void subscribe(PropertyChangeListener listener,
                          List<ADVEvent> events) {
        events.forEach(e -> subscribe(listener, e));
    }

    /**
     * Removes the listener for a specific event
     *
     * @param listener listener
     * @param event    ADV event
     */
    public void unsubscribe(PropertyChangeListener listener, ADVEvent event) {
        eventStore.removePropertyChangeListener(event.toString(), listener);
    }

    /**
     * Removes the listener for multiple events
     *
     * @param listener listener
     * @param events   ADV events
     */
    public void unsubscribe(PropertyChangeListener listener,
                            List<ADVEvent> events) {
        events.forEach(e -> unsubscribe(listener, e));
    }

    /**
     * Executes an ADV event and transmits the old and the new
     * value to all subscribers.
     *
     * @param event  event
     * @param oldVal value before change
     * @param newVal value after change
     */
    public void fire(ADVEvent event, Object oldVal, Object newVal) {
        logger.debug("Fired event {}", event.toString());
        eventStore.firePropertyChange(event.toString(), oldVal, newVal);
    }
}
