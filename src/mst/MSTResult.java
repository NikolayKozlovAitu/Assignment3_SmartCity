package mst;

import graph.Edge;
import java.util.List;

public class MSTResult {
    private final List<Edge> mstEdges;
    private final double totalWeight;
    private final boolean isConnected;
    private final long operationCount;

    public MSTResult(List<Edge> mstEdges, double totalWeight, boolean isConnected, long operationCount) {
        this.mstEdges = mstEdges;
        this.totalWeight = totalWeight;
        this.isConnected = isConnected;
        this.operationCount = operationCount;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public long getOperationCount() {
        return operationCount;
    }
}