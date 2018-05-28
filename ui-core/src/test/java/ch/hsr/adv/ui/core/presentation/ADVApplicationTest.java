package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.mocks.GuiceTestModule;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStore;
import ch.hsr.adv.ui.core.service.SocketServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.control.Button;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.junit.Assert.assertEquals;


@RunWith(JukitoRunner.class)
public class ADVApplicationTest extends FxRobot {


    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(
                new GuiceCoreModule(),
                new GuiceTestModule()
        );
        ADVApplication.setInjector(injector);
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(ADVApplication.class);
    }

    @Test
    public void loadSessionTest() {
        Button loadButton = lookup("#loadSessionButton").query();
        assertEquals("Load", loadButton.getText());
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(SocketServer.class);
            forceMock(SessionStore.class);
            forceMock(LayoutedSnapshotStore.class);
            forceMock(EventManager.class);
        }
    }
}