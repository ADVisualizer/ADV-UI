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
                             Arrow.DirectionType directionType) {

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

        switch (getStartNode().getConnectorTypeIngoing()) {
            case TOP:
                y -= avgHeight;
                break;
            case BOTTOM:
                y += avgHeight;
                break;
            default:
        }

        curve.setControlX1(mid.getX());
        curve.setControlY1(y);
        curve.setControlX2(mid.getX());
        curve.setControlY2(y);
    }

}
