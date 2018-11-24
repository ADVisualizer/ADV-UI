package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.presentation.widgets.IndexedNode;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.TreeParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
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

class TreeLayouterTestBase<T extends WalkerNode> {

    static final double DOUBLE_ACCURACY = 0.00001;

    private ModuleGroup moduleGroup;
    private TreeLayouterBase<T> sut;

    TreeLayouterTestBase(TreeLayouterBase<T> sut, TreeParser parser,
                         FileDatastoreAccess reader, String resource)
            throws TimeoutException, IOException {

        this.sut = sut;
        FxToolkit.registerPrimaryStage();
        URL url = getClass().getClassLoader().getResource(resource);

        if (url == null) {
            throw new FileNotFoundException();
        }

        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        moduleGroup = parser.parse(jsonElement);
    }

    Pane layoutTree() {
        return sut.layout(moduleGroup, moduleGroup.getFlags());
    }

    Map<Long, T> getNodes() {
        return sut.getNodes();
    }

    List<Node> getChildren(Pane pane, Predicate<? super Node> filter) {
        Group group = (Group) pane.getChildren().get(0);
        return group.getChildren().stream().filter(filter)
                .collect(Collectors.toList());
    }

    ModuleGroup getModuleGroup() {
        return moduleGroup;
    }

    void assertTreeContainsNode(Long nodeId) {
        layoutTree();
        Map<Long, T> actual = getNodes();

        assertTrue(actual.containsKey(nodeId));
    }

    void assertTreeContainsAllNodes(int expectedNodes) {
        layoutTree();
        assertEquals(expectedNodes, getNodes().size());
    }

    void assertPaneContainsAllNodes(int expectedNodes) {
        Pane pane = layoutTree();
        assertEquals(expectedNodes,
                getChildren(pane, e -> e instanceof IndexedNode).size());
    }

    void assertPaneContainsAllRelations(int expectedRelations) {
        Pane pane = layoutTree();
        assertEquals(expectedRelations,
                getChildren(pane, e -> e instanceof LabeledEdge).size());
    }

    void assertNodePositionedCorrect(long nodeIndex, double expectedX,
                                     double expectedY) {
        layoutTree();
        WalkerNode actual = getNodes().get(nodeIndex);

        assertEquals(expectedX, actual.getCenterX(), DOUBLE_ACCURACY);
        assertEquals(expectedY, actual.getCenterY(), DOUBLE_ACCURACY);
    }

    void assertTreeWithoutRootHasNoElements() {
        ModuleGroup emptyModule = new ModuleGroup("TestModule");
        Pane pane = sut.layout(emptyModule, emptyModule.getFlags());

        final int expectedNodes = 0;
        final int nodes =
                getChildren(pane, e -> e instanceof IndexedNode).size();
        assertEquals(expectedNodes, nodes);
    }
}
