package ch.hsr.adv.ui.core.presentation.widgets;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Auto-scales its content according to the available space of the Pane.
 * The content is always centered
 *
 * @author mwieland
 */
public class AutoScalePane extends Pane {

    private static final double MAX_SCALE = 2;
    private static final int DEFAULT_PADDING = 10;
    private Group content = new Group();

    public AutoScalePane() {
        this(DEFAULT_PADDING);
    }

    public AutoScalePane(int padding) {
        getChildren().add(content);
        setPadding(new Insets(padding));
    }

    /**
     * Adds nodes to the AutoScalePane
     *
     * @param children nodes
     */
    public void addChildren(Node... children) {
        content.getChildren().addAll(children);
        requestLayout();
    }

    @Override
    protected void layoutChildren() {

        final double paneWidth = getWidth();
        final double paneHeight = getHeight();
        final double insetTop = getInsets().getTop();
        final double insetRight = getInsets().getRight();
        final double insetLeft = getInsets().getLeft();
        final double insertBottom = getInsets().getBottom();

        final double contentWidth = (paneWidth - insetLeft - insetRight);
        final double contentHeight = (paneHeight - insetTop - insertBottom);

        // zoom
        final Bounds groupBounds = content.getBoundsInLocal();
        double factorX = contentWidth / groupBounds.getWidth();
        double factorY = contentHeight / groupBounds.getHeight();
        double factor = Math.min(factorX, factorY);
        factor = Math.min(factor, MAX_SCALE);
        content.setScaleX(factor);
        content.setScaleY(factor);

        layoutInArea(content, insetLeft, insetTop, contentWidth, contentHeight,
                getBaselineOffset(), HPos.CENTER, VPos.CENTER);

        content.getChildren().forEach(c -> {
            if (c instanceof LabeledEdge) {
                ((LabeledEdge) c).update();
            }
        });

    }
}
