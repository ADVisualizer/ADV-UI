package ch.hsr.adv.ui.tree.logic;

import ch.hsr.adv.ui.tree.domain.WalkerNode;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;

/**
 * Layouts a tree according to the Reingold-Tilford Algorithm which was improved
 * by Walker et al. to accommodate for n-ary trees and by Buchheim et al. to
 * still run in linear time.
 */
public class WalkerTreeAlgorithm {

    private final int verticalDistance;
    private final int horizontalDistance;
    private WalkerNode root;

    public WalkerTreeAlgorithm(WalkerNode root, int horizontalDistance,
                               int verticalDistance) {
        this.root = root;
        this.horizontalDistance = horizontalDistance;
        this.verticalDistance = verticalDistance;
    }

    /**
     * uses improved Walker-algorithm to position nodes of tree
     */
    public void positionNodes() {
        firstWalk(root, null);
        secondWalk(root, -root.getPreliminary(), 0, 0);
    }

    /**
     * Bottom-up traversal of the tree. The position of each node is
     * preliminary.
     *
     * @param v vertex v
     */
    private void firstWalk(WalkerNode v, WalkerNode leftSibling) {
        if (v.isLeaf()) {
            if (leftSibling != null) {
                v.setPreliminary(leftSibling.getPreliminary()
                        + horizontalDistance);
            }
        } else {
            WalkerNode defaultAncestor = v.getChildren().get(0);
            WalkerNode previousChild = null;
            int childNumber = 0;
            for (WalkerNode child : v.getChildren()) {
                child.setChildNumber(++childNumber);
                firstWalk(child, previousChild);
                defaultAncestor = apportion(child, defaultAncestor,
                        previousChild, v);
                previousChild = child;
            }
            executeShifts(v);
            double midpoint = (v.getChildren().get(0).getPreliminary()
                    + v.getChildren().get(v.getChildren().size() - 1)
                    .getPreliminary()) / 2.0;

            if (leftSibling != null) {
                double preliminary = leftSibling.getPreliminary()
                        + horizontalDistance;
                v.setPreliminary(preliminary);
                v.setMod(preliminary - midpoint);
            } else {
                v.setPreliminary(midpoint);
            }
        }
    }

    /**
     * Top-down traversal of the tree. Compute all real positions in linear
     * time.
     * The real position of a vertex is its preliminary position plus the
     * aggregated modifier given by the sum of all modifiers on the path from
     * the parent of the vertex to the root.
     *
     * @param v      vertex v
     * @param modSum current modSum
     * @param depth  depth of the vertex
     * @param y      vertical position of v
     */
    private void secondWalk(WalkerNode v, double modSum, int depth, double y) {
        double x = v.getPreliminary() + modSum;
        v.setCenterX(x);
        v.setCenterY(y);

        if (!v.isLeaf()) {
            double nextDepthStart = y + verticalDistance;
            double nextModSum = modSum + v.getMod();
            for (WalkerNode child : v.getChildren()) {
                secondWalk(child, nextModSum, depth + 1, nextDepthStart);
            }
        }
    }

