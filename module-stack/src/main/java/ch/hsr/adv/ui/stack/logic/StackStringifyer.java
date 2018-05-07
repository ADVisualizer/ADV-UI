package ch.hsr.adv.ui.stack.logic;

import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Generates a json representation of a stack session.
 */
@Singleton
@Module("stack")
public class StackStringifyer implements Stringifyer {

    private final GsonProvider gsonProvider;

    @Inject
    public StackStringifyer(GsonProvider gsonProvider) {
        this.gsonProvider = gsonProvider;
    }

    /**
     * Builds a json string from an graph module group.
     *
     * @param moduleGroup the moduleGroup to be transmitted
     * @return json string representation of the session
     */
    @Override
    public JsonElement stringify(ModuleGroup moduleGroup) {
        return gsonProvider.getPrettifyer().create().toJsonTree(moduleGroup);
    }
}

