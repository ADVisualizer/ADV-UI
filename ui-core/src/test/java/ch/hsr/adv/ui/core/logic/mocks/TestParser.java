package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;
import com.google.inject.Singleton;

@Singleton
public class TestParser implements Parser {

    @Override
    public ModuleGroup parse(JsonElement json) {
        return null;
    }

}
