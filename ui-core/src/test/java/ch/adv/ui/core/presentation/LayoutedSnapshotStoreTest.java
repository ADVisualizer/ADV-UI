package ch.adv.ui.core.presentation;

import ch.adv.ui.core.logic.EventManager;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import jdk.jfr.Event;
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
        storeUnderTest.addLayoutedSnapshot(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest.hasSnapshot(sessionId,
                layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, storeUnderTest.getSnapshotPanes(sessionId).size());
        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(0));
        assertEquals(1, storeUnderTest.getLayoutedSnapshots(sessionId).size());
        assertEquals(layoutedSnapshot1, storeUnderTest.getLayoutedSnapshots(sessionId)
                .get(0));
    }

    @Test
    public void mergeSessionTest() {
        storeUnderTest.addLayoutedSnapshot(sessionId, layoutedSnapshot1);
        storeUnderTest.addLayoutedSnapshot(sessionId, layoutedSnapshot2);

        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot2.getSnapshotId()));

        assertEquals(2, storeUnderTest.getSnapshotPanes(sessionId).size());

        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(0));
        assertEquals(pane, storeUnderTest.getSnapshotPanes(sessionId).get(1));

        assertEquals(2, storeUnderTest.getLayoutedSnapshots(sessionId).size());
    }

    @Test
    public void addExistingSessionTest() {
        storeUnderTest.addLayoutedSnapshot(sessionId, layoutedSnapshot1);
        storeUnderTest.addLayoutedSnapshot(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, storeUnderTest.getSnapshotPanes(sessionId).size());
        assertEquals(1, storeUnderTest.getLayoutedSnapshots(sessionId).size());
    }



    @Test
    public void deleteSessionTest() {
        storeUnderTest.addLayoutedSnapshot(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
        storeUnderTest.deleteSession(sessionId);
        assertFalse(storeUnderTest
                .hasSnapshot(sessionId, layoutedSnapshot1.getSnapshotId()));
    }
}