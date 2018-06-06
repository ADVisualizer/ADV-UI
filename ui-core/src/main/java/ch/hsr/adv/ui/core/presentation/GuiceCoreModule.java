package ch.hsr.adv.ui.core.presentation;


import ch.hsr.adv.ui.core.access.DatastoreAccess;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.service.ADVConnectionFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Guice DI configuration class.
 * <p>
 * If this class grows to a confusing size, it should be split up in
 * multiple modules.
 *
 * @author mwieland
 */
public class GuiceCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(ADVConnectionFactory.class));
        install(new FactoryModuleBuilder().build(SessionReplayFactory.class));
        install(new FactoryModuleBuilder().build(ReplayViewModelFactory.class));
        install(new FactoryModuleBuilder().build(
                SteppingViewModelFactory.class));

        // -------- Access Layer -------- //
        bind(DatastoreAccess.class).to(FileDatastoreAccess.class);
    }
}
