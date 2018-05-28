package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.Session;
import ch.hsr.adv.ui.core.access.DatastoreAccess;
import ch.hsr.adv.ui.core.logic.ADVFlowControl;
import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.mocks.TestSession;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStore;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

/**
 * Rather an integration test for the whole flow control load
 */
@RunWith(JukitoRunner.class)
public class ADVFlowControlMockedTest {

    @Inject
    private TestSession testSession;
    @Inject
    private SessionStore testSessionStore;
    @Inject
    private LayoutedSnapshotStore layoutedSnapshotStore;

    @Inject
    private ADVFlowControl sut;

    @Before
    public void setUp() {
        Session session = testSession.getSession();
        session.getSnapshots().forEach(s -> {
            LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot(1, new Pane
                    (), new ArrayList<>());
            layoutedSnapshot.setSnapshotDescription(s.getSnapshotDescription());
            layoutedSnapshotStore.add(session.getSessionId(), layoutedSnapshot);
        });
        testSessionStore.add(session);
    }

    @Test
    public void saveSessionTest(EventManager eventManagerMock, DatastoreAccess accessMock) throws
            IOException {
        // WHEN
        sut.save(testSession.getSession(), null);

        // THEN
        verify(eventManagerMock, atLeast(1)).fire(any(), any(), any());
        verify(accessMock, atLeast(1)).write(any(), any());

    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(EventManager.class);

        }
    }

}