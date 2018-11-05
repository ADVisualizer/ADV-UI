package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.GeneralWalkerNode;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.generaltree.TreeGeneralTreeParser;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class TreeGeneralTreeLayouterTest {

    private TreeLayouterTestBase base;

    @Inject
    private TreeGeneralTreeParser testParser;
    @Inject
    private TreeGeneralTreeLayouter sut;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException,
            TimeoutException {
        base = new TreeLayouterTestBase(sut, testParser, reader,
                "general-tree-module-group.json");
    }

    @Test
    public void layoutTreeContainsRootTest() {
        final long rootId = 1;
        base.assertTreeContainsNode(rootId);
    }

    @Test
    public void layoutTreeNodeDHas4ChildrenTest() {
        final long nodeDIndex = 6L;
        base.layoutTree();
        GeneralWalkerNode nodeD = base.getNodes().get(nodeDIndex);
        assertEquals(4, nodeD.getChildren().size());
    }

    @Test
    public void layoutTreeContainsAllNodesTest() {
        final int expectedNodes = 10;
        base.assertTreeContainsAllNodes(expectedNodes);
    }

    @Test
    public void layoutTreePaneContainsAllNodesTest() {
        final int expectedNodes = 10;
        base.assertPaneContainsAllNodes(expectedNodes);
    }

    @Test
    public void layoutTreeContainsAllRelationsTest() {
        final int expectedRelations = 9;
        base.assertPaneContainsAllRelations(expectedRelations);
    }

    @Test
    public void layoutTreePositionsVerticesCorrectTest() {
        final long nodeIndex = 9L;
        final double expectedX = 2.0;
        final double expectedY = 2.0;
        base.assertNodePositionedCorrect(nodeIndex, expectedX, expectedY);
    }
}
