package ch.adv.ui.core.logic;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class GuiceTestModule extends AbstractModule {

    public static final String MODULE_NAME = "test";

    @Override
    protected void configure() {
        MapBinder<String, ADVModule> moduleMapBinder =
                MapBinder.newMapBinder(binder(), String.class, ADVModule.class);
        moduleMapBinder.addBinding(MODULE_NAME).to(TestModule.class);
    }
}
