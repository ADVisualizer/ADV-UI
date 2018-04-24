package ch.adv.ui.core.app;

import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.beans.PropertyChangeListener;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(JukitoRunner.class)
public class EventManagerTest {

    @Inject
    private EventManager eventManagerUnterTest;

    @Inject
    private PropertyChangeListener listenerTest;


    @Test
    public void fireTest() {
        String oldValue = "Before";
        String newValue = "After";

        PropertyChangeListener testListener = e -> {
            assertEquals(e.getPropertyName(), ADVEvent.STEP_FIRST.toString());
            assertEquals(e.getOldValue(), oldValue);
            assertEquals(e.getNewValue(), newValue);
        };

        eventManagerUnterTest.subscribe(testListener, ADVEvent.STEP_FIRST);
        eventManagerUnterTest.fire(ADVEvent.STEP_FIRST, oldValue, newValue);
    }

    @Test
    public void subscribeMultipleEvents() {
        eventManagerUnterTest.subscribe(listenerTest, List.of(ADVEvent
                .STEP_FIRST, ADVEvent.STEP_LAST, ADVEvent.STEP_FORWARD));

        eventManagerUnterTest.fire(ADVEvent.STEP_FIRST, null, null);
        eventManagerUnterTest.fire(ADVEvent.STEP_LAST, null, null);

        Mockito.verify(listenerTest, times(2)).propertyChange(any());
    }

    @Test
    public void unsubscribeMultipleEvents() {
        eventManagerUnterTest.subscribe(listenerTest, List.of(ADVEvent
                .STEP_FIRST, ADVEvent.STEP_LAST, ADVEvent.STEP_FORWARD));

        eventManagerUnterTest.unsubscribe(listenerTest, List.of(ADVEvent
                .STEP_FIRST, ADVEvent.STEP_LAST, ADVEvent.STEP_FORWARD));

        eventManagerUnterTest.fire(ADVEvent.STEP_FIRST, null, null);
        eventManagerUnterTest.fire(ADVEvent.STEP_LAST, null, null);

        Mockito.verify(listenerTest, never()).propertyChange(any());
    }

    @Test
    public void filterEventHandleContext() {
        long sessionId = 42;

        eventManagerUnterTest.subscribe(listenerTest, ADVEvent.SNAPSHOT_ADDED,
                sessionId + "");
        eventManagerUnterTest.fire(ADVEvent.SNAPSHOT_ADDED, null, null,
                sessionId + "");

        Mockito.verify(listenerTest).propertyChange(any());
    }

    @Test
    public void receiveNoChangeEvent() {
        long sessionId = 123789;

        eventManagerUnterTest.subscribe(listenerTest, ADVEvent.SNAPSHOT_ADDED);
        eventManagerUnterTest.fire(ADVEvent.SNAPSHOT_ADDED, null, null,
                sessionId + "");

        Mockito.verify(listenerTest, never()).propertyChange(any());
    }
}