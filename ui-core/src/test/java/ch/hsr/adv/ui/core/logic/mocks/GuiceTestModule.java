package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.ui.core.access.DatastoreAccess;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.CoreLayouter;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.core.presentation.ReplayViewModelFactory;
import ch.hsr.adv.ui.core.presentation.SessionReplayFactory;
import ch.hsr.adv.ui.core.presentation.SteppingViewModelFactory;
import ch.hsr.adv.ui.core.service.ADVConnectionFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
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

        install(new FactoryModuleBuilder().build(ADVConnectionFactory.class));
        install(new FactoryModuleBuilder().build(SessionReplayFactory.class));
        install(new FactoryModuleBuilder().build(ReplayViewModelFactory.class));
        install(new FactoryModuleBuilder().build(
                SteppingViewModelFactory.class));

        // -------- Access Layer -------- //
        bind(DatastoreAccess.class).to(FileDatastoreAccess.class);
    }
}
