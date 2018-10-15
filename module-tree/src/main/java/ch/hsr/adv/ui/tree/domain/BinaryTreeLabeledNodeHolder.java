package ch.hsr.adv.ui.tree.domain;

import ch.hsr.adv.ui.tree.presentation.widgets.IndexedNode;

/**
 * Holds indexed labeled tree node and the references to the children
 */
public class BinaryTreeLabeledNodeHolder {
    private IndexedNode indexedNode;
    private BinaryTreeLabeledNodeHolder leftNode;
    private BinaryTreeLabeledNodeHolder rightNode;

    public BinaryTreeLabeledNodeHolder(IndexedNode indexedNode) {
        this.indexedNode = indexedNode;
    }

    public IndexedNode getNode() {
        return indexedNode;
    }

    public BinaryTreeLabeledNodeHolder getLeftNode() {
        return leftNode;
    }

    public BinaryTreeLabeledNodeHolder getRightNode() {
        return rightNode;
    }

    public void setLeftNode(BinaryTreeLabeledNodeHolder leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(BinaryTreeLabeledNodeHolder rightNode) {
        this.rightNode = rightNode;
    }
}
