package ch.hsr.adv.ui.core.logic.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of a data structure in the user's module
 * implementation. A snapshot always belongs to a session. It is sent to the
 * ADV UI to be displayed.
 */
public class Snapshot {

    private long snapshotId;
    private String snapshotDescription;
    private List<ModuleGroup> moduleGroups = new ArrayList<>();

    public long getSnapshotId() {
        return snapshotId;
    }

    /**
     * The snapshotId will always be set by the jsonBuilder according to the
     * value in the session-json. The setter should therefore not be used
     * manually.
     *
     * @param snapshotId to be set.
     */
    void setSnapshotId(long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getSnapshotDescription() {
        return snapshotDescription;
    }

    public void setSnapshotDescription(String snapshotDescription) {
        this.snapshotDescription = snapshotDescription;
    }

    public List<ModuleGroup> getModuleGroups() {
        return moduleGroups;
    }

    public void setModuleGroups(List<ModuleGroup> moduleGroups) {
        this.moduleGroups = moduleGroups;
    }
}
