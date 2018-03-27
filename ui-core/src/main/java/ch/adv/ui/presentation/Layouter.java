package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Snapshot;
import javafx.scene.layout.Pane;

/**
 * Builds JavaFX Elements from business objects contained in a snapshot.
 * Places and orients these elements on a JavaFX Pane.
 */
public interface Layouter {
    /**
     * Layouts the snapshot. Builds necessary JavaFX Elements and places them
     * on a pane, which will be displayed in the session view.
     *
     * @param snapshot to be layouted
     * @return a JavaFX Pane containing the snapshot elements
     */
    Pane layout(final Snapshot snapshot);
}
