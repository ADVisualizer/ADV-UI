package ch.adv.ui.stack.logic;

import ch.adv.ui.core.logic.Layouter;
import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.adv.ui.core.logic.domain.Module;
import ch.adv.ui.core.logic.domain.Snapshot;
import ch.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.util.StyleConverter;
import ch.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.adv.ui.core.presentation.widgets.LabeledNode;
import ch.adv.ui.stack.logic.domain.StackElement;
import com.google.inject.Singleton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Positions the StackElements on the Pane
 */
@Singleton
@Module("stack")
public class StackLayouter implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            StackLayouter.class);

    private final AutoScalePane scalePane = new AutoScalePane();
    private final VBox stackBox = new VBox();

    /**
     * Layouts an Stack snapshot if it is not already layouted
     *
     * @param snapshot to be layouted
     * @return layouted snapshot
     */
    @Override
    public LayoutedSnapshot layout(Snapshot snapshot, List<String> flags) {
        drawElements(snapshot);

        scalePane.addChildren(stackBox);

        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot(
                snapshot.getSnapshotId(),
                scalePane);
        layoutedSnapshot.setSnapshotDescription(
                snapshot.getSnapshotDescription());
        return layoutedSnapshot;
    }

    private void drawElements(Snapshot snapshot) {

        snapshot.getElements().forEach(e -> {
            StackElement element = (StackElement) e;
            ADVStyle style = element.getStyle();

            LabeledNode node = new LabeledNode(element.getContent());
            Color fillColor = StyleConverter
                    .getColorFromHexValue(style.getFillColor());

            node.setBackgroundColor(fillColor);
            node.setFontColor(StyleConverter.getLabelColor(fillColor));
            node.setBorder(style.getStrokeThickness(),
                    StyleConverter.getColorFromHexValue(style.getStrokeColor()),
                    StyleConverter.getStrokeStyle(style.getStrokeStyle()));

            stackBox.getChildren().add(node);
        });
    }

}
