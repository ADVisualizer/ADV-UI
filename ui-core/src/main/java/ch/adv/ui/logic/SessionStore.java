package ch.adv.ui.logic;

import ch.adv.ui.logic.model.Session;
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
 * Holds all active sessions. Fires change event, if new session is added or
 * a session is removed.
 */
@Singleton
public class SessionStore {

    private static final String SESSION_EVENT = "session";
    private static final Logger logger = LoggerFactory.getLogger(SessionStore
            .class);
    private final Map<Long, Session> sessions;
    private final PropertyChangeSupport changeSupport;
    private Session currentSession;

    public SessionStore() {
        this.sessions = new HashMap<>();
        this.changeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Add specified session to the store and set it as current session.
     *
     * @param newSession the session to add
     */
    public void addSession(Session newSession) {
        addSession(newSession, false);
    }

    /**
     * Add specified session to the store and set it as current session. If the
     * session already exists:
     * either merge its snapshots with existing session (for sessions
     * received over the socket)
     * or
     * ignore it (for sessions loaded via the ui)
     *
     * @param newSession      the session to add
     * @param isLoadedSession true if the session is loaded through the ui
     */
    public void addSession(Session newSession, boolean isLoadedSession) {
        if (newSession != null) {
            long id = newSession.getSessionId();
            Session existing = sessions.get(id);
            if (existing == null) {
                sessions.put(id, newSession);
                logger.info("New session {} added to SessionStore", id);
                currentSession = newSession;

            } else if (isLoadedSession) {
                currentSession = existing;
                logger.debug("Loaded session {} already exists", id);
                newSession = null;
            } else {
                mergeSession(existing, newSession);
                currentSession = newSession;
            }

            logger.debug("Fire change event");

            changeSupport.firePropertyChange(SESSION_EVENT, existing,
                    newSession);
        }
    }

    private void mergeSession(Session existing, Session newSession) {
        long existingSessionId = existing.getSessionId();

        existing.getSnapshots().addAll(newSession.getSnapshots());

        logger.info("Successfully merged new snapshots of session {} into "
                + "existing session", existingSessionId);
    }

    /**
     * @return sorted list of sessions
     */
    public List<Session> getSessions() {
        ArrayList<Session> list = new ArrayList<>(sessions.values());
        list.sort((s1, s2) -> (int) (s2.getSessionId() - s1.getSessionId()));
        return list;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    /**
     * Add change listener to be notified by changes to the session list.
     *
     * @param listener to be registered
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Delete the specified session and fire changeEvent.
     *
     * @param session to be deleted
     */
    public void deleteSession(Session session) {
        if (session != null) {
            long id = session.getSessionId();
            Session existing = sessions.get(id);

            if (existing != null) {
                sessions.remove(session.getSessionId());
                currentSession = null;
                logger.info("Session {} deleted from SessionStore", id);
            }
            logger.debug("Fire change event");
            changeSupport.firePropertyChange(SESSION_EVENT, existing,
                    null);
        }
    }
}
