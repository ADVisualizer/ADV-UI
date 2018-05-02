package ch.hsr.adv.ui.graph.logic;

import ch.adv.ui.core.logic.Stringifyer;
import ch.adv.ui.core.logic.domain.Module;
import ch.adv.ui.core.logic.domain.Session;
import com.google.inject.Singleton;

@Singleton
@Module("graph")
public class GraphStringifyer implements Stringifyer {
    @Override
    public String stringify(Session session) {
        return null;
    }
}
