package ch.hsr.adv.ui.array.presentation;

import ch.hsr.adv.commons.array.logic.domain.ArrayElement;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultElementStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultRelationStyle;
import ch.hsr.adv.ui.core.presentation.widgets.*;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Creates an JavaFX array representation in the look of an object array with
 * object references.
 */
@Singleton
public class ArrayObjectReferenceLayouter {
    private static final int SPACING = 30;
    private AutoScalePane scalePane;
    private VBox boxContainer;
    private HBox valueContainer;
    private HBox referenceContainer;

    /**
     * Layouts an array with object references
     *
     * @param moduleGroup moduleGroup to layout
     * @param showIndices flag to show indices above array
     * @return layouted pane
     */
    public Pane layout(ModuleGroup moduleGroup, boolean showIndices) {
        initializeContainer();
        drawElements(moduleGroup, showIndices);

        boxContainer.getChildren()
                .addAll(referenceContainer, valueContainer);
        scalePane.addChildren(boxContainer);

        return scalePane;
    }

    private void initializeContainer() {
        scalePane = new AutoScalePane();
        boxContainer = new VBox();
        valueContainer = new HBox();
        referenceContainer = new HBox();
        referenceContainer.setAlignment(Pos.CENTER);
        valueContainer.setAlignment(Pos.CENTER);
        valueContainer.setSpacing(SPACING);
        boxContainer.setSpacing(SPACING);
    }

    private void drawElements(ModuleGroup moduleGroup, boolean showIndices) {
        for (int i = 0; i < moduleGroup.getElements().size(); i++) {
            ArrayElement element =
                    (ArrayElement) moduleGroup.getElements().get(i);

            ADVStyle style = element.getStyle();
            if (style == null) {
                style = new ADVDefaultElementStyle();
            }

            IndexedNode referenceNode;
            if (element.getContent() != null) {
                LabeledNode valueNode = new LabeledNode(
                        element.getContent(), style);
                referenceNode = createIndexedNode("*", showIndices, i,
                        style);
                drawRelations(referenceNode.getLabeledNode(), valueNode);

                valueContainer.getChildren().add(valueNode);
            } else {
                referenceNode = createIndexedNode("null", showIndices, i,
                        style);
            }

            referenceContainer.getChildren().addAll(referenceNode);
        }
    }

    private void drawRelations(LabeledNode referenceNode,
                               LabeledNode valueNode) {

        LabeledEdge relation = new LabeledEdge("",
                referenceNode, ConnectorType.BOTTOM,
                valueNode, ConnectorType.TOP,
                new ADVDefaultRelationStyle(),
                LabeledEdge.DirectionType.UNIDIRECTIONAL);

        scalePane.addChildren(relation);
    }

    private IndexedNode createIndexedNode(String label, boolean showIndices,
                                          long index, ADVStyle style) {
        return new IndexedNode(index, label, style, false,
                showIndices, IndexPosition.TOP);
    }
}
