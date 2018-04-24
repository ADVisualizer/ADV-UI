package ch.adv.ui.array.logic;

import ch.adv.ui.core.logic.domain.Snapshot;
import ch.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.adv.ui.core.logic.domain.styles.presets.ADVDefaultLineStyle;
import ch.adv.ui.core.presentation.widgets.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Positions the array objects inclusive the references on the Pane
 */
@Singleton
public class ArrayObjectReferenceLayouter {

    private static final int SPACING = 30;

    private final ArrayLayouterUtil layouterUtil;

    private AutoScalePane scalePane;
    private VBox boxContainer;
    private HBox valueContainer;
    private HBox referenceContainer;

    @Inject
    public ArrayObjectReferenceLayouter(ArrayLayouterUtil layouterUtil) {
        this.layouterUtil = layouterUtil;
    }

    /**
     * Layout array object reference
     *
     * @param snapshot snapshot to layout
     * @return layouted pane
     */
    public Pane layout(Snapshot snapshot) {
        initializeContainer();
        drawElements(snapshot);

        boxContainer.getChildren().addAll(referenceContainer, valueContainer);
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

    private void drawElements(Snapshot snapshot) {
        snapshot.getElements().forEach(e -> {
            ArrayElement element = (ArrayElement) e;
            ADVStyle style = element.getStyle();

            LabeledNode referenceNode;
            if (element.getContent() != null) {
                LabeledNode valueNode = new LabeledNode(element.getContent());
                referenceNode = new LabeledNode("*");
                drawRelations(referenceNode, valueNode);

                layouterUtil.setStyling(valueNode, style);

                valueContainer.getChildren().add(valueNode);
            } else {
                referenceNode = new LabeledNode("null");
            }

            layouterUtil.setStyling(referenceNode, style);

            referenceContainer.getChildren().addAll(referenceNode);
        });
    }

    private void drawRelations(LabeledNode referenceNode,
                               LabeledNode valueNode) {
        referenceNode.setConnectorTypeOutgoing(ConnectorType.BOTTOM);
        valueNode.setConnectorTypeIncoming(ConnectorType.TOP);

        LabeledEdge relation = new CurvedLabeledEdge("",
                referenceNode, valueNode, new ADVDefaultLineStyle(),
                LabeledEdge.DirectionType.UNIDIRECTIONAL);

        scalePane.addChildren(relation);
    }
}
