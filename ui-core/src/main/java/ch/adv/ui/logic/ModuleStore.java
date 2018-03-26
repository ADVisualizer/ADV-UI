package ch.adv.ui.logic;

import ch.adv.ui.ADVModule;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ModuleStore {

    private final JsonParser JSON_PARSER = new JsonParser();

    private static final Map<String, ADVModule> AVAILABLE_MODULES = new
            HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger
            (ModuleStore.class);

    public ADVModule parseModule(String sessionJSON) {
        JsonElement sessionElement = JSON_PARSER.parse(sessionJSON);
        JsonObject sessionObject = sessionElement.getAsJsonObject();
        String parsedModuleName = sessionObject.get("moduleName").getAsString();

        logger.info("Parsed module '{}'", parsedModuleName);

        return AVAILABLE_MODULES.get(parsedModuleName);
    }

    public static void setAvailableModules(Map<String, ADVModule> modules) {
        AVAILABLE_MODULES.putAll(modules);
    }
}
