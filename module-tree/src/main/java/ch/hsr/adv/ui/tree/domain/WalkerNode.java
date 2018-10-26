package ch.hsr.adv.ui.tree.domain;

import java.util.List;

/**
 * Contains the common variables for a vertex in the WalkerTreeAlgorithm
 */
public abstract class WalkerNode {

    private double centerX;
    private double centerY;

    private int childNumber;
    private double mod;
    private double preliminary;
    private double change;
    private double shift;
    private WalkerNode thread;
    private WalkerNode parent;
    private WalkerNode ancestor;

    WalkerNode() {
        childNumber = -1;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public int getChildNumber() {
        return childNumber;
    }

    public void setChildNumber(int childNumber) {
        this.childNumber = childNumber;
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

    public WalkerNode getThread() {
        return thread;
    }

    public void setThread(WalkerNode thread) {
        this.thread = thread;
    }

    public WalkerNode getParent() {
        return parent;
    }

    public void setParent(WalkerNode parent) {
        this.parent = parent;
    }

    public WalkerNode getAncestor() {
        return ancestor;
    }

    public void setAncestor(WalkerNode ancestor) {
        this.ancestor = ancestor;
    }

    /**
     * @return true if vertex is a leaf-node
     */
    public abstract boolean isLeaf();

    /**
     * @return list of the children of the node
     */
    public abstract List<? extends WalkerNode> getChildren();
}
