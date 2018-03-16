package ch.adv.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Main class of ADV UI.
 * Starts the JavaFX GUI and the socket server.
 * <p>
 * Use command-line argument 'port' to configure the socket server: <code>--port=9876</code>
 *
 * @author mwieland
 */
@Singleton
public class ADVApplication extends Application {

    @Inject
    private SocketServer socketServer;

    @Inject
    private ResourceLocator resourceLocator;

    private Stage primaryStage;

    private Map<String, ADVModule> modules;

    private static final Logger logger = LoggerFactory.getLogger(ADVApplication.class);

    public void launchADV(String[] args, Map<String, ADVModule> modules){
        this.modules = modules;
        ADVApplication.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Injector injector = Guice.createInjector(new GuiceBaseModule());
        injector.injectMembers(this);

        this.primaryStage = primaryStage;
        // use command line arguments before socketServer is started
        retrieveCLIParams();

        logger.info("Starting SocketServer...");
        socketServer.start();

        logger.info("Starting ADV UI...");
        setUpFrame();

        logger.info("ADV UI ready");
    }

    /**
     * Checks command line arguments for configurable port number
     */
    private void retrieveCLIParams() {
        Map<String, String> params = getParameters().getNamed();
        params.forEach((k, v) -> logger.debug("Found param: {} -> {}", k, v));
        String port = params.get("port");
        if (port != null) {
            socketServer.setPort(Integer.parseInt(port));
        }
    }

    private void setUpFrame() {
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });


        AnchorPane rootLayout = (AnchorPane) resourceLocator.load(ResourceLocator.Resource.ROOTLAYOUT_FXML);

        Scene scene = new Scene(rootLayout, 400, 300);

        primaryStage.setTitle("ADV UI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
