package ch.adv.ui;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
}
