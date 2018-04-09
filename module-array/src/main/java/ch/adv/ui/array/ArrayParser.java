package ch.adv.ui.array;

import ch.adv.ui.core.access.InterfaceAdapter;
import ch.adv.ui.core.access.Parser;
import ch.adv.ui.core.domain.ADVElement;
import ch.adv.ui.core.domain.ADVRelation;
import ch.adv.ui.core.domain.Session;
import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.domain.styles.ADVValueStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a json of an array session to a Session object.
 */
@Singleton
public class ArrayParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(ArrayParser
            .class);
    private final Gson gson;

    /**
     * Registers Array specific types to the GsonBuilder
     */
    public ArrayParser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ADVElement.class, new
                InterfaceAdapter(ArrayElement.class));
        gsonBuilder.registerTypeAdapter(ADVRelation.class, new
                InterfaceAdapter(ArrayRelation.class));
        gsonBuilder.registerTypeAdapter(ADVStyle.class, new
                InterfaceAdapter(ADVValueStyle.class));
        gson = gsonBuilder.create();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Session parse(String json) {
        logger.debug("Parsing json: \n {}", json);
        Session session = gson.fromJson(json, Session.class);
        return session;
    }

}
