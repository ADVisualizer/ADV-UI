package ch.hsr.adv.ui.stack.logic;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.stack.logic.ConstantsStack;
import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates a json representation of a stack session.
 */
@Singleton
@Module(ConstantsStack.MODULE_NAME)
public class StackStringifyer implements Stringifyer {

    private static final Logger logger = LoggerFactory
            .getLogger(StackStringifyer.class);

    private final Gson gson;

    @Inject
    public StackStringifyer(GsonProvider gsonProvider) {
        this.gson = gsonProvider.getPrettifyer().create();
    }

    /**
     * Builds a json string from an graph module group.
     *
     * @param moduleGroup the moduleGroup to be transmitted
     * @return json string representation of the session
     */
    @Override
    public JsonElement stringify(ModuleGroup moduleGroup) {
        logger.info("Serialize stack group");
        String json = gson.toJson(moduleGroup);
        return gson.fromJson(json, JsonElement.class);
    }
}

