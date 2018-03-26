package ch.adv.ui.access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    private final GsonBuilder minifier;
    private final GsonBuilder prettifyer;

    public GsonProvider() {
        this.prettifyer = new GsonBuilder();
        this.prettifyer.setPrettyPrinting();

        this.minifier = new GsonBuilder();
        this.minifier.setPrettyPrinting();
        this.minifier.excludeFieldsWithModifiers(java.lang.reflect
                .Modifier.TRANSIENT);
    }

    /**
     * Get a pretty-printing json serializer to write to the data store.
     *
     * @return a pretty-printing json serializer
     */
    public Gson getMinifier() {
        return minifier.create();
    }

    /**
     * Get a pretty-printing json serializer to create a json representation
     * of an object.
     *
     * @return a pretty-printing json serializer
     */
    public Gson getPrettifyer() {
        return prettifyer.create();
    }
}
