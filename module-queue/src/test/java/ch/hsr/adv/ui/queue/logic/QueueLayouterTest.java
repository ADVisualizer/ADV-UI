package ch.hsr.adv.ui.queue.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import ch.hsr.adv.ui.core.presentation.widgets.CurvedLabeledEdge;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import ch.hsr.adv.ui.core.presentation.widgets.SelfReferenceEdge;
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
import org.testfx.api.FxToolkit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
@RunWith(JukitoRunner.class)
public class QueueLayouterTest {
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private QueueParser testParser;
    @Inject
    private QueueLayouter sut;

    private ModuleGroup moduleGroup;

    @Before
    public void setUp() throws IOException, TimeoutException,
            ADVParseException {
        FxToolkit.registerPrimaryStage();
        URL url = getClass().getClassLoader().getResource("module-group.json");
        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        moduleGroup = testParser.parse(gson.fromJson(json, JsonElement.class));
    }

    @Test
    public void layoutTest() {
        // WHEN
        Pane actual = sut.layout(moduleGroup, null);

        // THEN
        ObservableList<Node> children = actual.getChildren();
        Group group = (Group) children.get(0);
        HBox hbox = (HBox) group.getChildren().get(0);
        int arrayElementCount = hbox.getChildren().size();
        assertEquals(2, arrayElementCount);
    }
}