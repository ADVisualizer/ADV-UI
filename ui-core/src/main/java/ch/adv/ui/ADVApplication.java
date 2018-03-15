package ch.adv.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Main class of ADV UI.
 * Starts the JavaFX GUI and the socket server.
 * <p>
 * Use command-line argument 'port' to configure the socket server: <code>--port=9876</code>
 *
 * @author mwieland
 */
public class ADVApplication extends Application {

    private ADVModule extension;
    //TODO: to be injected
    private SocketServer socketServer;
    private Stage primaryStage;

    private static final Logger logger = LoggerFactory.getLogger(ADVApplication.class);

    public ADVApplication() {
        this.socketServer = new SocketServer();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // use command line arguments before socketServer is started
        retrieveCLIParams();

        logger.info("Starting SocketServer...");
        socketServer.start();

        logger.info("Starting ADV UI...");
        setUpFrame();
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
