package ch.adv.ui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


@Singleton
public class ADVFlowControl {

    private final JsonParser jsonParser;

    private static final Logger logger = LoggerFactory.getLogger(ADVFlowControl.class);

    public static Map<String, ADVModule> availableModules;

    public ADVFlowControl() {
        this.jsonParser = new JsonParser();
    }

    public void process(String sessionJSON) {
        ADVModule currentModule = parseModule(sessionJSON);

        Session session = currentModule.getParser().parse(sessionJSON);
    }

    private ADVModule parseModule(String sessionJSON) {
        JsonElement sessionElement = jsonParser.parse(sessionJSON);
        JsonObject sessionObject = sessionElement.getAsJsonObject();
        String parsedModuleName = sessionObject.get("module").getAsString();

        logger.info("Parsed module '{}'", parsedModuleName);

        return availableModules.get(parsedModuleName);
    }
}