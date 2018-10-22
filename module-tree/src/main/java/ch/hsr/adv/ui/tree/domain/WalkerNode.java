package ch.hsr.adv.ui.tree.domain;

import ch.hsr.adv.ui.tree.presentation.widgets.IndexedNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds indexed labeled tree node and the references to the children
 */
public class WalkerNode {

    private IndexedNode indexedNode;
    private WalkerNode leftChild;
    private WalkerNode rightChild;

    private double mod;
    private WalkerNode thread;
    private double preliminary;
    private double change;
    private double shift;
    private WalkerNode parent;
    private WalkerNode ancestor;
    private int childNumber;

    public WalkerNode(IndexedNode indexedNode) {
        this.indexedNode = indexedNode;
        childNumber = -1;
    }

    public IndexedNode getIndexedNode() {
        return indexedNode;
    }

    public double getMod() {
        return mod;
    }

    public void setMod(double mod) {
        this.mod = mod;
    }

    /**
     * adds value to current mod
     *
     * @param value value to add
     */
    public void addMod(double value) {
        mod += value;
    }

    public WalkerNode getThread() {
        return thread;
    }

    public void setThread(WalkerNode thread) {
        this.thread = thread;
    }

    public double getPreliminary() {
        return preliminary;
    }

    public void setPreliminary(double preliminary) {
        this.preliminary = preliminary;
    }

    /**
     * adds value to current preliminary
     *
     * @param value value to add
     */
    public void addPreliminary(double value) {
        preliminary += value;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getShift() {
        return shift;
    }

    public void setShift(double shift) {
        this.shift = shift;
    }

    public WalkerNode getAncestor() {
        return ancestor;
    }

    public void setAncestor(WalkerNode ancestor) {
        this.ancestor = ancestor;
    }

    public int getChildNumber() {
        return childNumber;
    }

    public void setChildNumber(int childNumber) {
        this.childNumber = childNumber;
    }

    public IndexedNode getNode() {
        return indexedNode;
    }

    public WalkerNode getLeftChild() {
        return leftChild;
    }

    public WalkerNode getRightChild() {
        return rightChild;
    }

    public void setLeftChild(WalkerNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(WalkerNode rightChild) {
        this.rightChild = rightChild;
    }

    public boolean isLeaf() {
        return getLeftChild() == null && getRightChild() == null;
    }

    /**
     * returns a list with all children that are not null
     *
     * @return list with children
     */
    public List<WalkerNode> getChildren() {
        ArrayList<WalkerNode> children = new ArrayList<>();
        if (leftChild != null) {
            children.add(leftChild);
        }
        if (rightChild != null) {
            children.add(rightChild);
        }
        return children;
    }

    public WalkerNode getParent() {
        return parent;
    }

    public void setParent(WalkerNode parent) {
        this.parent = parent;
    }

    /**
     * sets the center position of the indexed node
     *
     * @param x horizontal position
     * @param y vertical position
     */
    public void setCenterPosition(int x, int y) {
        indexedNode.setCenterX(x);
        indexedNode.setCenterY(y);
    }
}
