package ch.adv.ui.core.presentation.widgets;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Widget component for a labeled node with optional rounded corners
 */
public class LabeledNode extends StackPane {
    private static final int PADDING = 5;
    private final ObjectProperty<Background> backgroundProperty = new
            SimpleObjectProperty<>();
    private final ObjectProperty<Border> borderProperty = new
            SimpleObjectProperty<>();
    private Label label = new Label();
    private Paint backgroundColor = Color.BLACK;
    private boolean isRoundedDown;

    public LabeledNode(String labelText) {
        this(labelText, false);
    }

    /**
     * Create a LabeledNode with rounded corners
     *
     * @param labelText     to be used
     * @param isRoundedDown flag to round corners
     */
    public LabeledNode(String labelText, boolean isRoundedDown) {
        this.isRoundedDown = isRoundedDown;
        setBindings();
        label.setPadding(new Insets(PADDING));
        label.setText(labelText);
        getChildren().addAll(label);
    }

    private void setBindings() {
        //set default
        backgroundProperty
                .setValue(new Background(new BackgroundFill(backgroundColor,
                        CornerRadii.EMPTY, Insets.EMPTY)));
        borderProperty
                .setValue(createBorder(BorderWidths.DEFAULT
                        .getTop(), Color.BLACK, BorderStrokeStyle.NONE));

        this.widthProperty().addListener(this::changeBackground);

        backgroundProperty().bind(backgroundProperty);
        borderProperty().bind(borderProperty);
    }

    private CornerRadii cornerRadii() {
        if (isRoundedDown) {
            return new CornerRadii(this.widthProperty().get() / 2);
        } else {
            return CornerRadii.EMPTY;
        }
    }

    private void changeBackground(ObservableValue<? extends Object> observable,
                                  Object oldValue,
                                  Object newValue) {
        BackgroundFill fill = new BackgroundFill(backgroundColor,
                cornerRadii(), Insets.EMPTY);
        backgroundProperty.setValue(new Background(fill));
        BorderStroke borderStroke = borderProperty.get()
                .getStrokes().get(0);
        Paint color = borderStroke.getTopStroke();
        double width = borderStroke.getWidths().getTop();
        BorderStrokeStyle strokeStyle = borderStroke.getTopStyle();
        Border border = createBorder(width, color, strokeStyle);
        borderProperty.setValue(border);
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
     * Sets the background color of the rectangle
     *
     * @param color color
     */
    public void setBackgroundColor(Paint color) {
        this.backgroundColor = color;
    }

    /**
     * Sets the font color of the label
     *
     * @param color color
     */
    public void setFontColor(Paint color) {
        label.setTextFill(color);
    }

    /**
     * Sets the border of the rectangle
     *
     * @param width       of the border
     * @param color       of the border
     * @param strokeStyle of the border
     */
    public void setBorder(double width, Paint color, BorderStrokeStyle
            strokeStyle) {
        borderProperty
                .setValue(createBorder(width, color, strokeStyle));
    }

    private Border createBorder(double width, Paint color, BorderStrokeStyle
            strokeStyle) {
        return new Border(new BorderStroke(color, strokeStyle,
                cornerRadii(), new BorderWidths(width),
                Insets.EMPTY));

    }
}
