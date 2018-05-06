package ch.hsr.adv.ui.core.logic;


import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;

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
     * @param moduleGroup {@link ModuleGroup} to stringify
     * @return json string
     */
    JsonElement stringify(ModuleGroup moduleGroup);
}
