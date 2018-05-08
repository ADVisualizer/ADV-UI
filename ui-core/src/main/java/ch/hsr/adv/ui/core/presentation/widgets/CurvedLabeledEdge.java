package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.shape.CubicCurve;

/**
 * Curved Labeled Edge
 */
public class CurvedLabeledEdge extends LabeledEdge {

    public CurvedLabeledEdge(String labelText,
                             ADVNode startNode,
                             ConnectorType startConnector,
                             ADVNode endNode,
                             ConnectorType endConnector,
                             Region edgeContainer,
                             ADVStyle style) {

        super(labelText, startNode, startConnector, endNode, endConnector,
                edgeContainer, style);
    }

    public CurvedLabeledEdge(String labelText,
                             ADVNode startNode,
                             ConnectorType startConnector,
                             ADVNode endNode,
                             ConnectorType endConnector,
                             Region edgeContainer,
                             ADVStyle style,
                             LabeledEdge.DirectionType directionType) {

        super(labelText, startNode, startConnector,
                endNode, endConnector, edgeContainer,
                style, directionType);
    }

    @Override
    protected void setControlPoints(CubicCurve cubicCurve,
                                    Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {
        double startHeight = getStartNode().getBoundsInParent().getHeight();
        double endHeight = getEndNode().getBoundsInParent().getHeight();
        double avgHeight = (startHeight + endHeight) / 2;

        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        double y = mid.getY();
        double x = mid.getX();

        ConnectorType startConnector = getStartConnector();
        ConnectorType endConnector = getEndConnector();

        //TODO: is this right?
        if ((startConnector
                .equals(ConnectorType.BOTTOM)
                || startConnector
                .equals(ConnectorType.LEFT))
                && (endConnector
                .equals(ConnectorType.LEFT)
                || endConnector
                .equals(ConnectorType.BOTTOM))) {
            x -= avgHeight;
            y += avgHeight;
        } else if ((startConnector
                .equals(ConnectorType.TOP)
                || startConnector
                .equals(ConnectorType.RIGHT))
                && (endConnector
                .equals(ConnectorType.RIGHT)
                || endConnector
                .equals(ConnectorType.TOP))) {
            x += avgHeight;
            y -= avgHeight;
        } else if (startConnector
                .equals(ConnectorType.TOP)
                && endConnector
                .equals(ConnectorType.TOP)) {
            y -= avgHeight;
        } else if (startConnector
                .equals(ConnectorType.BOTTOM)
                && endConnector
                .equals(ConnectorType.BOTTOM)) {
            y += avgHeight;
        }

        cubicCurve.setControlX1(x);
        cubicCurve.setControlY1(y);
        cubicCurve.setControlX2(x);
        cubicCurve.setControlY2(y);
    }

}
