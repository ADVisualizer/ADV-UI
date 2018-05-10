package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.ui.array.logic.domain.ArrayElement;
import ch.hsr.adv.ui.core.logic.domain.Snapshot;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.presets.ADVDefaultLineStyle;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.ConnectorType;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Positions the array objects inclusive the references on the Pane
 */
@Singleton
public class ArrayObjectReferenceLayouter {
    private static final int SPACING = 30;
    private final ArrayLayouterUtil layouterUtil;
    private List<LabeledEdge> edgeList = new ArrayList<>();
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

        boxContainer.getChildren()
                .addAll(referenceContainer, valueContainer);
        scalePane.addChildren(boxContainer);

        return scalePane;
    }

    private void initializeContainer() {
        scalePane = new AutoScalePane();
        boxContainer = new VBox() {
            @Override
            protected void layoutChildren() {
                edgeList.forEach(LabeledEdge::update);
                super.layoutChildren();
            }
        };
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
                valueContainer.layout();
                boxContainer.layout();
            } else {
                referenceNode = new LabeledNode("null");
            }

            layouterUtil.setStyling(referenceNode, style);

            referenceContainer.getChildren().addAll(referenceNode);
            referenceContainer.layout();
            boxContainer.layout();
        });
    }

    private void drawRelations(LabeledNode referenceNode,
                               LabeledNode valueNode) {

        LabeledEdge relation = new LabeledEdge("",
                referenceNode, ConnectorType.BOTTOM,
                valueNode, ConnectorType.TOP,
                scalePane.getContent(),
                new ADVDefaultLineStyle(),
                LabeledEdge.DirectionType.UNIDIRECTIONAL);

        scalePane.addChildren(relation);
        edgeList.add(relation);
    }
}
