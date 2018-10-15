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

    private Label indexLabel;
    private int nodeLabelX;
    private int nodeLabelY;
    private boolean showIndex;

    public IndexedNode(long index, String labelText, ADVStyle style,
                       boolean isRoundedDown, boolean showIndex) {
        this.showIndex = showIndex;

        labeledNode = new LabeledNode(labelText, style, isRoundedDown);
        getChildren().add(labeledNode);

        indexLabel = new Label();
        indexLabel.setText(String.valueOf(index));
        indexLabel.setVisible(showIndex);
        getChildren().add(indexLabel);
    }

    private void setPosition() {
        if (showIndex) {
            layoutXProperty().set(nodeLabelX
                    - (indexLabel.getWidth() + INDEX_LABEL_DISTANCE));
        } else {
            layoutXProperty().set(nodeLabelX);
        }
        layoutYProperty().set(nodeLabelY);
    }

    /**
     * Sets the X property of the labeled node
     *
     * @param x x coordinate
     */
    public void setX(int x) {
        nodeLabelX = x;
        setPosition();
    }

    /**
     * Sets the y property of the labeled node
     *
     * @param y y coordinate
     */
    public void setY(int y) {
        nodeLabelY = y;
        setPosition();
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

            layoutInArea(indexLabel, insetLeft, insetTop,
                    indexLabel.getWidth(), contentHeight,
                    getBaselineOffset(), HPos.CENTER, VPos.CENTER);

            labeledNode
                    .setX((int) (indexLabel.getWidth() + INDEX_LABEL_DISTANCE));
        } else {
            labeledNode.setX(0);
        }
        labeledNode.setY(0);
        setPosition();

        super.layoutChildren();
    }
}