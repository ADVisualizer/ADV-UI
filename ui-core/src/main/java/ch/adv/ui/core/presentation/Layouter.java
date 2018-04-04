package ch.adv.ui.core.presentation;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.presentation.domain.SnapshotWrapper;

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
     * @return a wrapper containing a layouted JavaFX Pane and the snapshot
     */
    SnapshotWrapper layout(Snapshot snapshot);
}
