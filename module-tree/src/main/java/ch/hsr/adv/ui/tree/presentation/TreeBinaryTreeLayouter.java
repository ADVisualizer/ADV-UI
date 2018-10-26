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
import ch.hsr.adv.ui.tree.domain.BinaryWalkerNode;
import ch.hsr.adv.ui.tree.logic.WalkerTreeAlgorithm;
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
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_BINARY_TREE)
public class TreeBinaryTreeLayouter implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            TreeBinaryTreeLayouter.class);

    private static final long ROOT_ID = 1L;
    private static final int CHILD_OFFSET = 50;
    private static final int VERTEX_DISTANCE_HORIZONTAL = 75;
    private static final int VERTEX_DISTANCE_VERTICAL = 75;
    private static final int INDEX_WIDTH = 25;
    private AutoScalePane scalePane;
    private Map<Long, BinaryWalkerNode> nodes;
    private Map<Long, IndexedNode> indexedNodes;
    private boolean showIndex;

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        logger.info("Layouting binary-tree snapshot...");

        showIndex = flags != null
                && flags.contains(ConstantsTree.SHOW_ARRAY_INDICES);

        scalePane = new AutoScalePane();
        nodes = new TreeMap<>();
        indexedNodes = new TreeMap<>();
        translateModuleGroupToTree(moduleGroup);
        int horizontalDistance = VERTEX_DISTANCE_HORIZONTAL;
        if (showIndex) {
            horizontalDistance += INDEX_WIDTH;
        }
        adjustBinaryTreeNodePositions();
        WalkerTreeAlgorithm positionAlgorithm = new WalkerTreeAlgorithm(
                getRoot(), horizontalDistance, VERTEX_DISTANCE_VERTICAL);
        positionAlgorithm.positionNodes();
        addVerticesToPane();
        addRelationsToPane(moduleGroup);
        return scalePane;
    }

    private void adjustBinaryTreeNodePositions() {
        for (BinaryWalkerNode node : nodes.values()) {
            if (node.getLeftChild() != null && node.getRightChild() == null) {
                node.addMod(-CHILD_OFFSET);
            }
            if (node.getRightChild() != null && node.getLeftChild() == null) {
                node.addMod(CHILD_OFFSET);
            }
        }
    }

    private void translateModuleGroupToTree(ModuleGroup moduleGroup) {
        for (ADVElement<?> element : moduleGroup.getElements()) {
            TreeNodeElement node = (TreeNodeElement) element;
            ADVStyle nodeStyle = node.getStyle();
            if (nodeStyle == null) {
                nodeStyle = new ADVDefaultElementStyle();
            }
            IndexedNode indexedNode = new IndexedNode(node.getId(),
                    node.getContent(), nodeStyle, true,
                    showIndex);
            indexedNodes.put(node.getId(), indexedNode);
            nodes.put(node.getId(), new BinaryWalkerNode());
        }
        setNodeChildren();
    }

    private void setNodeChildren() {
        for (Entry<Long, BinaryWalkerNode> entry
                : nodes.entrySet()) {
            long leftChildId = 2 * entry.getKey();
            long rightChildId = leftChildId + 1;
            if (nodes.containsKey(leftChildId)) {
                nodes.get(leftChildId).setParent(entry.getValue());
                entry.getValue().setLeftChild(nodes.get(leftChildId));
            }
            if (nodes.containsKey(rightChildId)) {
                nodes.get(rightChildId).setParent(entry.getValue());
                entry.getValue().setRightChild(nodes.get(rightChildId));
            }
        }
    }

    private BinaryWalkerNode getRoot() {
        return nodes.get(ROOT_ID);
    }

    private void addVerticesToPane() {
        for (Entry<Long, BinaryWalkerNode> entry : nodes.entrySet()) {
            IndexedNode indexedNode = indexedNodes.get(entry.getKey());
            indexedNode.setCenterX((int) entry.getValue().getCenterX());
            indexedNode.setCenterY((int) entry.getValue().getCenterY());
            scalePane.addChildren(indexedNode);
        }
    }

    private void addRelationsToPane(ModuleGroup moduleGroup) {
        for (ADVRelation<?> rel : moduleGroup.getRelations()) {
            TreeNodeRelation relation = (TreeNodeRelation) rel;
            ADVStyle relationStyle = rel.getStyle();

            if (relationStyle == null) {
                relationStyle = new ADVDefaultRelationStyle();
            }

            LabeledEdge labeledRelation = new LabeledEdge(
                    relation.getLabel(),
                    indexedNodes.get(relation.getSourceElementId())
                            .getLabeledNode(),
                    ConnectorType.BOTTOM,
                    indexedNodes.get(relation.getTargetElementId())
                            .getLabeledNode(),
                    ConnectorType.TOP,
                    relationStyle);
            scalePane.addChildren(labeledRelation);
        }
    }

    /**
     * Returns all node-holders
     *
     * @return map with all holders and their node-id
     */
    Map<Long, BinaryWalkerNode> getNodes() {
        return nodes;
    }
}
