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
        this.minifier.excludeFieldsWithModifiers(java.lang.reflect
                .Modifier.TRANSIENT);
    }

    public Gson getMinifier() {
        return minifier.create();
    }

    public Gson getPrettifyer() {
        return prettifyer.create();
    }
}
