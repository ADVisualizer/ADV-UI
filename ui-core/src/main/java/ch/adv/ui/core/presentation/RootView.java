package ch.adv.ui.core.presentation;

import ch.adv.ui.core.domain.Session;
import ch.adv.ui.core.util.ResourceLocator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.controlsfx.control.SegmentedButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.Locale;
import java.util.Optional;

/**
 * Main UI View
 */
public class RootView {

    private static final Logger logger = LoggerFactory.getLogger(RootView
            .class);
    private final RootViewModel rootViewModel;
    private final FileChooser fileChooser = new FileChooser();
    private final ObjectProperty<Session> activeSession = new
            SimpleObjectProperty<>();
    @FXML
    private Button loadSessionButton;
    @FXML
    private Button clearAllSessionsButton;
    @FXML
    private Button saveActiveSessionButton;
    @FXML
    private Button removeActiveSessionButton;
    @FXML
    private ListView<Session> sessionListView;
    @FXML
    private TabPane sessionTabPane;
    @FXML
    private TitledPane sessionListViewTitle;
    @FXML
    private SegmentedButton changeLanguageButton;
    @FXML
    private ToggleButton english;
    @FXML
    private ToggleButton german;
    @Inject
    private ResourceLocator resourceLocator;


    @Inject
    public RootView(RootViewModel viewModel) {
        this.rootViewModel = viewModel;
        FileChooser.ExtensionFilter extensionFilter = new FileChooser
                .ExtensionFilter("ADV files (*.adv)", "*.adv");

        fileChooser.getExtensionFilters().add(extensionFilter);
    }

    /**
     * Will be called once on a controller when the contents of
     * its associated document have been completely loaded
     */
    @FXML
    public void initialize() {
        bindI18nStrings();
        sessionListView.setItems(rootViewModel.getAvailableSessions());
        sessionListView.setCellFactory(lv -> new CustomListCell());


        rootViewModel.getAvailableSessions()
                .addListener(handleAvailableSessionUpdate());

        sessionListView.setOnMouseClicked(e -> {
            updateSelected(sessionListView
                    .getSelectionModel(), sessionTabPane.getSelectionModel());
        });

        sessionTabPane.setOnMouseClicked(e -> {
            updateSelected(sessionTabPane.getSelectionModel(), sessionListView
                    .getSelectionModel());
        });

        handleLogoVisibility();
        bindButtonProperties();
        initLanguageButtons();
        setToolTips();
    }

