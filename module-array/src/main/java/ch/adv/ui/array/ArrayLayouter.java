package ch.adv.ui.array;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import ch.adv.ui.core.presentation.util.StyleConverter;
import ch.adv.ui.core.presentation.widgets.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Positions the ArrayElements on the Pane
 */
@Singleton
public class ArrayLayouter implements Layouter {

    private static final int SPACING = 30;
    private static final Logger logger = LoggerFactory.getLogger(
            ArrayLayouter.class);

    private ArrayObjectLayouter arrayObjectLayouter;

    @Inject
    public ArrayLayouter(ArrayObjectLayouter objectLayouter) {
        this.arrayObjectLayouter = objectLayouter;
    }

    /**
     * Layouts an Array snapshot if it is not already layouted
     *
     * @param snapshot to be layouted
     * @return layouted snapshot
     */
    @Override
    public LayoutedSnapshot layout(Snapshot snapshot) {
        AutoScalePane scalePane = new AutoScalePane();

        Map<Long, ADVNode> nodeMap = drawElements(snapshot, scalePane);
        drawRelations(snapshot, scalePane, nodeMap);

        return createLayoutedSnapshot(snapshot, scalePane);
    }

    private LayoutedSnapshot createLayoutedSnapshot(Snapshot snapshot,
                                                    AutoScalePane scalePane) {
        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot();
        layoutedSnapshot.setSnapshotDescription(
                snapshot.getSnapshotDescription());
        layoutedSnapshot.setSnapshotId(snapshot.getSnapshotId());
        layoutedSnapshot.setPane(scalePane);
        return layoutedSnapshot;
    }

    private Map<Long, ADVNode> drawElements(Snapshot snapshot, AutoScalePane
            scalePane) {
        logger.info("Drawing array elements...");
        Map<Long, ADVNode> availableNodesMap = new HashMap<>();

        VBox boxContainer = new VBox();
        HBox valueContainer = new HBox();
        HBox referenceContainer = new HBox();
        referenceContainer.setAlignment(Pos.CENTER);
        valueContainer.setAlignment(Pos.CENTER);
        boxContainer.setSpacing(SPACING);

        snapshot.getElements().forEach(e -> {
            ArrayElement arrayElement = (ArrayElement) e;
            ADVStyle style = arrayElement.getStyle();
            LabeledNode valueNode = createLabeledNode(arrayElement, style);
            if (arrayElement.isShowObjectReference()) {
                valueContainer.setSpacing(SPACING);
                logger.info("Delegating array layouting to show object "
                        + "references.");
                arrayObjectLayouter.layoutObjectReference(valueNode,
                        arrayElement, scalePane, valueContainer,
                        referenceContainer);
            } else {
                valueNode.setConnectorTypeOutgoing(ConnectorType.TOP);
                valueNode.setConnectorTypeIncoming(ConnectorType.TOP);

                if (arrayElement.getFixedPosX() > 0
                        && arrayElement.getFixedPosY() > 0) {

                    valueNode.setY(arrayElement.getFixedPosY());
                    valueNode.setX(arrayElement.getFixedPosX());

                    // add fixed positioned elements directly on the scale pane
                    // because the hbox would ignore the fixed position.
                    scalePane.addChildren(valueNode);
                } else {
                    valueContainer.getChildren().add(valueNode);
                }
                availableNodesMap.put(arrayElement.getElementId(), valueNode);
            }
        });

        // add wrapper container to master pane
        boxContainer.getChildren().addAll(referenceContainer, valueContainer);
        scalePane.addChildren(boxContainer);

        return availableNodesMap;
    }

    private LabeledNode createLabeledNode(ArrayElement arrayElement, ADVStyle
            style) {
        LabeledNode valueNode = new LabeledNode(arrayElement
                .getContent());
        valueNode.setBackgroundColor(StyleConverter.getColor(
                style.getFillColor()));
        valueNode.setFontColor(Color.WHITE);
        valueNode.setBorder(style.getStrokeThickness(),
                StyleConverter.getColor(style.getStrokeColor()),
                StyleConverter.getStrokeStyle(style.getStrokeStyle()));
        return valueNode;
    }

    //TODO: Do we event need this?
    private void drawRelations(Snapshot snapshot, AutoScalePane scalePane,
                               Map<Long, ADVNode> nodeMap) {
        logger.info("Drawing array relations...");
        snapshot.getRelations().forEach(r -> {

            ADVNode sourceNode = nodeMap.get(r.getSourceElementId());
            ADVNode endNode = nodeMap.get(r.getTargetElementId());

            if (sourceNode != null && endNode != null) {

                LabeledEdge edge = new CurvedLabeledEdge(r.getLabel(),
                        sourceNode,
                        endNode,
                        r.getStyle(), LabeledEdge.DirectionType.UNIDIRECTIONAL);

                scalePane.addChildren(edge);
            }
        });
    }

}
