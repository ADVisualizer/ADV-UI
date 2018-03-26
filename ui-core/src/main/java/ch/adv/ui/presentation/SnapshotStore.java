package ch.adv.ui.presentation;

import javafx.scene.layout.Pane;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SnapshotStore {
    private final Map<Long, List<Pane>> snapshotPaneMap;

    public SnapshotStore() {
        this.snapshotPaneMap = new HashMap<>();
    }

    public void addSnapshotPane(long sessionId, Pane newPane) {
        List<Pane> snapshotPanes = snapshotPaneMap.get(sessionId);
        if (snapshotPanes == null) {
            snapshotPanes = new ArrayList<>();
            snapshotPaneMap.put(sessionId, snapshotPanes);
        }
        snapshotPanes.add(newPane);
    }

    public List<Pane> getSnapshotPanes(long sessionId) {
        return snapshotPaneMap.get(sessionId);
    }


}
