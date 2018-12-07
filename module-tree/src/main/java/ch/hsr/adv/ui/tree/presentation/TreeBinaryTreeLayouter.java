package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.tree.domain.BinaryWalkerNode;
import com.google.inject.Singleton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * Creates JavaFX Nodes for the tree elements and adds them to a pane
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_BINARY_TREE)
public class TreeBinaryTreeLayouter extends TreeLayouterBase<BinaryWalkerNode>
        implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            TreeBinaryTreeLayouter.class);

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        logger.info("Layouting binary-tree snapshot...");

        boolean showIndex = flags != null
                && flags.contains(ConstantsTree.SHOW_ARRAY_INDICES);

        initializeLayouting(moduleGroup);

        addDummyNodesToSingleChildParents();
        fillTreeWithDummyNodes(moduleGroup.getMetaData());

        positionNodes();

        Pane invisiblePane = createInvisiblePane(showIndex);

        removeDummyNodes();

        AutoScalePane visualizationPane = generatePane(showIndex);
        visualizationPane.addChildren(invisiblePane);

        return visualizationPane;
    }

    /**
     * fills max tree with dummy nodes so that fixed node-position is
     * possible
     *
     * @param metaData metaData that contains tree-layout-information
     */
    private void fillTreeWithDummyNodes(Map<String, String> metaData) {
        BinaryWalkerNode root = getDefaultRoot();
        if (root != null) {
            fillBranchWithDummyNodes(
                    metaData.get(ConstantsTree.MAX_TREE_HEIGHT_LEFT),
                    root.getLeftChild(), () -> {
                        addLeftDummyNodeToParent(root);
                        return root.getLeftChild();
                    });
            fillBranchWithDummyNodes(
                    metaData.get(ConstantsTree.MAX_TREE_HEIGHT_RIGHT),
                    root.getRightChild(), () -> {
                        addRightDummyNodeToParent(root);
                        return root.getRightChild();
                    });
        }
    }

    private void fillBranchWithDummyNodes(
            String maxHeightValue, BinaryWalkerNode branchRoot,
            Supplier<BinaryWalkerNode> createDummyBranchRoot) {
        if (maxHeightValue != null) {
            final int maxHeight = Integer.parseInt(maxHeightValue);
            if (maxHeight > 0) {
                if (branchRoot == null) {
                    branchRoot = createDummyBranchRoot.get();
                }
                fillBranchWithDummyNodes(branchRoot, maxHeight - 1);
            }
        }
    }

    private void fillBranchWithDummyNodes(BinaryWalkerNode node, int height) {
        if (height > 0) {
            if (node.getLeftChild() == null) {
                addLeftDummyNodeToParent(node);
            }
            if (node.getRightChild() == null) {
                addRightDummyNodeToParent(node);
            }
            fillBranchWithDummyNodes(node.getLeftChild(), height - 1);
            fillBranchWithDummyNodes(node.getRightChild(), height - 1);
        }
    }

    /**
     * inserts dummy-children to nodes with one child
     * this positions left children to the left side of the parent
     * and right children to the right side of the parent
     */
    private void addDummyNodesToSingleChildParents() {
        for (BinaryWalkerNode node : getNodes().values()) {
            if (node.getLeftChild() == null && node.getRightChild() != null) {
                addLeftDummyNodeToParent(node);
            }
            if (node.getLeftChild() != null && node.getRightChild() == null) {
                addRightDummyNodeToParent(node);
            }
        }
    }

    private void addLeftDummyNodeToParent(BinaryWalkerNode parent) {
        BinaryWalkerNode dummyChild = new BinaryWalkerNode(true);
        parent.setLeftChild(dummyChild);
        dummyChild.setParent(parent);
    }

    private void addRightDummyNodeToParent(BinaryWalkerNode parent) {
        BinaryWalkerNode dummyChild = new BinaryWalkerNode(true);
        parent.setRightChild(dummyChild);
        dummyChild.setParent(parent);
    }

    /**
     * creates invisible Pane to prevent AutoScalePane from that fits the
     * boundaries of the tree
     *
     * @param showIndex true if node indices are visible
     * @return invisible Pane
     */
    private Pane createInvisiblePane(boolean showIndex) {
        double[] leftPosition = new double[2];
        double[] rightPosition = new double[2];

        if (getDefaultRoot() != null) {
            leftPosition = getLeftmostPosition(getDefaultRoot());
            rightPosition = getRightmostPosition(getDefaultRoot());
        }

        double horizontalDistance = getHorizontalVertexDistance(showIndex);
        double verticalDistance = getVerticalVertexDistance();
        double left = (leftPosition[0] - 0.5) * horizontalDistance;
        double right = (rightPosition[0] + 0.5) * horizontalDistance;
        double top = -0.5 * verticalDistance;
        double bottom = (Math.max(leftPosition[1], rightPosition[1]) + 0.5)
                * verticalDistance;
        double width = right - left;
        double height = bottom - top;

        Pane invisiblePane = new Pane();
        invisiblePane.setPrefWidth(width);
        invisiblePane.setPrefHeight(height);
        invisiblePane.setLayoutX(left);
        invisiblePane.setLayoutY(top);
        invisiblePane.setVisible(true);
        invisiblePane.setBackground(Background.EMPTY);

        return invisiblePane;
    }

    private double[] getRightmostPosition(BinaryWalkerNode node) {
        if (node.getRightChild() != null) {
            return getRightmostPosition(node.getRightChild());
        }
        return new double[] {node.getCenterX(), node.getCenterY()};
    }

    private double[] getLeftmostPosition(BinaryWalkerNode node) {
        if (node.getLeftChild() != null) {
            return getLeftmostPosition(node.getLeftChild());
        }
        return new double[] {node.getCenterX(), node.getCenterY()};
    }

    private void removeDummyNodes() {
        for (BinaryWalkerNode node : getNodes().values()) {
            if (node.getLeftChild() != null
                    && node.getLeftChild().isDummy()) {
                node.setLeftChild(null);
            }
            if (node.getRightChild() != null
                    && node.getRightChild().isDummy()) {
                node.setRightChild(null);
            }
        }
    }

    @Override
    void setNodeChildren() {
        Map<Long, BinaryWalkerNode> nodes = getNodes();
        for (Entry<Long, BinaryWalkerNode> entry : nodes.entrySet()) {
            BinaryWalkerNode node = entry.getValue();
            long leftChildId = 2 * entry.getKey();
            long rightChildId = leftChildId + 1;
            if (nodes.containsKey(leftChildId)) {
                BinaryWalkerNode leftChild = nodes.get(leftChildId);
                leftChild.setParent(entry.getValue());
                node.setLeftChild(leftChild);
            }
            if (nodes.containsKey(rightChildId)) {
                BinaryWalkerNode rightChild = nodes.get(rightChildId);
                rightChild.setParent(entry.getValue());
                node.setRightChild(rightChild);
            }
        }
    }

    @Override
    BinaryWalkerNode createWalkerNode() {
        return new BinaryWalkerNode();
    }
}
