package ch.hsr.adv.ui.stack.logic;

import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.Snapshot;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import ch.hsr.adv.ui.stack.logic.domain.StackElement;
import com.google.inject.Singleton;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
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

    /**
     * Layouts n Stack snapshot if it is not already layouted
     *
     * @param snapshot to be layouted
     * @return layouted snapshot
     */
    @Override
    public LayoutedSnapshot layout(Snapshot snapshot, List<String> flags) {
        Pane pane = drawElements(snapshot);

        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot(
                snapshot.getSnapshotId(), pane);
        layoutedSnapshot.setSnapshotDescription(
                snapshot.getSnapshotDescription());

        return layoutedSnapshot;
    }

    private Pane drawElements(Snapshot snapshot) {
        AutoScalePane scalePane = new AutoScalePane();
        VBox stackBox = new VBox();
        stackBox.setSpacing(5);
        stackBox.setPadding(new Insets(2));
        String borderStyle = "-fx-border-color: transparent black black black;"
                + "-fx-border-width: 2;";
        stackBox.setStyle(borderStyle);

        stackBox.getStyleClass().add("stack-box");

        snapshot.getElements().forEach(e -> {
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