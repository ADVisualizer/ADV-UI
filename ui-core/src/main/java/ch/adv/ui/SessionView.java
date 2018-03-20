package ch.adv.ui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


public class SessionView {

    @FXML
    private Button replayButton;

    @FXML
    private Button cancelReplayButton;

    @FXML
    private Button stepFirstButton;

    @FXML
    private Button stepBackwardButton;

    @FXML
    private Button stepForwardButton;

    @FXML
    private Button stepLastButton;

    @FXML
    private Slider replaySpeedSlider;

    @FXML
    private ProgressBar stepProgressBar;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private TextArea snapshotDescription;

    @Inject
    private ReplayController replayController;

    @Inject
    private ReplaySliderStringConverter replaySliderStringConverter;


    private final FontAwesomeIconView pauseIcon;
    private final FontAwesomeIconView playIcon;

    private static final Logger logger = LoggerFactory.getLogger(SessionView.class);

    public SessionView() {
        this.pauseIcon = new FontAwesomeIconView();
        pauseIcon.setIcon(FontAwesomeIcon.PAUSE);

        this.playIcon = new FontAwesomeIconView();
        playIcon.setIcon(FontAwesomeIcon.PLAY);
    }

    @FXML
    public void initialize() {
        replayButton.setOnAction(e -> handleReplayButtonClicked());
        cancelReplayButton.setOnAction(e -> handleCancelReplayButtonClicked());
        stepFirstButton.setOnAction(e -> handleStepFirstButtonClicked());
        stepBackwardButton.setOnAction(e -> handleStepBackwardButtonClicked());
        stepForwardButton.setOnAction(e -> handleStepForwardButtonClicked());
        stepLastButton.setOnAction(e -> handleStepLastButtonClicked());

        replayController.getReplaySpeed().bind(replaySpeedSlider.valueProperty());
        replaySpeedSlider.setLabelFormatter(replaySliderStringConverter);
    }

    private void handleReplayButtonClicked() {
        replayButton.setGraphic(pauseIcon);
    }

    private void handleCancelReplayButtonClicked() {
        replayButton.setGraphic(playIcon);
    }

    private void handleStepFirstButtonClicked() {
        //TODO
    }

    private void handleStepBackwardButtonClicked() {
        //TODO
    }

    private void handleStepForwardButtonClicked() {
        //TODO
    }

    private void handleStepLastButtonClicked() {
        //TODO
    }

}
