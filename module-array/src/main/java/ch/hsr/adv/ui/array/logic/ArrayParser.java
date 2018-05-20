package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.commons.array.logic.ConstantsArray;
import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVValueStyle;
import ch.hsr.adv.ui.array.logic.domain.ArrayElement;
import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.InterfaceAdapter;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a json of an array session to a Session object.
 */
@Singleton
@Module(ConstantsArray.MODULE_NAME)
public class ArrayParser implements Parser {

    private static final Logger logger = LoggerFactory
            .getLogger(ArrayParser.class);

    private final Gson gson;

    @Inject
    public ArrayParser(GsonProvider gsonProvider) {
        GsonBuilder builder = gsonProvider.getMinifier();
        builder.registerTypeAdapter(ADVElement.class, new
                InterfaceAdapter(ArrayElement.class));
        builder.registerTypeAdapter(ADVStyle.class, new
                InterfaceAdapter(ADVValueStyle.class));
        gson = builder.create();
    }

    @Override
    public ModuleGroup parse(JsonElement json) {
        logger.debug("Parsing json: \n {}", json);
        return gson.fromJson(json, ModuleGroup.class);
    }
}
