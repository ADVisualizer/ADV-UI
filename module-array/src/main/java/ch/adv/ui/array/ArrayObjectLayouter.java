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
    public void layoutObjectReference(ArrayElement arrayElement,
                                      AutoScalePane scalePane,
                                      HBox valueContainer,
                                      HBox referenceContainer) {

        ADVStyle style = arrayElement.getStyle();

        LabeledNode referenceNode;
        LabeledNode valueNode;

        // create nods
        if (arrayElement.getContent() != null) {
            referenceNode = new LabeledNode("*");
            valueNode = new LabeledNode(arrayElement.getContent());

            // value styles
            valueNode.setFontColor(Color.WHITE);
            valueNode.setBackgroundColor(StyleConverter.getColor(
                    style.getFillColor()));
            valueNode.setBorder(style.getStrokeThickness(),
                    StyleConverter.getColor(style.getStrokeColor()),
                    StyleConverter
                            .getStrokeStyle(style.getStrokeStyle()));

            // relation
            referenceNode.setConnectorTypeOutgoing(ConnectorType.BOTTOM);
            valueNode.setConnectorTypeIncoming(ConnectorType.TOP);

            LabeledEdge relation = new CurvedLabeledEdge("",
                    referenceNode, valueNode, new ADVDefaultLineStyle());
            scalePane.addChildren(relation);

            valueContainer.getChildren().add(valueNode);
        } else {
            referenceNode = new LabeledNode("null");
        }

        // reference styles
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
