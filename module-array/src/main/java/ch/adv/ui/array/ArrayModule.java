package ch.adv.ui.array;

import ch.adv.ui.ADVModule;
import ch.adv.ui.Layouter;
import ch.adv.ui.Parser;
import ch.adv.ui.Stringifyer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayModule implements ADVModule {
    private static final Logger logger = LoggerFactory.getLogger(ArrayModule.class);

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
