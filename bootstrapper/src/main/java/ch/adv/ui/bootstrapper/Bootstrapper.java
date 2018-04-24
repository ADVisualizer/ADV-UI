package ch.adv.ui.bootstrapper;

import ch.adv.ui.array.logic.GuiceArrayModule;
import ch.adv.ui.core.presentation.ADVApplication;
import ch.adv.ui.core.presentation.GuiceCoreModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers all available modules in the ADV UI Core
 *
 * @author mwieland
 */
@Singleton
public class Bootstrapper {

    private static final Logger logger = LoggerFactory.getLogger(
            Bootstrapper.class);

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
        Injector injector = Guice.createInjector(
                new GuiceCoreModule(),
                new GuiceArrayModule()
        );

        ADVApplication.setInjector(injector);

        //TODO pass injector to adv application
        Application.launch(ADVApplication.class, args);
    }


}
