package ch.adv.ui.core.presentation.domain;

import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Wraps the JavaFX Pane and the displayed information of the
 * snapshot.
 * <p>
 * This class only exists, so we don't mix JavaFX Code with business
 * Domain objects, in order to keep the ability to easily replace the UI
 * framework.
 */
public class LayoutedSnapshot {

    private long snapshotId;
    private String snapshotDescription;
    private Pane pane;

    public long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getSnapshotDescription() {
        return snapshotDescription;
    }

    public void setSnapshotDescription(String snapshotDescription) {
        this.snapshotDescription = snapshotDescription;
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
        LayoutedSnapshot that = (LayoutedSnapshot) o;
        return Objects.equals(snapshotDescription, that.snapshotDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshotDescription);
    }
}
