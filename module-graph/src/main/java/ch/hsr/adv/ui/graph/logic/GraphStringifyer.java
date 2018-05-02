package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.logic.GsonProvider;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.Session;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Module("graph")
public class GraphStringifyer implements Stringifyer {

    private final GsonProvider gsonProvider;

    @Inject
    public GraphStringifyer(GsonProvider gsonProvider) {
        this.gsonProvider = gsonProvider;
    }

    /**
     * Builds a json string from an graph session.
     *
     * @param session the session to be transmitted
     * @return json string representation of the session
     */
    @Override
    public String stringify(final Session session) {
        return gsonProvider.getPrettifyer().toJson(session);
    }

}
