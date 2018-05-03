package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
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

    private final DirectionType directionType;
    private final ArrowHead startArrowHead;
    private final ArrowHead endArrowHead;


    public LabeledEdge(String labelText, ADVNode startNode, ADVNode endNode,
                       ADVStyle style) {
        this(labelText, startNode, endNode, style, DirectionType.NONE);
    }

    public LabeledEdge(String labelText, ADVNode startNode, ADVNode endNode,
                       ADVStyle style, DirectionType directionType) {

        this.style = style;
        this.startNode = startNode;
        this.endNode = endNode;
        this.directionType = directionType;

        // draw arrow
        switch (directionType) {
            case BIDIRECTIONAL:
                this.startArrowHead = new ArrowHead(curve, 0.0f);
                this.endArrowHead = new ArrowHead(curve, 1.0f);
                getChildren().addAll(startArrowHead, endArrowHead);
                break;
            case UNIDIRECTIONAL:
                this.startArrowHead = null;
                this.endArrowHead = new ArrowHead(curve, 1.0f);
                getChildren().add(endArrowHead);
                break;
            default:
                this.startArrowHead = null;
                this.endArrowHead = null;
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
        curve.setStroke(StyleConverter
                .getColorFromHexValue(style.getStrokeColor()));
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
        label.setTextFill(StyleConverter.getLabelColor(
                StyleConverter.getColorFromHexValue(style.getFillColor())));
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

            if (startNode.getParent() != null && endNode.getParent() != null) {

                if (!startNode.getParent().equals(endNode.getParent())) {

                    Node parent = findFirstCommonAncestor(startNode, endNode);
                    if (parent != null) {
                        startConnectorPoint = localToAncestor(
                                startConnectorPoint, startNode, parent);
                        endConnectorPoint = localToAncestor(
                                endConnectorPoint, endNode, parent);
                    }
                }
            }

            curve.setStartX(startConnectorPoint.getX());
            curve.setStartY(startConnectorPoint.getY());
            curve.setEndX(endConnectorPoint.getX());
            curve.setEndY(endConnectorPoint.getY());
            setControlPoints(curve, startConnectorPoint, endConnectorPoint);

            // arrow
            switch (directionType) {
                case BIDIRECTIONAL:
                    startArrowHead.update();
                    endArrowHead.update();
                    break;
                case UNIDIRECTIONAL:
                    endArrowHead.update();
                    break;
                case NONE:
                    break;
                default:
                    logger.warn("Unsupported direction type: {}",
                            directionType);
            }
        }
    }

    private Point2D localToAncestor(Point2D point, Node local, Node ancestor) {
        if (local == ancestor) {
            return point;
        }

        Point2D transformedPoint = local.localToParent(point);
        return localToAncestor(transformedPoint, local.getParent(), ancestor);
    }

    private Node findFirstCommonAncestor(Node node1, Node node2) {
        if (node1 == null || node2 == null) {
            return null;
        }
        if (isDescendant(node1, node2)) {
            return node2;
        }
        return findFirstCommonAncestor(node1, node2.getParent());
    }

    private boolean isDescendant(Node candidate, Node node) {
        if (candidate == null) {
            return false;
        } else if (candidate == node) {
            return true;
        }
        return isDescendant(candidate.getParent(), node);
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

                Point2D inside;
                Point2D outside;
                if (node == endNode) {
                    inside = endNode.getCenter();
                    outside = startNode.getCenter();
                } else {
                    inside = startNode.getCenter();
                    outside = endNode.getCenter();
                }

                return findIntersectionPoint(node, inside, outside);
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

    /**
     * Relationship type of the arrow
     * <p>
     * Can be unidirectional, bidirectional or no arrow.
     */
    public enum DirectionType {
        UNIDIRECTIONAL, BIDIRECTIONAL, NONE
    }
}