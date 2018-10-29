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
import ch.hsr.adv.ui.tree.domain.GeneralWalkerNode;
import ch.hsr.adv.ui.tree.logic.WalkerTreeAlgorithm;
import ch.hsr.adv.ui.tree.presentation.widgets.IndexedNode;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Creates JavaFX Nodes for the tree elements and adds them to a pane
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_GENERAL_TREE)
public class TreeGeneralTreeLayouter implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            TreeGeneralTreeLayouter.class);

    private static final long ROOT_ID = 1L;
    private static final int VERTEX_DISTANCE_HORIZONTAL = 75;
    private static final int VERTEX_DISTANCE_VERTICAL = 75;
    private AutoScalePane scalePane;
    private Map<Long, GeneralWalkerNode> nodes;
    private Map<Long, IndexedNode> indexedNodes;

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        logger.info("Layouting general-tree snapshot...");

        scalePane = new AutoScalePane();
        nodes = new TreeMap<>();
        indexedNodes = new TreeMap<>();
        translateModuleGroupToTree(moduleGroup);
        WalkerTreeAlgorithm positionAlgorithm = new WalkerTreeAlgorithm(
                getRoot(), VERTEX_DISTANCE_HORIZONTAL,
                VERTEX_DISTANCE_VERTICAL);
        positionAlgorithm.positionNodes();
        addVerticesToPane();
        addRelationsToPane(moduleGroup);
        return scalePane;
    }

    private void translateModuleGroupToTree(ModuleGroup moduleGroup) {
        for (ADVElement<?> element : moduleGroup.getElements()) {
            TreeNodeElement node = (TreeNodeElement) element;
            ADVStyle nodeStyle = node.getStyle();
            if (nodeStyle == null) {
                nodeStyle = new ADVDefaultElementStyle();
            }
            IndexedNode indexedNode = new IndexedNode(node.getId(),
                    node.getContent(), nodeStyle, true, false);
            indexedNodes.put(node.getId(), indexedNode);
            nodes.put(node.getId(), new GeneralWalkerNode());
        }
        setNodeChildren(moduleGroup);
    }

    private void setNodeChildren(ModuleGroup moduleGroup) {
        for (ADVRelation<?> rel : moduleGroup.getRelations()) {
            TreeNodeRelation relation = (TreeNodeRelation) rel;
            GeneralWalkerNode parent = nodes.get(relation.getSourceElementId());
            GeneralWalkerNode child = nodes.get(relation.getTargetElementId());
            parent.addChild(child);
        }
    }

    private GeneralWalkerNode getRoot() {
        return nodes.get(ROOT_ID);
    }

    private void addVerticesToPane() {
        for (Map.Entry<Long, GeneralWalkerNode> entry : nodes.entrySet()) {
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
    Map<Long, GeneralWalkerNode> getNodes() {
        return nodes;
    }
}
