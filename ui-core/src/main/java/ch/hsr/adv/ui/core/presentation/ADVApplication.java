package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.presentation.util.ResourceLocator;
import ch.hsr.adv.ui.core.service.SocketServer;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Main class of ADV UI.
 * Starts the JavaFX GUI and the socket server.
 *
 * @author mwieland
 */
@Singleton
public class ADVApplication extends Application {

    private static final Logger logger = LoggerFactory
            .getLogger(ADVApplication.class);
    private static Injector injector;
    @Inject
    private SocketServer socketServer;
    @Inject
    private ResourceLocator resourceLocator;
    private Stage primaryStage;

    public static void setInjector(Injector injector) {
        ADVApplication.injector = injector;
    }

    @Override
    public void init() {
        injector.injectMembers(this);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        // use command line arguments before socketServer is started
        retrieveCLIParams();

        logger.info("Starting SocketServer...");
        socketServer.start();

        logger.info("Starting ADV UI...");
        setupStage();
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
        String host = params.get("host");
        if (host != null) {
            socketServer.setHost(host);
        }
    }

    private void setupStage() {
        Parent rootLayout = resourceLocator.loadFXML(
                ResourceLocator.Resource.ROOT_LAYOUT_FXML);
        Scene scene = new Scene(rootLayout);

        String globalCss = resourceLocator
                .getResourcePath(ResourceLocator.Resource.CSS_GLOBAL)
                .toExternalForm();

        scene.getStylesheets().add(globalCss);

        Image advIconImage = new Image(resourceLocator.getResourceAsStream(
                ResourceLocator.Resource.ICON_IMAGE));

        primaryStage.setTitle("ADV UI");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(advIconImage);

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        logger.info("ADV UI ready");
        primaryStage.show();
    }
}
