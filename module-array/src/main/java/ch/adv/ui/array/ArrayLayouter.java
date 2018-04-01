package ch.adv.ui.array;

import ch.adv.ui.logic.model.Snapshot;
import ch.adv.ui.presentation.Layouter;
import ch.adv.ui.presentation.model.LabeledNode;
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
     * @param snapshot to be layouted
     * @return
     */
    @Override
    public Pane layout(Snapshot snapshot) {
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

        snapshot.setLayouted(true);
        return vBox;

    }
}
