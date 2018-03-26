package ch.adv.ui.logic.model;

import ch.adv.ui.ADVModule;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Session {

    private final List<Snapshot> snapshots = new ArrayList<>();
    private long sessionId;
    private String sessionName;
    private String moduleName;
    private transient ADVModule module;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(final Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(final String sessionName) {
        this.sessionName = sessionName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public ADVModule getModule() {
        return module;
    }

    public void setModule(final ADVModule module) {
        this.module = module;
        if (module != null) {
            this.moduleName = module.getName();
        } else {
            moduleName = "";
        }
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    @Override
    public String toString() {
        String time = String.format("%tT", sessionId - TimeZone.getDefault()
                .getRawOffset());
        return sessionName + " - " + time;
    }
}
