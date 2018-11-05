package ch.hsr.adv.ui.tree.logic.collectiontree;

import ch.hsr.adv.commons.core.access.GsonProvider;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.tree.logic.TreeParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Parses a json representation of a collection-tree to a ModuleGroup.
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_COLLECTION_TREE)
public class TreeCollectionTreeParser extends TreeParser implements Parser {

    /**
     * Registers collection-tree specific types to the GsonBuilder
     *
     * @param gsonProvider preconfigured gson builder
     */
    @Inject
    public TreeCollectionTreeParser(GsonProvider gsonProvider) {
        super(gsonProvider);
    }
}
