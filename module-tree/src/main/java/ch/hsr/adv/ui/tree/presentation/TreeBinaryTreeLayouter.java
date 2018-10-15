package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ADVRelation;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultElementStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultRelationStyle;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.ConnectorType;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.BinaryTreeLabeledNodeHolder;
import ch.hsr.adv.ui.tree.presentation.widgets.IndexedNode;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Creates JavaFX Nodes for the tree elements and adds them to a pane
 * <p>
 * This class suppresses rawtype warnings, because Gson does not support
 * generic wildcards. See
 * <a href="https://github.com/ADVisualizer/ADV-Lib/issues/31">Issue 31</a>
 * for more details.
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_BINARY_TREE)
public class TreeBinaryTreeLayouter implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            TreeBinaryTreeLayouter.class);

    private static final long ROOT_ID = 1L;
    private static final int ROOT_POSITION_X = 0;
    private static final int ROOT_POSITION_Y = 0;
    private static final int NODE_DISTANCE_HORIZONTAL = 50;
    private static final int NODE_DISTANCE_VERTICAL = 75;
    private AutoScalePane scalePane;
    private Map<Long, BinaryTreeLabeledNodeHolder> nodes;
    private boolean showIndex;

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        logger.info("Layouting graph snapshot...");

        if (flags != null && flags.contains(ConstantsTree.SHOW_ARRAY_INDICES)) {
            showIndex = true;
        }

        scalePane = new AutoScalePane();
        nodes = new TreeMap<>();
        putElementsToMap(moduleGroup);
        setNodeChildren();
        addNodesToPane(ROOT_ID, ROOT_POSITION_X, ROOT_POSITION_Y);
        addRelationsToPane(moduleGroup);
        return scalePane;
    }

    private void addNodesToPane(long id, int xPos, int yPos) {
        long leftChildId = 2 * id;
        long rightChildId = leftChildId + 1;

        BinaryTreeLabeledNodeHolder holder = nodes.get(id);

        if (holder != null) {
            int height = getTreeHeight(id);
            int offset = (int) Math.pow(2, height - 1);
            IndexedNode indexedNode = holder.getNode();
            indexedNode.setX(xPos);
            indexedNode.setY(yPos);
            scalePane.addChildren(indexedNode);
            addNodesToPane(leftChildId,
                    xPos - NODE_DISTANCE_HORIZONTAL * offset,
                    yPos + NODE_DISTANCE_VERTICAL);
            addNodesToPane(rightChildId,
                    xPos + NODE_DISTANCE_HORIZONTAL * offset,
                    yPos + NODE_DISTANCE_VERTICAL);
        }
    }

    @SuppressWarnings("rawtypes")
    private void addRelationsToPane(ModuleGroup moduleGroup) {
        for (ADVRelation rel : moduleGroup.getRelations()) {
            TreeNodeRelation relation = (TreeNodeRelation) rel;
            ADVStyle relationStyle = rel.getStyle();

            if (relationStyle == null) {
                relationStyle = new ADVDefaultRelationStyle();
            }

            LabeledEdge labeledRelation = new LabeledEdge(
                    relation.getLabel(),
                    nodes.get(relation.getSourceElementId()).getNode()
                            .getLabeledNode(),
                    ConnectorType.BOTTOM,
                    nodes.get(relation.getTargetElementId()).getNode()
                            .getLabeledNode(),
                    ConnectorType.TOP,
                    relationStyle);
            scalePane.addChildren(labeledRelation);
        }
    }

    @SuppressWarnings("rawtypes")
    private void putElementsToMap(ModuleGroup moduleGroup) {
        for (ADVElement element : moduleGroup.getElements()) {
            TreeNodeElement node = (TreeNodeElement) element;
            ADVStyle nodeStyle = node.getStyle();
            if (nodeStyle == null) {
                nodeStyle = new ADVDefaultElementStyle();
            }
            IndexedNode indexedNode = new IndexedNode(node.getId(),
                    node.getContent(), nodeStyle, true, showIndex);
            nodes.put(node.getId(),
                    new BinaryTreeLabeledNodeHolder(indexedNode));
        }
    }

    private void setNodeChildren() {
        for (Entry<Long, BinaryTreeLabeledNodeHolder> entry : nodes
                .entrySet()) {
            long leftChildId = 2 * entry.getKey();
            long rightChildId = leftChildId + 1;
            entry.getValue().setLeftNode(nodes.get(leftChildId));
            entry.getValue().setRightNode(nodes.get(rightChildId));
        }
    }

    /**
     * Returns the height of the tree for a given node
     *
     * @param id node-id
     * @return height of tree for node
     */
    protected int getTreeHeight(long id) {
        BinaryTreeLabeledNodeHolder root = nodes.get(id);
        if (root == null) {
            return -1;
        }

        long leftChildId = 2 * id;
        long rightChildId = leftChildId + 1;
        return 1 + Math.max(getTreeHeight(leftChildId),
                getTreeHeight(rightChildId));
    }

    /**
     * Returns all node-holders
     *
     * @return map with all holders and their node-id
     */
    protected Map<Long, BinaryTreeLabeledNodeHolder> getNodes() {
        return nodes;
    }
}
