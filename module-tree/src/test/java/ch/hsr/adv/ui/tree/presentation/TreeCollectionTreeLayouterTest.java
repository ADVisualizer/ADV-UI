package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.tree.domain.GeneralWalkerNode;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.collectiontree.TreeCollectionTreeParser;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class TreeCollectionTreeLayouterTest {

    private TreeLayouterTestBase<GeneralWalkerNode> base;

    @Inject
    private TreeCollectionTreeParser testParser;
    @Inject
    private TreeCollectionTreeLayouter sut;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException,
            TimeoutException {
        base = new TreeLayouterTestBase<>(sut, testParser, reader,
                "collection-tree-module-group.json");
    }

    @Test
    public void layoutTreeContainsAllRootsTest() {
        final long root1Index = 1L;
        final long root2Index = 2L;
        final long root3Index = 6L;
        base.assertTreeContainsNode(root1Index);
        base.assertTreeContainsNode(root2Index);
        base.assertTreeContainsNode(root3Index);
    }

    @Test
    public void layoutTreeRootAHasNoChildren() {
        final long rootAIndex = 1L;
        base.layoutTree();

        WalkerNode rootA = base.getNodes().get(rootAIndex);
        assertEquals(0, rootA.getChildren().size());
    }

    @Test
    public void layoutTreeContainsAllNodesTest() {
        final int expectedNodes = 9;
        base.assertTreeContainsAllNodes(expectedNodes);
    }

    @Test
    public void layoutTreePaneContainsAllNodesTest() {
        final int expectedNodes = 9;
        base.assertPaneContainsAllNodes(expectedNodes);
    }

    @Test
    public void layoutTreePaneContainsAllRelationsTest() {
        final int expectedRelations = 6;
        base.assertPaneContainsAllRelations(expectedRelations);
    }

    @Test
    public void layoutTreePositionsVerticesCorrectTest() {
        final long nodeIndex = 5L;
        final double expectedX = 3.0;
        final double expectedY = 1.0;
        base.assertNodePositionedCorrect(nodeIndex, expectedX, expectedY);
    }
}
