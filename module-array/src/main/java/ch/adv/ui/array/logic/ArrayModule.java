package ch.adv.ui.array.logic;

import ch.adv.ui.core.logic.ADVModule;
import ch.adv.ui.core.logic.Layouter;
import ch.adv.ui.core.logic.Parser;
import ch.adv.ui.core.logic.Stringifyer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Module encapsulating module-specific Stringifyer, Parser and Layouter.
 */
@Singleton
public class ArrayModule implements ADVModule {

    private static final String MODULE_NAME = "array";

    private final ArrayLayouter arrayLayouter;
    private final ArrayStringifyer arrayStringifyer;
    private final ArrayParser arrayParser;
    private final StepEventController stepEventController;


    @Inject
    public ArrayModule(ArrayLayouter arrayLayouter, ArrayStringifyer
            arrayStringifyer, ArrayParser arrayParser, StepEventController
                               stepEventController) {

        this.arrayLayouter = arrayLayouter;
        this.arrayStringifyer = arrayStringifyer;
        this.arrayParser = arrayParser;
        this.stepEventController = stepEventController;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Layouter getLayouter() {
        return arrayLayouter;
    }

    @Override
    public Stringifyer getStringifyer() {
        return arrayStringifyer;
    }

    @Override
    public Parser getParser() {
        return arrayParser;
    }

}
