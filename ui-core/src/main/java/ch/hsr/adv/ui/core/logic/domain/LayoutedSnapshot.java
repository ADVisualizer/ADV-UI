package ch.hsr.adv.ui.core.logic.domain;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

import java.util.List;
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

    private final long snapshotId;
    private final Region pane;
    private final List<SplitPane.Divider> dividers;

    private String snapshotDescription;

    public LayoutedSnapshot(long snapshotId, Region pane, List<SplitPane
            .Divider> dividers) {
        this.snapshotId = snapshotId;
        this.pane = pane;
        this.dividers = dividers;
    }

    public long getSnapshotId() {
        return snapshotId;
    }

    public String getSnapshotDescription() {
        return snapshotDescription;
    }

    public void setSnapshotDescription(String snapshotDescription) {
        this.snapshotDescription = snapshotDescription;
    }

    public Region getPane() {
        return pane;
    }

    public List<SplitPane.Divider> getDividers() {
        return dividers;
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
        return snapshotId == that.snapshotId
                && Objects
                .equals(snapshotDescription, that.snapshotDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshotId, snapshotDescription);
    }
}
