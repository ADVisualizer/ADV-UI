package ch.adv.ui.array;

import ch.adv.ui.logic.model.Snapshot;
import ch.adv.ui.presentation.Layouter;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Positions the ArrayElements on the Pane
 */
public class ArrayLayouter implements Layouter {

    @Override
    public Pane layout(Snapshot snapshot) {
        Pane pane = new Pane();
        //TODO: actually do some work here ;)
        pane.getChildren().add(new Label(snapshot.getSnapshotDescription()));
        return pane;
    }
}
