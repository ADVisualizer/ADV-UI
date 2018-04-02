package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Snapshot;
import ch.adv.ui.presentation.model.SnapshotWrapper;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Holds all layouted snapshots on a Pane mapped to their sessionId. Fires
 * change event, if new snapshot arrives.
 */
@Singleton
public class SnapshotStore {
    private static final Logger logger = LoggerFactory.getLogger(
            SnapshotStore.class);
    private final Map<Long, List<SnapshotWrapper>> snapshotMap;
    private final PropertyChangeSupport changeSupport;


    public SnapshotStore() {
        this.snapshotMap = new HashMap<>();
        this.changeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Adds a new {@link SnapshotWrapper} to the Snapshot store
     *
     * @param sessionId related session id
     * @param wrapper   the snapshotwrapper to add
     */
    public void addWrapper(long sessionId, SnapshotWrapper wrapper) {
        List<SnapshotWrapper> snapshotList = snapshotMap.get(sessionId);
        if (snapshotList == null) {
            snapshotList = new ArrayList<>();
            snapshotMap.put(sessionId, snapshotList);
        }
        if (!snapshotList.contains(wrapper)) {
            snapshotList.add(wrapper);
        }
        logger.debug("Fire change event");
        changeSupport.firePropertyChange(sessionId + "", null,
                wrapper);
    }

    /**
     * Returns all Snapshots for the given session id
     *
     * @param sessionId session id
     * @return List of stored Snapshots
     */
    public List<SnapshotWrapper> getSnapshots(long sessionId) {
        return snapshotMap.get(sessionId);
    }

    /**
     * Add change listener to be notified by changes to the session list.
     *
     * @param sessionId key to be registered for
     * @param listener  to be registered
     */
    public void addPropertyChangeListener(long sessionId,
                                          final PropertyChangeListener
                                                  listener) {
        changeSupport.addPropertyChangeListener(sessionId + "", listener);
    }


    public boolean hasSnapshot(long sessionId, Snapshot snapshot) {
        if (snapshotMap.get(sessionId) == null){
            return false;
        }
        return snapshotMap.get(sessionId).stream().anyMatch(wrapper -> wrapper
                .getSnapshot()
                .equals(snapshot));
    }

    public List<Pane> getSnapshotPanes(long sessionId) {
        return snapshotMap.get(sessionId).stream().map(wrapper -> wrapper
                .getPane()).collect(Collectors.toList());
    }
}
