package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.*;

/**
 * The correct solution to AStarSolver.
 * Created by hug.
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    AStarGraph<Vertex> g;
    List<Vertex> solution;
    Map<Vertex, WeightedEdge<Vertex>> edgeTo = new HashMap<>();
    Map<Vertex, Double> distTo = new HashMap<>();
    Vertex goal;
    SolverOutcome outcome;
    int numStatesExplored = 0;
    double explorationTime;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        g = input;
        this.goal = end;
        ExtrinsicMinPQ<Vertex> pq = new DoubleMapPQ<>();

        pq.add(start, g.estimatedDistanceToGoal(start, end));
        edgeTo.put(start, null);
        distTo.put(start, 0.0);

        // assumes puzzle has a solution
        Stopwatch timer = new Stopwatch();
        while (!(pq.size() == 0) && !pq.getSmallest().equals(end)
                && timer.elapsedTime() < timeout) {
            Vertex v = pq.removeSmallest();
            numStatesExplored += 1;
            for (WeightedEdge<Vertex> e : g.neighbors(v)) {
                Vertex w = e.to();
                double currentDistance = distTo(w);
                double thisDistance = distTo(v) + e.weight();
                if (thisDistance < currentDistance) {
                    edgeTo.put(w, e);
                    distTo.put(w, thisDistance);
                    double priority = g.estimatedDistanceToGoal(w, end) + distTo(w);
                    if (pq.contains(w)) {
                        pq.changePriority(w, priority);
                    } else {
                        pq.add(w, priority);
                    }
                }
            }
        }
        explorationTime = timer.elapsedTime();

        if (pq.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            solution = new ArrayList<>();
            return;
        }

        solution = constructPath(start, pq.getSmallest());

        if (pq.getSmallest().equals(end)) {
            outcome = SolverOutcome.SOLVED;
        } else {
            outcome = SolverOutcome.TIMEOUT;
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    /* Constructs a path back from Vertex w back to vertex start. */
    private List<Vertex> constructPath(Vertex start, Vertex w) {
        List<Vertex> path = new ArrayList<>();
        path.add(w);
        while (edgeTo.get(w) != null) {
            WeightedEdge<Vertex> e = edgeTo.get(w);
            path.add(e.from());
            w = e.from();
        }
        Collections.reverse(path);
        return path;
    }


    private double distTo(Vertex w) {
        if (distTo.containsKey(w)) {
            return distTo.get(w);
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public double solutionWeight() {
        return distTo(goal);
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return explorationTime;
    }
}
