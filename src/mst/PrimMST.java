package mst;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMST {

    public static MSTResult findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Prim's algorithm requires an undirected graph.");
        }

        int vertices = graph.getVerticesCount();
        if (vertices == 0) {
            return new MSTResult(new ArrayList<>(), 0, true, 0);
        }

        boolean[] inMST = new boolean[vertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        List<Edge> mst = new ArrayList<>();
        double totalWeight = 0;
        long operationCount = 0;

        inMST[0] = true;
        for (Edge edge : graph.getAdjacencyList().get(0)) {
            pq.add(edge);
            operationCount++;
        }

        while (!pq.isEmpty() && mst.size() < vertices - 1) {
            Edge edge = pq.poll();
            operationCount++;

            int dest = edge.getDestination();
            if (inMST[dest]) {
                continue;
            }

            inMST[dest] = true;
            mst.add(edge);
            totalWeight += edge.getWeight();

            for (Edge neighbor : graph.getAdjacencyList().get(dest)) {
                operationCount++;
                if (!inMST[neighbor.getDestination()]) {
                    pq.add(neighbor);
                }
            }
        }

        boolean isConnected = (mst.size() == vertices - 1);
        return new MSTResult(mst, totalWeight, isConnected, operationCount);
    }
}