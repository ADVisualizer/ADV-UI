package ch.hsr.adv.ui.array.presentation;

import ch.hsr.adv.commons.array.logic.domain.ArrayElement;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultElementStyle;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.IndexPosition;
import ch.hsr.adv.ui.core.presentation.widgets.IndexedNode;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Creates an JavaFX array representation in the look of a primitive array.
 */
@Singleton
public class ArrayDefaultLayouter {

    private AutoScalePane scalePane;
    private HBox valueContainer;

    /**
     * Layouts an array to look like a primitive array.
     *
     * @param moduleGroup moduleGroup to layout
     * @param showIndices flag to show indices above array
     * @return layouted pane
     */
    public Pane layout(ModuleGroup moduleGroup, boolean showIndices) {
        initializeContainer();
        drawElements(moduleGroup, showIndices);

        scalePane.addChildren(valueContainer);

        return scalePane;
    }

    private void initializeContainer() {
        scalePane = new AutoScalePane();
        valueContainer = new HBox();
        valueContainer.setAlignment(Pos.CENTER);
    }

    private void drawElements(ModuleGroup moduleGroup, boolean showIndices) {
        for (int i = 0; i < moduleGroup.getElements().size(); i++) {
            ArrayElement element =
                    (ArrayElement) moduleGroup.getElements().get(i);

            ADVStyle style = element.getStyle();
            if (style == null) {
                style = new ADVDefaultElementStyle();
            }

            IndexedNode valueNode = new IndexedNode(i, element.getContent(),
                    style, false, showIndices,
                    IndexPosition.TOP);

            valueContainer.getChildren().add(valueNode);
        }
    }
}
