package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.Type;

@Singleton
public class ModuleGroupSerializer implements JsonSerializer<ModuleGroup> {

    private final ServiceProvider serviceProvider;

    @Inject
    public ModuleGroupSerializer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public JsonElement serialize(ModuleGroup moduleGroup, Type typeOfSrc,
                                 JsonSerializationContext context) {

        String moduleName = moduleGroup.getModuleName();
        Stringifyer stringifyer = serviceProvider.getStringifyer(moduleName);

        return stringifyer.stringify(moduleGroup);
    }
}
