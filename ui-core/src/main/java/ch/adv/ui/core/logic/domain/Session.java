package ch.adv.ui.core.logic.domain;

import ch.adv.ui.core.logic.ADVModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Represents the session which holds multiple snapshots
 */
public class Session {

    private final List<Snapshot> snapshots = new ArrayList<>();
    private final List<String> flags = new ArrayList<>();
    private long sessionId;
    private String sessionName;
    private String moduleName;
    private transient ADVModule module;

    public long getSessionId() {
        return sessionId;
    }

    /**
     * The sessionId will always be set by the jsonBuilder according to the
     * value in the session-json. The setter should therefore not be used
     * manually.
     *
     * @param sessionId to be set.
     */
    void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ADVModule getModule() {
        return module;
    }

    /**
     * Sets the related {@link ADVModule} and stores its name, which needs to
     * be stringified to the filesystem.
     *
     * @param module related module
     */
    public void setModule(ADVModule module) {
        this.module = module;
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public List<String> getFlags() {
        return flags;
    }

    /**
     * Returns the first Snapshot (index 0) of all snapshots if it exists.
     * Otherwise returns a new Snapshot.
     *
     * @return the first Snapshot or a new Snapshot
     */
    public Snapshot getFirstSnapshot() {
        if (snapshots.isEmpty()) {
            return new Snapshot();
        }
        return snapshots.get(0);
    }

    @Override
    public String toString() {
        String time = String.format("%tT", sessionId - TimeZone.getDefault()
                .getRawOffset() + TimeZone.getDefault().getDSTSavings());

        return time + " - " + sessionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return sessionId == session.sessionId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(sessionId);
    }

    /**
     * @param id of the snapshot
     * @return the snapshot with the speified id or a new 'dummy' if no such
     * snapshot exists.
     */
    public Snapshot getSnapshotById(long id) {
        return snapshots.stream().filter(s -> s
                .getSnapshotId() == id).findFirst().orElse(new Snapshot());
    }
}
