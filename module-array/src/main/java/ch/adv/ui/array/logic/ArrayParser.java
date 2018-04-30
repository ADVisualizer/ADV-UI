package ch.adv.ui.array.logic;

import ch.adv.ui.array.logic.domain.ArrayRelation;
import ch.adv.ui.core.logic.InterfaceAdapter;
import ch.adv.ui.core.logic.Parser;
import ch.adv.ui.core.logic.domain.ADVElement;
import ch.adv.ui.core.logic.domain.ADVRelation;
import ch.adv.ui.core.logic.domain.Session;
import ch.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.adv.ui.core.logic.domain.styles.ADVValueStyle;
import ch.adv.ui.core.logic.util.ADVParseException;
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
        gsonBuilder.registerTypeAdapter(ADVStyle.class, new
                InterfaceAdapter(ADVValueStyle.class));
        gson = gsonBuilder.create();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Session parse(String json) throws ADVParseException {
        logger.debug("Parsing json: \n {}", json);
        Session session = gson.fromJson(json, Session.class);
        // sessionId wasn't found in json, so id is default initialized
        if (session.getSessionId() == 0) {
            throw new ADVParseException("No SessionId found.");
        }
        return session;
    }

}
