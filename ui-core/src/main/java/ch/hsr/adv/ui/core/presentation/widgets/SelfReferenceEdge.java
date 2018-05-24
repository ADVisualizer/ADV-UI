package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.shape.CubicCurve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Draws a curved connection for edges with the same start and end node.
 *
 * @author mtrentini
 */
public class SelfReferenceEdge extends CurvedLabeledEdge {
    private static final Logger logger = LoggerFactory.getLogger(
            SelfReferenceEdge.class);
    private static final double OFFSET_FACTOR = 0.2;
    private static final double MIN_OFFSET = 20;
    private static final double CURVATURE_FACTOR = 2;
    private final LabeledNode startNode;

    public SelfReferenceEdge(String labelText, LabeledNode startNode,
                             ConnectorType startConnector, ConnectorType
                                     endConnector,
                             ADVStyle style) {
        this(labelText, startNode, startConnector, endConnector,
                style, DirectionType.NONE);

    }

    public SelfReferenceEdge(String labelText, LabeledNode startNode,
                             ConnectorType startConnector, ConnectorType
                                     endConnector,
                             ADVStyle style, DirectionType directionType) {
        super(labelText, startNode, startConnector, startNode, endConnector,
                style, directionType);
        this.startNode = startNode;
    }

    @Override
    protected Point2D getConnectorPoint(Bounds boundsInCommonAncestor,
                                        ConnectorType connectorType) {
        if (connectorType.equals(ConnectorType.DIRECT)) {
            connectorType = ConnectorType.RIGHT;
        }
        return super.getConnectorPoint(boundsInCommonAncestor, connectorType);
    }

    @Override
    protected void drawLabel() {
        CubicCurve curve = getCurve();
        Label label = getLabel();
        DoubleBinding xProperty = Bindings
                .createDoubleBinding(() -> {
                    if (label.getWidth() != 0) {
                        double labelCenter = (label.getWidth() / 2);
                        return (curve.getControlX1() + curve
                                .getControlX2()) / 2 - labelCenter;
                    }
                    return 0.0;
                }, curve.controlX1Property(), curve.controlX2Property(), label
                        .widthProperty());

        DoubleBinding yProperty = Bindings
                .createDoubleBinding(() -> {
                    if (label.getHeight() != 0) {
                        double labelCenter = (label.getHeight() / 2);
                        return (curve.getControlY1() + curve
                                .getControlY2()) / 2 - labelCenter;
                    }
                    return 0.0;
                }, curve.controlY1Property(), curve.controlY2Property(), label
                        .heightProperty());

        label.layoutXProperty().bind(xProperty);
        label.layoutYProperty().bind(yProperty);
    }


    @Override
    protected void setControlPoints(Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {
        // create BiConnectionType
        CurvedLabeledEdge.BiConnectionType biConnectionType =
                CurvedLabeledEdge.BiConnectionType.valueOf(
                        getStartConnector(), getEndConnector());
        double offset = Math.max(OFFSET_FACTOR * startNode.getBoundsInParent()
                .getWidth(), MIN_OFFSET);

        double x1 = startIntersectionPoint.getX();
        double y1 = startIntersectionPoint.getY();
        double x2 = endIntersectionPoint.getX();
        double y2 = endIntersectionPoint.getY();

        switch (biConnectionType) {
            case RIGHTBOTTOM:
            case BOTTOMRIGHT:
            case LEFTBOTTOM:
            case BOTTOMLEFT:
                Point2D distanceVector = createDistanceVector(
                        startIntersectionPoint, endIntersectionPoint,
                        CURVATURE_FACTOR);
                Point2D midPoint = startIntersectionPoint
                        .midpoint(endIntersectionPoint);
                createOneControlPoint(
                        midPoint.getX() - distanceVector.getX(),
                        midPoint.getY() - distanceVector.getY());
                break;
            case LEFTTOP:
            case TOPLEFT:
            case RIGHTTOP:
            case TOPRIGHT:
                distanceVector = createDistanceVector(
                        startIntersectionPoint, endIntersectionPoint,
                        CURVATURE_FACTOR);
                midPoint = startIntersectionPoint
                        .midpoint(endIntersectionPoint);
                createOneControlPoint(
                        midPoint.getX() + distanceVector.getX(),
                        midPoint.getY() + distanceVector.getY());
                break;
            case LEFTLEFT:
                x1 -= offset;
                y1 -= offset;
                x2 -= offset;
                y2 += offset;
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            case BOTTOMBOTTOM:
                x1 -= offset;
                y1 += offset;
                x2 += offset;
                y2 += offset;
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            case RIGHTRIGHT:
                x1 += offset;
                y1 -= offset;
                x2 += offset;
                y2 += offset;
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            case TOPTOP:
                x1 -= offset;
                y1 -= offset;
                x2 += offset;
                y2 -= offset;
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            case RIGHTLEFT:
                x1 -= MIN_OFFSET;
                y1 -= (getStartBounds().getHeight() / 2 + MIN_OFFSET);
                x2 += MIN_OFFSET;
                y2 -= (getStartBounds().getHeight() / 2 + MIN_OFFSET);
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            case LEFTRIGHT:
                x1 += MIN_OFFSET;
                y1 += (getStartBounds().getHeight() / 2 + MIN_OFFSET);
                x2 -= MIN_OFFSET;
                y2 += (getStartBounds().getHeight() / 2 + MIN_OFFSET);
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            case TOPBOTTOM:
                x1 -= (getStartBounds().getWidth() / 2 + MIN_OFFSET);
                y1 -= MIN_OFFSET;
                x2 -= (getStartBounds().getWidth() / 2 + MIN_OFFSET);
                y2 += MIN_OFFSET;
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            case BOTTOMTOP:
                x1 -= (getStartBounds().getWidth() / 2 + MIN_OFFSET);
                y1 += MIN_OFFSET;
                x2 -= (getStartBounds().getWidth() / 2 + MIN_OFFSET);
                y2 -= MIN_OFFSET;
                createTwoControlPoints(x1, y1, x2, y2);
                break;
            default:
                logger.error("No recognizable ConnectorTypes found.");
        }
    }
}
