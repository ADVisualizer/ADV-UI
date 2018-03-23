package ch.adv.ui.logic;

import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.model.Snapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
@Singleton
public class SessionStore {

    private final Session currentSession;
    private final Snapshot currentSnapshot;
    private final Map<Long, Session> sessions;
    private final PropertyChangeSupport changeSupport;


    private static final Logger logger = LoggerFactory.getLogger(SessionStore
            .class);

    public SessionStore() {
        this.currentSession = null;
        this.currentSnapshot = null;
        this.sessions = new HashMap<>();
        this.changeSupport = new PropertyChangeSupport(this);
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

            logger.debug("Fire change event");

            changeSupport.firePropertyChange("session", existing, newSession);
        }
    }

    private void mergeSession(Session existing, Session newSession) {
        long existingSessionId = existing.getSessionId();

        existing.getSnapshots().addAll(newSession.getSnapshots());

        logger.info("Successfully merged new snapshots of session {} into " +
                "existing session", existingSessionId);
    }

    public List<Session> getSessions() {
        ArrayList<Session> list = new ArrayList<>(sessions.values());
        list.sort(
                (s1, s2) -> {
                    if (s1.getSessionId() == s2.getSessionId()) {
                        return 0;
                    }
                    if (s1.getSessionId() < s2.getSessionId()) {
                        return -1;
                    }
                    return 1;
                }
        );
        return list;
    }

    public List<Snapshot> getSnapshots() {
        if (currentSession != null) {
            return currentSession.getSnapshots();
        }
        logger.debug("No current session is set. Returning empty Snapshot " +
                "list.");
        return new ArrayList<>();

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void deleteSession(Session session) {
        if (session != null) {
            long id = session.getSessionId();
            Session existing = sessions.get(id);

            if (existing != null) {
                sessions.remove(session.getSessionId());
                logger.info("Session {} deleted from SessionStore", id);
            }
            logger.debug("Fire change event");
            changeSupport.firePropertyChange("session", existing, null);
        }
    }
}
