package ch.hsr.adv.ui.stack.logic;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.stack.logic.ConstantsStack;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import ch.hsr.adv.ui.stack.logic.domain.StackElement;
import com.google.inject.Singleton;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Positions the StackElements on the Pane
 */
@Singleton
@Module(ConstantsStack.MODULE_NAME)
public class StackLayouter implements Layouter {

    private static final int SPACING = 2;
    private static final int PADDING = 2;
    private static final int MIN_WIDTH = 25;
    private static final int MIN_HEIGHT = 35;
    private static final String BORDER_STYLE = "-fx-border-color: transparent"
            + " black black black;"
            + "-fx-border-width: 2;";

    /**
     * Layouts a Stack snapshot if it is not already layouted
     *
     * @param moduleGroup to be layouted
     * @return layouted snapshot
     */
    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        return drawElements(moduleGroup);
    }

    private Pane drawElements(ModuleGroup moduleGroup) {
        AutoScalePane parent = new AutoScalePane();

        VBox stackBox = new VBox();
        stackBox.setSpacing(SPACING);
        stackBox.setMinHeight(MIN_HEIGHT);
        stackBox.setMinWidth(MIN_WIDTH);
        stackBox.setStyle(BORDER_STYLE);
        stackBox.setPadding(new Insets(PADDING));

        moduleGroup.getElements().forEach(e -> {
            StackElement element = (StackElement) e;
            ADVStyle style = element.getStyle();

            LabeledNode node = new LabeledNode(element.getContent(), style);

            stackBox.getChildren().add(node);
        });

        parent.addChildren(stackBox);
        return parent;
    }

}
