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
     * Use command-line arguments 'port' and 'host' to configure the socket
     * server
     * <code>
     * java -jar adv-ui.jar --port=9876 --host=192.168.xxx.yyy
     * </code>
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

        Application.launch(ADVApplication.class, args);
    }


}
