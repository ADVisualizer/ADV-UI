package ch.adv.ui.array;

import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.util.StyleConverter;
import ch.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Singleton;
import javafx.scene.paint.Color;

@Singleton
public class ArrayLayouterUtil {

    public void setStyling(LabeledNode node, ADVStyle style) {
        node.setFontColor(Color.WHITE);
        node.setBackgroundColor(StyleConverter.getColor(
                style.getFillColor()));
        node.setBorder(style.getStrokeThickness(),
                StyleConverter.getColor(style.getStrokeColor()),
                StyleConverter.getStrokeStyle(style.getStrokeStyle()));
    }
}
