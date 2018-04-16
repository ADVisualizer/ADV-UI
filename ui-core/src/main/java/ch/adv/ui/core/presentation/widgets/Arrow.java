package ch.adv.ui.core.presentation.widgets;

import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.util.Arrays;


/**
 * Represents an arrow head at a arbitrary position on the curve
 * <p>
 * In most cases, the relativePositionOnCurve the curve will be 0 or 1.
 */
public class Arrow extends Polygon {

    private static final double[] SHAPE = new double[] {0, 0, 5, 15, -5, 15};

    private Rotate rotation;
    private float pos;
    private CubicCurve curve;

    /**
     * @param curve              cubic curve
     * @param relativePosOnCurve pos between 0 (start) and 1 (end)
     */
    public Arrow(CubicCurve curve, float relativePosOnCurve) {
        this.curve = curve;
        this.pos = relativePosOnCurve;
        this.rotation = new Rotate();

        rotation.setAxis(Rotate.Z_AXIS);
        getTransforms().addAll(rotation);

        Arrays.stream(SHAPE).forEach(p -> getPoints().add(p));

        update();
    }


    /**
     * Recomputes the angle and position of the arrow
     */
    public void update() {
        setFill(curve.getStroke());

        Point2D orientation = computePosition();

        setTranslateX(orientation.getX());
        setTranslateY(orientation.getY());

        Point2D tangent = computeTangent().normalize();
        double angle = Math.atan2(tangent.getY(), tangent.getX());
        angle = Math.toDegrees(angle);

        // arrow origin is top => apply offset
        double offset = -90;
        if (pos > 0.5) {
            offset = +90;
        }

        rotation.setAngle(angle + offset);
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
        double x = Math.pow(1 - pos, 3) * curve.getStartX()
                + 3 * pos * Math.pow(1 - pos, 2) * curve.getControlX1()
                + 3 * (1 - pos) * Math.pow(pos, 2) * curve.getControlX2()
                + Math.pow(pos, 3) * curve.getEndX();

        double y = Math.pow(1 - pos, 3) * curve.getStartY()
                + 3 * pos * Math.pow(1 - pos, 2) * curve.getControlY1()
                + 3 * (1 - pos) * Math.pow(pos, 2) * curve.getControlY2()
                + Math.pow(pos, 3) * curve.getEndY();

        return new Point2D(x, y);
    }

    /**
     * Computes the tangent of the arrow head on the cubic curve.
     * <p>
     * The relative position must be in the range between 0 and 1!
     * 0 = start
     * 1 = end
     *
     * @return a Point2D
     */
    private Point2D computeTangent() {
        double x = -3 * Math.pow(1 - pos, 2) * curve.getStartX()
                + 3 * (Math.pow(1 - pos, 2) - 2 * pos * (1 - pos))
                * curve.getControlX1()
                + 3 * ((1 - pos) * 2 * pos - pos * pos) * curve.getControlX2()
                + 3 * Math.pow(pos, 2) * curve.getEndX();

        double y = -3 * Math.pow(1 - pos, 2) * curve.getStartY()
                + 3 * (Math.pow(1 - pos, 2) - 2 * pos * (1 - pos))
                * curve.getControlY1()
                + 3 * ((1 - pos) * 2 * pos - pos * pos) * curve.getControlY2()
                + 3 * Math.pow(pos, 2) * curve.getEndY();

        return new Point2D(x, y);
    }

    /**
     * Relationship type of the arrow
     * <p>
     * Can be unidirectionl, bidirectional or no arrow.
     */
    public enum DirectionType {
        UNIDIRECTIONAL, BIDIRECTIONAL, NONE;
    }
}

