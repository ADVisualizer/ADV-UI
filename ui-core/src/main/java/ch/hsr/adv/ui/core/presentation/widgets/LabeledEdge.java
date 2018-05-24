package ch.hsr.adv.ui.core.presentation.widgets;

import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets
        .ADVDefaultRelationStyleTEST;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Bounds;
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
 * @author mtrentini
 */
public class LabeledEdge extends Group {

    private static final Logger logger = LoggerFactory.getLogger(
            LabeledEdge.class);

    private static final int LABEL_MARGIN = 5;
    private static final int LABEL_FONT_SIZE = 10;

    private final LabeledNode startNode;
    private final ConnectorType startConnector;
    private final LabeledNode endNode;
    private final ConnectorType endConnector;
    private final CubicCurve curve = new CubicCurve();
    private final Label label = new Label();
    private final ADVStyle style;
    private final DirectionType directionType;
    private final ArrowHead startArrowHead;
    private final ArrowHead endArrowHead;
    private Bounds startBounds;
    private Bounds endBounds;


    public LabeledEdge(
            String labelText,
            LabeledNode startNode,
            ConnectorType startConnector,
            LabeledNode endNode,
            ConnectorType endConnector,
            ADVStyle style) {
        this(labelText, startNode, startConnector, endNode, endConnector,
                style, DirectionType.NONE);
    }

    public LabeledEdge(
            String labelText,
            LabeledNode startNode,
            ConnectorType startConnector,
            LabeledNode endNode,
            ConnectorType endConnector,
            ADVStyle style, DirectionType directionType) {

        this.style = style;
        this.startNode = startNode;
        this.startConnector = startConnector;
        this.endNode = endNode;
        this.endConnector = endConnector;
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

        initializeComponent(labelText);
    }

    private void initializeComponent(String labelText) {
        applyStyle(labelText);
        drawLabel();

        getChildren().addAll(curve, label);
    }

    private void applyStyle(String labelText) {
        label.setText(labelText);
        label.setTextFill(StyleConverter.getColorFromHexValue(
                style.getFillColor()));
        label.setFont(new Font(LABEL_FONT_SIZE));

        curve.setStrokeWidth(style.getStrokeThickness());
        curve.setStroke(StyleConverter
                .getColorFromHexValue(style.getStrokeColor()));
        curve.getStrokeDashArray().addAll(StyleConverter.getStrokeStyle(
                style.getStrokeStyle()).getDashArray());
        curve.setFill(Color.TRANSPARENT); // area of the bezier curve
        curve.setStrokeType(StrokeType.CENTERED);
    }

    /**
     * Draws a centered label
     */
    protected void drawLabel() {
        DoubleBinding xProperty = Bindings.createDoubleBinding(() -> {
            if (label.getWidth() > 0) {
                double centerX = (curve.getControlX1()
                        + curve.getControlX2()) / 2;
                double labelCenter = (label.getWidth() / 2);
                return centerX - labelCenter;
            }
            return 0.0;
        }, curve.startXProperty(), curve.endXProperty());

        DoubleBinding yProperty = Bindings.createDoubleBinding(() -> {
            double centerY = (curve.getControlY1() + curve.getControlY2()) / 2;
            return centerY - LABEL_MARGIN;
        }, curve.startYProperty(), curve.endYProperty());

        label.layoutXProperty().bind(xProperty);
        label.layoutYProperty().bind(yProperty);
    }

