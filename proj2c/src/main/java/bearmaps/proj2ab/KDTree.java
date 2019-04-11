package bearmaps.proj2ab;

import bearmaps.hw4.streetmap.Node;
import bearmaps.proj2c.utils.DistanceUtils;

import java.util.List;

/**
 * Created by hug.
 */
public class KDTree implements PointSet {
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;
    private KDTreeNode root;

    private class KDTreeNode {
        private Node p;
        private boolean orientation;
        private KDTreeNode leftChild;  // also refers to the down" child
        private KDTreeNode rightChild; // also refers to the "up" child

        public KDTreeNode(Node givenP, boolean o) {
            p = givenP;
            orientation = o;
        }
    }

    public KDTree(List<Node> points) {
        /* An easy optimization is to shuffle the points to avoid worst case behavior,
         * but this reference solution does not, so as to better match typical student
         * code behavior. Uncomment the line below to enable this minor optimization. */
        // Collections.shuffle(points);
        for (Node p : points) {
            root = add(p, root, HORIZONTAL);
        }
    }

    private KDTreeNode add(Node p, KDTreeNode n, boolean orientation) {
        if (n == null) {
            return new KDTreeNode(p, orientation);
        }
        if (p.equals(n.p)) {
            return n;
        }

        int cmp = comparePoints(p, n.p, orientation);
        if (cmp < 0) {
            n.leftChild = add(p, n.leftChild, !orientation);
        } else if (cmp >= 0) {
            n.rightChild = add(p, n.rightChild, !orientation);
        }
        return n;
    }

    /** Compares point a to point b using the given orientation.
     *  That is, if horizontal, compares x coordinate, otherwise compares y coordinate.
     * @return
     */
    private int comparePoints(Node a, Node b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(a.lat(), b.lat());
        } else {
            return Double.compare(a.lon(), b.lon());
        }
    }

    @Override
    public Node nearest(double x, double y) {
        Node goal = Node.of(-1, x, y);
        KDTreeNode bestKDTreeNode = nearestHelper(root, goal, root);
        return bestKDTreeNode.p;
    }

    /** Return the closer point to goal between these two choices:
     *  1. The node passed in as 'best'.
     *  2. The node in n that is closest to goal.
     *
     *  For bearmaps.proj2c.example, if there is no node in n that is closer than 'best',
     *  then this method will just return best.
     *  */
    private KDTreeNode nearestHelper(KDTreeNode n, Node goal, KDTreeNode best) {
        /* If the tree is empty, return the old best. */
        if (n == null) {
            return best;
        }

        /* If the current node n is better than the old best, then set best. */
        if (DistanceUtils.distance(n.p, goal) < DistanceUtils.distance(goal, best.p)) {
            best = n;
        }

        /* Figure out which is the "good" side of the node and which is the bad side.
        *  where "good" is the side that is closest to the query point. */
        KDTreeNode goodChild;
        KDTreeNode badChild;

        if (comparePoints(goal, n.p, n.orientation) < 0) {
            goodChild = n.leftChild;
            badChild = n.rightChild;
        } else {
            goodChild = n.rightChild;
            badChild = n.leftChild;
        }

        /* Always look on the good side. There's always a chance that there is something
           better over there. */
        best = nearestHelper(goodChild, goal, best);

        /* Create the hypothetical best point that could possible exist on the bad side.
        *  Note, this "hypothetical best" is based on the simplified green-line pruning rule.
        *  The better purple-line pruning rule is more complicated and is not included
        *  in this reference solution. */
        Node hypotheticalBest;
        if (n.orientation == VERTICAL) {
            hypotheticalBest = Node.of(-1, goal.lat(), n.p.lat());
        } else {
            hypotheticalBest = Node.of(-1, n.p.lon(), goal.lon());
        }

        /* If that hypothetical best point on the bad side is better than the current
           best, then it's worth looking on the bad side. Otherwise, don't!
         */
        if (DistanceUtils.distance(hypotheticalBest, goal) < DistanceUtils.distance(best.p, goal)) {
            best = nearestHelper(badChild, goal, best);
        }

        return best;
    }

    /** This is the implementation of the nearestHelper method, but unoptimized.
     *  Students should try writing something that looks like this first, before
     *  moving onto the optimized version.
     */
    private KDTreeNode nearestHelperUnoptimized(KDTreeNode n, Node goal, KDTreeNode best) {
        if (n == null) {
            return best;
        }

        if (DistanceUtils.distance(n.p, goal) < DistanceUtils.distance(goal, best.p)) {
            best = n;
        }
        best = nearestHelperUnoptimized(n.leftChild, goal, best);
        best = nearestHelperUnoptimized(n.rightChild, goal, best);
        return best;
    }
}
