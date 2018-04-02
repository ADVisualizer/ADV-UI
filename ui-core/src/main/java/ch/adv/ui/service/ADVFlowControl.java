package ch.adv.ui.service;

import ch.adv.ui.logic.ADVModule;
import ch.adv.ui.logic.ModuleStore;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.model.Snapshot;
import ch.adv.ui.presentation.Layouter;
import ch.adv.ui.presentation.SnapshotStore;
import ch.adv.ui.presentation.model.SnapshotWrapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Framework component which controls the default visualization flow.
 */
@Singleton
public class ADVFlowControl {

    private static final Logger logger = LoggerFactory.getLogger(
            ADVFlowControl.class);

    private final ModuleStore moduleStore;
    private final SessionStore sessionStore;
    private final SnapshotStore snapshotStore;

    @Inject
    public ADVFlowControl(final ModuleStore moduleStore, SessionStore
            sessionStore, final SnapshotStore snapshotStore) {
        this.moduleStore = moduleStore;
        this.sessionStore = sessionStore;
        this.snapshotStore = snapshotStore;
    }

    /**
     * Parses and stores incoming json either from the socket or the file
     * system. Kicks off the layouting process.
     *
     * @param sessionJSON json
     */
    public void process(String sessionJSON) {
        // parse module
        ADVModule currentModule = moduleStore.parseModule(sessionJSON);

        // parse session
        Session session = currentModule.getParser().parse(sessionJSON);
        session.setModule(currentModule);
        long sessionId = session.getSessionId();

        Layouter layouter = currentModule.getLayouter();

        // filter new snapshots
        List<Snapshot> newSnapshots = session.getSnapshots().stream()
                .filter(s -> !snapshotStore.hasSnapshot(sessionId, s))
                .collect(Collectors.toList());

        newSnapshots.forEach(snapshot -> {
            SnapshotWrapper wrapper = new SnapshotWrapper();
            wrapper.setSnapshot(snapshot);

            // layout
            Pane newSnapshotPane = layouter.layout(snapshot);
            wrapper.setPane(newSnapshotPane);
            wrapper.setLayouted(true);

            // store pane
            snapshotStore.addWrapper(sessionId, wrapper);
        });

        if (!newSnapshots.isEmpty()) {
            sessionStore.addSession(session);
        } else {
            sessionStore.setCurrentSession(sessionId);
        }
    }

}
