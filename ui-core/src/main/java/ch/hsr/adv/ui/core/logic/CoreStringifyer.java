package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

/**
 * Serializes an ADV Session
 *
 * @author mwieland
 */
public class CoreStringifyer {

    private final Gson gson;

    @Inject
    public CoreStringifyer(GsonProvider gsonProvider,
                           ModuleGroupSerializer serializer) {
        GsonBuilder builder = gsonProvider.getPrettifyer();
        builder.registerTypeAdapter(ModuleGroup.class, serializer);
        gson = builder.create();
    }

    /**
     * Builds a json string from an graph session.
     *
     * @param session the session to be transmitted
     * @return json string representation of the session
     */
    public String stringify(Session session) {
        return gson.toJson(session);
    }
}
