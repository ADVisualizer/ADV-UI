package ch.adv.ui.logic.model;

import ch.adv.ui.ADVModule;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Session {

    private long sessionId;
    private String sessionName;
    private String moduleName;
    private transient ADVModule module;
    private final List<Snapshot> snapshots = new ArrayList<>();


    public long getSessionId() {
        return sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public ADVModule getModule() {
        return module;
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setModule(ADVModule module) {
        this.module = module;
        if (module!= null) {
            this.moduleName = module.getName();
        } else {
            moduleName = "";
        }
    }

    @Override
    public String toString() {
        String time = String.format("%tT", sessionId - TimeZone.getDefault()
                .getRawOffset());
        return sessionName + " - " + time;
    }
}
