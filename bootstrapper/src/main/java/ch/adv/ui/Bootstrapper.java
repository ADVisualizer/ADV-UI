package ch.adv.ui;

import ch.adv.ui.array.ArrayModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class Bootstrapper {
    private Map<String, ADVModule> modules;

    private static final Logger logger = LoggerFactory.getLogger(Bootstrapper.class);

    public static void main(String[] args) {
        logger.info("Bootstrapping ADV UI");
        Bootstrapper bootstraper = new Bootstrapper();

        bootstraper.bootstrap(args);
    }

    public void bootstrap(String[] args){
        ADVApplication adv = new ADVApplication();
        registerModules();
        adv.launchADV(args, modules);
    }

    /**
     * Registers all available modules with their corresponding keyword to make them known to the adv application.
     * The specified keyword must be the same as the one being used in the transmitted JSON!
     */
    private void registerModules() {
        modules = new HashMap<>();
        modules.put("array", new ArrayModule());
    }
}