    /**
     * This method satisfies the 6th aesthetic property required to display
     * trees:
     * 6) The children of a node should be equally spaced.
     * <p>
     * Mechanism: each child of the current root is placed as close to the right
     * of its left sibling as possible. Traversing the contours of the subtree
     * then finds conflicting neighbours. Such conflicts are resolved by
     * shifting affected subtrees to the right.
     * <p>
     * Variables:<br>
     * insideRightVertex: vertex used for traversal along the inside contour of
     * the right subtree <br>
     * insideLeftVertex: vertex used for traversal along the inside contour
     * of the left subtree<br>
     * outsideRightVertex: vertex used for traversal along the outside
     * contour of the right subtree<br>
     * outsideLeftVertex: vertex used for traversal along the outside contour of
     * the left subtree<br>
     * associated modSum variables are used to summing up the modifiers along
     * the corresponding contour.
     *
     * @param v               vertex v
     * @param defaultAncestor default ancestor
     * @param leftSibling     left sibling of vertex
     * @param parentOfV       parent of vertex v
     * @return the (possibly changed) default ancestor
     */
    private WalkerNode apportion(WalkerNode v, WalkerNode defaultAncestor,
                                 WalkerNode leftSibling,
                                 WalkerNode parentOfV) {
        if (leftSibling == null) {
            return defaultAncestor;
        }
        WalkerNode outsideRightVertex = v;
        WalkerNode insideRightVertex = v;
        WalkerNode insideLeftVertex = leftSibling;
        WalkerNode outsideLeftVertex = parentOfV.getChildren().get(0);

        double outsideRightModSum = outsideRightVertex.getMod();
        double insideRightModSum = insideRightVertex.getMod();
        double insideLeftModSum = insideLeftVertex.getMod();
        double outsideLeftModSum = outsideLeftVertex.getMod();

        WalkerNode nextRight = nextRight(insideLeftVertex);
        WalkerNode nextLeft = nextLeft(insideRightVertex);

        while (nextRight != null && nextLeft != null) {
            insideLeftVertex = nextRight;
            insideRightVertex = nextLeft;
            outsideLeftVertex = nextLeft(outsideLeftVertex);
            outsideRightVertex = nextRight(outsideRightVertex);
            outsideRightVertex.setAncestor(v);
            double currentShift = calculateCurrentShift(insideRightVertex,
                    insideLeftVertex, insideRightModSum, insideLeftModSum);

            if (currentShift > 0) {
                WalkerNode ancestor = ancestor(insideLeftVertex, parentOfV,
                        defaultAncestor);
                moveSubtree(ancestor, v, currentShift);
                insideRightModSum += currentShift;
                outsideRightModSum += currentShift;
            }
            insideLeftModSum += insideLeftVertex.getMod();
            insideRightModSum += insideRightVertex.getMod();
            outsideLeftModSum += outsideLeftVertex.getMod();
            outsideRightModSum += outsideRightVertex.getMod();

            nextRight = nextRight(insideLeftVertex);
            nextLeft = nextLeft(insideRightVertex);
        }

        if (nextRight != null && nextRight(outsideRightVertex) == null) {
            outsideRightVertex.setThread(nextRight);
            outsideRightVertex.addMod(insideLeftModSum - outsideRightModSum);
        }

        if (nextLeft != null && nextLeft(outsideLeftVertex) == null) {
            outsideLeftVertex.setThread(nextLeft);
            outsideLeftVertex.addMod(insideRightModSum - outsideLeftModSum);
            defaultAncestor = v;
        }
        return defaultAncestor;
    }

    private double calculateCurrentShift(WalkerNode insideRightVertex,
                                         WalkerNode insideLeftVertex,
                                         double insideRightModSum,
                                         double insideLeftModSum) {
        return (insideLeftVertex.getPreliminary() + insideLeftModSum)
                - (insideRightVertex.getPreliminary() + insideRightModSum)
                + horizontalDistance;
    }

    /**
     * Shifts the current subtree of input vertex. When moving a subtree
     * rooted at
     * vertex, only its mod and preliminary x-coordinate are adjusted by the
     * amount of shifting.
     *
     * @param v vertex v
     */
    private void executeShifts(WalkerNode v) {
        double currentShift = 0;
        double currentChange = 0;
        for (WalkerNode w : getVertexChildrenReversed(v)) {
            currentChange += w.getChange();
            w.setPreliminary(w.getPreliminary() + currentShift);
            w.setMod(w.getMod() + currentShift);
            currentShift += w.getShift() + currentChange;
        }
    }

    private WalkerNode ancestor(WalkerNode insideLeftVertex, WalkerNode parent,
                                WalkerNode defaultAncestor) {
        WalkerNode ancestorVertex = insideLeftVertex.getAncestor();
        if (isChildOfParent(ancestorVertex, parent)) {
            return ancestorVertex;
        } else {
            return defaultAncestor;
        }
    }

    private boolean isChildOfParent(WalkerNode child, WalkerNode parent) {
        if (child == null) {
            return false;
        }
        return child.getParent().equals(parent);
    }

    private void moveSubtree(WalkerNode v, WalkerNode w, double currentShift) {
        int subtrees = w.getChildNumber() - v.getChildNumber();
        w.setChange(w.getChange() - currentShift / subtrees);
        w.setShift(w.getShift() + currentShift);
        v.setChange(v.getChange() + currentShift / subtrees);
        w.setPreliminary(w.getPreliminary() + currentShift);
        w.setMod(w.getMod() + currentShift);
    }

    /**
     * Used to traverse the left contour of a subtree.
     *
     * @param v vertex v
     * @return the successor of the vertex on this contour
     */
    private WalkerNode nextLeft(WalkerNode v) {
        return next(v, vertex -> vertex.getChildren().get(0));
    }

    /**
     * Used to traverse the right contour of a subtree.
     *
     * @param v vertex v
     * @return the successor of the vertex on this contour
     */
    private WalkerNode nextRight(WalkerNode v) {
        return next(v,
                vertex -> vertex.getChildren().get(v.getChildren().size() - 1));
    }

    private WalkerNode next(WalkerNode v,
                            Function<WalkerNode, WalkerNode> getNextChild) {
        if (v.isLeaf()) {
            return v.getThread();
        }
        return getNextChild.apply(v);
    }

    private List<? extends WalkerNode> getVertexChildrenReversed(
            WalkerNode vertex) {
        return Lists.reverse(vertex.getChildren());
    }
}
