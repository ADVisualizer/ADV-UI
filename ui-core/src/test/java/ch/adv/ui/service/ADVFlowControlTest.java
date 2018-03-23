package ch.adv.ui.service;

import ch.adv.ui.ADVModule;
import ch.adv.ui.access.Parser;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.logic.model.Session;
import com.google.inject.Inject;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
          //  bindSpy(Parser.class);
        }
    }

    @Before
    public void setUp() {
       Mockito.doReturn(testSession).when(testParser).parse(any());
        Mockito.doReturn(testParser).when(testModule).getParser();
        Map<String, ADVModule> modules = new HashMap<>();
        modules.put("array", testModule);
        flowControl.setAvailableModules(modules);
    }

    @Inject
    private ADVModule testModule;

    @Inject
    private Parser testParser;

    @Inject
    private Session testSession;

    @Inject
    private SessionStore testStore;

    @Inject
    private ADVFlowControl flowControl;

    @Test
    public void processTest() {
       String testJSON = "{\"module\": \"array\"}";
        flowControl.process(testJSON);
        List<Session> sessions = testStore.getSessions();
        assertTrue(sessions.contains(testSession));
    }
}