package ch.adv.ui.core.app;

import ch.adv.ui.core.util.ResourceLocator;
import ch.adv.ui.core.service.SocketServer;
import com.google.inject.Guice;
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

    private static final Logger logger = LoggerFactory.getLogger(
            ADVApplication.class);
    @Inject
    private SocketServer socketServer;
    @Inject
    private ResourceLocator resourceLocator;
    private Stage primaryStage;
    private Image advIconImage;

    @Override
    public void start(Stage stage) {
        Injector injector = Guice.createInjector(new GuiceBaseModule());
        injector.injectMembers(this);

        this.primaryStage = stage;
        this.advIconImage = new Image(resourceLocator.getResourceAsStream(
                ResourceLocator.Resource.ICON_IMAGE));
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

    }

    private void setupStage() {
        Parent rootLayout = resourceLocator.loadFXML(ResourceLocator.Resource
                .ROOT_LAYOUT_FXML);
        Scene scene = new Scene(rootLayout);

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
