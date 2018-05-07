package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.presentation.util.I18n;
import ch.hsr.adv.ui.core.presentation.util.ResourceLocator;
import de.jensd.shichimifx.utils.TabPaneDetacher;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.StatusBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.Locale;

/**
 * Main UI View
 */
public class RootView {

    private static final Logger logger = LoggerFactory.getLogger(RootView
            .class);
    private final RootViewModel rootViewModel;
    private final FileChooser fileChooser = new FileChooser();
    @FXML
    private Button loadSessionButton;
    @FXML
    private Button closeAllSessionsButton;
    @FXML
    private Button saveActiveSessionButton;
    @FXML
    private Button closeActiveSessionButton;
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
    @FXML
    private StatusBar notificationBar;
    @Inject
    private ResourceLocator resourceLocator;
    private ViewStore viewStore;


    @Inject
    protected RootView(RootViewModel viewModel) {
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
    protected void initialize() {
        bindI18nStrings();
        sessionListView.setItems(rootViewModel.getAvailableSessions());

        TabPaneDetacher detacher = TabPaneDetacher.create();
        String globalCss = resourceLocator
                .getResourcePath(ResourceLocator.Resource.CSS_GLOBAL)
                .toExternalForm();
        String sessionCss = resourceLocator
                .getResourcePath(ResourceLocator.Resource.CSS_SESSION)
                .toExternalForm();

        detacher.stylesheets(globalCss, sessionCss);
        detacher.makeTabsDetachable(sessionTabPane);

        viewStore = new ViewStore(Window.getWindows(), sessionTabPane
                .getTabs(), sessionListView.getItems());


        rootViewModel.getAvailableSessions()
                .addListener(handleAvailableSessionUpdate());

        sessionListView.setOnMouseClicked(e -> updateSelected(
                sessionListView.getSelectionModel().getSelectedItem()));

        sessionTabPane.setOnMouseClicked(e -> updateSelected(
                sessionTabPane.getSelectionModel().getSelectedItem()));

        handleLogoVisibility();
        bindButtonProperties();
        initButtons();
        setToolTips();
        handleNotifications();
    }

    private void handleNotifications() {
        notificationBar.textProperty().bind(rootViewModel
                .getNotificationMessageProperty());
        rootViewModel.getNotificationMessageProperty().addListener(e -> {
            if (rootViewModel.getNotificationMessageProperty().get()
                    .isEmpty()) {
                notificationBar.getStyleClass().remove("active-notification");
            } else {
                notificationBar.getStyleClass().add("active-notification");
            }
        });
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
                    Tab existingTab = viewStore.getTab(session);
                    if (existingTab == null) {
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
                    Tab existingTab = viewStore.getTab(session);
                    if (existingTab != null) {
                        sessionTabPane.getTabs().remove(existingTab);
                    } else {
                        Window existingWindow = viewStore.getWindow(session);
                        Stage stage = (Stage) existingWindow;
                        stage.close();
                    }
                });
            }
        };
    }

    private void updateSelected(Session session) {
        Tab tabToSelect = viewStore.getTab(session);
        if (tabToSelect == null) {
            Window windowToFocus = viewStore.getWindow(session);
            windowToFocus.requestFocus();
        } else {
            sessionTabPane.getSelectionModel().select(tabToSelect);
        }
        rootViewModel.getCurrentSessionProperty()
                .setValue(session);
    }

    private void updateSelected(Tab tab) {
        Session sessionToSelect = viewStore.getSession(tab);
        sessionListView.getSelectionModel().select(sessionToSelect);
        rootViewModel.getCurrentSessionProperty()
                .setValue(sessionToSelect);
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
        sessionTabPane.getTabs().addListener((ListChangeListener<? super Tab>)
                c -> {
                    int tabNumber = sessionTabPane.getTabs().size();
                    if (tabNumber == 0) {
                        sessionTabPane.getStyleClass().add("logo");
                    } else {
                        sessionTabPane.getStyleClass().remove("logo");
                    }
                });
    }

    private void bindButtonProperties() {
        saveActiveSessionButton.disableProperty()
                .bind(rootViewModel.getNoSessionsProperty());
        closeActiveSessionButton.disableProperty()
                .bind(rootViewModel.getNoSessionsProperty());
        closeAllSessionsButton.disableProperty()
                .bind(rootViewModel.getNoSessionsProperty());
    }

    private void initButtons() {
        loadSessionButton.textProperty().bind(I18n
                .createStringBinding("button.session-bar.load_session"));

        saveActiveSessionButton.textProperty().bind(I18n
                .createStringBinding("button.session-bar.save_session"));


        saveActiveSessionButton.sceneProperty().addListener((e, o, n) -> {
            if (n != null) {
                logger.debug("Setting key shortcuts.");
                ObservableMap<KeyCombination, Runnable> accelerators =
                        saveActiveSessionButton.getScene().getAccelerators();
                accelerators.put(
                        new KeyCodeCombination(KeyCode.S, KeyCombination
                                .SHORTCUT_DOWN),
                        () -> saveActiveSessionButton.fire()
                );
                accelerators.put(
                        new KeyCodeCombination(KeyCode.O, KeyCombination
                                .SHORTCUT_DOWN),
                        () -> loadSessionButton.fire()
                );
                accelerators.put(
                        new KeyCodeCombination(KeyCode.W, KeyCombination
                                .SHORTCUT_DOWN),
                        () -> closeActiveSessionButton.fire()
                );
                accelerators.put(
                        new KeyCodeCombination(KeyCode.W, KeyCombination
                                .SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN),
                        () -> closeAllSessionsButton.fire()
                );
            }
        });

        closeActiveSessionButton.textProperty().bind(I18n
                .createStringBinding("button.session-bar.close_active"));

        closeAllSessionsButton.textProperty().bind(I18n
                .createStringBinding("button.session-bar.close_all"));

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
        closeAllSessionsButton.setTooltip(I18n
                .tooltipForKey("tooltip.session-bar.close_all"));
        saveActiveSessionButton.setTooltip(I18n
                .tooltipForKey("tooltip.session-bar.save_session"));
        closeActiveSessionButton.setTooltip(I18n
                .tooltipForKey("tooltip.session-bar.close_active"));
        english.setTooltip(I18n
                .tooltipForKey("tooltip.session-bar.english"));
        german.setTooltip(I18n.tooltipForKey("tooltip.session-bar.german"));
    }

    /**
     * Event handler for clear all sessions
     */
    @FXML
    protected void handleClearAllSessionsClicked() {
        rootViewModel.clearAllSessions();
    }

    /**
     * Event handler for load session action
     */
    @FXML
    protected void handleLoadSessionClicked() {
        Window stage = sessionTabPane.getScene().getWindow();
        fileChooser.setTitle("Load Session File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null && file.exists()) {
            rootViewModel.loadSession(file);
        }
    }

    /**
     * Event handler for save session action
     */
    @FXML
    protected void handleSaveSessionClicked() {
        Window stage = sessionTabPane.getScene().getWindow();
        fileChooser.setTitle("Save Session File");
        fileChooser.setInitialFileName(sessionListView.getSelectionModel()
                .getSelectedItem().getSessionName());
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

    /**
     * Event handler for remove session action
     */
    @FXML
    protected void handleRemoveSessionClicked() {
        rootViewModel.removeCurrentSession();
    }
}
