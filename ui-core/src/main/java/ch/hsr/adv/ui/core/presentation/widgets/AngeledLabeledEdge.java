package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.transform.Rotate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AngeledLabeledEdge extends LabeledEdge {
    private static final Logger logger = LoggerFactory.getLogger(
            AngeledLabeledEdge.class);
    private static final double ANGLE_OFFSET = 5;
    private String startLabel;
    private String endLabel;

    public AngeledLabeledEdge(String labelText, LabeledNode startNode,
                              ConnectorType startConnector, LabeledNode
                                      endNode, ConnectorType endConnector,
                              ADVStyle style) {
        super(labelText, startNode, startConnector, endNode, endConnector,
                style);
    }

    public AngeledLabeledEdge(String labelText, LabeledNode startNode,
                              ConnectorType startConnector, LabeledNode
                                      endNode, ConnectorType endConnector,
                              ADVStyle style, DirectionType directionType) {
        super(labelText, startNode, startConnector, endNode, endConnector,
                style, directionType);
        this.startLabel = startNode.getLabel();
        this.endLabel = endNode.getLabel();
    }

    @Override
    protected void drawLabel(String labelText) {
        //TODO: reposition label according to curve
    }

    @Override
    protected void setControlPoints(Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {
        Point2D normVector = new Point2D(0, 1);
        Point2D midPoint = startIntersectionPoint
                .midpoint(endIntersectionPoint);
        Point2D vectorStartEnd = endIntersectionPoint
                .subtract(startIntersectionPoint);
        double angle = normVector.angle(vectorStartEnd);
        logger.debug("Vector: {}{}, Angle: {}", startLabel, endLabel, angle);
        logger.debug("{}: {},{} / {}: {},{}", startLabel, startIntersectionPoint
                .getX(), startIntersectionPoint
                .getY(), endLabel, endIntersectionPoint
                .getX(), endIntersectionPoint.getY());

        Rotate r = Rotate.rotate(ANGLE_OFFSET, startIntersectionPoint
                .getX(), startIntersectionPoint.getY());


        Point2D control = r.deltaTransform(midPoint);
        CubicCurve curve = getCurve();
        double x = control.getX();
        double y = control.getY();
        curve.setControlX1(x);
        curve.setControlX2(x);
        curve.setControlY1(y);
        curve.setControlY2(y);

        logger.debug("MidPoint: {},{}", midPoint.getX(), midPoint.getY());
        logger.debug("ControlPoint: {},{}", x, y);
        logger.debug("-----------------------------------");

    }
}
