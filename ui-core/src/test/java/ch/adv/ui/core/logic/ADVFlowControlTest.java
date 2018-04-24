package ch.adv.ui.core.logic;

import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.adv.ui.core.logic.domain.Session;
import ch.adv.ui.core.presentation.GuiceBaseModule;
import com.google.gson.Gson;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@RunWith(JukitoRunner.class)
@UseModules({GuiceBaseModule.class})
public class ADVFlowControlTest {
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private Gson gson;
    @Inject
    private ADVModule testModule;
    @Inject
    private Parser testParser;
    @Inject
    private Layouter testLayouter;
    @Inject
    private Session testSession;
    @Inject
    private Pane testPane;
    @Inject
    private ModuleStore testModuleStore;
    @Inject
    private SessionStore testSessionStore;
    @Inject
    private LayoutedSnapshotStore testLayoutedSnapshotStore;
    @Inject
    private FlowControl flowControl;

    private LayoutedSnapshot testLayoutedSnapshot;
    private String testJSON;

    @Before
    public void setUp() throws IOException {
        URL url1 = SessionStoreTest.class.getClassLoader()
                .getResource("session1.json");

        testJSON = reader.read(new File(url1.getPath()));

        testSession = gson.fromJson(testJSON, Session.class);
        testLayoutedSnapshot = new LayoutedSnapshot(1, testPane);

        Map<String, ADVModule> modules = new HashMap<>();
        modules.put("test", testModule);
        testModuleStore.setAvailableModules(modules);

        Mockito.doReturn(testParser).when(testModule).getParser();
        Mockito.doReturn(testLayouter).when(testModule).getLayouter();

        Mockito.doReturn(testSession).when(testParser).parse(any());
        Mockito.doReturn(testLayoutedSnapshot).when(testLayouter).
                layout(any(), anyList());
    }

    @Test
    public void processTest() {
        flowControl.process(testJSON);
        List<Session> sessions = testSessionStore.getAll();
        assertTrue(sessions.contains(testSession));

        long testSessionId = testSession.getSessionId();
        List<Pane> panes = testLayoutedSnapshotStore
                .getAllPanes(testSessionId);
        assertTrue(panes.contains(testPane));
        assertTrue(testLayoutedSnapshotStore
                .contains(testSessionId, testLayoutedSnapshot
                        .getSnapshotId()));
    }


    @Test
    public void processDuplicatedSessionTest() {
        flowControl.process(testJSON);
        flowControl.process(testJSON);
        assertEquals(1, testSessionStore.getAll().size());
        assertEquals(1, testLayoutedSnapshotStore
                .getAll(testSession.getSessionId()).size());
    }
}