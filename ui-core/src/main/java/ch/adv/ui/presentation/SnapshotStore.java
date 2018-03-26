package ch.adv.ui.presentation;

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

@Singleton
public class SnapshotStore {
    private final Map<Long, List<Pane>> snapshotPaneMap;
    private final PropertyChangeSupport changeSupport;

    private static final Logger logger = LoggerFactory.getLogger
            (SnapshotStore.class);


    public SnapshotStore() {
        this.snapshotPaneMap = new HashMap<>();
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public void addSnapshotPane(long sessionId, Pane newPane) {
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

    public List<Pane> getSnapshotPanes(long sessionId) {
        return snapshotPaneMap.get(sessionId);
    }

    /**
     * Add change listener to be notified by changes to the session list.
     *
     * @param listener to be registered
     */
    public void addPropertyChangeListener(long sessionId,
                                          PropertyChangeListener
                                                  listener) {
        changeSupport.addPropertyChangeListener(sessionId + "", listener);
    }


}
