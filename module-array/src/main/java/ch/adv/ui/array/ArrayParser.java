package ch.adv.ui.array;

import ch.adv.ui.access.InterfaceAdapter;
import ch.adv.ui.access.Parser;
import ch.adv.ui.logic.model.ADVElement;
import ch.adv.ui.logic.model.ADVRelation;
import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.model.styles.ADVDefaultStyle;
import ch.adv.ui.logic.model.styles.ADVStyle;
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

    private final Gson gson;

    private static final Logger logger = LoggerFactory.getLogger(ArrayParser
            .class);

    /**
     * Registers Array specific types to the GsonBuilder
     */
    public ArrayParser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ADVElement.class, new
                InterfaceAdapter(ArrayElement.class));
        gsonBuilder.registerTypeAdapter(ADVRelation.class, new
                InterfaceAdapter(ArrayElement.class));
        //TODO: handle style parsing!
        gsonBuilder.registerTypeAdapter(ADVStyle.class, new
                InterfaceAdapter(ADVDefaultStyle.class));
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
