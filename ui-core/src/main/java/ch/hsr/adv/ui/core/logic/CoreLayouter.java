package ch.hsr.adv.ui.core.logic;

import com.google.inject.Singleton;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.List;

/**
 * The Core Layouter wraps the panes of the module layouter in a SplitPane.
 */
@Singleton
public class CoreLayouter {

    /**
     * Wraps child panes in a SplitPane
     *
     * @param panes child panes of the module-specific layouter
     * @return split pane
     */
    public Region layout(List<Pane> panes) {
        SplitPane parentPane = new SplitPane();
        parentPane.setOrientation(Orientation.HORIZONTAL);
        parentPane.getItems().addAll(panes);
        return parentPane;
    }
}
