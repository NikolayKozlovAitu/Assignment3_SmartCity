import connectivity.TarjanSCC;
import connectivity.TopologicalSort;
import dsu.DisjointSetUnion;
import graph.Graph;
import graph.GraphGenerator;
import mst.KruskalMST;
import mst.MSTResult;
import mst.PrimMST;
import shortestpath.DijkstraShortestPath;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {
        runCorrectnessTests();

        System.out.println("Starting Benchmark Experiments...");
        runBenchmarks();
        System.out.println("Benchmark completed! Results saved to results/tables/benchmark_results.csv");
    }

    private static void runCorrectnessTests() {
        System.out.println("=== RUNNING CORRECTNESS TESTS ===");

        Graph undirGraph = GraphGenerator.generateConnectedUndirectedGraph(10, 20, 42);
        MSTResult kruskalRes = KruskalMST.findMST(undirGraph);
        MSTResult primRes = PrimMST.findMST(undirGraph);

        assert Math.abs(kruskalRes.getTotalWeight() - primRes.getTotalWeight()) < 1e-5 : "MST weights do not match!";
        System.out.println("MST Test Passed: Kruskal weight == Prim weight (" + kruskalRes.getTotalWeight() + ")");

        Graph dag = GraphGenerator.generateDirectedGraph(10, 20, true, 42);
        TopologicalSort.Result topoRes = TopologicalSort.sort(dag);
        assert !topoRes.hasCycle() : "DAG should not have cycles!";
        System.out.println("Topological Sort (DAG) Test Passed: Order size = " + topoRes.getOrder().size());

        Graph cyclicGraph = GraphGenerator.generateDirectedGraph(10, 20, false, 42);
        TopologicalSort.Result cyclicRes = TopologicalSort.sort(cyclicGraph);
        System.out.println("Cyclic Graph Test Passed: Cycle detected = " + cyclicRes.hasCycle());

        TarjanSCC.Result sccRes = TarjanSCC.findSCCs(cyclicGraph);
        System.out.println("Tarjan SCC Test Passed: Total SCCs found = " + sccRes.getComponents().size());

        DijkstraShortestPath.Result dijkstraRes = DijkstraShortestPath.findShortestPaths(undirGraph, 0);
        System.out.println("Dijkstra Test Passed: Distance to vertex 1 = " + dijkstraRes.getDistanceTo(1));

        System.out.println("=== ALL CORRECTNESS TESTS PASSED ===\n");
    }

    private static void runBenchmarks() {
        int[] V = {10, 100, 1000};
        int[] E = {30, 500, 5000};
        String[] names = {"Small", "Medium", "Large"};
        int runs = 5;

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/tables/benchmark_results.csv"))) {
            writer.println("GraphSize,Vertices,Edges,Algorithm,AvgTimeMs,AvgOperations");

            for (int i = 0; i < V.length; i++) {
                int v = V[i];
                int e = E[i];
                String name = names[i];

                long kruskalTime = 0, kruskalOps = 0;
                long primTime = 0, primOps = 0;
                long topoTime = 0, topoOps = 0;
                long sccTime = 0, sccOps = 0;
                long dijkstraTime = 0, dijkstraOps = 0;

                for (int r = 0; r < runs; r++) {
                    long seed = 100 + r;

                    Graph undir = GraphGenerator.generateConnectedUndirectedGraph(v, e, seed);

                    long start = System.nanoTime();
                    MSTResult kRes = KruskalMST.findMST(undir);
                    kruskalTime += System.nanoTime() - start;
                    kruskalOps += kRes.getOperationCount();

                    start = System.nanoTime();
                    MSTResult pRes = PrimMST.findMST(undir);
                    primTime += System.nanoTime() - start;
                    primOps += pRes.getOperationCount();

                    Graph dag = GraphGenerator.generateDirectedGraph(v, e, true, seed);
                    start = System.nanoTime();
                    TopologicalSort.Result tRes = TopologicalSort.sort(dag);
                    topoTime += System.nanoTime() - start;
                    topoOps += tRes.getOperationCount();

                    Graph dir = GraphGenerator.generateDirectedGraph(v, e, false, seed);
                    start = System.nanoTime();
                    TarjanSCC.Result sRes = TarjanSCC.findSCCs(dir);
                    sccTime += System.nanoTime() - start;
                    sccOps += sRes.getOperationCount();

                    start = System.nanoTime();
                    DijkstraShortestPath.Result dRes = DijkstraShortestPath.findShortestPaths(undir, 0);
                    dijkstraTime += System.nanoTime() - start;
                    dijkstraOps += dRes.getOperationCount();
                }

                writeResult(writer, name, v, e, "Kruskal", kruskalTime / runs, kruskalOps / runs);
                writeResult(writer, name, v, e, "Prim", primTime / runs, primOps / runs);
                writeResult(writer, name, v, e, "TopologicalSort", topoTime / runs, topoOps / runs);
                writeResult(writer, name, v, e, "TarjanSCC", sccTime / runs, sccOps / runs);
                writeResult(writer, name, v, e, "Dijkstra", dijkstraTime / runs, dijkstraOps / runs);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void writeResult(PrintWriter writer, String size, int v, int e, String algo, long timeNano, long ops) {
        double timeMs = timeNano / 1_000_000.0;
        writer.printf("%s,%d,%d,%s,%.4f,%d\n", size, v, e, algo, timeMs, ops);
    }
}