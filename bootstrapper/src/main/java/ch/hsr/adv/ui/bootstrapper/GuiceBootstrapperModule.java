package ch.hsr.adv.ui.bootstrapper;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final String PACKAGE = "ch.hsr.adv.ui";

    /**
     * Finds ADV services annotated with {@link Module} and adds them to the DI
     * container.
     */
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

        Reflections reflections = new Reflections(PACKAGE);
        Set<Class<?>> annotated = reflections
                .getTypesAnnotatedWith(Module.class);
        annotated.forEach(instance -> {

            String moduleNameKey = instance.getAnnotation(Module.class).value();

            for (Class<?> clazz : instance.getInterfaces()) {

                if (Layouter.class.isAssignableFrom(clazz)) {
                    Class<? extends Layouter> layouter =
                            (Class<? extends Layouter>) instance;
                    layouterMapBinder.addBinding(moduleNameKey).to(layouter);

                } else if (Parser.class.isAssignableFrom(clazz)) {
                    Class<? extends Parser> parser =
                            (Class<? extends Parser>) instance;
                    parserMapBinder.addBinding(moduleNameKey).to(parser);

                } else if (Stringifyer.class.isAssignableFrom(clazz)) {
                    Class<? extends Stringifyer> stringifyer =
                            (Class<? extends Stringifyer>) instance;
                    stringifyerMapBinder.addBinding(moduleNameKey)
                            .to(stringifyer);
                } else {
                    logger.error("Module annotation error: No fitting type found. Type was: {}", clazz);
                }
            }
        });
    }
}
