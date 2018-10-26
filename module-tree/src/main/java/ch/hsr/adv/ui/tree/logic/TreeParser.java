package ch.hsr.adv.ui.tree.logic;

import ch.hsr.adv.commons.core.access.GsonProvider;
import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ADVRelation;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVValueStyle;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.core.logic.InterfaceAdapter;
import ch.hsr.adv.ui.core.logic.Parser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a json representation of a tree to a ModuleGroup.
 */
public abstract class TreeParser implements Parser {

    private static final Logger logger = LoggerFactory
            .getLogger(TreeParser.class);
    private final Gson gson;

    /**
     * Registers tree specific types to the GsonBuilder
     *
     * @param gsonProvider preconfigured gson builder
     */
    protected TreeParser(GsonProvider gsonProvider) {
        GsonBuilder builder = gsonProvider.getMinifier();
        builder.registerTypeAdapter(ADVElement.class, new
                InterfaceAdapter<>(TreeNodeElement.class));
        builder.registerTypeAdapter(ADVRelation.class, new
                InterfaceAdapter<>(TreeNodeRelation.class));
        builder.registerTypeAdapter(ADVStyle.class, new
                InterfaceAdapter<>(ADVValueStyle.class));
        gson = builder.create();
    }

    @Override
    public ModuleGroup parse(JsonElement json) {
        logger.debug("Parsing json: \n {}", json);
        return gson.fromJson(json, ModuleGroup.class);
    }
}
