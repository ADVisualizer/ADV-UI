package ch.hsr.adv.ui.core.presentation.widgets;


import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import javafx.beans.Observable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Widget component for a labeled node with optional rounded corners
 *
 * @author mtrentini
 */
public class LabeledNode extends Region {

    private static final int LABEL_PADDING = 3;

    private final boolean isRoundedDown;
    private final Label label = new Label();
    private final ADVStyle style;
    private Color fillColor;
    private Color strokeColor;
    private BorderStrokeStyle strokeStyle;
    private BorderWidths borderWidth;


    public LabeledNode(String labelText, ADVStyle style) {
        this(labelText, style, false);
    }

    /**
     * Create a LabeledNode with rounded corners
     *
     * @param labelText     to be used
     * @param style         adv style
     * @param isRoundedDown flag to round corners
     */
    public LabeledNode(String labelText, ADVStyle style, boolean
            isRoundedDown) {
        this.isRoundedDown = isRoundedDown;
        this.style = style;
        label.setText(labelText);

        styleNode();

        widthProperty().addListener(this::updateRoundedCorner);

        getChildren().addAll(label);
    }

    private void updateRoundedCorner(Observable observable) {
        // background
        Background background = new Background(new BackgroundFill(fillColor,
                cornerRadius(), Insets.EMPTY));
        setBackground(background);

        // border
        BorderStroke stroke = new BorderStroke(strokeColor, strokeStyle,
                cornerRadius(), borderWidth, Insets.EMPTY);
        setBorder(new Border(stroke));
    }

    private void styleNode() {
        // padding
        label.setPadding(new Insets(LABEL_PADDING));

        // background
        fillColor = StyleConverter
                .getColorFromHexValue(style.getFillColor());

        // border
        strokeColor = StyleConverter
                .getColorFromHexValue(style.getStrokeColor());
        strokeStyle = StyleConverter
                .getStrokeStyle(style.getStrokeStyle());
        borderWidth = new BorderWidths(style.getStrokeThickness());

        // font
        label.setTextFill(StyleConverter.getLabelColor(fillColor));

    }

    private CornerRadii cornerRadius() {
        if (isRoundedDown) {
            double radius = getBoundsInParent().getWidth() / 2;
            return new CornerRadii(radius);
        } else {
            return CornerRadii.EMPTY;
        }
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

    @Override
    protected void layoutChildren() {
        final double nodeWidth = getWidth();
        final double nodeHeight = getHeight();
        final double insetTop = getInsets().getTop();
        final double insetRight = getInsets().getRight();
        final double insetLeft = getInsets().getLeft();
        final double insertBottom = getInsets().getBottom();

        final double contentWidth = (nodeWidth - insetLeft - insetRight);
        final double contentHeight = (nodeHeight - insetTop - insertBottom);

        layoutInArea(label, insetLeft, insetTop, contentWidth, contentHeight,
                getBaselineOffset(), HPos.CENTER, VPos.CENTER);
    }
}
