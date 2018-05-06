package ch.hsr.adv.ui.core.logic;

import com.google.inject.Singleton;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.List;

@Singleton
public class CoreLayouter {

    public Region layout(List<Pane> panes) {
        SplitPane parentPane = new SplitPane();
        parentPane.setOrientation(Orientation.HORIZONTAL);
        parentPane.getItems().addAll(panes);
        return parentPane;
    }
}
