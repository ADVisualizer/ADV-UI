import javafx.application.Application;
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
        System.exit(0);
    }
}
