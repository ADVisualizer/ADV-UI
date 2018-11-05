package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.BinaryWalkerNode;
import ch.hsr.adv.ui.tree.logic.binarytree.TreeBinaryTreeParser;
import ch.hsr.adv.ui.tree.logic.binarytree.TreeBinaryTreeParserTest;
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
    private static final double DOUBLE_ACCURACY = 0.00001;

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
                .getResource("binary-tree-module-group.json");

        if (url == null) {
            throw new FileNotFoundException();
        }

        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        moduleGroup = testParser.parse(jsonElement);
    }

    private Map<Long, BinaryWalkerNode> buildTree() {
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
        Map<Long, BinaryWalkerNode> actual = buildTree();

        assertTrue(actual.containsKey(ROOT_ID));
    }

    @Test
    public void layoutTreeNodeBHasNoLeftChildTest() {
        final long nodeBIndex = 2L;
        BinaryWalkerNode actual = buildTree().get(nodeBIndex);

        assertNotNull(actual.getRightChild());
        assertNull(actual.getLeftChild());
    }

    @Test
    public void layoutTreeContainsAllNodesTest() {
        Pane actual = layoutTree();

        assertEquals(6,
                getChildren(actual, e -> e instanceof IndexedNode).size());
    }

    @Test
    public void layoutTreeContainsAllRelationsTest() {
        Pane actual = layoutTree();

        assertEquals(5,
                getChildren(actual, e -> e instanceof LabeledEdge).size());
    }

    @Test
    public void layoutTreePositionsVerticesCorrectTest() {
        final long nodeDIndex = 5L;
        BinaryWalkerNode actual = buildTree().get(nodeDIndex);

        assertEquals(-0.5, actual.getCenterX(), DOUBLE_ACCURACY);
        assertEquals(2.0, actual.getCenterY(), DOUBLE_ACCURACY);
    }
}
