package ch.adv.ui.array;

import ch.adv.ui.core.access.Parser;
import ch.adv.ui.core.access.Stringifyer;
import ch.adv.ui.core.app.ADVModule;
import ch.adv.ui.core.presentation.Layouter;

/**
 * Module encapsulating module specific Stringifyer, Parser and Layouter.
 */
public class ArrayModule implements ADVModule {

    private static final String MODULE_NAME = "array";

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public Layouter getLayouter() {
        return new ArrayLayouter();
    }

    @Override
    public Stringifyer getStringifyer() {
        return new ArrayStringifyer();
    }

    @Override
    public Parser getParser() {
        return new ArrayParser();
    }

}
