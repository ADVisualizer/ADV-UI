package ch.adv.ui.bootstrapper;

import ch.adv.ui.core.app.ADVApplication;
import ch.adv.ui.array.ArrayModule;
import ch.adv.ui.core.app.ADVModule;
import ch.adv.ui.core.logic.ModuleStore;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registers all available modules in the ADV UI Core
 *
 * @author mwieland
 */
public class Bootstrapper {

    private static final Map<String, ADVModule> REGISTERED_MODULES;
    private static final Logger logger = LoggerFactory.getLogger(Bootstrapper
            .class);

    /**
     * ADV UI entry point
     * <p>
     * Use command-line argument 'port' to configure the socket server:
     * <code>--port=9876</code>
     *
     * @param args cli arguments
     */
    public static void main(String[] args) {
        logger.info("Bootstrapping ADV UI");

        ModuleStore.setAvailableModules(REGISTERED_MODULES);
        Application.launch(ADVApplication.class, args);
    }


    /**
     * Static map of all available modules
     */
    static {
        Map<String, ADVModule> modules = new HashMap<>();
        modules.put("array", new ArrayModule());

        REGISTERED_MODULES = Collections.unmodifiableMap(modules);
    }
}
