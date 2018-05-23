package ch.hsr.adv.ui.core.presentation.widgets;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;

public class PositionedLabel extends Label {
    // relative position of the label on the curve
    private static final float POS = 0.5f;
    private CubicCurve curve;

    /**
     * @param curve cubic curve
     */
    public PositionedLabel(CubicCurve curve) {
        this.curve = curve;
    }

    /**
     * Recomputes the angle and position of the arrow
     */
    public void update() {
        Color textColor = (Color)curve.getStroke();
        Color invertedColor = textColor.invert();
        textColor = textColor.interpolate(invertedColor, 0.5);
        setTextFill(textColor);

        Point2D orientation = computePosition();

        setTranslateX(orientation.getX() - getWidth() / 2);
        setTranslateY(orientation.getY()- getHeight() / 2);
    }

    /**
     * Computes the x/y coordinates of the arrow head on the cubic curve.
     * <p>
     * The relative position must be in the range between 0 and 1!
     * 0 = start
     * 1 = end
     *
     * @return a Point2D
     */
    private Point2D computePosition() {
        double x = Math.pow(1 - POS, 3) * curve.getStartX()
                + 3 * POS * Math.pow(1 - POS, 2) * curve.getControlX1()
                + 3 * (1 - POS) * Math.pow(POS, 2) * curve.getControlX2()
                + Math.pow(POS, 3) * curve.getEndX();

        double y = Math.pow(1 - POS, 3) * curve.getStartY()
                + 3 * POS * Math.pow(1 - POS, 2) * curve.getControlY1()
                + 3 * (1 - POS) * Math.pow(POS, 2) * curve.getControlY2()
                + Math.pow(POS, 3) * curve.getEndY();

        return new Point2D(x, y);
    }
}
