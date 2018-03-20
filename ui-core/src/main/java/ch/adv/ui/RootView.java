package ch.adv.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class RootView {

    @FXML
    public MenuItem menuItemClose;

    @FXML
    public MenuItem menuItemLoadSession;

    @FXML
    public MenuItem menuItemStoreSession;

    @FXML
    private ListView<String> sessionListView;

    @FXML
    private TabPane sessionTabPane;

    @Inject
    private ResourceLocator resourceLocator;

    private final RootViewModel rootViewModel;

    private static final Logger logger = LoggerFactory.getLogger(RootView.class);

    @Inject
    public RootView(RootViewModel viewModel) {
        this.rootViewModel = viewModel;
    }

    @FXML
    public void initialize() {
        menuItemClose.setOnAction(e -> handleCloseMenuItemClicked());
        menuItemLoadSession.setOnAction(e -> handleLoadSessionMenuItemClicked());
        menuItemStoreSession.setOnAction(e -> handleStoreSessionMenuItemClicked());
        sessionListView.setItems(rootViewModel.getAvailableSessions());

        openNewTab();
    }


    private void openNewTab() {
        sessionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, sessionName) -> {

            if (sessionName != null) {
                Node sessionView = resourceLocator.load(ResourceLocator.Resource.SESSION_VIEW_FXML);

                Optional<Tab> existingTab = sessionTabPane.getTabs().stream().filter(t -> t.getText().equals(sessionName)).findFirst();
                Tab newTab = existingTab.orElse(new Tab(sessionName, sessionView));

                if (!existingTab.isPresent()) {
                    sessionTabPane.getTabs().add(newTab);
                }

                SingleSelectionModel<Tab> selectionModel = sessionTabPane.getSelectionModel();
                selectionModel.select(newTab);
            }
        });
    }

    private void handleStoreSessionMenuItemClicked() {
        //TODO
    }

    private void handleLoadSessionMenuItemClicked() {
        //TODO
    }

    private void handleCloseMenuItemClicked() {
        Platform.exit();
        System.exit(0);
    }

}
