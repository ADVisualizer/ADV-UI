package ch.hsr.adv.ui.core.logic;

import com.google.inject.Singleton;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The Core Layouter arranges the panes created by the module layouters in a
 * grid formation using SplitPanes.
 *
 * @author mtrentini
 */
@Singleton
public class CoreLayouter {
    private static final Logger logger = LoggerFactory.getLogger(
            CoreLayouter.class);

    /**
     * Arranges child panes in a grid formation. All created SplitPane
     * Dividers will be stored in the supplied list. The dividers of all
     * snapshots in a session are bound together so that the user can
     * rearrange them for a session.
     *
     * @param dividers empty list to store split pane dividers
     * @param panes    child panes of the module-specific layouter
     * @return split pane
     */
    public Region layout(List<Pane> panes,
                         List<SplitPane.Divider> dividers) {
        logger.debug("Creating grid layout for all active modules...");
        if (!dividers.isEmpty()) {
            logger.info("Expecting an empty list of dividers. Correcting...");
            dividers.clear();
        }
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
            dividers.addAll(rowPane.getDividers());
        }
        dividers.addAll(parentPane.getDividers());
        return parentPane;
    }

    private SplitPane addNewRow(SplitPane parentPane) {
        SplitPane rowPane = new SplitPane();
        rowPane.setOrientation(Orientation.HORIZONTAL);
        parentPane.getItems().add(rowPane);
        return rowPane;
    }
}
