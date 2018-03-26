package ch.adv.ui.service;

import ch.adv.ui.ADVModule;
import ch.adv.ui.logic.ModuleStore;
import ch.adv.ui.logic.SessionStore;
import ch.adv.ui.logic.model.Session;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


@Singleton
public class ADVFlowControl {

    private final SessionStore sessionStore;
    private final ModuleStore moduleStore;

    private static final Logger logger = LoggerFactory.getLogger
            (ADVFlowControl.class);

    @Inject
    public ADVFlowControl(SessionStore sessionStore, ModuleStore moduleStore) {
        this.sessionStore = sessionStore;
        this.moduleStore = moduleStore;
    }

    public void process(String sessionJSON) {
        ADVModule currentModule = moduleStore.parseModule(sessionJSON);

        Session session = currentModule.getParser().parse(sessionJSON);
        session.setModule(currentModule);

        sessionStore.addSession(session);
    }

}
