package ch.adv.ui.core.logic;

import ch.adv.ui.core.logic.util.ADVParseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Stores all available modules.
 */
@Singleton
public class ModuleParser {

    private static final Logger logger = LoggerFactory
            .getLogger(ModuleParser.class);

    private final Map<String, ADVModule> moduleMap;
    private final JsonParser jsonParser = new JsonParser();

    @Inject
    public ModuleParser(Map<String, ADVModule> moduleMap) {
        this.moduleMap = moduleMap;
    }

    /**
     * Parses the <code>moduleName</code> field of the given sessionJSON.
     * If the module is known by the sessionStore it will return it.
     * Otherwise returns null.
     *
     * @param sessionJSON json string
     * @return parsed module
     * @throws ADVParseException if module can't be found
     */
    public ADVModule parseModule(final String sessionJSON) throws
            ADVParseException {
        try {
            JsonElement sessionElement = jsonParser.parse(sessionJSON);
            JsonObject sessionObject = sessionElement.getAsJsonObject();
            String parsedModuleName = sessionObject.get("moduleName")
                    .getAsString();

            logger.info("Parsed module '{}'", parsedModuleName);
            ADVModule module = moduleMap.get(parsedModuleName);
            if (module == null) {
                throw new ADVParseException("Can't find module");
            }
            return module;
        } catch (NullPointerException e) {
            throw new ADVParseException("Can't find module");
        }


    }
}
