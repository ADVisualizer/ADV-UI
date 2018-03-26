package ch.adv.ui.service;

import ch.adv.ui.ADVModule;
import ch.adv.ui.logic.ModuleStore;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.logic.model.Session;
import ch.adv.ui.presentation.Layouter;
import ch.adv.ui.presentation.SnapshotStore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Framework component which controls the default visualization flow.
 */
@Singleton
public class ADVFlowControl {

    private final ModuleStore moduleStore;
    private final SessionStore sessionStore;
    private final SnapshotStore snapshotStore;

    private static final Logger logger = LoggerFactory.getLogger
            (ADVFlowControl.class);

    @Inject
    public ADVFlowControl(ModuleStore moduleStore, SessionStore sessionStore,
                          SnapshotStore snapshotStore) {
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
        ADVModule currentModule = moduleStore.parseModule(sessionJSON);

        Session session = currentModule.getParser().parse(sessionJSON);
        session.setModule(currentModule);

        sessionStore.addSession(session);

        long sessionId = session.getSessionId();
        Layouter layouter = currentModule.getLayouter();

        snapshotStore.addSnapshotPane(sessionId, layouter.layout(session
                .getSnapshots().get(0)));
    }

}
