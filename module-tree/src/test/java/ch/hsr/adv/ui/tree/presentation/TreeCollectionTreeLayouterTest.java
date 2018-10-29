package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.collectiontree.TreeCollectionTreeParser;
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
public class TreeCollectionTreeLayouterTest {

    private static final double DOUBLE_ACCURACY = 0.00001;

    @Inject
    private TreeCollectionTreeParser testParser;
    @Inject
    private TreeCollectionTreeLayouter sut;

    private ModuleGroup moduleGroup;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException,
            TimeoutException {
        FxToolkit.registerPrimaryStage();
        URL url = getClass().getClassLoader()
                .getResource("collection-tree-module-group.json");

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
    public void layoutTreeContainsAllRootsTest() {
        Map<Long, WalkerNode> actual = buildTree();

        assertTrue(actual.containsKey(1L));
        assertTrue(actual.containsKey(2L));
        assertTrue(actual.containsKey(6L));
    }

    @Test
    public void layoutTreeRootAHasNoChildren() {
        final long rootAIndex = 1L;
        WalkerNode actual = buildTree().get(rootAIndex);

        assertEquals(0, actual.getChildren().size());
    }

    @Test
    public void layoutTreeContainsAllNodesTest() {
        Pane actual = layoutTree();

        assertEquals(9,
                getChildren(actual, e -> e instanceof IndexedNode).size());
    }

    @Test
    public void layoutTreeContainsAllRelationsTest() {
        Pane actual = layoutTree();

        assertEquals(6,
                getChildren(actual, e -> e instanceof LabeledEdge).size());
    }

    @Test
    public void layoutTreePositionsVerticesCorrectTest() {
        final long nodeDIndex = 5L;
        WalkerNode actual = buildTree().get(nodeDIndex);

        assertEquals(3.0, actual.getCenterX(), DOUBLE_ACCURACY);
        assertEquals(1.0, actual.getCenterY(), DOUBLE_ACCURACY);
    }
}
