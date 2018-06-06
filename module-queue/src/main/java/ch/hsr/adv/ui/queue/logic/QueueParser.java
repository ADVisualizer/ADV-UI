package ch.hsr.adv.ui.queue.logic;

import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVValueStyle;
import ch.hsr.adv.commons.queue.logic.ConstantsQueue;
import ch.hsr.adv.commons.queue.logic.domain.QueueElement;
import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.InterfaceAdapter;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a json representation of a queue to a ModuleGroup.
 */
@Singleton
@Module(ConstantsQueue.MODULE_NAME)
public class QueueParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(QueueParser
            .class);
    private final Gson gson;

    /**
     * Registers queue specific types to the GsonBuilder
     *
     * @param gsonProvider preconfigured gson builder
     */
    @Inject
    public QueueParser(GsonProvider gsonProvider) {
        GsonBuilder builder = gsonProvider.getMinifier();
        builder.registerTypeAdapter(ADVElement.class, new
                InterfaceAdapter(QueueElement.class));
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
