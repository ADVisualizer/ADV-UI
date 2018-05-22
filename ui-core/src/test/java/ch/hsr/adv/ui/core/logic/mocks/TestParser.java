package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.Parser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TestParser implements Parser {

    private Gson gson;

    @Inject
    public TestParser(GsonProvider gsonProvider) {
        GsonBuilder builder = gsonProvider.getMinifier();
        gson = builder.create();
    }

    @Override
    public ModuleGroup parse(JsonElement json) {
        return gson.fromJson(json, ModuleGroup.class);
    }

}
