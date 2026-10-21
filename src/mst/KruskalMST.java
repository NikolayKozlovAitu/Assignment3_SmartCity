package mst;

import dsu.DisjointSetUnion;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalMST {

    public static MSTResult findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Kruskal's algorithm requires an undirected graph.");
        }

        int vertices = graph.getVerticesCount();
        List<Edge> edges = new ArrayList<>(graph.getAllEdges());
        Collections.sort(edges);

        DisjointSetUnion dsu = new DisjointSetUnion(vertices);
        List<Edge> mst = new ArrayList<>();
        double totalWeight = 0;
        long operationCount = 0;

        for (Edge edge : edges) {
            operationCount++;
            if (dsu.union(edge.getSource(), edge.getDestination())) {
                mst.add(edge);
                totalWeight += edge.getWeight();
                if (mst.size() == vertices - 1) {
                    break;
                }
            }
        }

        operationCount += dsu.getOperationCount();
        boolean isConnected = (vertices == 0) || (mst.size() == vertices - 1);

        return new MSTResult(mst, totalWeight, isConnected, operationCount);
    }
}