package ch.hsr.adv.ui.core.logic.stores;

import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
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

    @Inject
    private LayoutedSnapshotStore sut;

    @Inject
    private Pane pane;

    //don't inject these. we need two different instances
    private LayoutedSnapshot layoutedSnapshot1;
    private LayoutedSnapshot layoutedSnapshot2;

    @Before
    public void setUp() {
        layoutedSnapshot1 = new LayoutedSnapshot(1, pane, null);
        layoutedSnapshot1.setSnapshotDescription("Snapshot 1");
        layoutedSnapshot2 = new LayoutedSnapshot(2, pane, null);
        layoutedSnapshot2.setSnapshotDescription("Snapshot 2");
    }

    @Test
    public void storeSnapshotTest() {
        // WHEN
        sut.add(sessionId, layoutedSnapshot1);

        // THEN
        assertTrue(sut.contains(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, sut.getAllPanes(sessionId).size());
        assertEquals(pane, sut.getAllPanes(sessionId).get(0));
        assertEquals(1, sut.getAllPanes(sessionId).size());
        assertEquals(layoutedSnapshot1, sut.getAll(sessionId).get(0));
    }

    @Test
    public void mergeSessionTest() {
        // WHEN
        sut.add(sessionId, layoutedSnapshot1);
        sut.add(sessionId, layoutedSnapshot2);

        // THEN
        assertTrue(sut.contains(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertTrue(sut.contains(sessionId, layoutedSnapshot2.getSnapshotId()));
        assertEquals(2, sut.getAllPanes(sessionId).size());
        assertEquals(pane, sut.getAllPanes(sessionId).get(0));
        assertEquals(pane, sut.getAllPanes(sessionId).get(1));
        assertEquals(2, sut.getAll(sessionId).size());
    }

    @Test
    public void addExistingSessionTest() {
        // THEN
        sut.add(sessionId, layoutedSnapshot1);
        sut.add(sessionId, layoutedSnapshot1);

        // THEN
        assertTrue(sut.contains(sessionId, layoutedSnapshot1.getSnapshotId()));
        assertEquals(1, sut.getAllPanes(sessionId).size());
        assertEquals(1, sut.getAll(sessionId).size());
    }


    @Test
    public void deleteSessionTest() {
        // GIVEN
        sut.add(sessionId, layoutedSnapshot1);
        assertTrue(sut.contains(sessionId, layoutedSnapshot1.getSnapshotId()));

        // WHEN
        sut.deleteAll(sessionId);

        // THEN
        assertFalse(sut.contains(sessionId, layoutedSnapshot1.getSnapshotId()));
    }
}