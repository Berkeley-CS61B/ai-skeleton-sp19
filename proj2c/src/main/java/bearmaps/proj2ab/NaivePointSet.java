package bearmaps.proj2ab;

import bearmaps.hw4.streetmap.Node;
import bearmaps.proj2c.utils.DistanceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive nearest neighbor implementation using a linear scan.
 *
 * This implementation is immutable.
 *
 * @author Michelle Hwang
 * @since 2019-03-02
 */
public class NaivePointSet implements PointSet {

    private List<Node> points;

    public NaivePointSet(List<Node> points) {
        this.points = new ArrayList(points);
    }

    /**
     * Returns point that is the closest to the inputted x and y by
     * Euclidean distance.
     */
    public Node nearest(double x, double y) {
        double minDistance = Double.POSITIVE_INFINITY;
        Node minPoint = null;
        double distance;
        for (Node pt : points) {
            distance = DistanceUtils.distance(pt.lon(), y, pt.lat(), x);
            if (distance < minDistance) {
                minDistance = distance;
                minPoint = pt;
            }
        }
        return minPoint;
    }
}
