package ch.adv.ui.presentation;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * The JavaFX Controller class for session-view.fxml. Initializes the view
 * and holds bindings to the {@link SessionViewModel}.
 */
public class SessionView {

    private static final double NO_MARGIN_ANCHOR = 0.0;
    private static final Logger logger = LoggerFactory.getLogger(SessionView
            .class);
    private final SessionViewModel sessionViewModel;
    private final FontAwesomeIconView pauseIcon;
    private final FontAwesomeIconView playIcon;
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

    @Inject
    public SessionView(final SessionViewModel sessionViewModel,
                       FontAwesomeIconView
                               fontAwesomePauseView, final FontAwesomeIconView
                               fontAwesomePlayView) {
        this.sessionViewModel = sessionViewModel;

        this.pauseIcon = fontAwesomePauseView;
        pauseIcon.setIcon(FontAwesomeIcon.PAUSE);

        this.playIcon = fontAwesomePlayView;
        playIcon.setIcon(FontAwesomeIcon.PLAY);

    }

    /**
     * Will be called once on an controller when the contents of
     * its associated document have been completely loaded
     */
    @FXML
    public void initialize() {
        replayButton.setOnAction(e -> handleReplayButtonClicked());
        cancelReplayButton.setOnAction(e -> handleCancelReplayButtonClicked());
        stepFirstButton.setOnAction(e -> handleStepFirstButtonClicked());
        stepBackwardButton.setOnAction(e -> handleStepBackwardButtonClicked());
        stepForwardButton.setOnAction(e -> handleStepForwardButtonClicked());
        stepLastButton.setOnAction(e -> handleStepLastButtonClicked());

        replayController.getReplaySpeed().bind(replaySpeedSlider
                .valueProperty());
        replaySpeedSlider.setLabelFormatter(replaySliderStringConverter);

        setCurrentSnapshotAsContent();
        sessionViewModel.getCurrentSnapshotPaneProperty().addListener(
                (event, oldV, newV) -> setCurrentSnapshotAsContent());

        this.snapshotDescription.textProperty().bind(sessionViewModel
                .getCurrentSnapshotDescriptionProperty());


    }

    private void setCurrentSnapshotAsContent() {
        Pane currentSnapshot = sessionViewModel.getCurrentSnapshotPaneProperty()
                .get();
        this.contentPane.getChildren().add(currentSnapshot);
        setAnchors(currentSnapshot);
    }

    private void setAnchors(final Pane currentSnapshot) {
        AnchorPane.setBottomAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
        AnchorPane.setTopAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
        AnchorPane.setLeftAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
        AnchorPane.setRightAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
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
