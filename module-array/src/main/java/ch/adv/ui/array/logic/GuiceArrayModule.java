package ch.adv.ui.array.logic;

import ch.adv.ui.core.logic.ADVModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Adds the array module in the module map, which is maintained by guice
 *
 * @see <a href="https://github.com/google/guice/wiki/Multibindings">
 * Multibindings</a>
 */
public class GuiceArrayModule extends AbstractModule {

    public static final String MODULE_NAME = "array";

    @Override
    protected void configure() {
        MapBinder<String, ADVModule> moduleMapBinder =
                MapBinder.newMapBinder(binder(), String.class, ADVModule.class);
        moduleMapBinder.addBinding(MODULE_NAME).to(ArrayModule.class);
    }
}
