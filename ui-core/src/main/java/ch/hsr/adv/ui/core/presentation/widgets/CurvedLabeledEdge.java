package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Curved Labeled Edge
 *
 * @author mtrentini
 */
public class CurvedLabeledEdge extends LabeledEdge {

    private static final Logger logger = LoggerFactory.getLogger(
            CurvedLabeledEdge.class);

    private static final double CURVATURE_FACTOR = 0.2;

    public CurvedLabeledEdge(String labelText,
                             LabeledNode startNode,
                             ConnectorType startConnector,
                             LabeledNode endNode,
                             ConnectorType endConnector,
                             ADVStyle style) {

        super(labelText, startNode, startConnector, endNode, endConnector,
                style);
    }

    public CurvedLabeledEdge(String labelText,
                             LabeledNode startNode,
                             ConnectorType startConnector,
                             LabeledNode endNode,
                             ConnectorType endConnector,
                             ADVStyle style,
                             LabeledEdge.DirectionType directionType) {

        super(labelText, startNode, startConnector,
                endNode, endConnector, style, directionType);
    }

    @Override
    protected void drawLabel(String labelText) {
        //TODO: reposition label according to curve
    }

    @Override
    protected void setControlPoints(Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {

        logger.debug("Calculating curvature...");

        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        double y = mid.getY();
        double x = mid.getX();

        // scale distance
        Point2D distanceVector = createDistanceVector(startIntersectionPoint,
                endIntersectionPoint, CURVATURE_FACTOR);

        // create
        BiConnectionType biConnectionType = BiConnectionType.valueOf(
                getStartConnector(), getEndConnector());


        switch (biConnectionType) {
            case LEFTLEFT:
            case BOTTOMBOTTOM:
            case RIGHTLEFT:
            case TOPBOTTOM:
                createOneControlPoint(x - distanceVector
                        .getX(), y - distanceVector.getY());
                break;
            case TOPTOP:
            case RIGHTRIGHT:
            case LEFTRIGHT:
            case BOTTOMTOP:
            default:
                createOneControlPoint(x + distanceVector
                        .getX(), y + distanceVector.getY());
        }
    }

    /**
     * @param startIntersectionPoint start point
     * @param endIntersectionPoint   end point
     * @param curvatureFactor        the bigger the factor, the more the
     *                               curve is bent
     * @return a point representing a vector which is perpendicular to the
     * vector given by the input points.
     */
    protected Point2D createDistanceVector(Point2D startIntersectionPoint,
                                           Point2D endIntersectionPoint,
                                           double curvatureFactor) {

        // calculate offset
        Point2D startEndVector = startIntersectionPoint
                .subtract(endIntersectionPoint);
        Point2D distanceVector = new Point2D(startEndVector
                .getY(), -startEndVector.getX());


        // change direction if vector direction is in 1st or 4th quadrant
        if (endIntersectionPoint.getX() > startIntersectionPoint.getX()) {
            distanceVector = distanceVector.multiply(-1);
        }

        // scale distance
        distanceVector = distanceVector.multiply(curvatureFactor);

        return new Point2D(distanceVector.getX(), distanceVector.getY());
    }

    /**
     * Sets the same x and y coordinates for both control points of the curve
     * . This leads to a smoother curve.
     *
     * @param x coordinate for the control points
     * @param y coordinate for the control points
     */
    protected void createOneControlPoint(double x, double y) {
        createTwoControlPoints(x, y, x, y);
    }

    /**
     * @param x1 x coordinate of 1st control point
     * @param y1 y coordinate of 1st control point
     * @param x2 x coordinate of 2nd control point
     * @param y2 y coordinate of 2nd control point
     */
    protected void createTwoControlPoints(double x1, double y1, double x2,
                                          double y2) {
        CubicCurve cubicCurve = getCurve();
        cubicCurve.setControlX1(x1);
        cubicCurve.setControlY1(y1);
        cubicCurve.setControlX2(x2);
        cubicCurve.setControlY2(y2);
    }

    /**
     * Helper Enum which represents all supported representations of
     * connector type combinations. Allows us to use a switch statement
     * instead of string matching.
     */
    protected enum BiConnectionType {
        TOPTOP(ConnectorType.TOP, ConnectorType.TOP),
        BOTTOMBOTTOM(ConnectorType.BOTTOM, ConnectorType.BOTTOM),
        LEFTLEFT(ConnectorType.LEFT, ConnectorType.LEFT),
        RIGHTRIGHT(ConnectorType.RIGHT, ConnectorType.RIGHT),
        LEFTRIGHT(ConnectorType.LEFT, ConnectorType.RIGHT),
        RIGHTLEFT(ConnectorType.RIGHT, ConnectorType.LEFT),
        TOPBOTTOM(ConnectorType.TOP, ConnectorType.BOTTOM),
        BOTTOMTOP(ConnectorType.BOTTOM, ConnectorType.TOP),
        RIGHTBOTTOM(ConnectorType.RIGHT, ConnectorType.BOTTOM),
        BOTTOMRIGHT(ConnectorType.BOTTOM, ConnectorType.RIGHT),
        LEFTBOTTOM(ConnectorType.LEFT, ConnectorType.BOTTOM),
        BOTTOMLEFT(ConnectorType.BOTTOM, ConnectorType.LEFT),
        TOPLEFT(ConnectorType.TOP, ConnectorType.LEFT),
        LEFTTOP(ConnectorType.LEFT, ConnectorType.TOP),
        TOPRIGHT(ConnectorType.TOP, ConnectorType.RIGHT),
        RIGHTTOP(ConnectorType.RIGHT, ConnectorType.TOP),
        DEFAULT(null, null);

        private ConnectorType start;
        private ConnectorType end;

        BiConnectionType(ConnectorType start, ConnectorType end) {
            this.start = start;
            this.end = end;
        }

        /**
         * @param start connector type
         * @param end   connector type
         * @return an enum constructed by concatenating the start and end
         * ConnectorTypes
         */
        static BiConnectionType valueOf(ConnectorType start, ConnectorType
                end) {
            try {
                return Enum.valueOf(BiConnectionType.class, start.name() + end
                        .name());
            } catch (IllegalArgumentException e) {
                return DEFAULT;
            }
        }
    }

}
