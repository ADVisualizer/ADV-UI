package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.commons.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.mocks.TestSessionProvider;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import com.google.inject.Inject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class StateViewModelTest {
    private StateViewModel sut;
    @Inject
    private TestSessionProvider testSessionProvider;

    @Test
    public void propertiesTwoSnapshotTest(
            RootViewModel rootViewModelMock,
            LayoutedSnapshotStore layoutedSnapshotStoreMock,
            EventManager eventManagerMock,
            StepButtonState stepButtonState
    ) {
        // GIVEN
        setUpTwoSnapshots(rootViewModelMock, layoutedSnapshotStoreMock);

        // WHEN
        sut = new StateViewModel(rootViewModelMock,
                layoutedSnapshotStoreMock, eventManagerMock, stepButtonState);

        // THEN: buttonState
        assertTrue(stepButtonState.getStepFirstBtnDisableProperty().get());
        assertTrue(stepButtonState.getStepBackwardBtnDisableProperty().get());
        assertFalse(stepButtonState.getStepForwardBtnDisableProperty().get());
        assertFalse(stepButtonState.getStepLastBtnDisableProperty().get());

        // THEN: progress
        assertEquals(1, sut.getMaxSnapshotIndex());
        assertEquals(0.5, sut.getProgressProperty().get(), 0.05);
        assertEquals("1", sut.getCurrentIndexStringProperty().get());
        assertEquals("2", sut.getMaxIndexStringProperty().get());
    }

    @Test
    public void propertiesOneSnapshotTest(
            RootViewModel rootViewModelMock,
            LayoutedSnapshotStore layoutedSnapshotStoreMock,
            EventManager eventManagerMock,
            StepButtonState stepButtonState
    ) {
        // GIVEN
        setUpOneSnapshot(rootViewModelMock, layoutedSnapshotStoreMock);

        // WHEN
        sut = new StateViewModel(rootViewModelMock,
                layoutedSnapshotStoreMock, eventManagerMock, stepButtonState);

        // THEN: buttonState
        assertTrue(stepButtonState.getStepFirstBtnDisableProperty().get());
        assertTrue(stepButtonState.getStepBackwardBtnDisableProperty().get());
        assertTrue(stepButtonState.getStepForwardBtnDisableProperty().get());
        assertTrue(stepButtonState.getStepLastBtnDisableProperty().get());

        // THEN: progress
        assertEquals(0, sut.getMaxSnapshotIndex());
        assertEquals(1.0, sut.getProgressProperty().get(), 0.05);
        assertEquals("1", sut.getCurrentIndexStringProperty().get());
        assertEquals("1", sut.getMaxIndexStringProperty().get());
    }

    private void setUpTwoSnapshots(RootViewModel rootViewModelMock,
                                   LayoutedSnapshotStore
                                           layoutedSnapshotStoreMock) {
        List<Region> panes = new ArrayList<>();
        panes.add(new Pane());
        panes.add(new Pane());
        List<LayoutedSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new LayoutedSnapshot(1, new Pane(), new ArrayList<>()));
        snapshots.add(new LayoutedSnapshot(2, new Pane(), new ArrayList<>()));
        when(layoutedSnapshotStoreMock.getAllPanes(123456)).thenReturn(panes);
        when(layoutedSnapshotStoreMock.getAll(123456)).thenReturn(snapshots);
        when(rootViewModelMock.getCurrentSessionProperty())
                .thenReturn(new SimpleObjectProperty<>(testSessionProvider
                        .getSession()));
    }

    private void setUpOneSnapshot(RootViewModel rootViewModelMock,
                                  LayoutedSnapshotStore
                                          layoutedSnapshotStoreMock) {
        List<Region> panes = new ArrayList<>();
        panes.add(new Pane());
        List<LayoutedSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new LayoutedSnapshot(1, new Pane(), new ArrayList<>()));
        when(layoutedSnapshotStoreMock.getAllPanes(123456)).thenReturn(panes);
        when(layoutedSnapshotStoreMock.getAll(123456)).thenReturn(snapshots);
        Session session = testSessionProvider.getSession();
        session.getSnapshots().remove(1);
        when(rootViewModelMock.getCurrentSessionProperty())
                .thenReturn(new SimpleObjectProperty<>(session));
    }


    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(LayoutedSnapshotStore.class);
            forceMock(EventManager.class);
            forceMock(RootViewModel.class);

        }
    }
}