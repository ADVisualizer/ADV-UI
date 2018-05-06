package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.domain.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

public class CoreStringifyer {

    private final Gson gson;

    @Inject
    public CoreStringifyer(GsonBuilder gsonBuilder,
                           ModuleGroupSerializer serializer) {
        gsonBuilder.registerTypeAdapter(ModuleGroup.class, serializer);
        gson = gsonBuilder.create();
    }

    /**
     * Builds a json string from an graph session.
     *
     * @param session the session to be transmitted
     * @return json string representation of the session
     */
    public String stringify(final Session session) {
        return gson.toJson(session);
    }
}
