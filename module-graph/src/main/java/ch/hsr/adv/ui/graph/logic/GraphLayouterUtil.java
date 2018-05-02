package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.presets.ADVDefaultStyle;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Singleton;
import javafx.scene.paint.Color;

/**
 * Common layouter methods
 */
@Singleton
public class GraphLayouterUtil {

    /**
     * Sets the styling for a graph node
     *
     * @param node  array node
     * @param style style
     */
    public void setStyling(LabeledNode node, ADVStyle style) {
        if (style == null) {
            style = new ADVDefaultStyle();
        }
        Color fillColor = StyleConverter
                .getColorFromHexValue(style.getFillColor());

        node.setBackgroundColor(fillColor);
        node.setFontColor(StyleConverter.getLabelColor(fillColor));
        node.setBorder(style.getStrokeThickness(),
                StyleConverter.getColorFromHexValue(style.getStrokeColor()),
                StyleConverter.getStrokeStyle(style.getStrokeStyle()));
    }
}
