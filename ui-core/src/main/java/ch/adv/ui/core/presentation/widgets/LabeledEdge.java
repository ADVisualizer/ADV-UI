package ch.adv.ui.core.presentation.widgets;

import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.util.StyleConverter;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeType;


/**
 * Generic component for an labeled edge with optional arrows.
 * <p>
 * Can be used to display the relation between two nodes.
 *
 * @author mwielands
 */
public class LabeledEdge extends Group {

    private final ADVStyle style;
    private final Node startNode;
    private final Node endNode;

    private final CubicCurve curve = new CubicCurve();
    private final Label label = new Label();

    private final ObjectProperty<Point2D> startCenter = new
            SimpleObjectProperty<>();
    private final ObjectProperty<Point2D> endCenter = new
            SimpleObjectProperty<>();

    public LabeledEdge(String labelText, Node startNode, Node endNode,
                       boolean directed, ADVStyle style) {

        this.startNode = startNode;
        this.endNode = endNode;
        this.style = style;

        label.setManaged(false);
        curve.setManaged(false);

        startNode.boundsInParentProperty()
                .addListener(this::computeStartCenter);
        endNode.boundsInParentProperty().addListener(this::computeEndCenter);

        startCenter.addListener(this::updateCurvePoints);
        endCenter.addListener(this::updateCurvePoints);

        applyStyle();
        drawLabel(labelText);
        if (directed) {
            drawArrows();
        }

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
            double maxX = Math.max(curve.startXProperty().get(),
                    curve.endXProperty().get());
            double minX = Math.min(curve.startXProperty().get(),
                    curve.endXProperty().get());
            return (maxX - minX) / 2;
        }, curve.startXProperty(), curve.endXProperty());

        Binding yProperty = Bindings.createDoubleBinding(() -> {
            double maxY = Math.max(curve.startYProperty().get(),
                    curve.endYProperty().get());
            double minY = Math.min(curve.startYProperty().get(),
                    curve.endYProperty().get());
            return (maxY - minY) / 2;
        }, curve.startYProperty(), curve.endYProperty());

        label.layoutXProperty().bind(xProperty);
        label.layoutYProperty().bind(yProperty);
        label.setText(labelText);
    }

    private void computeStartCenter(Observable o) {
        if (startNode.getBoundsInParent().getHeight() > 0 &&
                startNode.getBoundsInParent().getWidth() > 0) {
            Point2D center = computeCenter(startNode);
            startCenter.set(center);
        }
    }


    private void computeEndCenter(Observable o) {
        if (endNode.getBoundsInParent().getHeight() > 0 &&
                endNode.getBoundsInParent().getWidth() > 0) {
            Point2D center = computeCenter(endNode);
            endCenter.set(center);
        }
    }

    private Point2D computeCenter(Node node) {
        double centerX = node.getLayoutX() + node.getBoundsInParent()
                .getWidth() / 2;
        double centerY = node.getLayoutY() + node.getBoundsInParent()
                .getHeight() / 2;

        return new Point2D(centerX, centerY);
    }

    private void updateCurvePoints(Observable o) {
        if (startCenter.get() != null && endCenter.get() != null &&
                startNode != null && endNode != null) {

            Point2D startIntersection = findIntersectionPoint(startNode,
                    startCenter.get(),
                    endCenter.get());

            Point2D endIntersection = findIntersectionPoint(endNode,
                    endCenter.get(),
                    startCenter.get());

            Point2D mid = startIntersection.midpoint(endIntersection);

            curve.setStartX(startIntersection.getX());
            curve.setStartY(startIntersection.getY());
            curve.setEndX(endIntersection.getX());
            curve.setEndY(endIntersection.getY());
            curve.setControlX1(mid.getX());
            curve.setControlY1(mid.getY());
        }
    }

    /**
     * Determines the intersection point of the curve and the target node.
     * The intersection point lays on the border of the node.
     *
     * @param targetBounds target node
     * @param outside      point outside the target node
     * @param inside       point inside the target node
     * @return
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
            if (targetBounds.contains(middle)) {
                return findIntersectionPoint(targetBounds, middle, outside);
            } else {
                return findIntersectionPoint(targetBounds, inside, middle);
            }
        }
    }

    private void drawArrows() {
        // TODO:  draw arrowy
    }

}
