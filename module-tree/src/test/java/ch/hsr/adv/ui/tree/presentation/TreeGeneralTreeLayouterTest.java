package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.GeneralWalkerNode;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.generaltree.TreeGeneralTreeParser;
import ch.hsr.adv.ui.tree.presentation.widgets.IndexedNode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxToolkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JukitoRunner.class)
public class TreeGeneralTreeLayouterTest {

    private static final long ROOT_ID = 1L;
    private static final double DOUBLE_ACCURACY = 0.00001;

    @Inject
    private TreeGeneralTreeParser testParser;
    @Inject
    private TreeGeneralTreeLayouter sut;

    private ModuleGroup moduleGroup;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException,
            TimeoutException {
        FxToolkit.registerPrimaryStage();
        URL url = TreeGeneralTreeLayouterTest.class.getClassLoader()
                .getResource("general-tree-module-group.json");

        if (url == null) {
            throw new FileNotFoundException();
        }

        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        moduleGroup = testParser.parse(jsonElement);
    }

    private Map<Long, WalkerNode> buildTree() {
        layoutTree();
        return sut.getNodes();
    }

    private Pane layoutTree() {
        return sut.layout(moduleGroup, null);
    }

    private static List<Node> getChildren(Pane pane,
                                          Predicate<? super Node> filter) {
        Group group = (Group) pane.getChildren().get(0);
        return group.getChildren().stream().filter(filter)
                .collect(Collectors.toList());
    }

    @Test
    public void layoutTreeContainsRootTest() {
        Map<Long, WalkerNode> actual = buildTree();

        assertTrue(actual.containsKey(ROOT_ID));
    }

    @Test
    public void layoutTreeNodeDHas4ChildrenTest() {
        final long nodeDIndex = 6L;
        GeneralWalkerNode actual = (GeneralWalkerNode) buildTree()
                .get(nodeDIndex);

        assertEquals(4, actual.getChildren().size());
    }

    @Test
    public void layoutTreeContainsAllNodesTest() {
        Pane actual = layoutTree();

        assertEquals(10,
                getChildren(actual, e -> e instanceof IndexedNode).size());
    }

    @Test
    public void layoutTreeContainsAllRelationsTest() {
        Pane actual = layoutTree();

        assertEquals(9,
                getChildren(actual, e -> e instanceof LabeledEdge).size());
    }

    @Test
    public void layoutTreePositionsVerticesCorrectTest() {
        final long nodeIIndex = 9L;
        WalkerNode actual = buildTree().get(nodeIIndex);

        assertEquals(2.0, actual.getCenterX(), DOUBLE_ACCURACY);
        assertEquals(2.0, actual.getCenterY(), DOUBLE_ACCURACY);
    }
}
