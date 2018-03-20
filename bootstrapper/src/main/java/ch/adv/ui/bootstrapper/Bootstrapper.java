package ch.adv.ui.bootstrapper;

import ch.adv.ui.ADVApplication;
import ch.adv.ui.ADVFlowControl;
import ch.adv.ui.ADVModule;
import ch.adv.ui.array.ArrayModule;
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

    private static final Map<String, ADVModule> registeredModules;
    private static final Logger logger = LoggerFactory.getLogger(Bootstrapper.class);

    public static void main(String[] args) {
        logger.info("Bootstrapping ADV UI");

        ADVFlowControl.availableModules = registeredModules;
        Application.launch(ADVApplication.class, args);
    }

    static {
        Map<String, ADVModule> modules = new HashMap<>();
        modules.put("array", new ArrayModule());

        registeredModules = Collections.unmodifiableMap(modules);
    }
}