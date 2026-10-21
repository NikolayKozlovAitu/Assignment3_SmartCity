package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraShortestPath {

    public static class Node implements Comparable<Node> {
        public final int vertex;
        public final double distance;

        public Node(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public static class Result {
        private final double[] distances;
        private final int[] parent;
        private final int source;
        private final long operationCount;

        public Result(double[] distances, int[] parent, int source, long operationCount) {
            this.distances = distances;
            this.parent = parent;
            this.source = source;
            this.operationCount = operationCount;
        }

        public double getDistanceTo(int dest) {
            return distances[dest];
        }

        public List<Integer> getPathTo(int dest) {
            if (distances[dest] == Double.POSITIVE_INFINITY) {
                return Collections.emptyList();
            }
            List<Integer> path = new ArrayList<>();
            for (int at = dest; at != -1; at = parent[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            return path;
        }

        public double[] getDistances() {
            return distances;
        }

        public long getOperationCount() {
            return operationCount;
        }
    }

    public static Result findShortestPaths(Graph graph, int source) {
        int v = graph.getVerticesCount();
        double[] distances = new double[v];
        int[] parent = new int[v];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        distances[source] = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(source, 0));

        long operationCount = 0;

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            operationCount++;

            int u = current.vertex;
            if (current.distance > distances[u]) {
                continue;
            }

            for (Edge edge : graph.getAdjacencyList().get(u)) {
                operationCount++;
                if (edge.getWeight() < 0) {
                    throw new IllegalArgumentException("Dijkstra's algorithm does not support negative edge weights.");
                }

                int neighbor = edge.getDestination();
                double newDist = distances[u] + edge.getWeight();

                if (newDist < distances[neighbor]) {
                    distances[neighbor] = newDist;
                    parent[neighbor] = u;
                    pq.add(new Node(neighbor, newDist));
                }
            }
        }

        return new Result(distances, parent, source, operationCount);
    }
}