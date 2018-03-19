package ch.adv.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.swing.text.html.Option;
import java.util.Optional;

public class RootViewModel {


    @Inject
    private ResourceLocator resourceLocator;

    @FXML
    private ListView<String> sessionListView;

    @FXML
    private TabPane sessionTabPane;

    private ObservableList<String> sessions;

    private static final Logger logger = LoggerFactory.getLogger(RootViewModel.class);

    public RootViewModel() {
        this.sessions = FXCollections.observableArrayList();
    }


    @FXML
    public void initialize() {
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

}
