package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.Type;

/**
 * Custom serializer which delegates serialization to the module specific
 * stringifier.
 *
 * @author mwieland
 */
@Singleton
public class ModuleGroupSerializer implements JsonSerializer<ModuleGroup> {

    private final ServiceProvider serviceProvider;
    private final DefaultStringifyer defaultStringifyer;


    @Inject
    public ModuleGroupSerializer(ServiceProvider serviceProvider,
                                 DefaultStringifyer defaultStringifyer) {
        this.serviceProvider = serviceProvider;
        this.defaultStringifyer = defaultStringifyer;
    }

    /**
     * Serializes the module group with the correct module stringifyer.
     * If no module-specific stringifyer is available, the default is used.
     *
     * @param moduleGroup module group
     * @param typeOfSrc   module group type
     * @param context     serialization context
     * @return serialized json
     */
    @Override
    public JsonElement serialize(ModuleGroup moduleGroup, Type typeOfSrc,
                                 JsonSerializationContext context) {

        String moduleName = moduleGroup.getModuleName();
        Stringifyer moduleStringifyer = serviceProvider
                .getStringifyer(moduleName);
        if (moduleStringifyer != null) {
            return moduleStringifyer.stringify(moduleGroup);
        } else {
            return defaultStringifyer.stringify(moduleGroup);
        }
    }
}
