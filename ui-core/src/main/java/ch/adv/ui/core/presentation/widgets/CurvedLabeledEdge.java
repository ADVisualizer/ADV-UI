package ch.adv.ui.core.presentation.widgets;

import ch.adv.ui.core.domain.styles.ADVStyle;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;

/**
 * Curved Labeled Edge
 */
public class CurvedLabeledEdge extends LabeledEdge {

    public CurvedLabeledEdge(String labelText,
                             ADVNode startNode,
                             ADVNode endNode,
                             ADVStyle style) {

        super(labelText, startNode, endNode, style);
    }

    public CurvedLabeledEdge(String labelText, ADVNode startNode,
                             ADVNode endNode, ADVStyle style,
                             LabeledEdge.DirectionType directionType) {

        super(labelText, startNode, endNode, style, directionType);
    }

    @Override
    protected void setControlPoints(CubicCurve curve,
                                    Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {
        double startHeight = getStartNode().getBoundsInParent().getHeight();
        double endHeight = getEndNode().getBoundsInParent().getHeight();
        double avgHeight = (startHeight + endHeight) / 2;

        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        double y = mid.getY();
        double x = mid.getX();

        if ((getStartNode().getConnectorTypeOutgoing()
                .equals(ConnectorType.BOTTOM)
                || getStartNode().getConnectorTypeOutgoing()
                .equals(ConnectorType.LEFT))
                && (getEndNode().getConnectorTypeIncoming()
                .equals(ConnectorType.LEFT)
                || getEndNode().getConnectorTypeIncoming()
                .equals(ConnectorType.BOTTOM))) {
            x -= avgHeight;
            y += avgHeight;
        } else if ((getStartNode().getConnectorTypeOutgoing()
                .equals(ConnectorType.TOP)
                || getStartNode().getConnectorTypeOutgoing()
                .equals(ConnectorType.RIGHT))
                && (getEndNode().getConnectorTypeIncoming()
                .equals(ConnectorType.RIGHT)
                || getEndNode().getConnectorTypeIncoming()
                .equals(ConnectorType.TOP))) {
            x += avgHeight;
            y -= avgHeight;
        } else if (getStartNode().getConnectorTypeOutgoing()
                .equals(ConnectorType.TOP)
                && getEndNode().getConnectorTypeIncoming()
                .equals(ConnectorType.TOP)) {
            y -= avgHeight;
        } else if (getStartNode().getConnectorTypeOutgoing()
                .equals(ConnectorType.BOTTOM)
                && getEndNode().getConnectorTypeIncoming()
                .equals(ConnectorType.BOTTOM)) {
            y += avgHeight;
        }

        curve.setControlX1(x);
        curve.setControlY1(y);
        curve.setControlX2(x);
        curve.setControlY2(y);
    }

}
