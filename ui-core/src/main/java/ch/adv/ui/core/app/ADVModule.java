package ch.adv.ui.core.app;

import ch.adv.ui.core.access.Parser;
import ch.adv.ui.core.access.Stringifyer;
import ch.adv.ui.core.presentation.Layouter;

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
