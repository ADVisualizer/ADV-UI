package ch.adv.ui.util;

import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author mwieland
 */
@Singleton
public class ResourceLocator {

    @Inject
    private Injector injector;

    private static final Logger logger = LoggerFactory.getLogger
            (ResourceLocator.class);

    public URL getResourcePath(Resource res) {
        return ResourceLocator.class.getClassLoader().getResource(res
                .getRelativePath());
    }

    public InputStream getResourceAsStream(Resource res) {
        return ResourceLocator.class.getClassLoader().getResourceAsStream(res
                .getRelativePath());
    }

    public Parent load(Resource resource) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getResourcePath(resource));
        loader.setControllerFactory(instantiatedClass -> injector.getInstance
                (instantiatedClass));
        try {
            return loader.load();
        } catch (IOException e) {
            logger.debug("Cannot load resource {}", resource, e);
            return null;
        }
    }

    public enum Resource {

        ROOT_LAYOUT_FXML("fxml/root-layout.fxml"),
        SESSION_VIEW_FXML("fxml/session-view.fxml"),

        ICON_IMAGE("images/adv-icon.png"),
        LOGO_IMAGE("images/adv-logo.png");


        private String relativePath = "./";

        Resource(String relativePath) {

            this.setRelativePath(relativePath);
        }

        public String getRelativePath() {
            return relativePath;
        }

        public void setRelativePath(String relativePath) {
            if (relativePath != null) {
                this.relativePath = relativePath;
            }
        }

    }

}
