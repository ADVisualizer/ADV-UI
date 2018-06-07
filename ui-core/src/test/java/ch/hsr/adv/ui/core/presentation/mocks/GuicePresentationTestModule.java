package ch.hsr.adv.ui.core.presentation.mocks;

import ch.hsr.adv.ui.core.presentation.SteppingViewModelFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class GuicePresentationTestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(
                SteppingViewModelFactory.class));
    }
}
