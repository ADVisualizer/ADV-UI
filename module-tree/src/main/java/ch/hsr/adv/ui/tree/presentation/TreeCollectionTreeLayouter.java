package ch.hsr.adv.ui.tree.presentation;

import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.tree.domain.GeneralWalkerNode;
import ch.hsr.adv.ui.tree.domain.WalkerNode;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates JavaFX Nodes for the tree elements and adds them to a pane
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_COLLECTION_TREE)
public class TreeCollectionTreeLayouter
        extends TreeLayouterBase<GeneralWalkerNode> implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            TreeCollectionTreeLayouter.class);

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {

        logger.info("Layouting collection-tree snapshot...");

        initializeLayouting(moduleGroup);
        positionNodes(findRoots());
        return generatePane(false);
    }

    @Override
    void setNodeChildren() {
        for (TreeNodeRelation relation : getRelations()) {
            GeneralWalkerNode parent = getNodes()
                    .get(relation.getSourceElementId());
            GeneralWalkerNode child = getNodes()
                    .get(relation.getTargetElementId());
            child.setParent(parent);
            parent.addChild(child);
        }
    }

    @Override
    GeneralWalkerNode createWalkerNode() {
        return new GeneralWalkerNode();
    }

    private List<WalkerNode> findRoots() {
        List<WalkerNode> roots = new ArrayList<>();

        for (WalkerNode node : getNodes().values()) {
            if (node.getParent() == null) {
                roots.add(node);
            }
        }

        return roots;
    }
}
