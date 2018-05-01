package ch.adv.ui.bootstrapper;

import ch.adv.ui.core.logic.Layouter;
import ch.adv.ui.core.logic.Parser;
import ch.adv.ui.core.logic.Stringifyer;
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

            for (Type t : instance.getInterfaces()) {

                String type = t.getTypeName();

                if (type.equals(Layouter.class.getName())) {
                    Class<? extends Layouter> layouter =
                            (Class<? extends Layouter>) instance;
                    layouterMapBinder.addBinding(moduleNameKey).to(layouter);
                } else if (type.equals(Parser.class.getName())) {
                    Class<? extends Parser> parser =
                            (Class<? extends Parser>) instance;
                    parserMapBinder.addBinding(moduleNameKey).to(parser);
                } else if (type.equals(Stringifyer.class.getName())) {
                    Class<? extends Stringifyer> stringifyer =
                            (Class<? extends Stringifyer>) instance;
                    stringifyerMapBinder.addBinding(moduleNameKey)
                            .to(stringifyer);
                } else {
                    logger.debug("No fitting type found. Type was: {}", type);
                }

            }
        });
    }
}
