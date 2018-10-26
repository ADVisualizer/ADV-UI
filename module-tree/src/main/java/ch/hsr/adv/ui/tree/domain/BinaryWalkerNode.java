package ch.hsr.adv.ui.tree.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains variables for a binary-tree-vertex in the WalkerTreeAlgorithm
 */
public class BinaryWalkerNode extends WalkerNode {

    private BinaryWalkerNode leftChild;
    private BinaryWalkerNode rightChild;

    public BinaryWalkerNode getLeftChild() {
        return leftChild;
    }

    public BinaryWalkerNode getRightChild() {
        return rightChild;
    }

    public void setLeftChild(BinaryWalkerNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(BinaryWalkerNode rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public boolean isLeaf() {
        return getLeftChild() == null && getRightChild() == null;
    }

    @Override
    public List<BinaryWalkerNode> getChildren() {
        ArrayList<BinaryWalkerNode> children = new ArrayList<>();
        if (leftChild != null) {
            children.add(leftChild);
        }
        if (rightChild != null) {
            children.add(rightChild);
        }
        return children;
    }
}
