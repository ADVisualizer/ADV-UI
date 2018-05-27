package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxRobot;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class ArrayDefaultLayouterTest extends FxRobot {
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private ArrayParser testParser;
    @Inject
    private ArrayDefaultLayouter sut;

    private ModuleGroup moduleGroup;

    @Before
    public void setUp() throws IOException {
        URL url = getClass().getClassLoader().getResource("module-group.json");
        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        moduleGroup = testParser.parse(gson.fromJson(json, JsonElement.class));
    }

    @Test
    public void layoutTest() {
        // WHEN
        Pane actual = sut.layout(moduleGroup);

        // THEN
        ObservableList<Node> children = actual.getChildren();
        Group group = (Group) children.get(0);
        HBox hbox = (HBox) group.getChildren().get(0);
        int arrayElementCount = hbox.getChildren().size();
        assertEquals(2, arrayElementCount);
    }
}