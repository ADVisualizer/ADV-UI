package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.commons.core.access.GsonProvider;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.graph.logic.ConstantsGraph;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serializes a graph ModuleGroup to json
 */
@Singleton
@Module(ConstantsGraph.MODULE_NAME)
public class GraphStringifyer implements Stringifyer {

    private static final Logger logger = LoggerFactory
            .getLogger(GraphStringifyer.class);

    private final Gson gson;

    @Inject
    public GraphStringifyer(GsonProvider gsonProvider) {
        this.gson = gsonProvider.getPrettifyer().create();
    }

    /**
     * Builds a json string from a graph module group.
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
