package ch.adv.ui.core.access;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * JSON Deserializer for a specific class
 *
 * @param <T>
 */
public class InterfaceAdapter<T> implements JsonDeserializer<T> {

    private final Class className;

    public InterfaceAdapter(final Class className) {
        this.className = className;
    }

    /**
     * @inheritDoc
     */
    @Override
    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return context.deserialize(jsonObject, className);
    }
}
