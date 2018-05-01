package ch.adv.ui.bootstrapper;

import ch.adv.ui.core.logic.ADVModule;
import ch.adv.ui.core.logic.domain.Module;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Uses reflection to add module implementations with the {@link Module}
 * annotation to a Guice MapBinder to be injected into the adv ui core.
 * Classes injected this way will be lazily loaded once they are needed.
 *
 * @author mtrentini
 */
public class GuiceBootstrapperModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(
            GuiceBootstrapperModule.class);
    private static final String PACKAGE = "ch.adv.ui";

    @Override
    protected void configure() {

        MapBinder<String, ADVModule> moduleMapBinder =
                MapBinder.newMapBinder(binder(), String.class,
                        ADVModule.class);

        Reflections reflections = new Reflections(PACKAGE);
        Set<Class<?>> annotated = reflections
                .getTypesAnnotatedWith(Module.class);
        annotated.forEach(e -> {
            String nameKey = e.getAnnotation(Module.class).value();

            for (Type t : e.getInterfaces()) {
                String type = t.getTypeName();
                if (type.equals(ADVModule.class.getName())) {
                    Class<? extends ADVModule> module =
                            (Class<? extends ADVModule>) e;
                    moduleMapBinder.addBinding(nameKey).to(module);
                } else {
                    logger.debug("No fitting type found. Type was: {}",
                            type);
                }
            }
        });
    }
}
