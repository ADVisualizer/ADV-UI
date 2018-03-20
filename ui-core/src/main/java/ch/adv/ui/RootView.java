package ch.adv.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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


    private ObservableList<String> sessions;

    private static final Logger logger = LoggerFactory.getLogger(RootView.class);

    public RootView() {
        this.sessions = FXCollections.observableArrayList();
    }


    @FXML
    public void initialize() {
        menuItemClose.setOnAction(e -> handleCloseMenuItemClicked());
        menuItemLoadSession.setOnAction(e -> handleLoadSessionMenuItemClicked());
        menuItemStoreSession.setOnAction(e -> handleStoreSessionMenuItemClicked());

        loadSessionView();
        openNewTab();
    }


    private void loadSessionView() {
        //TODO Fill with socket Data
        sessions.add("Array 1");
        sessions.add("Graph 1");
        sessions.add("Array 2");

        sessionListView.setItems(sessions);
    }

    private void openNewTab() {
        sessionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Node sessionView = resourceLocator.load(ResourceLocator.Resource.SESSION_VIEW_FXML);

            //TODO show time in tab name
            Optional<Tab> existingTab = sessionTabPane.getTabs().stream().filter(t -> t.getText().equals(newValue)).findFirst();
            Tab newTab = existingTab.orElse(new Tab(newValue, sessionView));

            if (!existingTab.isPresent()) {
                sessionTabPane.getTabs().add(newTab);
            }

            SingleSelectionModel<Tab> selectionModel = sessionTabPane.getSelectionModel();
            selectionModel.select(newTab);
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
