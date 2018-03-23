package ch.adv.ui.logic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Session {

    private String module;
    private long sessionId;
    private String sessionName;

    private final List<Snapshot> snapshots;

    public Session() {
        this.snapshots = new ArrayList<>();
    }

    public long getSessionId() {
        return sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getModule() {
        return module;
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override
    public String toString() {
        String time = String.format("%tT", sessionId - TimeZone.getDefault()
                .getRawOffset());
        return sessionName + " - " + time;
    }
}
