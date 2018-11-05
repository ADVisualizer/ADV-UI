package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.tree.domain.BinaryWalkerNode;
import ch.hsr.adv.ui.tree.logic.binarytree.TreeBinaryTreeParser;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JukitoRunner.class)
public class TreeBinaryTreeLayouterTest {

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
    public void layoutTreeNodeBHasNoLeftChildTest() {
        final long nodeBIndex = 2L;
        base.layoutTree();
        BinaryWalkerNode nodeB = base.getNodes().get(nodeBIndex);

        assertNotNull(nodeB.getRightChild());
        assertNull(nodeB.getLeftChild());
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
    public void layoutTreePositionsVerticesCorrectTest() {
        final long nodeIndex = 5L;
        final double expectedX = -0.5;
        final double expectedY = 2.0;
        base.assertNodePositionedCorrect(nodeIndex, expectedX, expectedY);
    }
}
