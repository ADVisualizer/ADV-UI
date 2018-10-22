package ch.hsr.adv.ui.tree.presentation.widgets;

import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * Widget component for a indexed labeled node with optional rounded corners
 */
public class IndexedNode extends Region {

    private static final double INDEX_LABEL_DISTANCE = 5;

    private final LabeledNode labeledNode;

    private int centerX;
    private int centerY;
    private Label indexLabel;
    private boolean showIndex;

    public IndexedNode(long index, String labelText, ADVStyle style,
                       boolean isRoundedDown, boolean showIndex) {
        this.showIndex = showIndex;

        labeledNode = new LabeledNode(labelText, style, isRoundedDown);
        labeledNode.setX(0);
        labeledNode.setY(0);
        getChildren().add(labeledNode);

        indexLabel = new Label();
        indexLabel.setText(String.valueOf(index));
        indexLabel.setVisible(showIndex);
        getChildren().add(indexLabel);
    }

    /**
     * Sets the X property of the center of the labeled node
     *
     * @param x x coordinate
     */
    public void setCenterX(int x) {
        centerX = x;
        layoutXProperty().set(centerX - labeledNode.getWidth() / 2);
    }

    /**
     * Sets the y property of the center of the labeled node
     *
     * @param y y coordinate
     */
    public void setCenterY(int y) {
        centerY = y;
        layoutYProperty().set(centerY - labeledNode.getHeight() / 2);
    }

    public LabeledNode getLabeledNode() {
        return labeledNode;
    }

    /**
     * Centers the label and the index on the node
     */
    @Override
    protected void layoutChildren() {
        if (showIndex) {
            final double nodeHeight = getHeight();
            final double insetTop = getInsets().getTop();
            final double insetLeft = getInsets().getLeft();
            final double insetBottom = getInsets().getBottom();

            final double contentHeight = (nodeHeight - insetTop - insetBottom);
            double leftPos = insetLeft
                    + labeledNode.getWidth() + INDEX_LABEL_DISTANCE;

            layoutInArea(indexLabel, leftPos, insetTop,
                    indexLabel.getWidth(), contentHeight,
                    getBaselineOffset(), HPos.LEFT, VPos.CENTER);
        }

        setCenterX(centerX);
        setCenterY(centerY);

        super.layoutChildren();
    }
}