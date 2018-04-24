package ch.adv.ui.core.logic;

import ch.adv.ui.core.app.ADVEvent;
import ch.adv.ui.core.app.ADVModule;
import ch.adv.ui.core.logic.domain.Session;
import ch.adv.ui.core.logic.domain.Snapshot;
import ch.adv.ui.core.app.EventManager;
import ch.adv.ui.core.presentation.util.I18n;
import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
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
public class ADVFlowControl implements FlowControl {

    private static final Logger logger = LoggerFactory.getLogger(
            ADVFlowControl.class);

    private final ModuleStore moduleStore;
    private final SessionStore sessionStore;
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private final EventManager eventManager;

    @Inject
    public ADVFlowControl(ModuleStore moduleStore,
                          SessionStore sessionStore,
                          LayoutedSnapshotStore layoutedSnapshotStore,
                          EventManager eventManager) {

        this.moduleStore = moduleStore;
        this.sessionStore = sessionStore;
        this.layoutedSnapshotStore = layoutedSnapshotStore;
        this.eventManager = eventManager;
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
                .filter(s -> !layoutedSnapshotStore.hasSnapshot(sessionId,
                        s.getSnapshotId()))
                .collect(Collectors.toList());

        // Layout only snapshots that have not yet been layouted
        newSnapshots.forEach(snapshot -> {

            // layout
            LayoutedSnapshot layoutedSnapshot = layouter.layout(snapshot,
                    session.getFlags());

            // store layouted snapshot
            layoutedSnapshotStore
                    .addLayoutedSnapshot(sessionId, layoutedSnapshot);
        });

        if (!newSnapshots.isEmpty()) {
            sessionStore.addSession(session);
            eventManager.fire(ADVEvent.NOTIFICATION, null,
                    I18n.NOTIFICATION_SESSION_LOAD_SUCCESSFUL);
        } else {
            sessionStore.setCurrentSession(sessionId);
            eventManager.fire(ADVEvent.NOTIFICATION, null,
                    I18n.NOTIFICATION_SESSION_LOAD_EXISTING);
        }
        logger.info("Process finished: delegated session and snapshot "
                + "creation.");
    }

}
