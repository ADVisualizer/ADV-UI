package ch.adv.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootViewModel {

    private static final Logger logger = LoggerFactory.getLogger(RootViewModel.class);

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        logger.info("clicked");
    }

}
