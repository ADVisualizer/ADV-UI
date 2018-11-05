package ch.hsr.adv.ui.tree.logic.collectiontree;

import ch.hsr.adv.commons.core.access.GsonProvider;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.tree.logic.TreeStringifyer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Serializes a collection-tree ModuleGroup to json
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_COLLECTION_TREE)
public class TreeCollectionTreeStringifyer extends TreeStringifyer
        implements Stringifyer {

    @Inject
    public TreeCollectionTreeStringifyer(GsonProvider gsonProvider) {
        super(gsonProvider);
    }

}
