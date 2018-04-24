package ch.adv.ui.core.access;

import ch.adv.ui.core.logic.domain.Session;

/**
 * Session parser
 * <p>
 * Abstraction Interface of the strategy pattern. Every Module supplies a
 * concrete strategy to be used.
 */
public interface Parser {

    /**
     * Parses the given json into a {@link Session}
     *
     * @param json json to parse
     * @return parsed {@link Session}
     */
    Session parse(String json);
}
