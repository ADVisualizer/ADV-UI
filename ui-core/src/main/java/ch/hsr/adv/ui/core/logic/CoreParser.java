package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for parsing a session and delegating work to module specific
 * parsers.
 *
 * @author mwieland
 */
@Singleton
public class CoreParser {

    private static final Logger logger = LoggerFactory.getLogger(
            CoreParser.class);

    private final Gson gson;

    @Inject
    public CoreParser(GsonProvider gsonProvider,
                      ModuleGroupDeserializer deserializer) {
        GsonBuilder builder = gsonProvider.getMinifier();
        builder.registerTypeAdapter(ModuleGroup.class, deserializer);
        gson = builder.create();
    }

    /**
     * Parses the session up to the module group which is handled
     * module-specifically.
     *
     * @param json session json
     * @return parsed session
     * @throws ADVParseException if no session id is set
     */
    public Session parse(String json) throws ADVParseException {
        logger.debug("Parsing json: \n {}", json);
        Session session = gson.fromJson(json, Session.class);
        // sessionId wasn't found in json, so id was default initialized
        if (session.getSessionId() == 0) {
            throw new ADVParseException("No SessionId found.");
        }
        return session;
    }

}
