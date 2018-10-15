package ch.hsr.adv.ui.tree.domain;

import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;

/**
 * Holds labeled tree node and the references to the children
 */
public class BinaryTreeLabeledNodeHolder {
    private LabeledNode labeledNode;
    private BinaryTreeLabeledNodeHolder leftNode;
    private BinaryTreeLabeledNodeHolder rightNode;

    public BinaryTreeLabeledNodeHolder(LabeledNode labeledEdge) {
        this.labeledNode = labeledEdge;
    }

    public LabeledNode getNode() {
        return labeledNode;
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
