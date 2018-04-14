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
import javafx.scene.text.Font;

/**
 * Generic component for an labeled edge with optional arrows.
 * <p>
 * Can be used to display the relation between two nodes.
 *
 * @author mwieland
 */
public class LabeledEdge extends Group {

    private static final int LABEL_FONT_SIZE = 12;
    protected final CubicCurve curve = new CubicCurve();
    protected final Label label = new Label();
    protected final Arrow.DirectionType directionType;
    protected final ADVStyle style;
    protected final Arrow startArrow;
    protected final Arrow endArrow;
    protected final Node startNode;
    protected final Node endNode;

    private final ObjectProperty<Point2D> startCenter = new
            SimpleObjectProperty<>();
    private final ObjectProperty<Point2D> endCenter = new
            SimpleObjectProperty<>();


    public LabeledEdge(String labelText, Node startNode, Node endNode,
                       ADVStyle style) {
        this(labelText, startNode, endNode, style,
                Arrow.DirectionType.UNIDIRECTIONAL);
    }

    public LabeledEdge(String labelText, Node startNode, Node endNode,
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
        startCenter.addListener(this::updateCurvePoints);
        endCenter.addListener(this::updateCurvePoints);

        startNode.boundsInParentProperty()
                .addListener(this::computeStartCenterPoint);
        endNode.boundsInParentProperty()
                .addListener(this::computeEndCenterPoint);

        initializeComponent(labelText);
    }

    public void initializeComponent(String labelText) {
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
        Binding xProperty = Bindings.createDoubleBinding(() ->
                        (curve.getStartX() + curve.getEndX()) / 2,
                curve.startXProperty(), curve.endXProperty());

        Binding yProperty = Bindings.createDoubleBinding(() ->
                        (curve.getStartY() + curve.getEndY()) / 2,
                curve.startYProperty(), curve.endYProperty());

        label.setText(labelText);
        //TODO: replace with fill color
        label.setTextFill(Color.BLACK);
        label.setFont(new Font(LABEL_FONT_SIZE));
        label.layoutXProperty().bind(xProperty);
        label.layoutYProperty().bind(yProperty);
    }

    private void computeStartCenterPoint(Observable o) {
        if (startNode.getBoundsInParent().getHeight() > 0 &&
                startNode.getBoundsInParent().getWidth() > 0) {

            Point2D center = computeCenter(startNode);
            startCenter.set(center);
        }
    }


    private void computeEndCenterPoint(Observable o) {
        if (endNode.getBoundsInParent().getHeight() > 0 &&
                endNode.getBoundsInParent().getWidth() > 0) {

            Point2D center = computeCenter(endNode);
            endCenter.set(center);
        }
    }

    private Point2D computeCenter(Node node) {
        double centerX = node.getBoundsInParent().getMinX()
                + node.getBoundsInParent().getWidth() / 2;
        double centerY = node.getBoundsInParent().getMinY()
                + node.getBoundsInParent().getHeight() / 2;
        return new Point2D(centerX, centerY);
    }

    private void updateCurvePoints(Observable o) {
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

            setControlPoints(startIntersection, endIntersection);

            switch (directionType) {
                case BIDIRECTIONAL:
                    startArrow.update();
                case UNIDIRECTIONAL:
                    endArrow.update();
            }
        }
    }

    protected void setControlPoints(Point2D start, Point2D endPoint) {
        // straight line
        Point2D mid = start.midpoint(endPoint);
        curve.setControlX1(mid.getX());
        curve.setControlY1(mid.getY());
        curve.setControlX2(mid.getX());
        curve.setControlY2(mid.getY());
    }

    /**
     * Determines the intersection of the curve and the target node.
     *
     * @param targetBounds target node
     * @param outside      point outside the target node
     * @param inside       point inside the target node
     * @return intersection point
     */
    protected Point2D findIntersectionPoint(Node targetBounds,
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
}
