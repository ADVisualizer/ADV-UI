package ch.hsr.adv.ui.core.logic.domain;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.Objects;

/**
 * Wraps the visually important information and representation of a snapshot
 * <p>
 * The purpose of this class is not to interfuse business domain objects with
 * JavaFX dependencies.
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
