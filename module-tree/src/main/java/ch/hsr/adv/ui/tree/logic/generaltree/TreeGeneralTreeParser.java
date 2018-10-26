package ch.hsr.adv.ui.tree.logic.generaltree;

import ch.hsr.adv.commons.core.access.GsonProvider;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.logic.Parser;
import ch.hsr.adv.ui.tree.logic.TreeParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Parses a json representation of a general-tree to a ModuleGroup.
 */
@Singleton
@Module(ConstantsTree.MODULE_NAME_GENERAL_TREE)
public class TreeGeneralTreeParser extends TreeParser implements Parser {

    /**
     * Registers general-tree specific types to the GsonBuilder
     *
     * @param gsonProvider preconfigured gson builder
     */
    @Inject
    public TreeGeneralTreeParser(GsonProvider gsonProvider) {
        super(gsonProvider);
    }
}
