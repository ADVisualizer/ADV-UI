package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.util.ADVParseException;
import com.google.gson.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@Singleton
public class ModuleGroupDeserializer implements JsonDeserializer<ModuleGroup> {

    private static final Logger logger = LoggerFactory
            .getLogger(ModuleGroupDeserializer.class);

    private final ServiceProvider serviceProvider;

    @Inject
    public ModuleGroupDeserializer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

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
