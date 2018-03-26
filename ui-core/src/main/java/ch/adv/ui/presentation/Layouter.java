package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Snapshot;
import javafx.scene.layout.Pane;

public interface Layouter {
    Pane layout(Snapshot snapshot);
}
