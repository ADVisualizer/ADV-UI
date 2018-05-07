package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.mocks.GuiceTestModule;
import ch.hsr.adv.ui.core.logic.mocks.TestLayouter;
import ch.hsr.adv.ui.core.logic.mocks.TestSession;
import ch.hsr.adv.ui.core.logic.mocks.TestStringifyer;
import ch.hsr.adv.ui.core.presentation.GuiceCoreModule;
import com.google.inject.Inject;
import javafx.scene.layout.Region;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Rather an integration test for the whole flow control process
 */
@RunWith(JukitoRunner.class)
@UseModules( {GuiceCoreModule.class, GuiceTestModule.class})
public class ADVFlowControlTest {

    @Inject
    private TestSession testSession;
    @Inject
    private TestLayouter testLayouter;
    @Inject
    private TestStringifyer stringifyer;
    @Inject
    private SessionStore testSessionStore;
    @Inject
    private LayoutedSnapshotStore layoutedSnapshotStore;

    @Inject
    private ADVFlowControl sut;

    @Test
    public void processTest() {
        // WHEN
        sut.process(stringifyer.getTestJSON());

        // THEN
        List<Session> sessions = testSessionStore.getAll();
        assertTrue(sessions.contains(testSession.getSession()));

        long testSessionId = testSession.getSession().getSessionId();
        List<Region> panes = layoutedSnapshotStore.getAllPanes(testSessionId);
        assertTrue(panes.contains(testLayouter.getTestPane()));
        assertTrue(layoutedSnapshotStore.contains(testSessionId,
                testLayouter.getLayoutedSnapshotTest().getSnapshotId()));
    }

    @Test
    public void processDuplicatedSessionTest() {
        // GIVEN
        String testJSON = stringifyer.getTestJSON();
        sut.process(testJSON);
        sut.process(testJSON);
        assertEquals(1, testSessionStore.getAll().size());
        assertEquals(1, layoutedSnapshotStore.getAll(
                testSession.getSession().getSessionId()).size());
    }


}