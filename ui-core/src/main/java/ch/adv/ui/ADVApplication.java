package ch.adv.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ADVApplication extends Application {

    private ADVModule extension;
    private SocketServer socketServer = new SocketServer();
    private Stage primaryStage;

    private static final Logger logger = LoggerFactory.getLogger(ADVApplication.class);

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        logger.info("Starting SocketServer...");
        socketServer.start();

        logger.info("Starting ADV UI...");
        setUpFrame();
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
