package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.presentation.GuiceCoreModule;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
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
@UseModules({GuiceCoreModule.class})
public class EventManagerTest {

    @Inject
    private EventManager sut;

    @Inject
    private PropertyChangeListener listenerTest;

    @Test
    public void fireTest() {
        // GIVEN
        String oldValue = "Before";
        String newValue = "After";

        PropertyChangeListener testListener = e -> {
            // THEN
            assertEquals(e.getPropertyName(), ADVEvent.STEP_FIRST.toString());
            assertEquals(e.getOldValue(), oldValue);
            assertEquals(e.getNewValue(), newValue);
        };
        sut.subscribe(testListener, ADVEvent.STEP_FIRST);

        // WHEN
        sut.fire(ADVEvent.STEP_FIRST, oldValue, newValue);
    }

    @Test
    public void subscribeMultipleEvents() {
        // GIVEN
        sut.subscribe(listenerTest, List.of(ADVEvent
                .STEP_FIRST, ADVEvent.STEP_LAST, ADVEvent.STEP_FORWARD));

        // WHEN
        sut.fire(ADVEvent.STEP_FIRST, null, null);
        sut.fire(ADVEvent.STEP_LAST, null, null);

        // THEN
        Mockito.verify(listenerTest, times(2)).propertyChange(any());
    }

    @Test
    public void unsubscribeMultipleEvents() {
        // GIVEN
        sut.subscribe(listenerTest, List.of(ADVEvent
                .STEP_FIRST, ADVEvent.STEP_LAST, ADVEvent.STEP_FORWARD));

        // WHEN
        sut.unsubscribe(listenerTest, List.of(ADVEvent
                .STEP_FIRST, ADVEvent.STEP_LAST, ADVEvent.STEP_FORWARD));
        sut.fire(ADVEvent.STEP_FIRST, null, null);
        sut.fire(ADVEvent.STEP_LAST, null, null);

        // THEN
        Mockito.verify(listenerTest, never()).propertyChange(any());
    }

    @Test
    public void filterEventHandleContext() {
        // GIVEN
        long sessionId = 42;
        sut.subscribe(listenerTest, ADVEvent.SNAPSHOT_ADDED,
                sessionId + "");

        // WHEN
        sut.fire(ADVEvent.SNAPSHOT_ADDED, null, null,
                sessionId + "");

        // THEN
        Mockito.verify(listenerTest).propertyChange(any());
    }

    @Test
    public void receiveNotifyForUniversalListener() {
        // GIVEN
        long sessionId = 42;
        sut.subscribe(listenerTest, ADVEvent.SNAPSHOT_ADDED);

        // WHEN
        sut.fire(ADVEvent.SNAPSHOT_ADDED, null, null,
                sessionId + "");

        // THEN
        Mockito.verify(listenerTest).propertyChange(any());
    }

    @Test
    public void receiveNoChangeEvent() {
        // GIVEN
        long sessionIdListened = 42;
        long sessionIdIgnored = 123789;
        sut.subscribe(listenerTest, ADVEvent.SNAPSHOT_ADDED, sessionIdListened+"");

        // WHEN
        sut.fire(ADVEvent.SNAPSHOT_ADDED, null, null,
                sessionIdIgnored + "");

        // THEN
        Mockito.verify(listenerTest, never()).propertyChange(any());
    }
}