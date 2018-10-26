package ch.hsr.adv.ui.tree.logic.binarytree;

import ch.hsr.adv.commons.core.access.GsonProvider;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.tree.logic.TreeParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Parses a json representation of a binary-tree to a ModuleGroup.
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_BINARY_TREE)
public class TreeBinaryTreeParser extends TreeParser implements Parser {

    /**
     * Registers binary-tree specific types to the GsonBuilder
     *
     * @param gsonProvider preconfigured gson builder
     */
    @Inject
    public TreeBinaryTreeParser(GsonProvider gsonProvider) {
        super(gsonProvider);
    }
}
