package ch.adv.ui.presentation.model;

import ch.adv.ui.logic.model.Snapshot;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class SnapshotWrapper {
    private Snapshot snapshot;
    private Pane pane;
    private long sessionId;
    private boolean isLayouted;

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

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isLayouted() {
        return isLayouted;
    }

    public void setLayouted(boolean layouted) {
        isLayouted = layouted;
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
        return sessionId == that.sessionId &&
                Objects.equals(snapshot, that.snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshot, sessionId);
    }
}
