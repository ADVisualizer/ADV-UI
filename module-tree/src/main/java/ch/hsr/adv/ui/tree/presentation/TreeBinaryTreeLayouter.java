package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.tree.domain.BinaryWalkerNode;
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
public class TreeBinaryTreeLayouter extends TreeLayouterBase<BinaryWalkerNode>
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
        for (BinaryWalkerNode node : getNodes().values()) {
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
