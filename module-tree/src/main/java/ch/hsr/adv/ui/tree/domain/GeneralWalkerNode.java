package ch.hsr.adv.ui.tree.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains variables for a general-tree-vertex in the WalkerTreeAlgorithm
 */
public class GeneralWalkerNode extends WalkerNode {

    private List<GeneralWalkerNode> children;

    public GeneralWalkerNode() {
        super();
        children = new ArrayList<>();
    }

    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public List<GeneralWalkerNode> getChildren() {
        return children;
    }

    /**
     * adds a new child to the node
     *
     * @param child new child
     */
    public void addChild(GeneralWalkerNode child) {
        children.add(child);
    }
}
