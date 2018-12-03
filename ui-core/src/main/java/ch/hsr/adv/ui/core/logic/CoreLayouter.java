package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModulePosition;
import com.google.inject.Singleton;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
     * @param panes     child panes of the module-specific layouter
     * @param positions desired positions of the panes
     * @param dividers  empty list to store split pane dividers
     * @return split pane
     */
    public Region layout(List<Pane> panes,
                         Map<Pane, ModulePosition> positions,
                         List<SplitPane.Divider> dividers) {
        logger.info("Creating grid layout for all active modules...");

        checkParameters(panes, positions, dividers);

        Queue<Pane> positionedPanes = new LinkedList<>();
        Queue<Pane> defaultPanes = new LinkedList<>();

        Pane mainPane = panes.get(0);
        defaultPanes.add(mainPane);

        for (int i = 1; i < panes.size(); i++) {
            Pane currentPane = panes.get(i);
            if (positions.get(currentPane) == ModulePosition.DEFAULT) {
                defaultPanes.add(currentPane);
            } else {
                positionedPanes.add(currentPane);
            }
        }

        return positionPanes(positions, dividers, positionedPanes,
                defaultPanes);
    }

    private void checkParameters(List<Pane> panes,
                                 Map<Pane, ModulePosition> positions,
                                 List<SplitPane.Divider> dividers) {
        if (panes.size() <= 0) {
            String message = "panes must contain at least one element";
            logger.error(message);
            throw new IllegalArgumentException(message);
        } else if (panes.size() != positions.size()) {
            String message = "panes and positions must have equal size";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        if (!dividers.isEmpty()) {
            logger.info("Expecting an empty list of dividers. Correcting...");
            dividers.clear();
        }
    }

    private Region positionPanes(Map<Pane, ModulePosition> positions,
                                 List<SplitPane.Divider> dividers,
                                 Queue<Pane> positionedPanes,
                                 Queue<Pane> defaultPanes) {
        Region centerPane;

        if (defaultPanes.size() > 1) {
            centerPane = generateCenterPane(defaultPanes, dividers);
        } else {
            centerPane = defaultPanes.poll();
        }

        while (!positionedPanes.isEmpty()) {
            Pane nextPane = positionedPanes.poll();
            ModulePosition position = positions.get(nextPane);
            centerPane = positionPane(dividers, centerPane, nextPane, position);
        }
        return centerPane;
    }

    private Region positionPane(List<SplitPane.Divider> dividers,
                                Region centerPane, Pane nextPane,
                                ModulePosition position) {
        SplitPane newPane = new SplitPane();
        switch (position) {
            case LEFT:
                newPane.setOrientation(Orientation.HORIZONTAL);
                newPane.getItems().addAll(nextPane, centerPane);
                break;
            case TOP:
                newPane.setOrientation(Orientation.VERTICAL);
                newPane.getItems().addAll(nextPane, centerPane);
                break;
            case RIGHT:
                newPane.setOrientation(Orientation.HORIZONTAL);
                newPane.getItems().addAll(centerPane, nextPane);
                break;
            case BOTTOM:
                newPane.setOrientation(Orientation.VERTICAL);
                newPane.getItems().addAll(centerPane, nextPane);
                break;
            default:
                String message = "Unknown state \"" + position + "\"";
                logger.error(message);
                throw new IllegalArgumentException(message);
        }
        dividers.addAll(newPane.getDividers());
        return newPane;
    }

    private SplitPane generateCenterPane(Queue<Pane> defaultPanes,
                                         List<SplitPane.Divider> dividers) {
        SplitPane centerPane = new SplitPane();
        centerPane.setOrientation(Orientation.VERTICAL);

        // calculate dimensions of the grid
        int columns = (int) Math.ceil(Math.sqrt(defaultPanes.size()));
        int rows = (int) Math.ceil(defaultPanes.size() / (double) columns);

        // fill the grid
        SplitPane rowPane;
        for (int rowCount = 0; rowCount < rows; rowCount++) {
            rowPane = addNewRow(centerPane);
            for (int colCount = 0; colCount < columns; colCount++) {
                if (!defaultPanes.isEmpty()) {
                    rowPane.getItems().add(defaultPanes.poll());
                }
            }
            dividers.addAll(rowPane.getDividers());
        }
        dividers.addAll(centerPane.getDividers());

        return centerPane;
    }

    private SplitPane addNewRow(SplitPane parentPane) {
        SplitPane rowPane = new SplitPane();
        rowPane.setOrientation(Orientation.HORIZONTAL);
        parentPane.getItems().add(rowPane);
        return rowPane;
    }
}