    /**
     * Reacts to changes in the underlying data structure. Adding a session
     * results in creating a tab and selecting the new session. Removing a
     * session results in closing the tab and selecting another existing
     * session.
     *
     * @return a ListChangeListener
     */
    private ListChangeListener<Session> handleAvailableSessionUpdate() {
        return change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(session -> {
                    Optional<Tab> existingTab = getExistingTab(session);
                    if (!existingTab.isPresent()) {
                        Node sessionView = resourceLocator
                                .loadFXML(ResourceLocator
                                        .Resource.SESSION_VIEW_FXML);
                        Tab newTab = new Tab(session
                                .toString(), sessionView);
                        sessionTabPane.getTabs().add(newTab);
                        sessionTabPane.getSelectionModel()
                                .select(newTab);
                        sessionListView.getSelectionModel()
                                .select(session);
                    }
                });
                change.getRemoved().forEach(session -> {
                    Optional<Tab> existingTab = getExistingTab(session);
                    if (existingTab.isPresent()) {
                        sessionTabPane.getTabs()
                                .remove(existingTab.get());
                    }
                });
            }
        };
    }

    private void updateSelected(SelectionModel source, SelectionModel target) {
        int selectedIndex = source.getSelectedIndex();
        target.select(selectedIndex);
        Session selectedSession = sessionListView.getItems().get(selectedIndex);
        rootViewModel.getCurrentSessionProperty().setValue(selectedSession);
    }

    private void bindI18nStrings() {
        sessionListViewTitle.textProperty()
                .bind(I18n.createStringBinding("title.session_list"));
        english.textProperty().bind(I18n.createStringBinding(
                "session-bar.english"));
        german.textProperty().bind(I18n.createStringBinding(
                "session-bar.german"));
    }

    private void handleLogoVisibility() {
        sessionTabPane.getStyleClass().add("logo");
        sessionTabPane.getTabs()
                .addListener((ListChangeListener<? super Tab>)
                        c -> {
                            int tabNumber = sessionTabPane.getTabs().size();
                            if (tabNumber == 0) {
                                sessionTabPane.getStyleClass().add("logo");
                            } else {
                                sessionTabPane.getStyleClass()
                                        .remove("logo");
                            }
                        });
    }

    private void bindButtonProperties() {
        saveActiveSessionButton.disableProperty()
                .bind(rootViewModel.getNoSessionsProperty());
        removeActiveSessionButton.disableProperty()
                .bind(rootViewModel.getNoSessionsProperty());
        clearAllSessionsButton.disableProperty()
                .bind(rootViewModel.getNoSessionsProperty());
    }


    /**
     * Checks if a Tab is already existing for the given session
     *
     * @param session session to check
     * @return optional tab
     */
    private Optional<Tab> getExistingTab(Session session) {
        return sessionTabPane.getTabs()
                .stream()
                .filter(t -> t.getText().equals(session.toString()))
                .findFirst();
    }

    private void initLanguageButtons() {
        changeLanguageButton.getToggleGroup().selectToggle(english);
        changeLanguageButton.getToggleGroup().selectedToggleProperty()
                .addListener((e, oldV, newV) -> {
                    if (newV == german) {
                        I18n.setLocale(new Locale("de", "CH"));
                    } else {
                        I18n.setLocale(Locale.UK);
                    }
                });
    }

    private void setToolTips() {
        loadSessionButton.setTooltip(I18n
                .tooltipForKey("tooltip.session-bar.load_session"));
        clearAllSessionsButton.setTooltip(I18n
                .tooltipForKey("tooltip.session-bar.delete_sessions"));
        saveActiveSessionButton.setTooltip(I18n
                .tooltipForKey("tooltip.session-list.save_session"));
        removeActiveSessionButton.setTooltip(I18n
                .tooltipForKey("tooltip.session-list.remove_session"));
        english.setTooltip(I18n
                .tooltipForKey("tooltip.session-bar.english"));
        german.setTooltip(I18n.tooltipForKey("tooltip.session-bar.german"));
    }

    @FXML
    private void handleClearAllSessionsClicked() {
        rootViewModel.clearAllSessions();
    }

    @FXML
    private void handleLoadSessionClicked() {
        Window stage = sessionTabPane.getScene().getWindow();
        fileChooser.setTitle("Load Session File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null && file.exists()) {
            rootViewModel.loadSession(file);
        }
    }

    @FXML
    private void handleSaveSessionClicked(ActionEvent event) {
        Window stage = sessionTabPane.getScene().getWindow();
        fileChooser.setTitle("Save Session File");
        fileChooser.setInitialFileName(sessionTabPane.getSelectionModel()
                .getSelectedItem().getText());
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String chosenFilePath = file.getPath();
            if (!chosenFilePath.endsWith(".adv")) {
                File fileWithExtension = new File(file.getPath() + ".adv");
                if (fileWithExtension.exists()) {
                    file = new File(chosenFilePath + "_copy.adv");
                } else {
                    file = fileWithExtension;
                }
            }

            rootViewModel.saveSession(file);
        }
    }

    @FXML
    private void handleRemoveSessionClicked() {
        rootViewModel.removeCurrentSession();
    }

    /**
     * Represents a ListCell in the {@link ListView}.
     * <p>
     * It contains the label and a save and remove button.
     */
    private class CustomListCell extends ListCell<Session> {

        private static final int ICON_SIZE = 16;
        private static final int SPACING = 12;
        private HBox hbox = new HBox();
        private Label label = new Label("(empty)");
        private Pane pane = new Pane();
        private Label removeButton = new Label();
        private Label saveButton = new Label();

        CustomListCell() {
            super();

            hbox.getChildren()
                    .addAll(label, pane, saveButton, removeButton);
            hbox.setSpacing(SPACING);
            hbox.setAlignment(Pos.CENTER);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }


        /**
         * {@inheritDoc}
         *
         * @param session the new session to be displayed
         */
        @Override
        protected void updateItem(Session session, boolean empty) {
            super.updateItem(session, empty);
            setText(null);  // No text in label of super class
            if (empty || session == null) {
                label.setText("null");
                setGraphic(null);
            } else {
                label.setText(session.toString());
                setGraphic(hbox);
            }
        }
    }
}
