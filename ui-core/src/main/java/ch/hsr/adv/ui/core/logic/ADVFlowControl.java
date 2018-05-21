package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.Snapshot;
import ch.hsr.adv.ui.core.access.DatastoreAccess;
import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.events.ADVEvent;
import ch.hsr.adv.ui.core.logic.events.EventManager;
import ch.hsr.adv.ui.core.logic.stores.LayoutedSnapshotStore;
import ch.hsr.adv.ui.core.logic.stores.SessionStore;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import ch.hsr.adv.ui.core.presentation.util.I18n;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Framework component which controls the default visualization flow.
 */
@Singleton
public class ADVFlowControl implements FlowControl {

    private static final Logger logger = LoggerFactory.getLogger(
            ADVFlowControl.class);

    private final SessionStore sessionStore;
    private final ServiceProvider serviceProvider;
    private final LayoutedSnapshotStore layoutedSnapshotStore;
    private final EventManager eventManager;
    private final CoreParser coreParser;
    private final CoreLayouter coreLayouter;
    private final CoreStringifyer coreStringifyer;
    private final DatastoreAccess fileAccess;


    @Inject
    public ADVFlowControl(SessionStore sessionStore,
                          ServiceProvider serviceProvider,
                          LayoutedSnapshotStore layoutedSnapshotStore,
                          EventManager eventManager,
                          CoreParser coreParser,
                          CoreLayouter coreLayouter,
                          CoreStringifyer coreStringifyer,
                          DatastoreAccess fileAccess) {

        this.sessionStore = sessionStore;
        this.serviceProvider = serviceProvider;
        this.layoutedSnapshotStore = layoutedSnapshotStore;
        this.eventManager = eventManager;
        this.coreParser = coreParser;
        this.coreLayouter = coreLayouter;
        this.coreStringifyer = coreStringifyer;
        this.fileAccess = fileAccess;

    }

    /**
     * Parses and stores incoming json either from the socket or the file
     * system. Kicks off the layouting process.
     *
     * @param sessionJSON json
     */
    public void load(String sessionJSON) {
        try {
            logger.info("Processing JSON...");

            Session session = parseSession(sessionJSON);

            boolean existing = layoutSession(session);

            if (existing) {
                changeCurrentSession(session);
            } else {
                updateSessionStore(session);
            }

            logger.info("JSON successfully processed.");

        } catch (ADVParseException e) {
            eventManager.fire(ADVEvent.NOTIFICATION, null,
                    I18n.NOTIFICATION_SESSION_LOAD_UNSUCCESSFUL);
        }
    }

    private Session parseSession(String sessionJSON) throws ADVParseException {
        return coreParser.parse(sessionJSON);
    }

    /**
     * @param session containing snapshots to be layouted
     * @return true, if the input session contains any snapshots, that have
     * not yet been layouted. Return false, if and only if the input session
     * is a duplicated session, i.e. contains no new snapshots
     */
    private boolean layoutSession(Session session) {
        // filter new snapshots
        long sessionId = session.getSessionId();
        List<Snapshot> newSnapshots = session.getSnapshots().stream()
                .filter(s -> !layoutedSnapshotStore.contains(sessionId,
                        s.getSnapshotId()))
                .collect(Collectors.toList());

        // Layout only snapshots that have not yet been layouted
        newSnapshots.forEach(snapshot -> {

            // layout
            List<Pane> panes = new ArrayList<>();
            snapshot.getModuleGroups().forEach(group -> {
                String moduleName = group.getModuleName();
                Layouter layouter = serviceProvider.getLayouter(moduleName);
                Pane pane = layouter.layout(group, group.getFlags());
                panes.add(pane);
            });

            // wrap in split pane
            List<SplitPane.Divider> dividers = new ArrayList<>();
            Region parent = coreLayouter.layout(panes, dividers);
            LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot(
                    snapshot.getSnapshotId(), parent, dividers);
            layoutedSnapshot
                    .setSnapshotDescription(snapshot.getSnapshotDescription());

            // store layouted snapshot
            layoutedSnapshotStore.add(sessionId, layoutedSnapshot);
        });

        return newSnapshots.isEmpty();
    }

    private void updateSessionStore(Session session) {
        sessionStore.add(session);
        eventManager.fire(ADVEvent.NOTIFICATION, null,
                I18n.NOTIFICATION_SESSION_LOAD_SUCCESSFUL);
    }

    private void changeCurrentSession(Session session) {
        sessionStore.setCurrent(session.getSessionId());
        eventManager.fire(ADVEvent.NOTIFICATION, null,
                I18n.NOTIFICATION_SESSION_LOAD_EXISTING);
    }

    /**
     * Delegates saving sessions to the access layer
     *
     * @param session the loaded session
     * @param file    the file to save the session
     */
    public void save(Session session, File file) {
        try {
            storeSnapshotDescription(session);

            String json = coreStringifyer.stringify(session);
            fileAccess.write(file, json);
            eventManager.fire(ADVEvent.NOTIFICATION, null,
                    I18n.NOTIFICATION_SESSION_SAVE_SUCCESSFUL);

        } catch (IOException e) {
            eventManager.fire(ADVEvent.NOTIFICATION, null,
                    I18n.NOTIFICATION_SESSION_SAVE_UNSUCCESSFUL);
        }
    }

    private void storeSnapshotDescription(Session session) {
        layoutedSnapshotStore.getAll(session.getSessionId())
                .forEach(layoutedSnapshot -> {
                    String description = layoutedSnapshot
                            .getSnapshotDescription();
                    long id = layoutedSnapshot.getSnapshotId();
                    session.getSnapshotById(id)
                            .setSnapshotDescription(description);
                });
    }

}
