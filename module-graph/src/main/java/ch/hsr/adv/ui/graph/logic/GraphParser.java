package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.InterfaceAdapter;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.domain.ADVElement;
import ch.hsr.adv.ui.core.logic.domain.ADVRelation;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVValueStyle;
import ch.hsr.adv.ui.core.logic.util.ADVParseException;
import ch.hsr.adv.ui.graph.logic.domain.GraphElement;
import ch.hsr.adv.ui.graph.logic.domain.GraphRelation;
import ch.hsr.adv.ui.graph.logic.domain.ModuleConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a json of an graph session to a Session object.
 */
@Singleton
@Module(ModuleConstants.MODULE_NAME)
public class GraphParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(GraphParser
            .class);
    private final Gson gson;


    @Inject
    public GraphParser(GsonProvider gsonProvider) {
        GsonBuilder builder = gsonProvider.getMinifier();
        builder.registerTypeAdapter(ADVElement.class, new
                InterfaceAdapter(GraphElement.class));
        builder.registerTypeAdapter(ADVRelation.class, new
                InterfaceAdapter(GraphRelation.class));
        builder.registerTypeAdapter(ADVStyle.class, new
                InterfaceAdapter(ADVValueStyle.class));
        gson = builder.create();
    }

    @Override
    public ModuleGroup parse(JsonElement json) throws ADVParseException {
        logger.debug("Parsing json: \n {}", json);
        return gson.fromJson(json, ModuleGroup.class);
    }
}
