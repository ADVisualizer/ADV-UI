package ch.adv.ui.presentation.model;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

/**
 * Widget component for a labeled node
 */
public class LabeledNode extends StackPane {

    private Ellipse e;
    private Label l;
    private DoubleProperty x;
    private DoubleProperty y;

    public LabeledNode(String labelText) {
        initializeFields();
        bindProperties();
        l.setText(labelText);
        getChildren().addAll(e, l);
    }

    private void initializeFields() {
        l = new Label();
        e = new Ellipse();
        this.x = new SimpleDoubleProperty();
        this.y = new SimpleDoubleProperty();
    }

    private void bindProperties() {
        e.centerXProperty().bind(this.x);
        e.centerYProperty().bind(this.y);
        e.radiusXProperty().bind(this.widthProperty().divide(2));
        e.radiusYProperty().bind(this.heightProperty().divide(2));
    }

    /**
     * Sets the X property
     *
     * @param x x coordinate
     */
    public void setX(int x) {
        this.x.set(x);
    }

    /**
     * Sets the y property
     *
     * @param y y coordinate
     */
    public void setY(int y) {
        this.y.set(y);
    }

    /**
     * Sets the background color of the ndoe
     * @param color color
     */
    public void setBackgroundColor(Paint color) {
        e.setFill(color);
    }

    /**
     * Sets the font color of the label
     * @param color color
     */
    public void setFontColor(Paint color) {
        l.setTextFill(color);
    }

}
