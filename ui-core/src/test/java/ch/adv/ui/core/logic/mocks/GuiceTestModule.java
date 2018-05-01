package ch.adv.ui.core.logic.mocks;

import ch.adv.ui.core.logic.Layouter;
import ch.adv.ui.core.logic.Parser;
import ch.adv.ui.core.logic.Stringifyer;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class GuiceTestModule extends AbstractModule {

    private static final String MODULE_NAME = "test";

    @Override
    protected void configure() {
        MapBinder<String, Layouter> layouterMapBinder =
                MapBinder.newMapBinder(binder(), String.class,
                        Layouter.class);

        MapBinder<String, Parser> parserMapBinder =
                MapBinder.newMapBinder(binder(), String.class,
                        Parser.class);

        MapBinder<String, Stringifyer> stringifyerMapBinder =
                MapBinder.newMapBinder(binder(), String.class,
                        Stringifyer.class);

        layouterMapBinder.addBinding(MODULE_NAME).to(TestLayouter.class);
        parserMapBinder.addBinding(MODULE_NAME).to(TestParser.class);
        stringifyerMapBinder.addBinding(MODULE_NAME).to(TestStringifyer.class);
    }
}
