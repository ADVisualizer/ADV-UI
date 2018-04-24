package ch.adv.ui.core.logic;

import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JukitoRunner.class)
public class LayoutedSnapshotStoreTest {
    private static final long sessionId = 123456;
    //don't inject these. we need two different instances of each obj

    @Inject
    private Pane pane;

    private LayoutedSnapshot layoutedSnapshot1;
    private LayoutedSnapshot layoutedSnapshot2;

    @Inject
    private LayoutedSnapshotStore storeUnderTest;


    @Before
    public void setUp() throws Exception {
        layoutedSnapshot1 = new LayoutedSnapshot(1, pane);
        layoutedSnapshot1.setSnapshotDescription("Snapshot 1");
        layoutedSnapshot2 = new LayoutedSnapshot(2, pane);
        layoutedSnapshot2.setSnapshotDescription("Snapshot 2");
    }

    @Test
    public void storeSnapshotTest() {
        storeUnderTest.add(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest.contains(sessionId,
                layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, storeUnderTest.getAllPanes(sessionId).size());
        assertEquals(pane, storeUnderTest.getAllPanes(sessionId).get(0));
        assertEquals(1, storeUnderTest.getAllPanes(sessionId).size());
        assertEquals(layoutedSnapshot1, storeUnderTest
                .getAll(sessionId)
                .get(0));
    }

    @Test
    public void mergeSessionTest() {
        storeUnderTest.add(sessionId, layoutedSnapshot1);
        storeUnderTest.add(sessionId, layoutedSnapshot2);

        assertTrue(storeUnderTest
                .contains(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertTrue(storeUnderTest
                .contains(sessionId, layoutedSnapshot2.getSnapshotId()));

        assertEquals(2, storeUnderTest.getAllPanes(sessionId).size());

        assertEquals(pane, storeUnderTest.getAllPanes(sessionId).get(0));
        assertEquals(pane, storeUnderTest.getAllPanes(sessionId).get(1));

        assertEquals(2, storeUnderTest.getAll(sessionId).size());
    }

    @Test
    public void addExistingSessionTest() {
        storeUnderTest.add(sessionId, layoutedSnapshot1);
        storeUnderTest.add(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest
                .contains(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, storeUnderTest.getAllPanes(sessionId).size());
        assertEquals(1, storeUnderTest.getAll(sessionId).size());
    }


    @Test
    public void deleteSessionTest() {
        storeUnderTest.add(sessionId, layoutedSnapshot1);
        assertTrue(storeUnderTest
                .contains(sessionId, layoutedSnapshot1.getSnapshotId()));
        storeUnderTest.deleteAll(sessionId);
        assertFalse(storeUnderTest
                .contains(sessionId, layoutedSnapshot1.getSnapshotId()));
    }
}