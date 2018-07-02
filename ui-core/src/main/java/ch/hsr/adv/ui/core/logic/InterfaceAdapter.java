package ch.hsr.adv.ui.core.logic;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Represents the mapping between an interface and a concrete class during
 * deserialization.
 * <p>
 * Usage:
 * <code>
 * gsonBuilder.registerTypeAdapter(Interface.class,
 * new InterfaceAdapter(ConcreteClass.class))
 * </code>
 *
 * @param <T> type
 * @author mtrentini
 */
public class InterfaceAdapter<T> implements JsonDeserializer<T> {

    private final Class<T> className;

    public InterfaceAdapter(Class<T> className) {
        this.className = className;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return context.deserialize(jsonObject, className);
    }
}
