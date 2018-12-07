package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.presentation.widgets.IndexedNode;
import ch.hsr.adv.ui.tree.domain.BinaryTreeTestNode;
import ch.hsr.adv.ui.tree.domain.BinaryWalkerNode;
import ch.hsr.adv.ui.tree.logic.binarytree.TreeBinaryTreeParser;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

@RunWith(JukitoRunner.class)
public class TreeBinaryTreeLayouterTest {

    private static final double DOUBLE_ACCURACY
            = TreeLayouterTestBase.DOUBLE_ACCURACY;

    private TreeLayouterTestBase<BinaryWalkerNode> base;

    @Inject
    private TreeBinaryTreeParser testParser;
    @Inject
    private TreeBinaryTreeLayouter sut;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException,
            TimeoutException {
        base = new TreeLayouterTestBase<>(sut, testParser, reader,
                "binary-tree-module-group.json");
    }

    @Test
    public void layoutTreeContainsRootTest() {
        final long rootId = 1L;
        base.assertTreeContainsNode(rootId);
    }

    @Test
    public void layoutTreeNodeCHasNoRightChildTest() {
        final long nodeCIndex = 3L;
        base.layoutTree();
        BinaryWalkerNode nodeC = base.getNodes().get(nodeCIndex);

        assertNull(nodeC.getRightChild());
        assertNotNull(nodeC.getLeftChild());
    }

    @Test
    public void layoutTreeContainsAllNodesTest() {
        final int expectedNodes = 6;
        base.assertTreeContainsAllNodes(expectedNodes);
    }

    @Test
    public void layoutTreePaneContainsAllNodesTest() {
        final int expectedNodes = 6;
        base.assertPaneContainsAllNodes(expectedNodes);
    }

    @Test
    public void layoutTreePaneContainsAllRelationsTest() {
        final int expectedRelations = 5;
        base.assertPaneContainsAllRelations(expectedRelations);
    }

    @Test
    public void layoutTreeWithoutFixationPositionsVerticesCorrectTest() {
        base.getModuleGroup().getMetaData().clear();

        final long nodeIndex = 6L;
        final double expectedX = 0.5;
        final double expectedY = 2.0;
        base.assertNodePositionedCorrect(nodeIndex, expectedX, expectedY);
    }

    @Test
    public void layoutTreeWithFixationPositionsVerticesCorrectTest() {
        final long nodeIndex = 5L;
        final double expectedX = -0.25;
        final double expectedY = 2.0;

        base.assertNodePositionedCorrect(nodeIndex, expectedX, expectedY);
    }

    @Test
    public void layoutTreePositionsInvisiblePaneCorrectTest() {
        Pane pane = base.getChildren(base.layoutTree(),
                e -> (e instanceof Pane && !(e instanceof IndexedNode)))
                .stream().map(n -> (Pane) n).findFirst().orElse(null);

        final double verticalDistance = TreeLayouterBase
                .getVerticalVertexDistance();
        final double horizontalDistance = TreeLayouterBase
                .getHorizontalVertexDistance(true);
        final double expectedX = -3.25 * horizontalDistance;
        final double expectedY = -0.5 * verticalDistance;
        final double expectedWidth = 5.5 * horizontalDistance;
        final double expectedHeight = 4 * verticalDistance;

        assertNotNull(pane);
        assertEquals(expectedX, pane.getLayoutX(), DOUBLE_ACCURACY);
        assertEquals(expectedY, pane.getLayoutY(), DOUBLE_ACCURACY);
        assertEquals(expectedWidth, pane.getPrefWidth(), DOUBLE_ACCURACY);
        assertEquals(expectedHeight, pane.getPrefHeight(), DOUBLE_ACCURACY);
    }

    @Test
    public void layoutTreeWithoutRootHasNoElementsTest() {
        base.assertTreeWithoutRootHasNoElements();
    }

    @Test
    public void layoutTreeWithoutRootAndWithFixationHasNoElementsTest() {
        ModuleGroup emptyModule = new ModuleGroup("TestModule");
        emptyModule.getMetaData().put(ConstantsTree.MAX_TREE_HEIGHT_LEFT, "1");
        emptyModule.getMetaData().put(ConstantsTree.MAX_TREE_HEIGHT_RIGHT, "2");

        Pane pane = sut.layout(emptyModule, emptyModule.getFlags());

        final int expectedNodes = 0;
        final int nodes =
                base.getChildren(pane, e -> e instanceof IndexedNode).size();
        assertEquals(expectedNodes, nodes);
    }

    @Test
    public void layoutTreeWithOnlyRootAndFixationPositionsRootCorrectTest() {
        BinaryTreeTestNode<String> root = new BinaryTreeTestNode<>("Hi");
        ModuleGroup emptyModule = new ModuleGroup("TestModule");
        emptyModule.addElement(new TreeNodeElement(root, 1));
        emptyModule.getMetaData().put(ConstantsTree.MAX_TREE_HEIGHT_LEFT, "0");
        emptyModule.getMetaData().put(ConstantsTree.MAX_TREE_HEIGHT_RIGHT, "1");

        Pane pane = sut.layout(emptyModule, emptyModule.getFlags());

        final int expectedNodes = 1;
        final int nodes =
                base.getChildren(pane, e -> e instanceof IndexedNode).size();
        assertEquals(expectedNodes, nodes);
    }
}
