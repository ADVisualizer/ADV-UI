package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import com.google.gson.JsonElement;
import com.google.inject.Singleton;

@Singleton
public class TestStringifyer implements Stringifyer {
    @Override
    public JsonElement stringify(ModuleGroup moduleGroup) {
        return null;
    }
}
