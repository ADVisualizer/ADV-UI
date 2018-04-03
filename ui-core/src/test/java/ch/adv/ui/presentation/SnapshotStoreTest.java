package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Snapshot;
import ch.adv.ui.presentation.model.SnapshotWrapper;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.beans.PropertyChangeListener;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(JukitoRunner.class)
public class SnapshotStoreTest {
    private static final long sessionId = 123456;
    //don't inject these. we need two different instances of each obj
    private Snapshot snapshot1 = new Snapshot();
    private Snapshot snapshot2 = new Snapshot();
    private SnapshotWrapper wrapper1 = new SnapshotWrapper();
    private SnapshotWrapper wrapper2 = new SnapshotWrapper();

    @Inject
    private Pane pane;

    @Inject
    private PropertyChangeListener listener;

    @Inject
    private SnapshotStore storeUnderTest;

    @Before
    public void setUp() throws Exception {
        wrapper1.setPane(pane);
        wrapper1.setSnapshot(snapshot1);
        wrapper2.setPane(pane);
        wrapper2.setSnapshot(snapshot2);
    }

    @Test
    public void storeSnapshotTest() {
        storeUnderTest.addWrapper(sessionId, wrapper1);
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, snapshot1));
        assertEquals(1, storeUnderTest.getSnapshotPanes(sessionId).size());
        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(0));
        assertEquals(1, storeUnderTest.getWrappers(sessionId).size());
        assertEquals(wrapper1, storeUnderTest.getWrappers(sessionId).get(0));
    }

    @Test
    public void mergeSessionTest() {
        storeUnderTest.addWrapper(sessionId, wrapper1);
        storeUnderTest.addWrapper(sessionId, wrapper2);

        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, snapshot1));
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, snapshot2));

        assertEquals(2, storeUnderTest.getSnapshotPanes(sessionId).size());

        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(0));
        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(1));

        assertEquals(2, storeUnderTest.getWrappers(sessionId).size());
    }


    @Test
    public void receiveChangeEventOnMySessionTest() {
        storeUnderTest.addPropertyChangeListener(sessionId, listener);
        storeUnderTest.addWrapper(sessionId, wrapper1);

        Mockito.verify(listener).propertyChange(any());
    }

    @Test
    public void receiveNoChangeEventOnOtherSessionTest() {
        storeUnderTest.addPropertyChangeListener(sessionId, listener);
        long otherId = 123789;
        storeUnderTest.addWrapper(otherId, wrapper1);

        Mockito.verify(listener, Mockito.times(0)).propertyChange(any());
    }

    @Test
    public void deleteSessionTest() {
        storeUnderTest.addWrapper(sessionId, wrapper1);
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, snapshot1));
        storeUnderTest.deleteSession(sessionId);
        assertFalse(storeUnderTest
                .hasSnapshot(sessionId, snapshot1));
    }
}