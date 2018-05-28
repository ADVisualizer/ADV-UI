package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStore;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(JukitoRunner.class)
public class ReplayViewModelTest {
    private ReplayViewModel sut;

    @Before
    public void setUp(SteppingViewModel steppingViewModelMock,
                      StateViewModel stateViewModelMock,
                      ReplayController replayController,
                      SessionReplayFactory sessionReplayFactory) throws
            Exception {
        sut = new ReplayViewModel(sessionReplayFactory, replayController,
                stateViewModelMock, steppingViewModelMock);

        when(stateViewModelMock.getReplayingProperty()).thenReturn(mock(BooleanProperty.class));
    }

    @Test
    public void replayTest(StateViewModel stateViewModelMock) {
        // GIVEN
        BooleanProperty propertyMock = stateViewModelMock
                .getReplayingProperty();

        // WHEN
        sut.replay();

        // THEN
        verify(propertyMock).set(true);
    }

    @Test
    public void pauseReplayTest(StateViewModel stateViewModelMock) {
        // GIVEN
        BooleanProperty propertyMock = stateViewModelMock
                .getReplayingProperty();

        // WHEN
        sut.replay();
        sut.replay();

        // THEN
        verify(propertyMock, atLeastOnce()).set(true);
        verify(propertyMock).set(false);
    }

    @Test
    public void cancelReplayTest(SteppingViewModel steppingViewModelMock) {
        // WHEN
        sut.cancelReplay();

        // THEN
        verify(steppingViewModelMock).navigateSnapshot(Navigate.FIRST);
    }


    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(LayoutedSnapshotStore.class);
            forceMock(SessionStore.class);
            forceMock(EventManager.class);
            forceMock(StateViewModel.class);
            forceMock(SteppingViewModel.class);
            install(new FactoryModuleBuilder()
                    .build(SessionReplayFactory.class));
        }
    }
}