package ch.hsr.adv.ui.core.logic.stores;

import ch.hsr.adv.commons.core.logic.domain.Session;
import ch.hsr.adv.commons.core.logic.domain.Snapshot;
import ch.hsr.adv.ui.core.logic.events.ADVEvent;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Holds all active sessions. Fires a change event, if a new session is added or
 * an existing session is removed.
 *
 * @author mtrentini, mwieland
 */
@Singleton
public class SessionStore {

    private static final Logger logger = LoggerFactory.getLogger(
            SessionStore.class);

    private final Map<Long, Session> sessions = new HashMap<>();
    private final EventManager eventManager;

    private Session currentSession;

    @Inject
    public SessionStore(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Adds specified session to the store and sets it as the current session.
     * If the session already exists:
     * either merges its snapshots with existing session (for sessions
     * received over the socket)
     * or
     * ignores it (for sessions loaded via the ui)
     *
     * @param newSession the session to add
     */
    public void add(Session newSession) {
        if (newSession != null) {

            long id = newSession.getSessionId();
            Session existing = sessions.get(id);

            if (existing == null) {
                sessions.put(id, newSession);
                setCurrent(newSession.getSessionId());
                eventManager.fire(ADVEvent.SESSION_ADDED, null, newSession);
            } else {
                mergeSession(existing, newSession);
                setCurrent(existing.getSessionId());
            }
        }
    }

    /**
     * Merges the new session into the existing session. The new session will
     * not be stored itself.
     *
     * @param existing   existing session (will be kept in the store)
     * @param newSession new session (will not itself be stored in the store)
     */
    private void mergeSession(Session existing, Session newSession) {
        logger.debug("Merge session {}", existing.getSessionId());

        Map<Long, Snapshot> existingSnapshots = existing.getSnapshots()
                .stream().collect(Collectors.toMap(Snapshot::getSnapshotId,
                        Function.identity()));

        newSession.getSnapshots().forEach(newSnapshot -> {
            Snapshot existingSnapshot = existingSnapshots.get(newSnapshot
                    .getSnapshotId());
            if (existingSnapshot == null) {
                existing.getSnapshots().add(newSnapshot);
                logger.debug("Add snapshot {} to session {}",
                        newSnapshot.getSnapshotId(), existing.getSessionId());
            }
        });

        logger.debug("Successfully merged new snapshots of session {} into "
                + "existing session", existing.getSessionId());
    }

    /**
     * @return sorted list of sessions
     */
    public List<Session> getAll() {
        ArrayList<Session> list = new ArrayList<>(sessions.values());
        list.sort((s1, s2) -> (int) (s2.getSessionId() - s1.getSessionId()));
        return list;
    }

    /**
     * Returns the session with the given id
     *
     * @param sessionId session id
     * @return existing session or null
     */
    public Session get(long sessionId) {
        return sessions.get(sessionId);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    /**
     * Sets the current session to the session with the specified id. Fires a
     * change event to inform listeners of the change.
     *
     * @param sessionId of the new current session
     */
    public void setCurrent(long sessionId) {
        logger.debug("New session {} added to SessionStore", sessionId);
        this.currentSession = sessions.get(sessionId);
        eventManager.fire(ADVEvent.CURRENT_SESSION_CHANGED, null,
                currentSession);
    }

    /**
     * Deletes the specified session and fires a changeEvent.
     *
     * @param id to be deleted
     */
    public void delete(long id) {
        Session existing = sessions.get(id);

        if (existing != null) {
            sessions.remove(id);
            currentSession = null;
            logger.info("Session {} deleted from SessionStore", id);
        }
        logger.debug("Fire change event");
        eventManager.fire(ADVEvent.SESSION_REMOVED, existing, null, id + "");
    }

    /**
     * Clears all sessions
     */
    public void clear() {
        this.sessions.clear();
    }

    /**
     * Checks whether the session store contains the given id
     *
     * @param sessionId session id
     * @return true if the session exists in the store
     */
    public boolean contains(long sessionId) {
        return this.sessions.containsKey(sessionId);
    }
}
