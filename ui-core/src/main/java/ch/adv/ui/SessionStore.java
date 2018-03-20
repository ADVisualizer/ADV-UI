package ch.adv.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class SessionStore {

    private final Session currentSession;
    private final Snapshot currentSnapshot;
    private final Map<Long, Session> sessions;
    private final Map<Long, List<Snapshot>> snapshots;

    private static final Logger logger = LoggerFactory.getLogger(SessionStore.class);

    public SessionStore() {
        this.currentSession = null;
        this.currentSnapshot = null;
        this.sessions = new HashMap<>();
        this.snapshots = new HashMap<>();
    }

    public void addSession(Session newSession) {
        if (newSession != null) {
            long id = newSession.getSessionId();
            Session existing = sessions.get(id);

            if (existing != null) {
                mergeSession(existing, newSession);
            } else {
                sessions.put(id, newSession);
                logger.info("New session {} added to SessionStore", id);
            }
        }
    }

    private void mergeSession(Session existing, Session newSession) {
        long newSessionId = newSession.getSessionId();
        long existingSessionId = existing.getSessionId();
        List<Snapshot> existingSnapshots = snapshots.get(existingSessionId);
        existingSnapshots.addAll(newSession.getSnapshots());
        logger.info("Successfully session {} into session {}", newSessionId, existingSessionId);
    }

    public List<Session> getSessions() {
        return sessions.values().stream().collect(Collectors.toList());
    }

    public List<Snapshot> getSnapshots() {
        return snapshots.values().stream()
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());
    }

}
