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

import java.util.List;

/**
 * Creates JavaFX Nodes for the tree elements and adds them to a pane
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_GENERAL_TREE)
public class TreeGeneralTreeLayouter extends TreeLayouterBase
        implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            TreeGeneralTreeLayouter.class);

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        logger.info("Layouting general-tree snapshot...");

        initializeLayouting(moduleGroup);
        positionNodes();
        return generatePane(false);
    }

    @Override
    void setNodeChildren() {
        for (TreeNodeRelation relation : getRelations()) {
            GeneralWalkerNode parent = (GeneralWalkerNode) getNodes()
                    .get(relation.getSourceElementId());
            GeneralWalkerNode child = (GeneralWalkerNode) getNodes()
                    .get(relation.getTargetElementId());
            child.setParent(parent);
            parent.addChild(child);
        }
    }

    @Override
    WalkerNode createWalkerNode() {
        return new GeneralWalkerNode();
    }
}
