package ch.adv.ui.array;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import ch.adv.ui.core.presentation.util.StyleConverter;
import ch.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Singleton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Positions the ArrayElements on the Pane
 */
@Singleton
public class ArrayLayouter implements Layouter {

    /**
     * Layouts an Array snapshot if it is not already layouted
     *
     * @param snapshot to be layouted
     * @return layouted snapshot
     */
    @Override
    public LayoutedSnapshot layout(Snapshot snapshot) {
        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        vBox.alignmentProperty().set(Pos.CENTER);
        HBox hbox = new HBox();
        hbox.alignmentProperty().set(Pos.CENTER);

        snapshot.getElements().forEach(e -> {
            ArrayElement arrElement = (ArrayElement) e;

            LabeledNode node = new LabeledNode(arrElement
                    .getContent());
            ADVStyle style = arrElement.getStyle();

            node.setBackgroundColor(StyleConverter
                    .getColor(style.getFillColor()));
            node.setFontColor(Color.WHITE);
            node.setBorder(style.getStrokeThickness(),
                    StyleConverter.getColor(style.getStrokeColor()),
                    StyleConverter.getStrokeStyle(style.getStrokeStyle()));
            hbox.getChildren().add(node);
        });

        vBox.getChildren().add(hbox);

        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot();
        layoutedSnapshot
                .setSnapshotDescription(snapshot.getSnapshotDescription());
        layoutedSnapshot.setSnapshotId(snapshot.getSnapshotId());
        layoutedSnapshot.setPane(vBox);

        return layoutedSnapshot;
    }
}
