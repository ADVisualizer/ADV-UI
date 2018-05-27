package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.presentation.util.I18n;
import ch.hsr.adv.ui.core.presentation.util.ReplaySliderStringConverter;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * The JavaFX Controller class for session-view.fxml. Initializes the view
 * and holds bindings to the {@link StateViewModel}.
 */
public class SessionView {

    private static final Logger logger = LoggerFactory.getLogger(SessionView
            .class);

    private static final KeyCodeCombination SHORTCUT_REPLAY = new
            KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN);
    private static final KeyCodeCombination SHORTCUT_REPLAY_CANCEL = new
            KeyCodeCombination(KeyCode.ESCAPE);

    private static final double NO_MARGIN_ANCHOR = 0.0;

    private final FontAwesomeIconView pauseIcon;
    private final FontAwesomeIconView playIcon;
    private final SteppingViewModel steppingViewModel;
    private final ReplayViewModel replayViewModel;
    private final StateViewModel stateViewModel;
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
    private Label replaySpeedSliderLabel;
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
    private Label descriptionLabel;
    @FXML
    private Label descriptionLabelNumber;
    @FXML
    private TextArea snapshotDescription;

    @Inject
    private ReplayController replayController;
    @Inject
    private ReplaySliderStringConverter replaySliderStringConverter;

    @Inject
    protected SessionView(SteppingViewModelFactory steppingViewModelFactory,
                          ReplayViewModelFactory replayViewModelFactory,
                          StateViewModel stateViewModel,
                          FontAwesomeIconView fontAwesomePauseView,
                          FontAwesomeIconView fontAwesomePlayView) {
        logger.debug("Construct SessionView");
        this.stateViewModel = stateViewModel;
        this.steppingViewModel = steppingViewModelFactory.create(
                stateViewModel);
        this.replayViewModel = replayViewModelFactory.create(
                stateViewModel, steppingViewModel);

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
    protected void initialize() {
        logger.debug("Initialize SessionView");
        bindButtonDisableProperties();
        bindReplayIcons();
        bindI18nStrings();
        setTooltips();
        setShortcuts();

        replaySpeedSlider.disableProperty().bind(stateViewModel
                .getSpeedSliderDisableProperty());

        replayController.getReplaySpeedProperty()
                .bindBidirectional(replaySpeedSlider.valueProperty());
        // set speed default
        replaySpeedSlider.setValue(2);

        replaySpeedSlider.setLabelFormatter(replaySliderStringConverter);
        //TODO: manage to change strings when changing language
        I18n.localeProperty().addListener((e, o, n) -> replaySpeedSlider
                .setLabelFormatter(new ReplaySliderStringConverter()));

        stepProgressBar.progressProperty().bind(stateViewModel
                .getProgressProperty());

        currentIndex.textProperty().bind(stateViewModel
                .getCurrentIndexStringProperty());
        maxIndex.textProperty().bind(stateViewModel
                .getMaxIndexStringProperty());

        setCurrentSnapshotAsContent();
        stateViewModel.getCurrentSnapshotPaneProperty().addListener(
                (event, oldV, newV) -> setCurrentSnapshotAsContent());

        this.snapshotDescription.textProperty()
                .bindBidirectional(stateViewModel
                        .getCurrentSnapshotDescriptionProperty());
    }


    private void setTooltips() {
        stepFirstButton.setTooltip(I18n
                .tooltipForKey("tooltip.snapshot-bar.step_first"));
        stepBackwardButton.setTooltip(I18n
                .tooltipForKey("tooltip.snapshot-bar.step_backward"));
        stepForwardButton.setTooltip(I18n
                .tooltipForKey("tooltip.snapshot-bar.step_forward"));
        stepLastButton.setTooltip(I18n
                .tooltipForKey("tooltip.snapshot-bar.step_last"));
        cancelReplayButton.setTooltip(I18n
                .tooltipForKey("tooltip.snapshot-bar.cancel",
                        SHORTCUT_REPLAY_CANCEL.getName()));
        replayButton
                .setTooltip(I18n.tooltipForKey("tooltip.snapshot-bar.play",
                        SHORTCUT_REPLAY.getDisplayText()));
    }

    private void bindI18nStrings() {
        replaySpeedSliderLabel.textProperty()
                .bind(I18n.createStringBinding("title.speed"));
        descriptionLabel.textProperty()
                .bind(I18n.createStringBinding("title.description"));
        descriptionLabelNumber.textProperty().bind(currentIndex.textProperty());
        snapshotDescription.promptTextProperty()
                .bind(I18n.createStringBinding("placeholder.description"));
    }

    private void bindReplayIcons() {
        this.cancelReplayButton.disableProperty().bind(
                stateViewModel.getReplayingProperty().not());

        stateViewModel.getReplayingProperty().addListener(
                (ObservableValue<? extends Boolean> observable,
                 Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        replayButton.setGraphic(pauseIcon);
                        replayButton.setTooltip(I18n
                                .tooltipForKey("tooltip.snapshot-bar.pause"));
                    } else {
                        replayButton.setGraphic(playIcon);
                        replayButton.setTooltip(I18n
                                .tooltipForKey("tooltip.snapshot-bar.play"));
                    }
                });
    }

    private void bindButtonDisableProperties() {
        stepFirstButton.disableProperty().bind(stateViewModel
                .getStepButtonState().getStepFirstBtnDisableProperty());
        stepBackwardButton.disableProperty().bind(stateViewModel
                .getStepButtonState().getStepBackwardBtnDisableProperty());
        stepForwardButton.disableProperty().bind(stateViewModel
                .getStepButtonState().getStepForwardBtnDisableProperty());
        stepLastButton.disableProperty().bind(stateViewModel
                .getStepButtonState().getStepLastBtnDisableProperty());
    }

    private void setShortcuts() {
        replayButton.sceneProperty().addListener((e, o, n) -> {
            if (n != null) {
                logger.debug("Setting key shortcuts.");
                ObservableMap<KeyCombination, Runnable> accelerators =
                        replayButton.getScene().getAccelerators();
                accelerators.put(SHORTCUT_REPLAY, () -> replayButton.fire());
                accelerators.put(SHORTCUT_REPLAY_CANCEL,
                        () -> cancelReplayButton.fire());
            }
        });
    }

    private void setCurrentSnapshotAsContent() {
        Region currentSnapshot = stateViewModel.getCurrentSnapshotPaneProperty()
                .get();
        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(currentSnapshot);
        setAnchors(currentSnapshot);
    }

    private void setAnchors(final Region currentSnapshot) {
        AnchorPane.setBottomAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
        AnchorPane.setTopAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
        AnchorPane.setLeftAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
        AnchorPane.setRightAnchor(currentSnapshot, NO_MARGIN_ANCHOR);
    }

    /**
     * Event handler for the replay action
     */
    @FXML
    protected void handleReplayButtonClicked() {
        if (stateViewModel.getReplayingProperty().get()) {
            replayViewModel.pauseReplay();
        } else {
            replayViewModel.replay();
        }
    }

    /**
     * Event handler for the cancel replay action
     */
    @FXML
    protected void handleCancelReplayButtonClicked() {
        replayViewModel.cancelReplay();
    }

    /**
     * Event handler for the step button clicked action
     *
     * @param e event
     */
    @FXML
    protected void handleStepButtonClicked(Event e) {
        Button source = (Button) e.getSource();
        if (source.equals(stepFirstButton)) {
            steppingViewModel.navigateSnapshot(Navigate.FIRST);
        } else if (source.equals(stepBackwardButton)) {
            steppingViewModel.navigateSnapshot(Navigate.BACKWARD);
        } else if (source.equals(stepForwardButton)) {
            steppingViewModel.navigateSnapshot(Navigate.FORWARD);
        } else {
            steppingViewModel.navigateSnapshot(Navigate.LAST);
        }
    }
}
