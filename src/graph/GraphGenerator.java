package graph;

import java.util.Random;

public class GraphGenerator {

    public static Graph generateConnectedUndirectedGraph(int vertices, int edges, long seed) {
        if (edges < vertices - 1) {
            throw new IllegalArgumentException("For a connected graph, edges must be at least V - 1.");
        }

        Graph graph = new Graph(vertices, false);
        Random random = new Random(seed);

        for (int i = 1; i < vertices; i++) {
            int parent = random.nextInt(i);
            double weight = 1 + random.nextDouble() * 99;
            graph.addEdge(i, parent, weight);
        }

        int remainingEdges = edges - (vertices - 1);
        while (remainingEdges > 0) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            if (u != v) {
                double weight = 1 + random.nextDouble() * 99;
                graph.addEdge(u, v, weight);
                remainingEdges--;
            }
        }

        return graph;
    }

    public static Graph generateDirectedGraph(int vertices, int edges, boolean makeDAG, long seed) {
        Graph graph = new Graph(vertices, true);
        Random random = new Random(seed);
        int addedEdges = 0;

        while (addedEdges < edges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);

            if (u == v) {
                continue;
            }

            if (makeDAG && u >= v) {
                continue;
            }

            double weight = 1 + random.nextDouble() * 99;
            graph.addEdge(u, v, weight);
            addedEdges++;
        }

        return graph;
    }
}