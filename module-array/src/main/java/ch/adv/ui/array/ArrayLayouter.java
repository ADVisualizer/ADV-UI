package ch.adv.ui.array;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import ch.adv.ui.core.presentation.widgets.LabeledNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Positions the ArrayElements on the Pane
 */
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
            LabeledNode n = new LabeledNode(arrElement.getContent());
            //TODO: use arrElement.getStyle()
            n.setBackgroundColor(Color.BLACK);
            n.setFontColor(Color.WHITE);
            hbox.getChildren().add(n);
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
