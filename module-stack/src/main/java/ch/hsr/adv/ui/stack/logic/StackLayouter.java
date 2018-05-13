package ch.hsr.adv.ui.stack.logic;

import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import ch.hsr.adv.ui.stack.logic.domain.ModuleConstants;
import ch.hsr.adv.ui.stack.logic.domain.StackElement;
import com.google.inject.Singleton;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Positions the StackElements on the Pane
 */
@Singleton
@Module(ModuleConstants.MODULE_NAME)
public class StackLayouter implements Layouter {

    public static final int PADDING = 5;

    /**
     * Layouts n Stack snapshot if it is not already layouted
     *
     * @param moduleGroup to be layouted
     * @return layouted snapshot
     */
    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String>
            flags) {
        return drawElements(moduleGroup);
    }

    private Pane drawElements(ModuleGroup moduleGroup) {
        AutoScalePane scalePane = new AutoScalePane();
        VBox stackBox = new VBox();
        stackBox.setSpacing(PADDING);
        stackBox.setPadding(new Insets(2));
        String borderStyle = "-fx-border-color: transparent black black black;"
                + "-fx-border-width: 2;";
        stackBox.setStyle(borderStyle);

        stackBox.getStyleClass().add("stack-box");

        moduleGroup.getElements().forEach(e -> {
            StackElement element = (StackElement) e;
            ADVStyle style = element.getStyle();

            LabeledNode node = new LabeledNode(element.getContent());
            Color fillColor = StyleConverter.getColorFromHexValue(
                    style.getFillColor());

            node.setBackgroundColor(fillColor);
            node.setFontColor(StyleConverter.getLabelColor(fillColor));
            node.setBorder(style.getStrokeThickness(),
                    StyleConverter.getColorFromHexValue(style.getStrokeColor()),
                    StyleConverter.getStrokeStyle(style.getStrokeStyle()));

            stackBox.getChildren().add(node);
        });

        scalePane.addChildren(stackBox);
        return scalePane;
    }

}
