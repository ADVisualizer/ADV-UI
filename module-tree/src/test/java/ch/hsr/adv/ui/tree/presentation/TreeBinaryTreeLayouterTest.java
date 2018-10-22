package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.TreeBinaryTreeParser;
import ch.hsr.adv.ui.tree.logic.TreeBinaryTreeParserTest;
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

import static org.junit.Assert.*;

@RunWith(JukitoRunner.class)
public class TreeBinaryTreeLayouterTest {

    private static final long ROOT_ID = 1L;

    @Inject
    private TreeBinaryTreeParser testParser;
    @Inject
    private TreeBinaryTreeLayouter sut;

    private ModuleGroup moduleGroup;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException,
            TimeoutException {
        FxToolkit.registerPrimaryStage();
        URL url = TreeBinaryTreeParserTest.class.getClassLoader()
                .getResource("module-group.json");

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
    public void testBuildTreeContainsRoot() {
        Map<Long, WalkerNode> actual = buildTree();

        assertTrue(actual.containsKey(ROOT_ID));
    }

    @Test
    public void testBuildTreeNodeBHasNoLeftChild() {
        Map<Long, WalkerNode> actual = buildTree();

        assertNotNull(actual.get(2L).getRightChild());
        assertNull(actual.get(2L).getLeftChild());
    }

    @Test
    public void testTreeHeightForRoot() {
        buildTree();
        int actual = sut.getTreeHeight(ROOT_ID);

        assertEquals(2, actual);
    }

    @Test
    public void testTreeHeightForLeaf() {
        buildTree();
        int actual = sut.getTreeHeight(6L);

        assertEquals(0, actual);
    }

    @Test
    public void testLayoutTreeContainsAllNodes() {
        Pane actual = layoutTree();

        assertEquals(6,
                getChildren(actual, e -> e instanceof IndexedNode).size());
    }

    @Test
    public void testLayoutTreeContainsAllRelations() {
        Pane actual = layoutTree();

        assertEquals(5,
                getChildren(actual, e -> e instanceof LabeledEdge).size());
    }
}
