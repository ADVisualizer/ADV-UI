package ch.adv.ui.service;

import ch.adv.ui.access.Parser;
import ch.adv.ui.logic.ADVModule;
import ch.adv.ui.logic.ModuleStore;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.model.Snapshot;
import ch.adv.ui.presentation.Layouter;
import ch.adv.ui.presentation.SnapshotStore;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(JukitoRunner.class)
public class ADVFlowControlTest {

    public static class Module extends JukitoModule {

        @Override
        protected void configureTest() {
            forceMock(Session.class);
        }
    }


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
    private Pane testPane;
    @Inject
    private SessionStore testSessionStore;
    @Inject
    private ModuleStore testModuleStore;
    @Inject
    private SnapshotStore testSnapshotStore;
    @Inject
    private ADVFlowControl flowControl;

    @Before
    public void setUp() {
        Mockito.doReturn(testSnapshot).when(testSession).getFirstSnapshot();
        Mockito.doReturn(testSession).when(testParser).parse(any());
        Mockito.doReturn(testParser).when(testModule).getParser();
        Mockito.doReturn(testLayouter).when(testModule).getLayouter();
        Mockito.doReturn(testPane).when(testLayouter).layout(any());

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

        List<Pane> snapshots = testSnapshotStore.getSnapshotPanes(testSession
                .getSessionId());
        assertTrue(snapshots.contains(testPane));
    }


}