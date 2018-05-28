package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.events.ADVEvent;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.mocks.TestSessionProvider;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStore;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(JukitoRunner.class)
public class SteppingViewModelTest {

    @Inject
    private TestSessionProvider testSessionProvider;
    private SteppingViewModel sut;

    @Before
    public void setUp(RootViewModel rootViewModel,
                      StateViewModel stateViewModelMock,
                      LayoutedSnapshotStore layoutedSnapshotStoreMock,
                      EventManager eventManagerMock)
            throws Exception {
        rootViewModel.getAvailableSessions()
                .add(testSessionProvider.getSession());
        rootViewModel.getCurrentSessionProperty()
                .set(testSessionProvider.getSession());

        sut = new SteppingViewModel(layoutedSnapshotStoreMock,
                eventManagerMock, stateViewModelMock);

        List<LayoutedSnapshot> snapshots = new ArrayList<>();
        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot(
                1, new Pane(), new ArrayList<>());
        snapshots.add(layoutedSnapshot);

        doReturn(snapshots).when(layoutedSnapshotStoreMock).getAll(0);
        doReturn(true).when(stateViewModelMock).isAllowedIndex(0);
    }

    @Test
    public void navigateFirstSnapshotTest(
            EventManager managerMock,
            StateViewModel stateViewModelMock) {
        // WHEN
        sut.navigateSnapshot(Navigate.FIRST);


        // THEN
        verify(stateViewModelMock).setCurrentSnapshotIndex(0);
        verify(managerMock).fire(eq(ADVEvent.STEP_FIRST), any(), any());
    }

    @Test
    public void navigateLastSnapshotTest(
            EventManager managerMock,
            StateViewModel stateViewModelMock) {
        // WHEN
        sut.navigateSnapshot(Navigate.LAST);

        // THEN
        verify(stateViewModelMock).setCurrentSnapshotIndex(0);
        verify(managerMock).fire(eq(ADVEvent.STEP_LAST), any(), any());
    }

    @Test
    public void navigateForwardSnapshotTest(
            EventManager managerMock,
            StateViewModel stateViewModelMock) {
        // WHEN
        sut.navigateSnapshot(Navigate.FORWARD);

        // THEN
        verify(stateViewModelMock).setCurrentSnapshotIndex(1);
        verify(managerMock, times(0))
                .fire(eq(ADVEvent.STEP_FORWARD), any(), any());
    }

    @Test
    public void navigateBackwardSnapshotTest(
            EventManager managerMock,
            StateViewModel stateViewModelMock) {
        // WHEN
        sut.navigateSnapshot(Navigate.BACKWARD);

        // THEN
        verify(stateViewModelMock).setCurrentSnapshotIndex(-1);
        verify(managerMock, times(0))
                .fire(eq(ADVEvent.STEP_BACKWARD), any(), any());
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(LayoutedSnapshotStore.class);
            forceMock(SessionStore.class);
            forceMock(EventManager.class);
            forceMock(StateViewModel.class);

        }
    }
}