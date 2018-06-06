package ch.hsr.adv.ui.core.logic.stores;

import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.events.ADVEvent;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Holds layoutedSnapshots mapped to their sessionId. Fires a
 * change event, if a new snapshot is added.
 *
 * @author mtrentini
 */
@Singleton
public class LayoutedSnapshotStore {
    private static final Logger logger = LoggerFactory.getLogger(
            LayoutedSnapshotStore.class);
    private final Map<Long, List<LayoutedSnapshot>> snapshotMap =
            new HashMap<>();

    private final EventManager eventManager;

    @Inject
    public LayoutedSnapshotStore(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Adds a new {@link LayoutedSnapshot} to the Snapshot store
     *
     * @param sessionId        related session id
     * @param layoutedSnapshot the layouted snapshot to add
     */
    public void add(long sessionId, LayoutedSnapshot layoutedSnapshot) {

        List<LayoutedSnapshot> layoutedSnapshotList = snapshotMap
                .computeIfAbsent(sessionId, k -> new ArrayList<>());

        if (!layoutedSnapshotList.contains(layoutedSnapshot)) {
            layoutedSnapshotList.add(layoutedSnapshot);
        }
        logger.debug("Fire change event");
        eventManager.fire(ADVEvent.SNAPSHOT_ADDED, null, layoutedSnapshot,
                sessionId + "");
    }

    /**
     * Returns all snapshots for the given session id
     *
     * @param sessionId session id
     * @return List of stored Snapshots
     */
    public List<LayoutedSnapshot> getAll(long sessionId) {
        return snapshotMap.get(sessionId);
    }

    /**
     * Checks whether the specified snapshot is already present in the store.
     *
     * @param sessionId  of the snapshot
     * @param snapshotId to check
     * @return true if the specified snapshot is already stored
     */
    public boolean contains(long sessionId, long snapshotId) {
        if (snapshotMap.get(sessionId) == null) {
            return false;
        }
        return snapshotMap.get(sessionId).stream().anyMatch(
                layoutedSnapshot -> layoutedSnapshot
                        .getSnapshotId() == snapshotId);
    }

    /**
     * Filters all panes of a session from the stored layouted snapshots.
     *
     * @param sessionId of the snapshots
     * @return a list of all the panes of a session
     */
    public List<Region> getAllPanes(long sessionId) {
        return snapshotMap.get(sessionId).stream()
                .map(LayoutedSnapshot::getPane).collect(Collectors.toList());
    }

    /**
     * Deletes all snapshots belonging to the specified sessionId
     *
     * @param sessionId of the deleted session
     */
    public void deleteAll(long sessionId) {
        snapshotMap.remove(sessionId);
    }

}
