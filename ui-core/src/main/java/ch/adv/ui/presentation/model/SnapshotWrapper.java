package ch.adv.ui.presentation.model;

import ch.adv.ui.logic.model.Snapshot;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Wrappes a {@link Snapshot} and a corresponding Pane
 */
public class SnapshotWrapper {
    private Snapshot snapshot;
    private Pane pane;

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SnapshotWrapper that = (SnapshotWrapper) o;
        return Objects.equals(snapshot, that.snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshot);
    }
}
