package ch.adv.ui.access;

import com.google.gson.*;

import java.lang.reflect.Type;

public class InterfaceAdapter<T> implements
        JsonDeserializer<T> {

    private final Class className;

    public InterfaceAdapter(Class className) {
        this.className = className;
    }

    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext
                                 jsonDeserializationContext) throws
            JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonDeserializationContext.deserialize(jsonObject,
                className);
    }
}
