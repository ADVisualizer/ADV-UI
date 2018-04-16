package ch.adv.ui.core.presentation.widgets;

import ch.adv.ui.core.domain.styles.ADVStyle;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;

/**
 * Curved Labeled Edge
 */
public class CurvedLabeledEdge extends LabeledEdge {

    private CurvePositon curvePositon;

    public CurvedLabeledEdge(String labelText, Node startNode, Node endNode,
                             ADVStyle style) {
        this(labelText, startNode, endNode, style, CurvePositon.TOP);
    }

    public CurvedLabeledEdge(String labelText, Node startNode, Node endNode,
                             ADVStyle style, CurvePositon curvePositon) {
        super(labelText, startNode, endNode, style);
        this.curvePositon = curvePositon;
    }

    public CurvedLabeledEdge(String labelText, Node startNode, Node endNode,
                             ADVStyle style, Arrow.DirectionType directionType,
                             CurvePositon curvePositon) {
        super(labelText, startNode, endNode, style, directionType);
        this.curvePositon = curvePositon;
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
        switch (curvePositon) {
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

    @Override
    protected Point2D findIntersectionPoint(Node targetBounds,
                                            Point2D inside,
                                            Point2D outside) {
        double y = inside.getY();
        switch (curvePositon) {
            case TOP:
                y -= targetBounds.getBoundsInParent().getHeight() / 2;
                return new Point2D(inside.getX(), y);
            case BOTTOM:
                y += targetBounds.getBoundsInParent().getHeight() / 2;
                return new Point2D(inside.getX(), y);
            default:
                return null;
        }
    }

    /**
     * Represents the position of the curve
     * <p>
     * Can be at the bottom or top of the element
     */
    public enum CurvePositon {
        TOP, BOTTOM;
    }
}
