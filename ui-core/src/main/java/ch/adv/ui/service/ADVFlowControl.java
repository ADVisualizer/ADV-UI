package ch.adv.ui.service;

import ch.adv.ui.app.ADVModule;
import ch.adv.ui.logic.ModuleStore;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.domain.Session;
import ch.adv.ui.domain.Snapshot;
import ch.adv.ui.presentation.Layouter;
import ch.adv.ui.presentation.SnapshotStore;
import ch.adv.ui.presentation.domain.SnapshotWrapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
        logger.info("Processing JSON...");
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

        // Layout only snapshots that have not yet been layouted
        newSnapshots.forEach(snapshot -> {

            // layout
            SnapshotWrapper wrapper = layouter.layout(snapshot);

            // store pane
            snapshotStore.addWrapper(sessionId, wrapper);
        });

        if (!newSnapshots.isEmpty()) {
            sessionStore.addSession(session);
        } else {
            sessionStore.setCurrentSession(sessionId);
        }
        logger.info("Process finished: delegated session and snapshot "
                + "creation.");
    }

}
