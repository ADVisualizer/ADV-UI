package ch.adv.ui;

import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;

/**
 * @author mwieland
 */
@Singleton
public class ResourceLocator {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLocator.class);

    private URL getResourcePath(Resource res) {
        return ResourceLocator.class.getClassLoader().getResource(res.getRelativePath());
    }

    public Object load(Resource resource) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getResourcePath(resource));
        try {
            return loader.load();
        } catch (IOException e) {
            logger.debug("Cannot load resource {}", resource, e);
        }
        return null;
    }


    public enum Resource {

        ROOTLAYOUT_FXML("fxml/root-layout.fxml");

        private String relativePath = "./";

        Resource(String relativePath) {

            this.setRelativePath(relativePath);
        }

        public String getRelativePath() {
            return relativePath;
        }

        public void setRelativePath(String relativePath) {
            if (relativePath != null){
                this.relativePath = relativePath;
            }
        }

    }

}
