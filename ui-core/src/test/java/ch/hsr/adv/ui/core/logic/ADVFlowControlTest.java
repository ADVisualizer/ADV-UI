package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.Session;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.mocks.GuiceTestModule;
import ch.hsr.adv.ui.core.logic.mocks.TestLayouter;
import ch.hsr.adv.ui.core.logic.mocks.TestParser;
import ch.hsr.adv.ui.core.logic.mocks.TestSession;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStoreTest;
import ch.hsr.adv.ui.core.presentation.GuiceCoreModule;
import com.google.inject.Inject;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Rather an integration test for the whole flow control load
 */
@RunWith(JukitoRunner.class)
@UseModules( {GuiceCoreModule.class, GuiceTestModule.class})
public class ADVFlowControlTest {

    @Inject
    private TestSession testSession;
    @Inject
    private TestLayouter testLayouter;
    @Inject
    private SessionStore testSessionStore;
    @Inject
    private LayoutedSnapshotStore layoutedSnapshotStore;

    @Inject
    private ADVFlowControl sut;

    @Test
    public void processTest(FileDatastoreAccess reader) throws IOException {
        // GIVEN
        URL url1 = SessionStoreTest.class.getClassLoader()
                .getResource("session1.json");
        String testJSON = reader.read(new File(url1.getPath()));

        // WHEN
        sut.load(testJSON);

        // THEN
        List<Session> sessions = testSessionStore.getAll();
        assertTrue(sessions.contains(testSession.getSession()));
        assertTrue(layoutedSnapshotStore.contains(
                testSession.getSession().getSessionId(),
                testLayouter.getLayoutedSnapshotTest().getSnapshotId()));
    }

    @Test
    public void processDuplicatedSessionTest(FileDatastoreAccess reader)
            throws IOException {
        // GIVEN
        URL url1 = SessionStoreTest.class.getClassLoader()
                .getResource("duplSession.json");
        String testJSON = reader.read(new File(url1.getPath()));

        // WHEN
        sut.load(testJSON);
        sut.load(testJSON);

        // THEN
        assertEquals(1, testSessionStore.getAll().size());
        assertEquals(2, layoutedSnapshotStore.getAll(
                testSession.getSession().getSessionId()).size());
    }

}