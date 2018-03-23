package ch.adv.ui.array;

import ch.adv.ui.access.Parser;
import ch.adv.ui.logic.model.ADVElement;
import ch.adv.ui.logic.model.ADVRelation;
import ch.adv.ui.logic.model.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * Parses a json of an array session to a Session object.
 */
@Singleton
public class ArrayParser implements Parser {

    private final Gson gson;

    private static final Logger logger = LoggerFactory.getLogger(ArrayParser
            .class);

    public ArrayParser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ADVElement.class, new
                ArrayElementInstanceCreator());
        gsonBuilder.registerTypeAdapter(ADVRelation.class, new
                ArrayRelationInstanceCreator());
        gson = gsonBuilder.create();
    }

    @Override
    public Session parse(String json) {
        logger.debug("Parsing json: \n {}", json);
        Session session = gson.fromJson(json, Session.class);
        return session;
    }

    private static class ArrayElementInstanceCreator implements
            InstanceCreator<ADVElement> {
        @Override
        public ADVElement createInstance(Type type) {
            return new ArrayElement();
        }

    }

    private static class ArrayRelationInstanceCreator implements
            InstanceCreator<ADVRelation> {
        @Override
        public ADVRelation createInstance(Type type) {
            return new ArrayRelation();
        }

    }

}
