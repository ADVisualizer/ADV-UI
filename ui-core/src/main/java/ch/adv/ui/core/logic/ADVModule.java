package ch.adv.ui.core.logic;

import ch.adv.ui.core.access.Parser;
import ch.adv.ui.core.access.Stringifyer;

/**
 * Represents a generic module
 */
public interface ADVModule {

    /**
     * Returns module name
     *
     * @return moduleName
     */
    String getModuleName();

    /**
     * Returns the module specific layouter, which positions the nodes on a
     * pane.
     *
     * @return ADV layouter
     */
    Layouter getLayouter();

    /**
     * Returns the module specific stringifyer, which serializes module
     * specific objects into json
     *
     * @return ADV stringifyer
     */
    Stringifyer getStringifyer();

    /**
     * Returns the module specific parser, which deserializes json into
     * module specific objects
     *
     * @return ADV parser
     */
    Parser getParser();
}
