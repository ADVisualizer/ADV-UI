package ch.adv.ui.core.presentation.widgets;

import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.util.StyleConverter;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic component for an labeled edge with optional arrows.
 * <p>
 * Can be used to display the relation between two nodes.
 *
 * @author mwieland
 */
public class LabeledEdge extends Group {

    private static final Logger logger = LoggerFactory.getLogger(
            LabeledEdge.class);
    private static final int LABEL_MARGIN = 5;
    private static final int LABEL_FONT_SIZE = 8;

    private final ADVNode startNode;
    private final ADVNode endNode;

    private final CubicCurve curve = new CubicCurve();
    private final Label label = new Label();
    private final ADVStyle style;

    private final Arrow.DirectionType directionType;
    private final Arrow startArrow;
    private final Arrow endArrow;


    public LabeledEdge(String labelText, ADVNode startNode, ADVNode endNode,
                       ADVStyle style) {
        this(labelText, startNode, endNode, style,
                Arrow.DirectionType.UNIDIRECTIONAL);
    }

    public LabeledEdge(String labelText, ADVNode startNode, ADVNode endNode,
                       ADVStyle style, Arrow.DirectionType directionType) {

        this.style = style;
        this.startNode = startNode;
        this.endNode = endNode;
        this.directionType = directionType;

        // draw arrow
        switch (directionType) {
            case BIDIRECTIONAL:
                this.startArrow = new Arrow(curve, 0.0f);
                this.endArrow = new Arrow(curve, 1.0f);
                getChildren().addAll(startArrow, endArrow);
                break;
            case UNIDIRECTIONAL:
                this.startArrow = null;
                this.endArrow = new Arrow(curve, 1.0f);
                getChildren().add(endArrow);
                break;
            default:
                this.startArrow = null;
                this.endArrow = null;
        }

        // bind listener
        startNode.centerProperty().addListener(this::drawCurve);
        endNode.centerProperty().addListener(this::drawCurve);

        initializeComponent(labelText);
    }

    private void initializeComponent(String labelText) {
        applyStyle();
        drawLabel(labelText);

        getChildren().addAll(curve, label);
    }


    private void applyStyle() {
        curve.setStrokeWidth(style.getStrokeThickness());
        curve.setStroke(StyleConverter.getColor(style.getStrokeColor()));
        curve.setFill(Color.TRANSPARENT);
        curve.setStrokeType(StrokeType.CENTERED);
    }

    /**
     * Draws a centered label
     *
     * @param labelText text
     */
    private void drawLabel(String labelText) {
        Binding xProperty = Bindings.createDoubleBinding(() -> {
            double centerX = (curve.getControlX1() + curve.getControlX2()) / 2;
            double labelWidth = (label.getWidth() / 2);
            return centerX - labelWidth;
        }, curve.startXProperty(), curve.endXProperty());

        Binding yProperty = Bindings.createDoubleBinding(() -> {
            double centerY = (curve.getControlY1() + curve.getControlY2()) / 2;
            return centerY - LABEL_MARGIN;
        }, curve.startYProperty(), curve.endYProperty());

        label.setText(labelText);
        //TODO: replace with fill color
        label.setTextFill(Color.BLACK);
        label.setFont(new Font(LABEL_FONT_SIZE));
        label.layoutXProperty().bind(xProperty);
        label.layoutYProperty().bind(yProperty);
    }

    private void drawCurve(Observable o) {
        if (startNode.getCenter() != null && endNode.getCenter() != null) {

            // start and end connector point
            Point2D startConnectorPoint = getConnectorPoint(startNode,
                    startNode.getConnectorTypeOutgoing());

            Point2D endConnectorPoint = getConnectorPoint(endNode,
                    endNode.getConnectorTypeIncoming());

            if (endNode.getParent() != null) {
                endConnectorPoint = endNode.getParent().localToParent
                        (endConnectorPoint);
            }

            curve.setStartX(startConnectorPoint.getX());
            curve.setStartY(startConnectorPoint.getY());
            curve.setEndX(endConnectorPoint.getX());
            curve.setEndY(endConnectorPoint.getY());

            setControlPoints(curve, startConnectorPoint, endConnectorPoint);

            // arrow
            switch (directionType) {
                case BIDIRECTIONAL:
                    startArrow.update();
                    endArrow.update();
                    break;
                case UNIDIRECTIONAL:
                    endArrow.update();
                    break;
                case NONE:
                    break;
                default:
                    logger.warn("Unsupported direction type: {}",
                            directionType);
            }
        }
    }

    private Point2D getConnectorPoint(ADVNode node,
                                      ConnectorType connectorType) {
        switch (connectorType) {
            case BOTTOM:
                return getBottomConnectorPoint(node);
            case TOP:
                return getTopConnectorPoint(node);
            case LEFT:
                return getLeftConnectorPoint(node);
            case RIGHT:
                return getRightConnectorPoint(node);
            case DIRECT:
                return findIntersectionPoint(node,
                        startNode.getCenter(),
                        endNode.getCenter());
            default:
                logger.warn("Unknown ConnectorType {}", connectorType);
                return null;
        }
    }

    private Point2D getTopConnectorPoint(ADVNode node) {
        double y = node.getCenter().getY();
        y -= node.getBoundsInParent().getHeight() / 2;
        return new Point2D(node.getCenter().getX(), y);
    }

    private Point2D getBottomConnectorPoint(ADVNode node) {
        double y = node.getCenter().getY();
        y += node.getBoundsInParent().getHeight() / 2;
        return new Point2D(node.getCenter().getX(), y);
    }

    private Point2D getRightConnectorPoint(ADVNode node) {
        double x = node.getCenter().getX();
        x += node.getBoundsInParent().getWidth() / 2;
        return new Point2D(x, node.getCenter().getY());
    }

    private Point2D getLeftConnectorPoint(ADVNode node) {
        double x = node.getCenter().getX();
        x -= node.getBoundsInParent().getWidth() / 2;
        return new Point2D(x, node.getCenter().getY());
    }

    /**
     * Determines the intersection of the curve and the target node.
     *
     * @param targetBounds target node
     * @param outside      point outside the target node
     * @param inside       point inside the target node
     * @return intersection point
     */
    private Point2D findIntersectionPoint(Node targetBounds,
                                          Point2D inside,
                                          Point2D outside) {

        Point2D middle = outside.midpoint(inside);

        double deltaX = outside.getX() - inside.getX();
        double deltaY = outside.getY() - inside.getY();

        if (Math.hypot(deltaX, deltaY) < 1.) {
            return middle;
        } else {
            if (targetBounds.contains(targetBounds.parentToLocal(middle))) {
                return findIntersectionPoint(targetBounds, middle, outside);
            } else {
                return findIntersectionPoint(targetBounds, inside, middle);
            }
        }
    }

    /**
     * Sets the control points of the curve
     *
     * @param respectiveCurve        curve
     * @param startIntersectionPoint calculated intersection point
     * @param endIntersectionPoint   calculated intersection point
     */
    protected void setControlPoints(CubicCurve respectiveCurve,
                                    Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {
        // straight line
        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        respectiveCurve.setControlX1(mid.getX());
        respectiveCurve.setControlY1(mid.getY());
        respectiveCurve.setControlX2(mid.getX());
        respectiveCurve.setControlY2(mid.getY());
    }

    protected ADVNode getStartNode() {
        return startNode;
    }

    protected ADVNode getEndNode() {
        return endNode;
    }
}
