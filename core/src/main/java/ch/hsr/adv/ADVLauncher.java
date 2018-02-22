package ch.hsr.adv;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ADVLauncher extends Application {

    private static final Logger logger = LoggerFactory.getLogger(ADVLauncher.class);

    public void startADV() {
        logger.debug("ADV Started");
        ADVLauncher.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Circle circ = new Circle(40, 40, 30);
        Group root = new Group(circ);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("My JavaFX Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
