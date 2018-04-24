package ch.adv.ui.core.app;


import ch.adv.ui.core.access.DatastoreAccess;
import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.presentation.SessionReplayFactory;
import ch.adv.ui.core.presentation.sessionviewmodel.ReplayViewModelFactory;
import ch.adv.ui.core.presentation.sessionviewmodel.SteppingViewModelFactory;
import ch.adv.ui.core.service.ADVConnectionFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Guice DI configuration class.
 * <p>
 * If this class grows to a certain extend, it should be split up in
 * multiple modules.
 */
public class GuiceBaseModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(ADVConnectionFactory.class));
        install(new FactoryModuleBuilder().build(SessionReplayFactory.class));
        install(new FactoryModuleBuilder().build(ReplayViewModelFactory.class));
        install(new FactoryModuleBuilder().build(
                SteppingViewModelFactory.class));


        // -------- Service Layer -------- //


        // -------- Business Logic Layer -------- //


        // -------- Access Layer -------- //
        bind(DatastoreAccess.class).to(FileDatastoreAccess.class);
    }
}
