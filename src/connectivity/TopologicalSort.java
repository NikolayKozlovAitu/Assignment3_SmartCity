package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class TopologicalSort {

    public static class Result {
        private final List<Integer> order;
        private final boolean hasCycle;
        private final long operationCount;

        public Result(List<Integer> order, boolean hasCycle, long operationCount) {
            this.order = order;
            this.hasCycle = hasCycle;
            this.operationCount = operationCount;
        }

        public List<Integer> getOrder() {
            return order;
        }

        public boolean hasCycle() {
            return hasCycle;
        }

        public long getOperationCount() {
            return operationCount;
        }
    }

    public static Result sort(Graph graph) {
        int v = graph.getVerticesCount();
        int[] inDegree = new int[v];
        long operationCount = 0;

        for (int i = 0; i < v; i++) {
            for (Edge edge : graph.getAdjacencyList().get(i)) {
                inDegree[edge.getDestination()]++;
                operationCount++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < v; i++) {
            operationCount++;
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            operationCount++;

            for (Edge edge : graph.getAdjacencyList().get(u)) {
                operationCount++;
                int neighbor = edge.getDestination();
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        boolean hasCycle = order.size() != v;
        return new Result(hasCycle ? null : order, hasCycle, operationCount);
    }
}