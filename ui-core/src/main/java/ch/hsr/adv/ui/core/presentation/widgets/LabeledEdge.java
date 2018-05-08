package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
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
    private final ConnectorType startConnector;
    private final ADVNode endNode;
    private final ConnectorType endConnector;
    private final CubicCurve curve = new CubicCurve();
    private final Label label = new Label();
    private final ADVStyle style;
    private final DirectionType directionType;
    private final ArrowHead startArrowHead;
    private final ArrowHead endArrowHead;
    private final Region edgeContainer;


    public LabeledEdge(
            String labelText,
            ADVNode startNode,
            ConnectorType startConnector,
            ADVNode endNode,
            ConnectorType endConnector,
            Region edgeContainer,
            ADVStyle style) {
        this(labelText, startNode, startConnector, endNode, endConnector,
                edgeContainer, style, DirectionType.NONE);
    }

    public LabeledEdge(
            String labelText,
            ADVNode startNode,
            ConnectorType startConnector,
            ADVNode endNode,
            ConnectorType endConnector,
            Region edgeContainer,
            ADVStyle style, DirectionType directionType) {

        this.style = style;
        this.startNode = startNode;
        this.startConnector = startConnector;
        this.endNode = endNode;
        this.endConnector = endConnector;
        this.edgeContainer = edgeContainer;
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
            if (startNode.getWidth() != 0 && startNode.getHeight() != 0 &&
                    endNode.getWidth() != 0 && endNode.getHeight() != 0) {
                Point2D startConnectorPoint = getConnectorPoint
                        (startNode, startConnector);
                startConnectorPoint = convertToEdgeContainer
                        (startConnectorPoint);
                curve.setStartX(startConnectorPoint.getX());
                curve.setStartY(startConnectorPoint.getY());


                Point2D endConnectorPoint = getConnectorPoint
                        (endNode, endConnector);
                endConnectorPoint = convertToEdgeContainer(endConnectorPoint);
                curve.setEndX(endConnectorPoint.getX());
                curve.setEndY(endConnectorPoint.getY());


                setControlPoints(curve, startConnectorPoint, endConnectorPoint);
            }
        }

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

    private Point2D convertToEdgeContainer(Point2D point) {
        //TODO:
        Point2D pointInLocal = ((AutoScalePane) edgeContainer).getContent()
                .sceneToLocal(point);
        return pointInLocal;
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
     * @param node    target node
     * @param outside point outside the target node
     * @param inside  point inside the target node
     * @return intersection point
     */
    private Point2D findIntersectionPoint(Node node,
                                          Point2D inside,
                                          Point2D outside) {
        Bounds bounds = node.getLayoutBounds();
        bounds = node.localToScene(bounds);

        Point2D middle = outside.midpoint(inside);

        double deltaX = outside.getX() - inside.getX();
        double deltaY = outside.getY() - inside.getY();

        if (Math.hypot(deltaX, deltaY) < 1.) {
            return middle;
        } else {
            if (bounds.contains(middle)) {
                return findIntersectionPoint(node, middle, outside);
            } else {
                return findIntersectionPoint(node, inside, middle);
            }
        }
    }

    /**
     * Sets the control points of the curve
     *
     * @param cubicCurve             curve to adapt curvature on
     * @param startIntersectionPoint calculated intersection point
     * @param endIntersectionPoint   calculated intersection point
     */
    // needs curve as an input parameter, so the curvature can be adapted in
    // subclasses
    protected void setControlPoints(CubicCurve cubicCurve, Point2D
            startIntersectionPoint,
                                    Point2D endIntersectionPoint) {
        // straight line
        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        cubicCurve.setControlX1(mid.getX());
        cubicCurve.setControlY1(mid.getY());
        cubicCurve.setControlX2(mid.getX());
        cubicCurve.setControlY2(mid.getY());
    }

    protected ADVNode getStartNode() {
        return startNode;
    }

    protected ADVNode getEndNode() {
        return endNode;
    }

    protected ConnectorType getStartConnector() {
        return startConnector;
    }

    protected ConnectorType getEndConnector() {
        return endConnector;
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
