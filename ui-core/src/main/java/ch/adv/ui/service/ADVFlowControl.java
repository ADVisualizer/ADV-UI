package ch.adv.ui.service;

import ch.adv.ui.logic.ADVModule;
import ch.adv.ui.logic.ModuleStore;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.logic.model.Session;
import ch.adv.ui.presentation.Layouter;
import ch.adv.ui.presentation.SnapshotStore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Parses and stores incoming json. Kicks off the layouting process.
     *
     * @param sessionJSON json
     */
    public void process(String sessionJSON) {
        // parse module
        ADVModule currentModule = moduleStore.parseModule(sessionJSON);

        // parse session
        Session session = currentModule.getParser().parse(sessionJSON);
        session.setModule(currentModule);
        sessionStore.addSession(session);

        session.getSnapshots().forEach(snapshot -> {

            // layout
            Layouter layouter = currentModule.getLayouter();
            Pane newSnapshotPane = layouter.layout(snapshot);

            // store pane
            long sessionId = session.getSessionId();
            snapshotStore.addSnapshot(sessionId, snapshot);
            snapshotStore.addSnapshotPane(sessionId, newSnapshotPane);
        });
    }

}
