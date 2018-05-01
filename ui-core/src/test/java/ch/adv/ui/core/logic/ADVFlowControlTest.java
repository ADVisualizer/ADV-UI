package ch.adv.ui.core.logic;

import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.logic.domain.Session;
import ch.adv.ui.core.logic.mocks.GuiceTestModule;
import ch.adv.ui.core.logic.mocks.TestLayouter;
import ch.adv.ui.core.logic.mocks.TestParser;
import ch.adv.ui.core.logic.mocks.TestStringifyer;
import ch.adv.ui.core.presentation.GuiceCoreModule;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JukitoRunner.class)
@UseModules( {GuiceCoreModule.class, GuiceTestModule.class})
public class ADVFlowControlTest {

    @Inject
    private TestParser testParser;
    @Inject
    private TestLayouter testLayouter;
    @Inject
    private TestStringifyer stringifyer;
    @Inject
    private SessionStore testSessionStore;
    @Inject
    private LayoutedSnapshotStore testLayoutedSnapshotStore;
    @Inject
    private ADVFlowControl flowControlUnderTest;

    @Test
    public void processTest() {
        flowControlUnderTest.process(stringifyer.getTestJSON());
        List<Session> sessions = testSessionStore.getAll();
        assertTrue(sessions.contains(testParser.getTestSession()));

        long testSessionId = testParser.getTestSession().getSessionId();
        List<Pane> panes = testLayoutedSnapshotStore
                .getAllPanes(testSessionId);
        assertTrue(panes.contains(testLayouter.getTestPane()));
        assertTrue(testLayoutedSnapshotStore
                .contains(testSessionId, testLayouter.getTestLayoutedSnapshot()
                        .getSnapshotId()));
    }


    @Test
    public void processDuplicatedSessionTest() {
        String testJSON = stringifyer.getTestJSON();
        flowControlUnderTest.process(testJSON);
        flowControlUnderTest.process(testJSON);
        assertEquals(1, testSessionStore.getAll().size());
        assertEquals(1, testLayoutedSnapshotStore
                .getAll(testParser.getTestSession().getSessionId()).size());
    }


}