package ch.adv.ui.core.access;


import ch.adv.ui.core.domain.Session;

/**
 * JSON Serializer
 * <p>
 * Abstraction Interface of the strategy pattern. Every Module supplies a
 * concrete strategy to be used.
 */
public interface Stringifyer {

    /**
     * Serializes the given Session into JSON
     *
     * @param session {@link Session} to stringify
     * @return json string
     */
    String stringify(Session session);
}
