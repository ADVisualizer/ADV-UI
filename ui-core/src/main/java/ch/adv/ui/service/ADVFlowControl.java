package ch.adv.ui.service;

import ch.adv.ui.ADVModule;
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
    private final JsonParser jsonParser;

    private static final Logger logger = LoggerFactory.getLogger
            (ADVFlowControl.class);

    private static final Map<String, ADVModule> AVAILABLE_MODULES = new
            HashMap<>();

    @Inject
    public ADVFlowControl(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
        this.jsonParser = new JsonParser();
    }

    public void process(String sessionJSON) {
        ADVModule currentModule = parseModule(sessionJSON);

        Session session = currentModule.getParser().parse(sessionJSON);
        sessionStore.addSession(session);
    }

    private ADVModule parseModule(String sessionJSON) {
        JsonElement sessionElement = jsonParser.parse(sessionJSON);
        JsonObject sessionObject = sessionElement.getAsJsonObject();
        String parsedModuleName = sessionObject.get("module").getAsString();

        logger.info("Parsed module '{}'", parsedModuleName);

        return AVAILABLE_MODULES.get(parsedModuleName);
    }

    public static void setAvailableModules(Map<String, ADVModule> modules) {
        AVAILABLE_MODULES.putAll(modules);
    }
}
