package ch.hsr.adv.ui.queue.logic;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultElementStyle;
import ch.hsr.adv.commons.queue.logic.ConstantsQueue;
import ch.hsr.adv.commons.queue.logic.domain.QueueElement;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Singleton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

import java.util.List;

/**
 * Positions the StackElements on the Pane
 */
@Singleton
@Module(ConstantsQueue.MODULE_NAME)
public class QueueLayouter implements Layouter {

    private static final int SPACING = 2;
    private static final int PADDING = 2;
    private static final int MIN_WIDTH = 25;
    private static final int MIN_HEIGHT = 35;
    private static final String BORDER_STYLE =
            "-fx-border-color: black transparent;"
                    + "-fx-border-width: 2;";

    /**
     * Layouts a queue snapshot if it is not already layouted
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

        HBox rootBox = new HBox();
        rootBox.setSpacing(SPACING * SPACING);
        rootBox.setPadding(new Insets(PADDING));
        rootBox.setAlignment(Pos.CENTER);

        HBox queueBox = new HBox();
        queueBox.setSpacing(SPACING);
        queueBox.setMinHeight(MIN_HEIGHT);
        queueBox.setMinWidth(MIN_WIDTH);
        queueBox.setStyle(BORDER_STYLE);
        queueBox.setPadding(new Insets(PADDING));


        moduleGroup.getElements().forEach(e -> {
            QueueElement element = (QueueElement) e;
            ADVStyle style = element.getStyle();
            if (style == null) {
                style = new ADVDefaultElementStyle();
            }

            LabeledNode node = new LabeledNode(element.getContent(), style);

            queueBox.getChildren().add(node);
        });

        // create head flag
        Node head = createFlag();

        rootBox.getChildren().addAll(head, queueBox);
        parent.addChildren(rootBox);
        return parent;
    }

    private Node createFlag() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                7.0, 0.0,
                0.0, 13.0,
                7.0, 25.0);
        polygon.setFill(StyleConverter.getColorFromHexValue(0xced8e0));
        return polygon;
    }

}
