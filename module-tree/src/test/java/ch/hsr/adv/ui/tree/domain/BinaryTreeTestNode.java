package ch.hsr.adv.ui.tree.domain;

import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.tree.logic.domain.ADVBinaryTreeNode;

public class BinaryTreeTestNode<T> implements ADVBinaryTreeNode<T> {

    private ADVBinaryTreeNode<T> leftChild;
    private ADVBinaryTreeNode<T> rightChild;
    private ADVStyle style;
    private T content;

    public BinaryTreeTestNode(T content, ADVStyle style,
                              ADVBinaryTreeNode<T> leftChild,
                              ADVBinaryTreeNode<T> rightChild) {
        this(content, style);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public BinaryTreeTestNode(T content, ADVStyle style) {
        this(content);
        this.style = style;
    }

    public BinaryTreeTestNode(T content) {
        this.content = content;
    }

    @Override
    public ADVBinaryTreeNode<T> getLeftChild() {
        return leftChild;
    }

    @Override
    public ADVBinaryTreeNode<T> getRightChild() {
        return rightChild;
    }

    @Override
    public ADVStyle getStyle() {
        return style;
    }

    @Override
    public T getContent() {
        return content;
    }
}
