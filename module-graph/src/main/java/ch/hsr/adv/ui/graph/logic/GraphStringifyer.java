package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Serializes a graph session to json
 */
@Singleton
@Module("graph")
public class GraphStringifyer implements Stringifyer {

    private final GsonProvider gsonProvider;

    @Inject
    public GraphStringifyer(GsonProvider gsonProvider) {
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
