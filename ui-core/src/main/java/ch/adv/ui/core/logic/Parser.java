package ch.adv.ui.core.logic;

import ch.adv.ui.core.logic.domain.Session;
import ch.adv.ui.core.logic.util.ADVParseException;

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
     * @throws ADVParseException if anything goes wrong with parsing the
     *                           session
     */
    Session parse(String json) throws ADVParseException;
}
