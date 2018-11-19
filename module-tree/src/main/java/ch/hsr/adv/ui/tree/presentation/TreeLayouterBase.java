package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ADVRelation;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultElementStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultRelationStyle;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.ConnectorType;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import ch.hsr.adv.ui.tree.logic.WalkerTreeAlgorithm;
import ch.hsr.adv.ui.core.presentation.widgets.IndexedNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Contains general fields and methods for layouting trees
 *
 * @param <T> type of WalkerNode
 */
abstract class TreeLayouterBase<T extends WalkerNode> implements Layouter {
    private static final long DEFAULT_ROOT_ID = 1L;
    private static final boolean ROUNDED_CORNER_STYLE = true;
    private static final double TREE_DISTANCE_FACTOR = 1.0;
    private static final int VERTEX_DISTANCE_HORIZONTAL = 75;
    private static final int VERTEX_DISTANCE_VERTICAL = 75;
    private static final int INDEX_WIDTH = 25;

    private Map<Long, T> nodes;
    private List<TreeNodeElement> elements;
    private List<TreeNodeRelation> relations;

    Map<Long, T> getNodes() {
        return nodes;
    }

    List<TreeNodeRelation> getRelations() {
        return relations;
    }

    /**
     * Prepares TreeLayouter for layout of new tree
     *
     * @param moduleGroup contains new tree-elements and relations
     */
    void initializeLayouting(ModuleGroup moduleGroup) {
        nodes = new TreeMap<>();
        elements = new ArrayList<>();
        relations = new ArrayList<>();
        for (ADVElement<?> element : moduleGroup.getElements()) {
            elements.add((TreeNodeElement) element);
        }
        for (ADVRelation<?> relation : moduleGroup.getRelations()) {
            relations.add((TreeNodeRelation) relation);
        }
        translateModuleGroupToTree();
    }

    /**
     * Positions the tree-vertices using the WalkerTreeAlgorithm
     */
    void positionNodes() {
        WalkerTreeAlgorithm algorithm = new WalkerTreeAlgorithm(
                nodes.get(DEFAULT_ROOT_ID));
        algorithm.positionNodes();
    }

    /**
     * Positions multiple trees using the WalkerTreeAlgorithm
     *
     * @param roots roots of the trees
     */
    void positionNodes(List<WalkerNode> roots) {
        List<WalkerTreeAlgorithm> trees = new ArrayList<>();
        for (WalkerNode root : roots) {
            WalkerTreeAlgorithm algorithm = new WalkerTreeAlgorithm(root);
            algorithm.positionNodes();
            trees.add(algorithm);
        }
        positionTrees(trees);
    }

    private void positionTrees(List<WalkerTreeAlgorithm> trees) {
        if (trees.size() <= 0) {
            return;
        }
        double moveDistance = trees.get(0).getHorizontalBounds()
                .getRightBound();
        for (int i = 1; i < trees.size(); i++) {
            WalkerTreeAlgorithm tree = trees.get(i);
            moveDistance += -tree.getHorizontalBounds().getLeftBound()
                    + TREE_DISTANCE_FACTOR;
            moveNodeHorizontally(tree.getRoot(), moveDistance);
            moveDistance += tree.getHorizontalBounds().getRightBound();
        }
    }

    private void moveNodeHorizontally(WalkerNode node, double distance) {
        node.setCenterX(node.getCenterX() + distance);
        for (WalkerNode child : node.getChildren()) {
            moveNodeHorizontally(child, distance);
        }
    }

    /**
     * creates new AutoScalePane and adds the vertices and relations of the tree
     *
     * @param showIndex decides if index is shown in IndexedNodes
     * @return AutoScalePane with vertices and relations
     */
    AutoScalePane generatePane(boolean showIndex) {
        AutoScalePane pane = new AutoScalePane();
        Map<Long, IndexedNode> indexedNodes = createIndexedNodes(showIndex);
        addVerticesToPane(pane, indexedNodes, showIndex);
        addRelationsToPane(pane, indexedNodes);
        return pane;
    }

    /**
     * adds the correct children to the nodes
     */
    abstract void setNodeChildren();

    /**
     * Creates a new node for the WalkerTreeAlgorithm
     *
     * @return WalkerNode for the specific tree-type
     */
    abstract T createWalkerNode();

    private void translateModuleGroupToTree() {
        for (TreeNodeElement element : elements) {
            nodes.put(element.getId(), createWalkerNode());
        }
        setNodeChildren();
    }

    private Map<Long, IndexedNode> createIndexedNodes(boolean showIndex) {
        Map<Long, IndexedNode> indexedNodes = new TreeMap<>();
        for (TreeNodeElement element : elements) {
            long index = element.getId();
            ADVStyle style = element.getStyle();
            if (style == null) {
                style = new ADVDefaultElementStyle();
            }
            IndexedNode indexedNode = new IndexedNode(index, element
                    .getContent(), style, ROUNDED_CORNER_STYLE, showIndex);
            indexedNodes.put(index, indexedNode);
        }
        return indexedNodes;
    }

    private void addVerticesToPane(AutoScalePane pane,
                                   Map<Long, IndexedNode> indexedNodes,
                                   boolean showIndex) {
        for (Map.Entry<Long, T> entry : nodes.entrySet()) {
            IndexedNode indexedNode = indexedNodes.get(entry.getKey());
            WalkerNode node = entry.getValue();

            double horizontalDistance = VERTEX_DISTANCE_HORIZONTAL;

            if (showIndex) {
                horizontalDistance += INDEX_WIDTH;
            }

            int x = (int) (node.getCenterX() * horizontalDistance);
            int y = (int) (node.getCenterY() * VERTEX_DISTANCE_VERTICAL);

            indexedNode.setCenterX(x);
            indexedNode.setCenterY(y);
            pane.addChildren(indexedNode);
        }
    }

    private void addRelationsToPane(AutoScalePane pane,
                                    Map<Long, IndexedNode> indexedNodes) {
        for (TreeNodeRelation relation : relations) {
            IndexedNode src = indexedNodes.get(relation.getSourceElementId());
            IndexedNode tgt = indexedNodes.get(relation.getTargetElementId());
            ADVStyle style = relation.getStyle();
            if (style == null) {
                style = new ADVDefaultRelationStyle();
            }
            LabeledEdge labeledRelation = new LabeledEdge(
                    relation.getLabel(), src.getLabeledNode(),
                    ConnectorType.BOTTOM, tgt.getLabeledNode(),
                    ConnectorType.TOP, style);
            pane.addChildren(labeledRelation);
        }
    }
}
