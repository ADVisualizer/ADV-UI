package ch.adv.ui.core.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores all available modules.
 */
@Singleton
public class ModuleStore {

    private static final Map<String, ADVModule> AVAILABLE_MODULES = new
            HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(
            ModuleStore.class);
    private final JsonParser jsonParser = new JsonParser();

    /**
     * Sets available modules.
     * Should only be called by the bootstrapper component.
     *
     * @param modules available modules
     */
    public static void setAvailableModules(final Map<String, ADVModule>
                                                   modules) {
        AVAILABLE_MODULES.putAll(modules);
    }

    /**
     * Parses the <code>moduleName</code> field of the given sessionJSON.
     * If the module is known by the sessionStore it will return it.
     * Otherwise returns null.
     *
     * @param sessionJSON json string
     * @return parsed module
     */
    public ADVModule parseModule(final String sessionJSON) {
        JsonElement sessionElement = jsonParser.parse(sessionJSON);
        JsonObject sessionObject = sessionElement.getAsJsonObject();
        String parsedModuleName = sessionObject.get("moduleName").getAsString();

        logger.info("Parsed module '{}'", parsedModuleName);

        return AVAILABLE_MODULES.get(parsedModuleName);
    }
}
