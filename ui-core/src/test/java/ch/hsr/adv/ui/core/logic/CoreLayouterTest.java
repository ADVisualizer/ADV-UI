package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModulePosition;
import com.google.inject.Inject;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxToolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JukitoRunner.class)
public class CoreLayouterTest {
    @Inject
    private CoreLayouter sut;

    private List<Pane> panes = new ArrayList<>();
    private Map<Pane, ModulePosition> positions = new HashMap<>();
    private List<SplitPane.Divider> dividers = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        FxToolkit.registerPrimaryStage();
        panes.add(new Pane());
        panes.add(new Pane());
        panes.add(new Pane());
        panes.add(new Pane());
        panes.add(new Pane());
        positions.put(panes.get(0), ModulePosition.DEFAULT);
        positions.put(panes.get(1), ModulePosition.LEFT);
        positions.put(panes.get(2), ModulePosition.BOTTOM);
        positions.put(panes.get(3), ModulePosition.RIGHT);
        positions.put(panes.get(4), ModulePosition.TOP);
    }

    @Test
    public void layoutTest() {
        // WHEN
        Region actual = sut.layout(panes, positions, dividers);

        // THEN
        assertEquals(4, dividers.size());
        assertTrue(actual instanceof SplitPane);
    }

    @Test
    public void layoutNonEmptyDividerListTest() {
        // WHEN
        dividers.add(new SplitPane.Divider());
        Region actual = sut.layout(panes, positions, dividers);

        // THEN
        assertEquals(4, dividers.size());
        assertTrue(actual instanceof SplitPane);
    }

    @Test
    public void layoutCreatesCorrectPanesTest() {
        Region actual = sut.layout(panes, positions, dividers);

        SplitPane outerPane = (SplitPane) actual;
        SplitPane innerPane1 = (SplitPane) outerPane.getItems().get(1);
        SplitPane innerPane2 = (SplitPane) innerPane1.getItems().get(0);
        SplitPane innerPane3 = (SplitPane) innerPane2.getItems().get(0);

        assertEquals(panes.get(4), outerPane.getItems().get(0));
        assertEquals(panes.get(3), innerPane1.getItems().get(1));
        assertEquals(panes.get(2), innerPane2.getItems().get(1));
        assertEquals(panes.get(1), innerPane3.getItems().get(0));
        assertEquals(panes.get(0), innerPane3.getItems().get(1));
    }

    @Test
    public void layoutWithMultipleDefaultPanesCreatesCorrectPanesTest() {
        positions.put(panes.get(1), ModulePosition.DEFAULT);
        Region actual = sut.layout(panes, positions, dividers);

        SplitPane outerPane = (SplitPane) actual;
        SplitPane innerPane1 = (SplitPane) outerPane.getItems().get(1);
        SplitPane innerPane2 = (SplitPane) innerPane1.getItems().get(0);
        SplitPane centerPane = (SplitPane) innerPane2.getItems().get(0);
        SplitPane rowPane = (SplitPane) centerPane.getItems().get(0);

        assertEquals(panes.get(0), rowPane.getItems().get(0));
        assertEquals(panes.get(1), rowPane.getItems().get(1));
    }

    @Test
    public void layoutWithoutDefaultPanesCreatesCorrectPanesTest() {
        positions.put(panes.get(0), ModulePosition.TOP);
        Region actual = sut.layout(panes, positions, dividers);

        SplitPane outerPane = (SplitPane) actual;
        SplitPane innerPane1 = (SplitPane) outerPane.getItems().get(1);
        SplitPane innerPane2 = (SplitPane) innerPane1.getItems().get(0);
        SplitPane innerPane3 = (SplitPane) innerPane2.getItems().get(0);

        assertEquals(panes.get(4), outerPane.getItems().get(0));
        assertEquals(panes.get(3), innerPane1.getItems().get(1));
        assertEquals(panes.get(2), innerPane2.getItems().get(1));
        assertEquals(panes.get(1), innerPane3.getItems().get(0));
        assertEquals(panes.get(0), innerPane3.getItems().get(1));
    }

    @Test
    public void layoutWithAllDefaultPanesCreatesCorrectPanesTest() {
        positions.put(panes.get(1), ModulePosition.DEFAULT);
        positions.put(panes.get(2), ModulePosition.DEFAULT);
        positions.put(panes.get(3), ModulePosition.DEFAULT);
        positions.put(panes.get(4), ModulePosition.DEFAULT);
        Region actual = sut.layout(panes, positions, dividers);

        SplitPane centerPane = (SplitPane) actual;
        SplitPane rowPane1 = (SplitPane) centerPane.getItems().get(0);
        SplitPane rowPane2 = (SplitPane) centerPane.getItems().get(1);

        assertEquals(2, centerPane.getItems().size());
        assertEquals(3, rowPane1.getItems().size());
        assertEquals(2, rowPane2.getItems().size());
        assertEquals(panes.get(0), rowPane1.getItems().get(0));
        assertEquals(panes.get(1), rowPane1.getItems().get(1));
        assertEquals(panes.get(2), rowPane1.getItems().get(2));
        assertEquals(panes.get(3), rowPane2.getItems().get(0));
        assertEquals(panes.get(4), rowPane2.getItems().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void layoutEmptyPanesTest() {
        panes.clear();
        positions.clear();
        sut.layout(panes, positions, dividers);
    }
}