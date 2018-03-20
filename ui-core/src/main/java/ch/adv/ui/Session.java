package ch.adv.ui;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private String module;
    private Long sessionId;
    private String sessionName;

    private final List<Snapshot> snapshots;

    public Session() {
        this.snapshots = new ArrayList<>();
    }

    public Long getSessionId() {
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
}
