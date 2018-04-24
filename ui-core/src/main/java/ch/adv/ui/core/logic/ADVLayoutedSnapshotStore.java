package ch.adv.ui.core.logic;

import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import javafx.scene.layout.Pane;
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
 * Holds all layouted snapshots on a Pane mapped to their sessionId. Fires
 * change event, if new snapshot arrives.
 *
 * @author mtrentini
 */
@Singleton
public class ADVLayoutedSnapshotStore implements LayoutedSnapshotStore {
    private static final Logger logger = LoggerFactory.getLogger(
            LayoutedSnapshotStore.class);
    private final Map<Long, List<LayoutedSnapshot>> snapshotMap =
            new HashMap<>();

    private final EventManager eventManager;

    @Inject
    public ADVLayoutedSnapshotStore(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Adds a new {@link LayoutedSnapshot} to the Snapshot store
     *
     * @param sessionId        related session id
     * @param layoutedSnapshot the layouted snapshot to add
     */
    @Override
    public void add(long sessionId, LayoutedSnapshot layoutedSnapshot) {

        List<LayoutedSnapshot> layoutedSnapshotList =
                snapshotMap.get(sessionId);
        if (layoutedSnapshotList == null) {
            layoutedSnapshotList = new ArrayList<>();
            snapshotMap.put(sessionId, layoutedSnapshotList);
        }
        if (!layoutedSnapshotList.contains(layoutedSnapshot)) {
            layoutedSnapshotList.add(layoutedSnapshot);
        }
        logger.debug("Fire change event");
        eventManager.fire(ADVEvent.SNAPSHOT_ADDED, null, layoutedSnapshot,
                sessionId + "");
    }

    /**
     * Returns all Snapshots for the given session id
     *
     * @param sessionId session id
     * @return List of stored Snapshots
     */
    @Override
    public List<LayoutedSnapshot> getAll(long sessionId) {
        return snapshotMap.get(sessionId);
    }

    /**
     * @param sessionId  of the snapshot
     * @param snapshotId to check
     * @return true if the specified snapshot is already stored
     */
    @Override
    public boolean contains(long sessionId, long snapshotId) {
        if (snapshotMap.get(sessionId) == null) {
            return false;
        }
        return snapshotMap.get(sessionId).stream().anyMatch(
                layoutedSnapshot -> layoutedSnapshot
                        .getSnapshotId() == snapshotId);
    }

    /**
     * @param sessionId of the snapshots
     * @return a list of all the Panes of a session
     */
    @Override
    public List<Pane> getAllPanes(long sessionId) {
        return snapshotMap.get(sessionId).stream()
                .map(LayoutedSnapshot::getPane).collect(Collectors.toList());
    }

    /**
     * Delete all snapshots belonging to the specified sessionId
     *
     * @param sessionId of the deleted session
     */
    @Override
    public void deleteAll(long sessionId) {
        snapshotMap.remove(sessionId);
    }

}
