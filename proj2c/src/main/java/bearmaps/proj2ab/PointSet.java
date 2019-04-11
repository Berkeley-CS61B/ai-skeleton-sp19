package bearmaps.proj2ab;

import bearmaps.hw4.streetmap.Node;

public interface PointSet {
    Node nearest(double x, double y);
}
