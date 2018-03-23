package ch.adv.ui.access;


import ch.adv.ui.logic.model.Session;

/**
 * JSON Serializer
 */
public interface Stringifyer {

    /**
     * Serializes the given Session into JSON
     *
     * @param session
     */
    String stringify(Session session);
}
