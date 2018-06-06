package ch.hsr.adv.ui.core.logic;

import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;

import java.lang.reflect.Modifier;

/**
 * Provides preconfigured GsonBuilders
 *
 * @author mtrentini
 */
@Singleton
public class GsonProvider {

    private final GsonBuilder minifier;
    private final GsonBuilder prettifyer;

    public GsonProvider() {
        this.prettifyer = new GsonBuilder();
        this.prettifyer.setPrettyPrinting();
        this.prettifyer.excludeFieldsWithModifiers(Modifier.TRANSIENT);

        // transient and static fields are excluded by default. We only want
        // transient fields to be excluded. That's why we exclude them
        // explicitly!
        this.minifier = new GsonBuilder();
        this.minifier.excludeFieldsWithModifiers(Modifier.TRANSIENT);
    }

    /**
     * Gets a json serializer to write a minfied json to the data store.
     *
     * @return a json serializer
     */
    public GsonBuilder getMinifier() {
        return minifier;
    }

    /**
     * Gets a pretty-printing json serializer to create a json representation
     * of an object.
     *
     * @return a pretty-printing json serializer
     */
    public GsonBuilder getPrettifyer() {
        return prettifyer;
    }
}
