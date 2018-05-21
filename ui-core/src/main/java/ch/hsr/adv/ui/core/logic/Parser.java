package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import com.google.gson.JsonElement;

/**
 * Session parser
 * <p>
 * Abstraction Interface of the strategy pattern. Every Module supplies a
 * concrete strategy to be used.
 */
public interface Parser {

    /**
     * Parses the given json into a
     * {@link ch.hsr.adv.ui.core.logic.domain.Session}
     *
     * @param json json to parse
     * @return parsed {@link ch.hsr.adv.ui.core.logic.domain.Session}
     * @throws ADVParseException if anything goes wrong with parsing the
     *                           session
     */
    ModuleGroup parse(JsonElement json) throws ADVParseException;
}
