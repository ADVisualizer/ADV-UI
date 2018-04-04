package ch.adv.ui.core.presentation;

import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
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
public class LayoutedSnapshotStoreTest {
    private static final long sessionId = 123456;
    //don't inject these. we need two different instances of each obj
    private static final LayoutedSnapshot layoutedSnapshot1 = new
            LayoutedSnapshot();
    private static final LayoutedSnapshot layoutedSnapshot2 = new
            LayoutedSnapshot();

    @Inject
    private Pane pane;

    @Inject
    private PropertyChangeListener listener;

    @Inject
    private LayoutedSnapshotStore storeUnderTest;

    @Before
    public void setUp() throws Exception {
        layoutedSnapshot1.setPane(pane);
        layoutedSnapshot1.setSnapshotId(1);
        layoutedSnapshot1.setSnapshotDescription("Snapshot 1");

        layoutedSnapshot2.setPane(pane);
        layoutedSnapshot2.setSnapshotId(2);
        layoutedSnapshot2.setSnapshotDescription("Snapshot 2");
    }

    @Test
    public void storeSnapshotTest() {
        storeUnderTest.addWrapper(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest.hasSnapshot(sessionId,
                layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, storeUnderTest.getSnapshotPanes(sessionId).size());
        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(0));
        assertEquals(1, storeUnderTest.getWrappers(sessionId).size());
        assertEquals(layoutedSnapshot1, storeUnderTest.getWrappers(sessionId)
                .get(0));
    }

    @Test
    public void mergeSessionTest() {
        storeUnderTest.addWrapper(sessionId, layoutedSnapshot1);
        storeUnderTest.addWrapper(sessionId, layoutedSnapshot2);

        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot2.getSnapshotId()));

        assertEquals(2, storeUnderTest.getSnapshotPanes(sessionId).size());

        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(0));
        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(1));

        assertEquals(2, storeUnderTest.getWrappers(sessionId).size());
    }

    @Test
    public void addExistingSessionTest() {
        storeUnderTest.addWrapper(sessionId, layoutedSnapshot1);
        storeUnderTest.addWrapper(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, storeUnderTest.getSnapshotPanes(sessionId).size());
        assertEquals(1, storeUnderTest.getWrappers(sessionId).size());
    }


    @Test
    public void receiveChangeEventOnMySessionTest() {
        storeUnderTest.addPropertyChangeListener(sessionId, listener);
        storeUnderTest.addWrapper(sessionId, layoutedSnapshot1);

        Mockito.verify(listener).propertyChange(any());
    }

    @Test
    public void receiveNoChangeEventOnOtherSessionTest() {
        storeUnderTest.addPropertyChangeListener(sessionId, listener);
        long otherId = 123789;
        storeUnderTest.addWrapper(otherId, layoutedSnapshot1);

        Mockito.verify(listener, Mockito.times(0)).propertyChange(any());
    }

    @Test
    public void deleteSessionTest() {
        storeUnderTest.addWrapper(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
        storeUnderTest.deleteSession(sessionId);
        assertFalse(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
    }
}