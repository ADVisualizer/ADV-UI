package ch.adv.ui.core.presentation;

import ch.adv.ui.core.logic.ADVEvent;
import ch.adv.ui.core.logic.EventManager;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
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
public class LayoutedSnapshotStore {
    private static final Logger logger = LoggerFactory.getLogger(
            LayoutedSnapshotStore.class);
    private final Map<Long, List<LayoutedSnapshot>> snapshotMap = new
            HashMap<>();

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
    public void addLayoutedSnapshot(long sessionId, LayoutedSnapshot
            layoutedSnapshot) {
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
    public List<LayoutedSnapshot> getLayoutedSnapshots(long sessionId) {
        return snapshotMap.get(sessionId);
    }

    /**
     * @param sessionId  of the snapshot
     * @param snapshotId to check
     * @return true if the specified snapshot is already stored
     */
    public boolean hasSnapshot(long sessionId, long snapshotId) {
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
    public List<Pane> getSnapshotPanes(long sessionId) {
        return snapshotMap.get(sessionId).stream()
                .map(layoutedSnapshot -> layoutedSnapshot
                        .getPane()).collect(Collectors.toList());
    }

    /**
     * Delete all snapshots belongign to the specified sessionId
     *
     * @param sessionId of the deleted session
     */
    public void deleteSession(long sessionId) {
        snapshotMap.remove(sessionId);
    }
}
