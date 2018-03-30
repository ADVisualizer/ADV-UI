package ch.adv.ui.presentation.model;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

/**
 * Widget component for a labeled node
 */
public class LabeledNode extends StackPane {
    private final static int PADDING = 5;
    private Ellipse e;
    private Label l;

    public LabeledNode(String labelText) {
        initializeFields();
        bindProperties();
        l.setPadding(new Insets(PADDING));
        l.setText(labelText);
        getChildren().addAll(e, l);
    }

    private void initializeFields() {
        l = new Label();
        e = new Ellipse();
    }

    private void bindProperties() {
        e.radiusXProperty().bind(this.widthProperty().divide(2));
        e.radiusYProperty().bind(this.heightProperty().divide(2));
    }

    /**
     * Sets the X property
     *
     * @param x x coordinate
     */
    public void setX(int x) {
        this.layoutXProperty().set(x);
    }

    /**
     * Sets the y property
     *
     * @param y y coordinate
     */
    public void setY(int y) {
        this.layoutYProperty().set(y);
    }

    /**
     * Sets the background color of the ndoe
     *
     * @param color color
     */
    public void setBackgroundColor(Paint color) {
        e.setFill(color);
    }

    /**
     * Sets the font color of the label
     *
     * @param color color
     */
    public void setFontColor(Paint color) {
        l.setTextFill(color);
    }

}
