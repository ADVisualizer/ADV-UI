package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.logic.InterfaceAdapter;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.domain.ADVElement;
import ch.hsr.adv.ui.core.logic.domain.ADVRelation;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVValueStyle;
import ch.hsr.adv.ui.core.logic.util.ADVParseException;
import ch.hsr.adv.ui.graph.logic.domain.GraphElement;
import ch.hsr.adv.ui.graph.logic.domain.GraphRelation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a json of an graph session to a Session object.
 */
@Singleton
@Module("graph")
public class GraphParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(GraphParser
            .class);
    private final Gson gson;


    @Inject
    public GraphParser(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(ADVElement.class, new
                InterfaceAdapter(GraphElement.class));
        gsonBuilder.registerTypeAdapter(ADVRelation.class, new
                InterfaceAdapter(GraphRelation.class));
        gsonBuilder.registerTypeAdapter(ADVStyle.class, new
                InterfaceAdapter(ADVValueStyle.class));
        gson = gsonBuilder.create();
    }

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