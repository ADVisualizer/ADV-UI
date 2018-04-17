package ch.adv.ui.array;

import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.domain.styles.presets.ADVDefaultLineStyle;
import ch.adv.ui.core.presentation.util.StyleConverter;
import ch.adv.ui.core.presentation.widgets.*;
import com.google.inject.Singleton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Positions the array objects inclusive the references on the Pane
 */
@Singleton
public class ArrayObjectLayouter {

    /**
     * Layout array elements with visible object reference
     *
     * @param arrayElement       element to layout
     * @param scalePane          parent pane
     * @param valueContainer     pane for value
     * @param referenceContainer pane for reference
     */
    public void layoutObjectReference(LabeledNode node, ArrayElement
            arrayElement,
                                      AutoScalePane scalePane,
                                      HBox valueContainer,
                                      HBox referenceContainer) {


        LabeledNode referenceNode;
        LabeledNode valueNode = node;

        // create nodes
        if (arrayElement.getContent() != null) {
            referenceNode = new LabeledNode("*");

            // relation
            referenceNode.setConnectorTypeOutgoing(ConnectorType.BOTTOM);
            valueNode.setConnectorTypeIncoming(ConnectorType.TOP);

            LabeledEdge relation = new CurvedLabeledEdge("",
                    referenceNode, valueNode, new ADVDefaultLineStyle(),
                    LabeledEdge.DirectionType.UNIDIRECTIONAL);
            scalePane.addChildren(relation);

            valueContainer.getChildren().add(valueNode);
        } else {
            referenceNode = new LabeledNode("null");
        }

        // reference styles
        ADVStyle style = arrayElement.getStyle();
        referenceNode.setConnectorTypeOutgoing(ConnectorType.BOTTOM);
        referenceNode.setFontColor(Color.WHITE);
        referenceNode.setBackgroundColor(StyleConverter.getColor(
                style.getFillColor()));
        referenceNode.setBorder(style.getStrokeThickness(),
                StyleConverter.getColor(style.getStrokeColor()),
                StyleConverter.getStrokeStyle(style.getStrokeStyle()));

        referenceContainer.getChildren().addAll(referenceNode);
    }
}
