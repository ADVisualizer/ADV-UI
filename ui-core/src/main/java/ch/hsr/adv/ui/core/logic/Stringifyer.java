package ch.hsr.adv.ui.core.logic;


import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;

/**
 * JSON Serializer
 * <p>
 * Abstraction Interface of the strategy pattern. Every Module can supply a
 * concrete strategy to be used. Otherwise the {@link DefaultStringifyer}
 * will be used.
 */
public interface Stringifyer {

    /**
     * Serializes the given module group to JSON
     *
     * @param moduleGroup {@link ModuleGroup}
     *                    to stringify
     * @return json string
     */
    JsonElement stringify(ModuleGroup moduleGroup);
}