    /**
     * Manually update the position of this edge
     */
    public void update() {
        startBounds = getRelativeBounds(startNode, getParent());
        endBounds = getRelativeBounds(endNode, getParent());
        if (startBounds.getHeight() != 0 && startBounds.getWidth() != 0
                && endBounds.getWidth() != 0 && endBounds.getHeight() != 0) {

            Point2D startCenter = getConnectorPoint(startBounds,
                    startConnector);
            curve.setStartX(startCenter.getX());
            curve.setStartY(startCenter.getY());

            Point2D endCenter = getConnectorPoint(endBounds, endConnector);
            curve.setEndX(endCenter.getX());
            curve.setEndY(endCenter.getY());

            setControlPoints(
                    new Point2D(curve.getStartX(), curve.getStartY()),
                    new Point2D(curve.getEndX(), curve.getEndY()));

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

    /**
     * Returns intersection point for the corresponding connector of an edge
     *
     * @param boundsInCommonAncestor anchestor bounds
     * @param connectorType          connector
     * @return x/y coordinates
     */
    protected Point2D getConnectorPoint(Bounds boundsInCommonAncestor,
                                        ConnectorType connectorType) {
        Point2D center = getCenter(boundsInCommonAncestor);
        Point2D startCenter = getCenter(startBounds);
        Point2D endCenter = getCenter(endBounds);
        double y = center.getY();
        double x = center.getX();
        switch (connectorType) {
            case BOTTOM:
                y += boundsInCommonAncestor.getHeight() / 2;
                return new Point2D(x, y);
            case TOP:
                y -= boundsInCommonAncestor.getHeight() / 2;
                return new Point2D(x, y);
            case LEFT:
                x -= boundsInCommonAncestor.getWidth() / 2;
                return new Point2D(x, y);
            case RIGHT:
                x += boundsInCommonAncestor.getWidth() / 2;
                return new Point2D(x, y);
            case DIRECT:

                Point2D inside;
                Point2D outside;

                if (center.equals(endCenter)) {
                    inside = endCenter;
                    outside = startCenter;
                } else {
                    inside = startCenter;
                    outside = endCenter;
                }

                return findIntersectionPoint(boundsInCommonAncestor, inside,
                        outside);
            default:
                logger.warn("Unknown ConnectorType {}", connectorType);
                return null;
        }
    }

    private Bounds getRelativeBounds(Node node, Node relativeNode) {
        Bounds nodeBoundsInScene = node.localToScene(node.getBoundsInLocal());
        return relativeNode.sceneToLocal(nodeBoundsInScene);
    }

    private Point2D getCenter(Bounds bounds) {
        return new Point2D(bounds.getMinX() + bounds.getWidth() / 2, bounds
                .getMinY() + bounds.getHeight() / 2);
    }

    /**
     * Determines the intersection of the curve and the target node.
     *
     * @param bounds  relative bounds
     * @param outside point outside the bounds
     * @param inside  point inside the bounds
     * @return intersection point
     */
    private Point2D findIntersectionPoint(Bounds bounds, Point2D
            inside, Point2D outside) {
        Point2D middle = outside.midpoint(inside);

        double deltaX = outside.getX() - inside.getX();
        double deltaY = outside.getY() - inside.getY();

        if (Math.hypot(deltaX, deltaY) < 1.) {
            return middle;
        } else {
            if (bounds.contains(middle)) {
                return findIntersectionPoint(bounds, middle, outside);
            } else {
                return findIntersectionPoint(bounds, inside, middle);
            }
        }
    }

    /**
     * Sets the control points of the curve
     *
     * @param startIntersectionPoint calculated intersection point
     * @param endIntersectionPoint   calculated intersection point
     */
    protected void setControlPoints(Point2D startIntersectionPoint,
                                    Point2D endIntersectionPoint) {
        // straight line
        Point2D mid = startIntersectionPoint.midpoint(endIntersectionPoint);
        curve.setControlX1(mid.getX());
        curve.setControlY1(mid.getY());
        curve.setControlX2(mid.getX());
        curve.setControlY2(mid.getY());
    }

    protected ConnectorType getStartConnector() {
        return startConnector;
    }

    protected ConnectorType getEndConnector() {
        return endConnector;
    }

    protected Bounds getStartBounds() {
        return startBounds;
    }

    protected Bounds getEndBounds() {
        return endBounds;
    }

    protected CubicCurve getCurve() {
        return curve;
    }
    protected Label getLabel() {
        return label;
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
