package ch.adv.ui.core.access;


import ch.adv.ui.core.domain.Session;

/**
 * JSON Serializer
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
