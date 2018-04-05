package ch.adv.ui.core.access;

import ch.adv.ui.core.domain.Session;

/**
 * Session parser
 */
public interface Parser {

    /**
     * Parses the given json into a {@link Session}
     * @param json json to parse
     * @return parsed {@link Session}
     */
    Session parse(String json);
}
