package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.tree.domain.BinaryWalkerNode;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.WalkerTreeAlgorithm;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Creates JavaFX Nodes for the tree elements and adds them to a pane
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_BINARY_TREE)
public class TreeBinaryTreeLayouter extends TreeLayouterBase
        implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            TreeBinaryTreeLayouter.class);

    private static final double CHILD_OFFSET =
            WalkerTreeAlgorithm.HORIZONTAL_DISTANCE / 2;

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        logger.info("Layouting binary-tree snapshot...");

        boolean showIndex = flags != null
                && flags.contains(ConstantsTree.SHOW_ARRAY_INDICES);

        initializeLayouting(moduleGroup);
        adjustBinaryTreeNodePositions();
        positionNodes();
        return generatePane(showIndex);
    }

    private void adjustBinaryTreeNodePositions() {
        for (WalkerNode walkerNode : getNodes().values()) {
            BinaryWalkerNode node = (BinaryWalkerNode) walkerNode;
            if (node.getLeftChild() != null && node.getRightChild() == null) {
                node.addMod(-CHILD_OFFSET);
            }
            if (node.getRightChild() != null && node.getLeftChild() == null) {
                node.addMod(CHILD_OFFSET);
            }
        }
    }

    @Override
    void setNodeChildren() {
        Map<Long, WalkerNode> nodes = getNodes();
        for (Entry<Long, WalkerNode> entry : nodes.entrySet()) {
            BinaryWalkerNode node = (BinaryWalkerNode) entry.getValue();
            long leftChildId = 2 * entry.getKey();
            long rightChildId = leftChildId + 1;
            if (nodes.containsKey(leftChildId)) {
                BinaryWalkerNode leftChild =
                        (BinaryWalkerNode) nodes.get(leftChildId);
                leftChild.setParent(entry.getValue());
                node.setLeftChild(leftChild);
            }
            if (nodes.containsKey(rightChildId)) {
                BinaryWalkerNode rightChild =
                        (BinaryWalkerNode) nodes.get(rightChildId);
                rightChild.setParent(entry.getValue());
                node.setRightChild(rightChild);
            }
        }
    }

    @Override
    WalkerNode createWalkerNode() {
        return new BinaryWalkerNode();
    }
}
