package ch.adv.ui.core.logic;

import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.adv.ui.core.logic.domain.Session;
import ch.adv.ui.core.logic.domain.Snapshot;
import ch.adv.ui.core.logic.util.ADVParseException;
import ch.adv.ui.core.presentation.util.I18n;
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

    private final ModuleParser moduleParser;
    private final SessionStore sessionStore;
    private final ServiceProvider serviceProvider;
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private final EventManager eventManager;

    @Inject
    public ADVFlowControl(ModuleParser moduleParser,
                          SessionStore sessionStore,
                          ServiceProvider serviceProvider,
                          LayoutedSnapshotStore layoutedSnapshotStore,
                          EventManager eventManager) {
        this.moduleParser = moduleParser;
        this.sessionStore = sessionStore;
        this.serviceProvider = serviceProvider;
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
        try {
            logger.info("Processing JSON...");
            // parse module
            String currentModule = moduleParser.parseModule(sessionJSON);

            // parse session
            Parser parser = serviceProvider.getParser(currentModule);
            Session session = parser.parse(sessionJSON);
            long sessionId = session.getSessionId();

            Layouter layouter = serviceProvider.getLayouter(currentModule);

            // filter new snapshots
            List<Snapshot> newSnapshots = session.getSnapshots().stream()
                    .filter(s -> !layoutedSnapshotStore.contains(sessionId,
                            s.getSnapshotId()))
                    .collect(Collectors.toList());

            // Layout only snapshots that have not yet been layouted
            newSnapshots.forEach(snapshot -> {

                // layout
                LayoutedSnapshot layoutedSnapshot = layouter.layout(snapshot,
                        session.getFlags());

                // store layouted snapshot
                layoutedSnapshotStore.add(sessionId, layoutedSnapshot);
            });

            if (!newSnapshots.isEmpty()) {
                sessionStore.add(session);
                eventManager.fire(ADVEvent.NOTIFICATION, null,
                        I18n.NOTIFICATION_SESSION_LOAD_SUCCESSFUL);
            } else {
                sessionStore.setCurrent(sessionId);
                eventManager.fire(ADVEvent.NOTIFICATION, null,
                        I18n.NOTIFICATION_SESSION_LOAD_EXISTING);
            }
            logger.info("Process finished: delegated session and snapshot "
                    + "creation.");
        } catch (ADVParseException e) {
            eventManager.fire(ADVEvent.NOTIFICATION, null,
                    I18n.NOTIFICATION_SESSION_LOAD_UNSUCCESSFUL);
        }
    }

}
