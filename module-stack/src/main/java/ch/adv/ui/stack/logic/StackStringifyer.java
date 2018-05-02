package ch.adv.ui.stack.logic;

import ch.adv.ui.core.logic.GsonProvider;
import ch.adv.ui.core.logic.Stringifyer;
import ch.adv.ui.core.logic.domain.Module;
import ch.adv.ui.core.logic.domain.Session;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates a json representation of an stack session.
 */
@Singleton
@Module("stack")
public class StackStringifyer implements Stringifyer {

    private static final String EXPECTED_MODULE = "stack";
    private static final Logger logger = LoggerFactory.getLogger(
            StackStringifyer.class);
    private final GsonProvider gsonProvider;

    //can't be injected, because has to be instantiated with 'new' keyword in
    // Module implementations (default methods of interfaces)
    public StackStringifyer() {
        this.gsonProvider = new GsonProvider();
    }

    /**
     * Builds a json string from an stack session.
     *
     * @param session the session to be transmitted
     * @return json string representation of the session
     */
    @Override
    public String stringify(final Session session) {
        if (EXPECTED_MODULE.equals(session.getModuleName())) {
            logger.debug("resulting json: {}", gsonProvider.getPrettifyer()
                    .toJson(session));
            return gsonProvider.getPrettifyer().toJson(session);
        } else {
            logger.error("Wrong session for this Stringifyer. Module name is "
                            + "{} but should be {}", session.getSessionName(),
                    EXPECTED_MODULE);
            return null;
        }
    }
}

