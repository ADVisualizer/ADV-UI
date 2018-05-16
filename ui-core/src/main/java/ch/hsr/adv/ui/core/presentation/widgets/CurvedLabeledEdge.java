package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Curved Labeled Edge
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
        CubicCurve cubicCurve = getCurve();
        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        double y = mid.getY();
        double x = mid.getX();

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
        distanceVector = distanceVector.multiply(CURVATURE_FACTOR);

        // create
        BiConnectionType biConnectionType = BiConnectionType.valueOf(
                getStartConnector(), getEndConnector());


        switch (biConnectionType) {
            case LEFTLEFT:
            case BOTTOMBOTTOM:
            case RIGHTLEFT:
            case TOPBOTTOM:
                createCurve(cubicCurve, x - distanceVector
                        .getX(), y - distanceVector.getY());
                break;
            case TOPTOP:
            case RIGHTRIGHT:
            case LEFTRIGHT:
            case BOTTOMTOP:
            default:
                createCurve(cubicCurve, x + distanceVector
                        .getX(), y + distanceVector.getY());
        }
    }

    private void createCurve(CubicCurve cubicCurve, double x, double y) {
        cubicCurve.setControlX1(x);
        cubicCurve.setControlY1(y);
        cubicCurve.setControlX2(x);
        cubicCurve.setControlY2(y);
    }

    /**
     * Helper Enum which represents all supported representations of
     * connector type combinations. Allows us to use a switch statement
     * instead of string matching.
     */
    private enum BiConnectionType {
        TOPTOP(ConnectorType.TOP, ConnectorType.TOP),
        BOTTOMBOTTOM(ConnectorType.BOTTOM, ConnectorType.BOTTOM),
        LEFTLEFT(ConnectorType.LEFT, ConnectorType.LEFT),
        RIGHTRIGHT(ConnectorType.RIGHT, ConnectorType.RIGHT),
        LEFTRIGHT(ConnectorType.LEFT, ConnectorType.RIGHT),
        RIGHTLEFT(ConnectorType.RIGHT, ConnectorType.LEFT),
        TOPBOTTOM(ConnectorType.TOP, ConnectorType.BOTTOM),
        BOTTOMTOP(ConnectorType.BOTTOM, ConnectorType.TOP),
        DEFAULT(null, null);

        private ConnectorType start;
        private ConnectorType end;

        BiConnectionType(ConnectorType start, ConnectorType end) {
            this.start = start;
            this.end = end;
        }

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
