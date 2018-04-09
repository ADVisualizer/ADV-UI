package ch.adv.ui.core.presentation;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Label currentIndex;
    @FXML
    private Label maxIndex;
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
     * Will be called once on a controller when the content of
     * its associated document has been completely loaded
     */
    @FXML
    public void initialize() {
        setButtonActions();
        bindButtonDisableProperties();
        bindReplayIcons();

        replaySpeedSlider.disableProperty().bind(sessionViewModel
                .getSpeedsliderDisableProperty());

        replayController.getReplaySpeedProperty()
                .bindBidirectional(replaySpeedSlider.valueProperty());
        replaySpeedSlider.setLabelFormatter(replaySliderStringConverter);

        stepProgressBar.progressProperty().bind(sessionViewModel
                .getProgressProperty());

        currentIndex.textProperty().bind(sessionViewModel
                .getCurrentIndexStringProperty());
        maxIndex.textProperty().bind(sessionViewModel
                .getMaxIndexStringProperty());

        setCurrentSnapshotAsContent();
        sessionViewModel.getCurrentSnapshotPaneProperty().addListener(
                (event, oldV, newV) -> setCurrentSnapshotAsContent());

        this.snapshotDescription.textProperty()
                .bindBidirectional(sessionViewModel
                        .getCurrentSnapshotDescriptionProperty());
    }

    private void setButtonActions() {
        replayButton.setOnAction(e -> handleReplayButtonClicked());
        cancelReplayButton.setOnAction(e -> handleCancelReplayButtonClicked());
        stepFirstButton.setOnAction(e -> handleStepFirstButtonClicked());
        stepBackwardButton.setOnAction(e -> handleStepBackwardButtonClicked());
        stepForwardButton.setOnAction(e -> handleStepForwardButtonClicked());
        stepLastButton.setOnAction(e -> handleStepLastButtonClicked());
    }

    private void bindReplayIcons() {
        sessionViewModel.isReplayingProperty()
                .addListener((ObservableValue<? extends Boolean> observable,
                              Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        replayButton.setGraphic(pauseIcon);
                    } else {
                        replayButton.setGraphic(playIcon);
                    }
                });
    }

    private void bindButtonDisableProperties() {
        stepFirstButton.disableProperty().bind(sessionViewModel
                .getStepFirstBtnDisableProperty());
        stepBackwardButton.disableProperty().bind(sessionViewModel
                .getStepBackwardBtnDisableProperty());
        stepForwardButton.disableProperty().bind(sessionViewModel
                .getStepForwardBtnDisableProperty());
        stepLastButton.disableProperty().bind(sessionViewModel
                .getStepLastBtnDisableProperty());
    }

    private void setCurrentSnapshotAsContent() {
        Pane currentSnapshot = sessionViewModel
                .getCurrentSnapshotPaneProperty()
                .get();
        this.contentPane.getChildren().clear();
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
        if (sessionViewModel.isReplayingProperty().get()) {
            sessionViewModel.pauseReplay();
        } else {
            sessionViewModel.replay();
        }
    }

    private void handleCancelReplayButtonClicked() {
        sessionViewModel.cancelReplay();
    }

    private void handleStepFirstButtonClicked() {
        sessionViewModel.navigateSnapshot(Navigate.FIRST);
    }

    private void handleStepBackwardButtonClicked() {
        sessionViewModel.navigateSnapshot(Navigate.BACKWARD);
    }

    private void handleStepForwardButtonClicked() {
        sessionViewModel.navigateSnapshot(Navigate.FORWARD);
    }

    private void handleStepLastButtonClicked() {
        sessionViewModel.navigateSnapshot(Navigate.LAST);
    }

}
