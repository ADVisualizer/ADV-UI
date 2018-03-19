package ch.adv.ui;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SessionStore {

    private final Session currentSession;
    private final Snapshot currentSnapshot;
    private final Map<String, List<Session>> sessions;
    private final Map<String, List<Snapshot>> snapshots;

    public SessionStore() {
        this.currentSession = null;
        this.currentSnapshot = null;
        this.sessions = new HashMap<>();
        this.snapshots = new HashMap<>();
    }

    public void addSession(Session newSession) {


    }

    public void mergeSession(Session existing, Session newSession) {

    }

}
