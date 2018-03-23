package ch.adv.ui.array;

import ch.adv.ui.access.GsonProvider;
import ch.adv.ui.access.Stringifyer;
import ch.adv.ui.logic.model.Session;
import ch.adv.ui.presentation.RootViewModel;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayStringifyer implements Stringifyer {

    private final GsonProvider gsonProvider;

    private static final Logger logger = LoggerFactory.getLogger
            (ArrayStringifyer.class);

    @Inject
    public ArrayStringifyer() {
        this.gsonProvider = new GsonProvider();
    }

    /**
     * Builds a json string from an array session.
     *
     * @param session the session to be transmitted
     * @return json string representation of the session
     */
    @Override
    public String stringify(Session session) {
        if ("array".equals(session.getModule().getName())) {
            logger.debug("resulting json: " + gsonProvider
                    .getPrettifyer().toJson(session));
            return gsonProvider.getMinifier().toJson
                    (session);
        } else {
            logger.error("Wrong session for this Stringifyer. Module name is " +
                    "{} but should be 'array'", session.getSessionName());
            return null;
        }


    }
}

