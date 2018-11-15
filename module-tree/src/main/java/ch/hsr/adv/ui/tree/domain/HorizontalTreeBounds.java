package ch.hsr.adv.ui.tree.domain;

/**
 * Contains the horizontal bounds of a tree
 */
public class HorizontalTreeBounds {

    private double leftBound;
    private double rightBound;

    public HorizontalTreeBounds(double leftBound, double rightBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    public double getLeftBound() {
        return leftBound;
    }

    public double getRightBound() {
        return rightBound;
    }

    /**
     * updates the tree-bounds that x is between the bounds
     * @param x horizontal position
     */
    public void update(double x) {
        if (x > rightBound) {
            rightBound = x;
        }
        if (x < leftBound) {
            leftBound = x;
        }
    }
}
