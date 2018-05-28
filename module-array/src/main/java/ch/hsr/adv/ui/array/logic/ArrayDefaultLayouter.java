package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.commons.array.logic.domain.ArrayElement;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets
        .ADVDefaultElementStyle;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Default array layouter
 */
@Singleton
public class ArrayDefaultLayouter {

    private AutoScalePane scalePane;
    private HBox valueContainer;

    /**
     * Layouts a default array
     *
     * @param moduleGroup moduleGroup to layout
     * @return layouted pane
     */
    public Pane layout(ModuleGroup moduleGroup) {
        initializeContainer();
        drawElements(moduleGroup);

        scalePane.addChildren(valueContainer);

        return scalePane;
    }

    private void initializeContainer() {
        scalePane = new AutoScalePane();
        valueContainer = new HBox();
        valueContainer.setAlignment(Pos.CENTER);
    }

    private void drawElements(ModuleGroup moduleGroup) {

        moduleGroup.getElements().forEach(e -> {
            ArrayElement element = (ArrayElement) e;
            ADVStyle style = element.getStyle();
            if (style == null) {
                style = new ADVDefaultElementStyle();
            }

            LabeledNode valueNode = new LabeledNode(element
                    .getContent(), style);

            valueContainer.getChildren().add(valueNode);
        });
    }
}
