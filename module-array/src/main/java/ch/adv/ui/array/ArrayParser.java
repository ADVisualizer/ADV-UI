package ch.adv.ui.array;

import ch.adv.ui.access.InterfaceAdapter;
import ch.adv.ui.access.Parser;
import ch.adv.ui.logic.model.ADVElement;
import ch.adv.ui.logic.model.ADVRelation;
import ch.adv.ui.logic.model.Session;
import ch.adv.ui.logic.model.styles.ADVDefaultStyle;
import ch.adv.ui.logic.model.styles.ADVStyle;
import com.google.gson.*;
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

    private static class ArrayElementDeserializer implements
            JsonDeserializer<ArrayElement> {

        @Override
        public ArrayElement deserialize(final JsonElement json, final Type
                typeOfT, final JsonDeserializationContext context)
                throws JsonParseException {

            final ArrayElement arrayElement = new ArrayElement();

            final JsonObject jsonObject = json.getAsJsonObject();

            arrayElement.setId(jsonObject.get("id").getAsLong());
            arrayElement.setFixedPosX(jsonObject.get("fixedPosX").getAsInt());
            arrayElement.setFixedPosY(jsonObject.get("fixedPosY").getAsInt());
            arrayElement.setContent(jsonObject.get("content").getAsString());

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson2 = gsonBuilder.create();
            ADVStyle style = gson2.fromJson(jsonObject.get("style"), ADVStyle
                    .class
            );
            arrayElement.setStyle(style);

            return arrayElement;
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
