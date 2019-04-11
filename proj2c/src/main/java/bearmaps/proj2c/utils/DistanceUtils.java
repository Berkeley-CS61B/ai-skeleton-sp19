package bearmaps.proj2c.utils;

import bearmaps.hw4.streetmap.Node;

/**
 * A utility class with helper methods to calculate distances
 *
 * Created by rahul
 */
public class DistanceUtils {


    public static double distance(Node p, Node q) {
        return distance(p.lon(), q.lon(), p.lat(), q.lat());
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param lonV  The longitude of the first vertex.
     * @param lonW  The longitude of the second vertex.
     * @param latV  The latitude of the first vertex.
     * @param latW  The latitude of the second vertex.
     *
     * @return  The distance between vertices
     */
    public static double distance(double lonV, double lonW, double latV, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param lonV  The longitude of the first vertex.
     * @param latV  The latitude of the first vertex.
     * @param lonW  The longitude of the second vertex.
     * @param latW  The latitude of the second vertex.
     * @return The initial bearing between the vertices.
     */
    public static double bearing(double lonV, double lonW, double latV, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }
}
