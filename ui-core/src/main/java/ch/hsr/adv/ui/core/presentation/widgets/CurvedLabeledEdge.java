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
    protected void setControlPoints(CubicCurve cubicCurve,
                                    Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {

        logger.debug("Calculating curvature...");
        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        double y = mid.getY();
        double x = mid.getX();

        // calculate offset
        Point2D connectionVector = startIntersectionPoint
                .subtract(endIntersectionPoint);
        Point2D distanceVector = new Point2D(Math.abs(connectionVector
                .getY()), -connectionVector.getX());
        distanceVector = distanceVector.multiply(0.1);


        BiConnectionType biConnectionType = BiConnectionType.valueOf(
                getStartConnector(), getEndConnector());


        switch (biConnectionType) {
            case TOPTOP:
                createCurve(cubicCurve, x + distanceVector
                        .getX(), y + distanceVector.getY());
                break;
            case LEFTLEFT:
                createCurve(cubicCurve, x - distanceVector
                        .getX(), y - distanceVector.getY());
                break;
            case RIGHTRIGHT:
                createCurve(cubicCurve, x + distanceVector
                        .getX(), y + distanceVector.getY());
                break;
            case BOTTOMBOTTOM:
                createCurve(cubicCurve, x - distanceVector
                        .getX(), y - distanceVector.getY());
                break;
            case LEFTRIGHT:
                createCurve(cubicCurve, x + distanceVector
                        .getX(), y + distanceVector.getY());
                break;
            case RIGHTLEFT:
                createCurve(cubicCurve, x - distanceVector
                        .getX(), y - distanceVector.getY());
                break;
            case TOPBOTTOM:
                createCurve(cubicCurve, x - distanceVector
                        .getX(), y - distanceVector.getY());
                break;
            case BOTTOMTOP:
                createCurve(cubicCurve, x + distanceVector
                        .getX(), y + distanceVector.getY());
                break;
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

    private enum BiConnectionType {
        TOPTOP(ConnectorType.TOP, ConnectorType.TOP),
        BOTTOMBOTTOM(ConnectorType.BOTTOM, ConnectorType.BOTTOM),
        LEFTLEFT(ConnectorType.LEFT, ConnectorType.LEFT),
        RIGHTRIGHT(ConnectorType.RIGHT, ConnectorType.RIGHT),
        LEFTRIGHT(ConnectorType.LEFT, ConnectorType.RIGHT),
        RIGHTLEFT(ConnectorType.RIGHT, ConnectorType.LEFT),
        TOPBOTTOM(ConnectorType.TOP, ConnectorType.BOTTOM),
        BOTTOMTOP(ConnectorType.BOTTOM, ConnectorType.TOP);

        private ConnectorType start;
        private ConnectorType end;

        BiConnectionType(ConnectorType start, ConnectorType end) {
            this.start = start;
            this.end = end;
        }

        static BiConnectionType valueOf(ConnectorType start, ConnectorType
                end) {
            return valueOf(start.name() + end.name());
        }
    }

}
