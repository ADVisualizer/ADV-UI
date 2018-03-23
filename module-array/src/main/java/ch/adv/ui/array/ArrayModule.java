package ch.adv.ui.array;

import ch.adv.ui.ADVModule;
import ch.adv.ui.presentation.Layouter;
import ch.adv.ui.access.Parser;
import ch.adv.ui.access.Stringifyer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module encapsulating module specific Stringifyer, Parser and Layouter.
 */
public class ArrayModule implements ADVModule {
    private static final Logger logger = LoggerFactory.getLogger(ArrayModule
            .class);

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
