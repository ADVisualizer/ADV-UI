package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.ui.core.logic.CoreLayouter;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import org.mockito.Mockito;

public class GuiceTestModule extends AbstractModule {

    private static final String MODULE_NAME = "test";

    @Override
    protected void configure() {
        bind(CoreLayouter.class).toInstance(Mockito.mock(CoreLayouter.class));

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
