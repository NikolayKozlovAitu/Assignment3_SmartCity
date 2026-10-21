package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final boolean isDirected;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> allEdges;

    public Graph(int vertices, boolean isDirected) {
        this.vertices = vertices;
        this.isDirected = isDirected;
        this.adjacencyList = new ArrayList<>(vertices);
        this.allEdges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int src, int dest, double weight) {
        Edge edge = new Edge(src, dest, weight);
        adjacencyList.get(src).add(edge);
        allEdges.add(edge);

        // Если граф неориентированный, добавляем обратное ребро
        if (!isDirected) {
            Edge backEdge = new Edge(dest, src, weight);
            adjacencyList.get(dest).add(backEdge);
        }
    }

    public int getVerticesCount() {
        return vertices;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }
}