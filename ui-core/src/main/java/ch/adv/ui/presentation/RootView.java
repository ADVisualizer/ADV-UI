package ch.adv.ui.presentation;

import ch.adv.ui.util.ResourceLocator;
import ch.adv.ui.logic.model.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;


public class RootView {

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private MenuItem menuItemLoadSession;

    @FXML
    private MenuItem menuItemStoreSession;

    @FXML
    private ListView<Session> sessionListView;

    @FXML
    private TabPane sessionTabPane;

    @Inject
    private ResourceLocator resourceLocator;

    private final RootViewModel rootViewModel;

    private static final Logger logger = LoggerFactory.getLogger(RootView
            .class);

    @Inject
    public RootView(RootViewModel viewModel) {
        this.rootViewModel = viewModel;
    }

    @FXML
    public void initialize() {
        menuItemClose.setOnAction(e -> handleCloseMenuItemClicked());
        menuItemLoadSession.setOnAction(e -> handleLoadSessionMenuItemClicked
                ());
        menuItemStoreSession.setOnAction(e ->
                handleStoreSessionMenuItemClicked());
        sessionListView.setItems(rootViewModel.getAvailableSessions());

        sessionListView.setCellFactory(lv -> new DeletableCell());

        openNewTab();
    }

    private void openNewTab() {
        sessionListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, session) -> {

                    if (session != null) {
                        Node sessionView = resourceLocator.load(ResourceLocator
                                .Resource.SESSION_VIEW_FXML);

                        Optional<Tab> existingTab = sessionTabPane.getTabs()
                                .stream()
                                .filter(t -> t.getText().equals(session
                                        .toString()))
                                .findFirst();
                        Tab newTab = existingTab.orElse(new Tab(session
                                .toString(), sessionView));

                        if (!existingTab.isPresent()) {
                            sessionTabPane.getTabs().add(newTab);
                        }

                        SingleSelectionModel<Tab> selectionModel =
                                sessionTabPane
                                        .getSelectionModel();
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

    private void handleDeleteSessionClicked(Session session) {
        logger.info("Deleting session {} ({})", session.getSessionName(),
                session.getSessionId());
        rootViewModel.deleteSession(session);
        Optional<Tab> existingTab = sessionTabPane.getTabs()
                .stream()
                .filter(t -> t.getText().equals(session.toString()))
                .findFirst();
        if (existingTab.isPresent()) {
            sessionTabPane.getTabs().remove(existingTab.get());
        }
    }

    class DeletableCell extends ListCell<Session> {
        private HBox hbox = new HBox();
        private Label label = new Label("(empty)");
        private Pane pane = new Pane();
        private Button button = new Button("x");

        DeletableCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> handleDeleteSessionClicked(getItem()));
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
