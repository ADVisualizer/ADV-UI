package ch.adv.ui;


import ch.adv.ui.presentation.SessionReplayFactory;
import ch.adv.ui.service.ADVConnectionFactory;
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
        install(new FactoryModuleBuilder()
                .build(ADVConnectionFactory.class));
        install(new FactoryModuleBuilder().build(SessionReplayFactory.class));

    }
}
