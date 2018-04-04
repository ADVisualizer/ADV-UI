package ch.adv.ui.core.service;

import ch.adv.ui.core.access.Parser;
import ch.adv.ui.core.app.ADVModule;
import ch.adv.ui.core.domain.Session;
import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.logic.ModuleStore;
import ch.adv.ui.core.logic.SessionStore;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.LayoutedSnapshotStore;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(JukitoRunner.class)
public class ADVFlowControlTest {

    @Inject
    private ADVModule testModule;
    @Inject
    private Parser testParser;
    @Inject
    private Layouter testLayouter;
    @Inject
    private Session testSession;
    @Inject
    private Snapshot testSnapshot;
    @Inject
    private LayoutedSnapshot testLayoutedSnapshot;
    @Inject
    private Pane testPane;
    @Inject
    private SessionStore testSessionStore;
    @Inject
    private ModuleStore testModuleStore;
    @Inject
    private LayoutedSnapshotStore testLayoutedSnapshotStore;
    @Inject
    private ADVFlowControl flowControl;

    @Before
    public void setUp() {
        List<Snapshot> snapshots = new ArrayList<>();
        snapshots.add(testSnapshot);
        Mockito.when(testSession.getSnapshots()).thenReturn(snapshots);

        testLayoutedSnapshot.setPane(testPane);

        Mockito.doReturn(testSession).when(testParser).parse(any());
        Mockito.doReturn(testParser).when(testModule).getParser();
        Mockito.doReturn(testLayouter).when(testModule).getLayouter();
        Mockito.doReturn(testLayoutedSnapshot).when(testLayouter).layout(any());

        Map<String, ADVModule> modules = new HashMap<>();
        modules.put("testModule", testModule);
        testModuleStore.setAvailableModules(modules);

    }

    @Test
    public void processTest() {
        String testJSON = "{\"moduleName\": \"testModule\"}";
        flowControl.process(testJSON);
        List<Session> sessions = testSessionStore.getSessions();
        assertTrue(sessions.contains(testSession));

        long testSessionId = testSession.getSessionId();
        List<Pane> panes = testLayoutedSnapshotStore.getSnapshotPanes(testSessionId);
        assertTrue(panes.contains(testPane));
    }

    public static class Module extends JukitoModule {

        @Override
        protected void configureTest() {
            forceMock(Session.class);
        }
    }


}