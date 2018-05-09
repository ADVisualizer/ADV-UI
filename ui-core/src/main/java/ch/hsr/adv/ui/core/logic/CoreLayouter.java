package ch.hsr.adv.ui.core.logic;

import com.google.inject.Singleton;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        Queue<Pane> remainingPanes = new LinkedList<>(panes);
        SplitPane parentPane = new SplitPane();
        parentPane.setOrientation(Orientation.VERTICAL);

        // calculate dimensions of the grid
        int columns = 1;
        int rows = 1;
        while (columns * rows < remainingPanes.size()) {
            columns++;
            if (columns * rows < remainingPanes.size()) {
                rows++;
            }
        }

        // fill the grid
        SplitPane rowPane;
        for (int rowCount = 0; rowCount < rows; rowCount++) {
            rowPane = addNewRow(parentPane);
            for (int colCount = 0; colCount < columns; colCount++) {
                if (!remainingPanes.isEmpty()) {
                    rowPane.getItems().add(remainingPanes.poll());
                }
            }
        }

        return parentPane;
    }

    private SplitPane addNewRow(SplitPane parentPane) {
        SplitPane rowPane = new SplitPane();
        rowPane.setOrientation(Orientation.HORIZONTAL);
        parentPane.getItems().add(rowPane);
        return rowPane;
    }
}
