package ch.adv.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvApplication extends Application {

    private AdvExtension extension;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Override
    public void start(Stage primaryStage) {
        logger.info("ADV Application started");

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("ADV UI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setExtension(AdvExtension extension) {
        this.extension = extension;
    }
}
