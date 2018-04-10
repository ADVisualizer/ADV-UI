package ch.adv.ui.core.presentation.widgets;

import ch.adv.ui.core.domain.styles.ADVStyle;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;


public class LabeledEdge extends Group {

    private final ADVStyle style;

    private final Node startNode;
    private final Node endNode;

    // center coordinates of the start and end node
    private final ObjectProperty<Point2D> startCenter = new
            SimpleObjectProperty<>();
    private final ObjectProperty<Point2D> endCenter = new
            SimpleObjectProperty<>();

    private final CubicCurve curve = new CubicCurve();
    private final Label label = new Label();

    public LabeledEdge(String labelText, Node startNode, Node endNode,
                       boolean directed, ADVStyle style) {

        this.startNode = startNode;
        this.endNode = endNode;
        this.style = style;

        curve.setManaged(false);
        label.setManaged(false);

        startNode.boundsInParentProperty().addListener(this::computeStartPoint);
        endNode.boundsInParentProperty().addListener(this::computeEndPoint);
        startCenter.addListener(this::coordinatesChangedListener);
        endCenter.addListener(this::coordinatesChangedListener);

        curve.setStrokeWidth(style.getStrokeThickness());
        curve.setStroke(Color.BLUEVIOLET);

        if (directed) {
            drawArrows();
        }

        drawLabel(labelText);

        getChildren().addAll(curve, label);
    }

    private void drawArrows() {

    }

    private void drawLabel(String labelText) {
        label.setText(labelText);

        label.layoutXProperty().bind(curve.startXProperty().subtract(curve
                .endXProperty()));
        label.layoutYProperty().bind(curve.startYProperty().subtract(curve
                .endYProperty()));
    }


    private void computeStartPoint(ObservableValue<? extends Bounds> observable,
                                   Bounds oldValue,
                                   Bounds newValue) {

        if (newValue.getHeight() > 0 && newValue.getWidth() > 0) {
            Point2D center = computeCenter(newValue);
            endCenter.set(center);
        }
    }


    private void computeEndPoint(ObservableValue<? extends Bounds> observable,
                                 Bounds oldValue,
                                 Bounds newValue) {
        if (newValue.getHeight() > 0 && newValue.getWidth() > 0) {
            Point2D center = computeCenter(newValue);
            startCenter.set(center);
        }
    }

    private void coordinatesChangedListener(Observable o) {
        if (startCenter.get() != null && endCenter.get() != null) {

            Point2D startIntersection = findIntersectionPoint(startNode,
                    startCenter.get(),
                    endCenter.get());

            Point2D endIntersection = findIntersectionPoint(endNode,
                    endCenter.get(),
                    startCenter.get());

            curve.setStartX(startIntersection.getX());
            curve.setStartY(startIntersection.getY());
            curve.setEndX(endIntersection.getX());
            curve.setEndY(endIntersection.getY());
        }
    }

    private Point2D computeCenter(Bounds bounds) {
        double centerX = bounds.getMaxX() - bounds.getMinX();
        double centerY = bounds.getMaxY() - bounds.getMinY();

        return new Point2D(centerX, centerY);
    }


    /**
     * Determines the intersection point of the curve with the target node.
     * This is equal to the very edge of the node.
     *
     * @param targetBounds target node
     * @param outside      point outside the target node
     * @param inside       point inside the target node
     * @return
     */
    private Point2D findIntersectionPoint(Node targetBounds, Point2D outside,
                                          Point2D inside) {

        Point2D middle = outside.midpoint(inside);

        double deltaX = outside.getX() - inside.getX();
        double deltaY = outside.getY() - inside.getY();

        if (Math.hypot(deltaX, deltaY) < 1.) {
            return middle;
        } else {
            if (targetBounds.contains(middle)) {
                return findIntersectionPoint(targetBounds, outside, middle);
            } else {
                return findIntersectionPoint(targetBounds, middle, inside);
            }
        }
    }

}
