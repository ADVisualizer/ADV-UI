package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import com.google.gson.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer which delegates deserialization to the module
 * specific parser.
 *
 * @author mwieland
 */
@Singleton
public class ModuleGroupDeserializer implements JsonDeserializer<ModuleGroup> {

    private static final Logger logger = LoggerFactory
            .getLogger(ModuleGroupDeserializer.class);

    private final ServiceProvider serviceProvider;

    @Inject
    public ModuleGroupDeserializer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    /**
     * Deserializes the module group json with the module parser.
     *
     * @param json    module group json
     * @param typeOfT module group type
     * @param context deserialization context
     * @return parsed module group
     * @throws JsonParseException gson exceptions
     */
    @Override
    public ModuleGroup deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        String moduleName = object.get("moduleName").getAsString();
        Parser moduleParser = serviceProvider.getParser(moduleName);

        try {
            return moduleParser.parse(json);
        } catch (ADVParseException e) {
            logger.error("Exception occured while deserializing module group "
                    + "with name {}", moduleName, e);
            return null;
        }
    }
}
