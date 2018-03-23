package ch.adv.ui;

import ch.adv.ui.access.Parser;
import ch.adv.ui.access.Stringifyer;
import ch.adv.ui.logic.Layouter;
/**
*TEST
*/
public interface ADVModule {

    Layouter getLayouter();
    Stringifyer getStringifyer();
    Parser getParser();

}
