package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.commons.core.logic.domain.Session;
import ch.hsr.adv.ui.core.access.DatastoreAccess;
import ch.hsr.adv.ui.core.logic.FlowControl;
import ch.hsr.adv.ui.core.logic.mocks.TestSessionProvider;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStore;
import com.google.inject.Inject;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxToolkit;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class RootViewModelTest {
    @Inject
    private TestSessionProvider testSessionProvider;
    @Inject
    private RootViewModel sut;


    @Before
    public void setUp() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void saveSessionE2ETest(FlowControl flowControlMock) {
        // WHEN
        sut.saveSession(null);

        // THEN
        verify(flowControlMock).save(any(), any());
    }

    @Test
    public void loadSessionE2ETest(FlowControl flowControlMock,
                                   DatastoreAccess accessMock) throws
            IOException {
        // WHEN
        File f = new File("test");
        sut.loadSession(f);

        // THEN
        verify(accessMock).read(f);
        verify(flowControlMock).load(any());

    }

    @Test
    public void removeCurrentSessionTest(LayoutedSnapshotStore
                                                 layoutedSnapshotStoreMock,
                                         SessionStore sessionStoreMock) {
        assertTrue(sut.getNoSessionsProperty().get());

        // GIVEN: manually add a new session
        Session newSession = testSessionProvider.getSession();
        sut.getCurrentSessionProperty().setValue(newSession);
        sut.getAvailableSessions().add(newSession);
        sut.getNoSessionsProperty().set(false);

        // WHEN
        sut.removeCurrentSession();

        // THEN
        verify(layoutedSnapshotStoreMock).deleteAll(newSession.getSessionId());
        verify(sessionStoreMock).delete(newSession.getSessionId());
        assertNotEquals(newSession, sut.getCurrentSessionProperty().get());
    }

    @Test
    public void clearAllSessionsTest(LayoutedSnapshotStore
                                             layoutedSnapshotStoreMock,
                                     SessionStore sessionStoreMock) {
        // GIVEN: manually add two new sessions
        Session newSession = testSessionProvider.getSession();
        sut.getCurrentSessionProperty().setValue(newSession);
        sut.getAvailableSessions().add(newSession);
        sut.getAvailableSessions().add(newSession);
        sut.getNoSessionsProperty().set(false);

        // WHEN
        sut.clearAllSessions();

        // THEN
        verify(layoutedSnapshotStoreMock, times(2))
                .deleteAll(newSession.getSessionId());
        verify(sessionStoreMock, times(2)).delete(newSession.getSessionId());
        assertFalse(newSession.equals(sut.getCurrentSessionProperty().get()));
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(LayoutedSnapshotStore.class);
            forceMock(SessionStore.class);
            forceMock(FlowControl.class);

        }
    }
}