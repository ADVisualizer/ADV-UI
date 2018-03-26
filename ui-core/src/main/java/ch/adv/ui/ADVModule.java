package ch.adv.ui;

import ch.adv.ui.access.DatastoreAccess;
import ch.adv.ui.access.Parser;
import ch.adv.ui.access.Stringifyer;
import ch.adv.ui.presentation.Layouter;


public interface ADVModule {

    String getName();

    Layouter getLayouter();
    Stringifyer getStringifyer();
    Parser getParser();
}
