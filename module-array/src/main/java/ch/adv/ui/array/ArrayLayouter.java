package ch.adv.ui.array;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import ch.adv.ui.core.presentation.util.StyleConverter;
import ch.adv.ui.core.presentation.widgets.*;
import com.google.inject.Singleton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Positions the ArrayElements on the Pane
 */
@Singleton
public class ArrayLayouter implements Layouter {

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
        layoutedSnapshot
                .setSnapshotDescription(snapshot.getSnapshotDescription());
        layoutedSnapshot.setSnapshotId(snapshot.getSnapshotId());
        layoutedSnapshot.setPane(scalePane);
        return layoutedSnapshot;
    }

    private Map<Long, ADVNode> drawElements(Snapshot snapshot,
                                            AutoScalePane scalePane) {

        Map<Long, ADVNode> nodeMap = new HashMap<>();
        HBox container = new HBox();
        snapshot.getElements().forEach(e -> {
            ArrayElement arrElement = (ArrayElement) e;

            LabeledNode node = new LabeledNode(arrElement
                    .getContent());
            ADVStyle style = arrElement.getStyle();

            node.setBackgroundColor(StyleConverter
                    .getColor(style.getFillColor()));
            node.setFontColor(Color.WHITE);
            node.setConnectorTypeOutgoing(ConnectorType.BOTTOM);
            node.setConnectorTypeIncoming(ConnectorType.LEFT);

            node.setBorder(style.getStrokeThickness(),
                    StyleConverter.getColor(style.getStrokeColor()),
                    StyleConverter.getStrokeStyle(style.getStrokeStyle()));

            if (arrElement.getFixedPosX() > 0
                    && arrElement.getFixedPosY() > 0) {

                node.setY(arrElement.getFixedPosY());
                node.setX(arrElement.getFixedPosX());

                // add fixed positioned elements directly on the scale pane
                // because the hbox would ignore the fixed position.
                scalePane.addChildren(node);
            } else {
                container.getChildren().add(node);
            }

            nodeMap.put(arrElement.getElementId(), node);
        });

        scalePane.addChildren(container);
        return nodeMap;
    }

    private void drawRelations(Snapshot snapshot, AutoScalePane scalePane,
                               Map<Long, ADVNode> nodeMap) {
        snapshot.getRelations().forEach(r -> {

            ADVNode sourceNode = nodeMap.get(r.getSourceElementId());
            ADVNode endNode = nodeMap.get(r.getTargetElementId());

            if (sourceNode != null && endNode != null) {

                LabeledEdge edge = new CurvedLabeledEdge(r.getLabel(),
                        sourceNode,
                        endNode,
                        r.getStyle());

                scalePane.addChildren(edge);
            }
        });
    }

}
