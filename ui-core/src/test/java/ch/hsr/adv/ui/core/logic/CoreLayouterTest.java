package ch.hsr.adv.ui.core.logic;

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
import java.util.List;

import static org.junit.Assert.*;
@RunWith(JukitoRunner.class)
public class CoreLayouterTest {
@Inject
private CoreLayouter sut;

private List<Pane> panes = new ArrayList<>();
private List<SplitPane.Divider> dividers  =new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        FxToolkit.registerPrimaryStage();
        panes.add(new Pane());
        panes.add(new Pane());
        panes.add(new Pane());
    }

    @Test
    public void layoutTest() {
        // WHEN
        Region actual = sut.layout(panes, dividers);

        // THEN
        assertEquals(2, dividers.size());
        assertTrue(actual instanceof SplitPane);
    }

    @Test
    public void layoutNonEmptyListTest() {
        // WHEN
        dividers.add(new SplitPane.Divider());
        Region actual = sut.layout(panes, dividers);

        // THEN
        assertEquals(2, dividers.size());
        assertTrue(actual instanceof SplitPane);
    }
}