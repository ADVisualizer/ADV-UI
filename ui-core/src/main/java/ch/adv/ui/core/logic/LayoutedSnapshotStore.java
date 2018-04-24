package ch.adv.ui.core.logic;

import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import javafx.scene.layout.Pane;

import java.util.List;

public interface LayoutedSnapshotStore {

    List<LayoutedSnapshot> getAll(long sessionId);

    List<Pane> getAllPanes(long sessionId);

    void add(long sessionId, LayoutedSnapshot object);

    void deleteAll(long sessionId);
    
    boolean contains(long sessionId, long snapshotId);
}
