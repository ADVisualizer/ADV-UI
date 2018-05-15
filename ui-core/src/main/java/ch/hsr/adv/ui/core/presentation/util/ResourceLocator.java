package ch.hsr.adv.ui.core.presentation.util;

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
 * Contains all static resources (FXML, Images, CSS) and provides loadFXML
 * methods
 *
 * @author mwieland
 */
@Singleton
public class ResourceLocator {

    private static final Logger logger = LoggerFactory.getLogger(
            ResourceLocator.class);

    @Inject
    private Injector injector;

    /**
     * Returns the relative path to the resources
     *
     * @param resource resource
     * @return url
     */
    public URL getResourcePath(Resource resource) {
        return ResourceLocator.class.getClassLoader().getResource(
                resource.getRelativePath());
    }

    /**
     * Returns the resource as stream
     *
     * @param resource resource
     * @return inputstream
     */
    public InputStream getResourceAsStream(Resource resource) {
        return ResourceLocator.class.getClassLoader().getResourceAsStream(
                resource.getRelativePath());
    }

    /**
     * Loads the FXML resource
     *
     * @param resource resource
     * @return fxml element
     */
    public Parent loadFXML(Resource resource) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getResourcePath(resource));
        loader.setControllerFactory(instantiatedClass -> injector.getInstance(
                instantiatedClass));
        try {
            return loader.load();
        } catch (IOException e) {
            logger.debug("Cannot loadFXML resource {}", resource, e);
            return null;
        }
    }

    /**
     * Enum for all available static resources in /src/main/resources/
     */
    public enum Resource {

        ROOT_LAYOUT_FXML("fxml/root-layout.fxml"),
        SESSION_VIEW_FXML("fxml/session-view.fxml"),

        ICON_IMAGE("images/adv-icon.png"),
        LOGO_IMAGE("images/adv-logo.png"),

        CSS_GLOBAL("css/global.css"),
        CSS_ROOT("css/root-view.css"),
        CSS_SESSION("css/session-view.css");



        private String relativePath = "./";

        Resource(String relativePath) {

            this.setRelativePath(relativePath);
        }

        public String getRelativePath() {
            return relativePath;
        }

        /**
         * Sets the relative path of the resource enum
         *
         * @param relativePath relative path
         */
        public void setRelativePath(String relativePath) {
            if (relativePath != null) {
                this.relativePath = relativePath;
            }
        }

    }

}
