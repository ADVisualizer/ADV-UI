package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Widget component for a indexed labeled node with optional rounded corners
 */
public class IndexedNode extends Pane {

    private static final double INDEX_LABEL_DISTANCE = 5;

    private final LabeledNode labeledNode;
    private final Label indexLabel;

    private int preferredX;
    private int preferredY;

    public IndexedNode(long index, String labelText, ADVStyle style,
                       boolean isRoundedDown, boolean showIndex,
                       IndexPosition indexPosition) {
        labeledNode = new LabeledNode(labelText, style, isRoundedDown);
        labeledNode.setX(0);
        labeledNode.setY(0);

        indexLabel = new Label();
        indexLabel.setText("[" + index + "]");
        indexLabel.setVisible(showIndex);
        indexLabel.setPadding(calculateInsets(indexPosition));
        indexLabel.getStyleClass().add("node-index");

        getChildren().add(createRootPane(indexPosition, showIndex));
    }

    public void setCenterX(int x) {
        preferredX = x;
    }

    public void setCenterY(int y) {
        preferredY = y;
    }

    public LabeledNode getLabeledNode() {
        return labeledNode;
    }

    private Pane createRootPane(IndexPosition position, boolean showIndex) {
        if (showIndex) {
            switch (position) {
                case TOP:
                    VBox vBox = new VBox();
                    vBox.setAlignment(Pos.TOP_CENTER);
                    vBox.getChildren().add(indexLabel);
                    vBox.getChildren().add(labeledNode);
                    return vBox;
                case RIGHT:
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.getChildren().add(labeledNode);
                    hBox.getChildren().add(indexLabel);
                    return hBox;
                default:
                    throw new IllegalArgumentException(
                            "unknown IndexPosition " + position.toString());
            }
        } else {
            StackPane pane = new StackPane();
            pane.setAlignment(Pos.CENTER);
            pane.getChildren().add(labeledNode);
            pane.getChildren().add(indexLabel);
            return pane;
        }
    }

    private Insets calculateInsets(IndexPosition position) {
        switch (position) {
            case TOP:
                return new Insets(0, 0, INDEX_LABEL_DISTANCE, 0);
            case RIGHT:
                return new Insets(0, 0, 0, INDEX_LABEL_DISTANCE);
            default:
                throw new IllegalArgumentException(
                        "unknown IndexPosition " + position.toString());
        }
    }

    /**
     * Updates position of IndexedNode so that the center of the Labeled Node
     * is at preferredX / preferredY
     */
    @Override
    protected void layoutChildren() {
        layoutXProperty().set(preferredX - labeledNode.getWidth() / 2);
        layoutYProperty().set(preferredY - labeledNode.getHeight() / 2);
        super.layoutChildren();
    }
}