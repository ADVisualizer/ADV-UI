package ch.adv.ui.bootstrapper;

import ch.adv.ui.array.ArrayModule;
import ch.adv.ui.core.app.ADVApplication;
import ch.adv.ui.core.app.ADVModule;
import ch.adv.ui.core.logic.ModuleStore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Registers all available modules in the ADV UI Core
 *
 * @author mwieland
 */
@Singleton
public class Bootstrapper {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrapper
            .class);

    @Inject
    private ArrayModule arrayModule;

    public Bootstrapper() {
        ADVApplication instance = ADVApplication.waitForADVApplication();
        instance.getInjector().injectMembers(this);
        registerModules();
    }

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

        new Thread(() -> Application.launch(ADVApplication.class, args))
                .start();
        new Bootstrapper();
    }

    private void registerModules() {
        Map<String, ADVModule> modules = Map
                .ofEntries(Map.entry(arrayModule.getModuleName(), arrayModule));

        ModuleStore.setAvailableModules(modules);
    }

}
