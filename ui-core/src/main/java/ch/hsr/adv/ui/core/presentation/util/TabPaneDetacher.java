package ch.hsr.adv.ui.core.presentation.util;


import com.google.inject.Singleton;
import de.jensd.shichimifx.utils.MouseRobot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.*;

/**
 * A simple Utility to make all {@link Tab}s of a {@link TabPane} detachable.
 * <br>
 * <h1>Usage</h1>
 * To get a {@link TabPane} in charge of control:
 * <br>
 * <b>Hint: Only already added {@link Tab}s are going to be in charge of
 * control!</b>
 * <p>
 * Tabs can then be detached simply by dragging a tab title to the desired
 * window position.
 *
 * @author mtrentini, Jens Deters (www.jensd.de)
 */
@Singleton
public class TabPaneDetacher {

    private final List<Tab> originalTabs = new ArrayList<>();
    private final Map<Integer, Tab> tapTransferMap = new HashMap<>();
    private final ObservableList<Stage> createdStages = FXCollections
            .observableArrayList();
    private final BooleanProperty alwaysOnTop = new SimpleBooleanProperty();
    private TabPane tabPane;
    private Tab currentTab;
    private String[] stylesheets = new String[] {};

    public BooleanProperty alwaysOnTopProperty() {
        return alwaysOnTop;
    }

    public Boolean isAlwaysOnTop() {
        return alwaysOnTop.get();
    }

    /**
     * Sets whether detached Tabs should be always on top.
     *
     * @param alwaysOnTop The state to be set.
     * @return The current TabPaneDetacher instance.
     */
    public TabPaneDetacher alwaysOnTop(boolean alwaysOnTop) {
        alwaysOnTopProperty().set(alwaysOnTop);
        return this;
    }

    /**
     * Sets the stylesheets that should be assigend to the new created
     * {@link Stage}.
     *
     * @param stylesheets The stylesheets to be set.
     * @return The current TabPaneDetacher instance.
     */
    public TabPaneDetacher stylesheets(String... stylesheets) {
        this.stylesheets = stylesheets;
        return this;
    }

    /**
     * Make all added {@link Tab}s of the given {@link TabPane} detachable.
     *
     * @param tabPane The {@link TabPane} to take over.
     * @return The current TabPaneDetacher instance.
     */
    public TabPaneDetacher makeTabsDetachable(TabPane tabPane) {
        this.tabPane = tabPane;
        originalTabs.addAll(tabPane.getTabs());
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            tapTransferMap.put(i, tabPane.getTabs().get(i));
        }
        tabPane.getTabs().stream().forEach(t -> {
            t.setClosable(false);
        });
        tabPane.setOnDragDetected(
                (MouseEvent event) -> {
                    if (event.getSource() instanceof TabPane) {
                        Pane rootPane = (Pane) tabPane.getScene().getRoot();
                        rootPane.setOnDragOver((DragEvent event1) -> {
                            event1.acceptTransferModes(TransferMode.ANY);
                            event1.consume();
                        });
                        currentTab = tabPane.getSelectionModel()
                                .getSelectedItem();
                        SnapshotParameters snapshotParams = new
                                SnapshotParameters();
                        snapshotParams.setTransform(Transform.scale(0.4, 0.4));
                        WritableImage snapshot = currentTab.getContent()
                                .snapshot(snapshotParams, null);
                        Dragboard db = tabPane
                                .startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent clipboardContent = new
                                ClipboardContent();
                        clipboardContent.put(DataFormat.PLAIN_TEXT, "detach");
                        db.setDragView(snapshot, 40, 40);
                        db.setContent(clipboardContent);
                    }
                    event.consume();
                }
        );
        tabPane.setOnDragDone(
                (DragEvent event) -> {
                    openTabInStage(currentTab);
                    tabPane.setCursor(Cursor.DEFAULT);
                    event.consume();
                }
        );
        return this;
    }

    /**
     * Opens the content of the given {@link Tab} in a separate Stage. While
     * the content is removed from the {@link Tab} it is
     * added to the root of a new {@link Stage}. The Window title is set to
     * the name of the {@link Tab};
     *
     * @param tab The {@link Tab} to get the content from.
     */
    public void openTabInStage(final Tab tab) {
        if (tab == null) {
            return;
        }
        int originalTab = originalTabs.indexOf(tab);
        tapTransferMap.remove(originalTab);
        Pane content = (Pane) tab.getContent();
        if (content == null) {
            throw new IllegalArgumentException("Can not detach Tab '" + tab
                    .getText() + "': content is empty (null).");
        }
        tab.setContent(null);
        System.out.println(content.getPrefWidth());
        final Scene scene = new Scene(content, content.getPrefWidth(), content
                .getPrefHeight());
        scene.getStylesheets().addAll(stylesheets);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(tab.getText());
        stage.setAlwaysOnTop(isAlwaysOnTop());
        Point2D p = MouseRobot.getMousePosition();
        stage.setX(p.getX());
        stage.setY(p.getY());
        stage.setOnCloseRequest((WindowEvent t) -> {
            stage.close();
            createdStages.remove(stage);
            tab.setContent(content);
            int originalTabIndex = originalTabs.indexOf(tab);
            tapTransferMap.put(originalTabIndex, tab);
            int index = 0;
            SortedSet<Integer> keys = new TreeSet<>(tapTransferMap.keySet());
            for (Integer key : keys) {
                Tab value = tapTransferMap.get(key);
                if (!tabPane.getTabs().contains(value)) {
                    tabPane.getTabs().add(index, value);
                }
                index++;
            }
            tabPane.getSelectionModel().select(tab);
        });
        stage.setOnShown((WindowEvent t) -> {
            tab.getTabPane().getTabs().remove(tab);
        });
        createdStages.add(stage);
        stage.show();
    }

    public ObservableList<Stage> getCreatedStages() {
        return createdStages;
    }
}