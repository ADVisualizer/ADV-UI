package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.access.GsonProvider;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default stringifyer is used when no module specific serialization is
 * needed or provided.
 *
 * @author mwieland
 */
@Singleton
public class DefaultStringifyer implements Stringifyer {

    private static final Logger logger = LoggerFactory
            .getLogger(DefaultStringifyer.class);
    private final Gson gson;

    @Inject
    public DefaultStringifyer(GsonProvider gsonProvider) {
        this.gson = gsonProvider.getPrettifyer().create();
    }

    /**
     * Builds a json string from an generic module group.
     *
     * @param moduleGroup the moduleGroup to serialize
     * @return json string representation of the session
     */
    @Override
    public JsonElement stringify(ModuleGroup moduleGroup) {
        logger.info("Serialize module group with default stringifyer");
        String json = gson.toJson(moduleGroup);
        return gson.fromJson(json, JsonElement.class);
    }
}