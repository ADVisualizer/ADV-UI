package ch.adv.ui.presentation;

import ch.adv.ui.logic.model.Snapshot;
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


/**
 * Holds all layouted snapshots on a Pane mapped to their sessionId. Fires
 * change event, if new snapshot arrives.
 */
@Singleton
public class SnapshotStore {
    private final Map<Long, List<Pane>> snapshotPaneMap;
    private final Map<Long, List<Snapshot>> snapshotMap;
    private final PropertyChangeSupport changeSupport;

    private static final Logger logger = LoggerFactory.getLogger(
            SnapshotStore.class);


    public SnapshotStore() {
        this.snapshotPaneMap = new HashMap<>();
        this.snapshotMap = new HashMap<>();
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public void addSnapshotPane(final long sessionId, Pane newPane) {
        List<Pane> snapshotPanes = snapshotPaneMap.get(sessionId);
        if (snapshotPanes == null) {
            snapshotPanes = new ArrayList<>();
            snapshotPaneMap.put(sessionId, snapshotPanes);
        }
        snapshotPanes.add(newPane);
        logger.debug("Fire change event");
        changeSupport.firePropertyChange(sessionId + "", null,
                newPane);
    }

    public List<Pane> getSnapshotPanes(final long sessionId) {
        return snapshotPaneMap.get(sessionId);
    }

    public void addSnapshot(long sessionId, final Snapshot snapshot) {
        List<Snapshot> snapshotList = snapshotMap.get(sessionId);
        if (snapshotList == null) {
            snapshotList = new ArrayList<>();
            snapshotMap.put(sessionId, snapshotList);
        }
        snapshotList.add(snapshot);
    }

    public List<Snapshot> getSnapshots(final long sessionId) {
        return snapshotMap.get(sessionId);
    }

    public Snapshot getNewestSnapshot(final long sessionId) {
        List<Snapshot> list = snapshotMap.get(sessionId);
        return list.get(list.size() - 1);
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
}
