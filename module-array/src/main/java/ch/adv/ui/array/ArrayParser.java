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

    /**
     * Registers Array specific types to the GsonBuilder
     */
    public ArrayParser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ADVElement.class, new
                ArrayElementInstanceCreator());
        gsonBuilder.registerTypeAdapter(ADVRelation.class, new
                ArrayRelationInstanceCreator());
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

    /**
     * Gson Instance creator for @see{ArrayElement}s
     */
    private static class ArrayElementInstanceCreator implements
            InstanceCreator<ADVElement> {

        /**
         * @inheritDoc
         */
        @Override
        public ADVElement createInstance(Type type) {
            return new ArrayElement();
        }

    }

    /**
     * Gson Instance creator for {@link ch.adv.ui.array.ArrayRelation}
     */
    private static class ArrayRelationInstanceCreator implements
            InstanceCreator<ADVRelation> {

        /**
         * @inheritDoc
         */
        @Override
        public ADVRelation createInstance(Type type) {
            return new ArrayRelation();
        }

    }

}
