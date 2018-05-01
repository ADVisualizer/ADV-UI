package ch.adv.ui.core.logic;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

/**
 * Contains a map of all service which need to be implemented by a module.
 * <p>
 * The services are bootstrapped in the bootstrapper and injected by guice.
 */
@Singleton
public class ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(
            ServiceProvider.class);

    private final Map<String, Layouter> layouterMap;
    private final Map<String, Parser> parserMap;
    private final Map<String, Stringifyer> stringifyerMap;

    @Inject
    public ServiceProvider(Map<String, Layouter> layouterMap,
                           Map<String, Parser> parserMap,
                           Map<String, Stringifyer> stringifyerMap) {

        this.layouterMap = layouterMap;
        this.parserMap = parserMap;
        this.stringifyerMap = stringifyerMap;
    }

    /**
     * Returns the layouter for the given module
     *
     * @param moduleName name of the module
     * @return layouter
     */
    public Layouter getLayouter(String moduleName) {
        Layouter layouter = layouterMap.get(moduleName.toLowerCase());
        if (layouter == null) {
            logger.error("Layouter not found for module {}", moduleName);
        }
        return layouter;
    }

    /**
     * Returns the parser for the given module
     *
     * @param moduleName name of the module
     * @return parser
     */
    public Parser getParser(String moduleName) {
        Parser parser = parserMap.get(moduleName.toLowerCase());
        if (parser == null) {
            logger.error("Parser not found for module {}", moduleName);
        }
        return parser;
    }

    /**
     * Returns the stringifyer for the given module
     *
     * @param moduleName name of the module
     * @return stringifyer
     */
    public Stringifyer getStringifyer(String moduleName) {
        Stringifyer stringifyer = stringifyerMap.get(moduleName.toLowerCase());
        if (stringifyer == null) {
            logger.error("Stringifyer not found for module {}", moduleName);
        }
        return stringifyer;
    }
}
