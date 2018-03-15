package ch.adv.ui;

import com.gluonhq.ignite.guice.GuiceContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
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

    private ADVModule extension;
    private Stage primaryStage;

    private final GuiceContext context = new GuiceContext(this, () -> Arrays.asList(new GuiceBaseModule()));

    private static final Logger logger = LoggerFactory.getLogger(ADVApplication.class);

    @Override
    public void start(Stage primaryStage) {
        context.init();

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
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("ADV UI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setExtension(ADVModule extension) {
        this.extension = extension;
    }
}
