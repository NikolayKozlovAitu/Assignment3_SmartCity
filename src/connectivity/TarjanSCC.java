package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class TarjanSCC {

    public static class Result {
        private final List<List<Integer>> components;
        private final long operationCount;

        public Result(List<List<Integer>> components, long operationCount) {
            this.components = components;
            this.operationCount = operationCount;
        }

        public List<List<Integer>> getComponents() {
            return components;
        }

        public long getOperationCount() {
            return operationCount;
        }
    }

    private static int index;
    private static int[] indices;
    private static int[] lowLink;
    private static boolean[] onStack;
    private static Stack<Integer> stack;
    private static List<List<Integer>> sccs;
    private static long operationCount;

    public static Result findSCCs(Graph graph) {
        int v = graph.getVerticesCount();
        index = 0;
        operationCount = 0;
        indices = new int[v];
        lowLink = new int[v];
        onStack = new boolean[v];
        stack = new Stack<>();
        sccs = new ArrayList<>();

        Arrays.fill(indices, -1);

        for (int i = 0; i < v; i++) {
            operationCount++;
            if (indices[i] == -1) {
                strongConnect(i, graph);
            }
        }

        return new Result(sccs, operationCount);
    }

    private static void strongConnect(int u, Graph graph) {
        indices[u] = index;
        lowLink[u] = index;
        index++;
        stack.push(u);
        onStack[u] = true;
        operationCount++;

        for (Edge edge : graph.getAdjacencyList().get(u)) {
            operationCount++;
            int v = edge.getDestination();
            if (indices[v] == -1) {
                strongConnect(v, graph);
                lowLink[u] = Math.min(lowLink[u], lowLink[v]);
            } else if (onStack[v]) {
                lowLink[u] = Math.min(lowLink[u], indices[v]);
            }
        }

        if (lowLink[u] == indices[u]) {
            List<Integer> scc = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                scc.add(w);
                operationCount++;
            } while (w != u);
            sccs.add(scc);
        }
    }
}