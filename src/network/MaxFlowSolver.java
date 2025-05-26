package network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MaxFlowSolver {
    // Method to parse the network from an input file and build the flow network
    public static FlowNetwork parseNetworkFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("benchmarks/" + filename));

        int numNodes = scanner.nextInt();
        FlowNetwork network = new FlowNetwork(numNodes);

        while (scanner.hasNextInt()) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            int capacity = scanner.nextInt();
            network.addEdge(from, to, capacity);
        }

        scanner.close();
        return network;
    }
    // Method to run the Edmonds-Karp algorithm to find the maximum flow
    public static int edmondsKarp(FlowNetwork network, int source, int sink) {
        int maxFlow = 0;// Initialize the maximum flow
        List<List<String>> stepByStepExplanation = new ArrayList<>();

        while (true) {
            List<Edge> path = new ArrayList<>();
            if (!bfs(network, source, sink, path)) {
                break;
            }
            // Find the minimum capacity (bottleneck) along the path
            int bottleneck = Integer.MAX_VALUE;
            for (Edge edge : path) {
                bottleneck = Math.min(bottleneck, edge.remainingCapacity());
            }

            for (Edge edge : path) {
                edge.addFlow(bottleneck);
            }
            // Add the bottleneck to the total max flow
            maxFlow += bottleneck;
            // Store the details of this step
            List<String> step = new ArrayList<>();
            step.add("Augmenting Path: " + pathToString(path));
            step.add("Bottleneck Value: " + bottleneck);
            step.add("Current Max Flow: " + maxFlow);
            stepByStepExplanation.add(step);
        }
        //        Print step-by-step explanation of the flow calculation
        System.out.println("\nStep-by-Step Maximum Flow Calculation:");
        for (int i = 0; i < stepByStepExplanation.size(); i++) {
            System.out.println("Step " + (i + 1) + ":");
            for (String detail : stepByStepExplanation.get(i)) {
                System.out.println("  " + detail);
            }
            System.out.println();
        }

        return maxFlow;// Return the maximum flow
    }
    // Method to perform BFS to find an augmenting path from source to sink

    private static boolean bfs(FlowNetwork network, int source, int sink, List<Edge> path) {
        int V = network.getV(); // Get the number of nodes
        boolean[] visited = new boolean[V]; // Array to track visited nodes
        Edge[] edgeTo = new Edge[V]; // Array to store the path

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source); // Start BFS from the source
        visited[source] = true; // Mark the source as visited


        while (!queue.isEmpty() && !visited[sink]) {
            int u = queue.poll();
            for (Edge edge : network.getAdj(u)) {
                int v = edge.to;
                if (!visited[v] && edge.remainingCapacity() > 0) {
                    edgeTo[v] = edge;
                    visited[v] = true;
                    queue.add(v);// Add v to the queue for further exploration
                }
            }
        }
        // If the sink is visited, construct the augmenting path
        if (visited[sink]) {
            for (int v = sink; v != source; v = edgeTo[v].from) {
                path.add(0, edgeTo[v]);
            }
            return true;// Augmenting path found
        }

        return false;// no Augmenting path found
    }
    // Method to convert a path of edges into a string representation
    private static String pathToString(List<Edge> path) {
        StringBuilder sb = new StringBuilder();
        sb.append(path.get(0).from);
        for (Edge edge : path) {
            sb.append(" -> ").append(edge.to);// Append the nodes in the path
        }
        return sb.toString();
    }
    // Method to print the details of the flow after the computation

    public static void printFlowDetails(FlowNetwork network, int source, int sink, int maxFlow) {
        System.out.println("Maximum Flow: " + maxFlow);

        int sourceOutFlow = 0;
        // Calculate the total flow leaving the source
        for (Edge edge : network.getAdj(source)) {
            if (edge.capacity > 0) {
                sourceOutFlow += edge.flow;
            }
        }

        int sinkInFlow = 0;
        // Calculate the total flow entering the sink
        for (int v = 0; v < network.getV(); v++) {
            for (Edge edge : network.getAdj(v)) {
                if (edge.to == sink && edge.capacity > 0) {
                    sinkInFlow += edge.flow;
                }
            }
        }
        // Print the flow details
        System.out.println("Flow leaving source: " + sourceOutFlow);
        System.out.println("Flow entering sink: " + sinkInFlow);

        System.out.println("\nEdge Flows:");
        for (int v = 0; v < network.getV(); v++) {
            for (Edge edge : network.getAdj(v)) {
                if (edge.capacity > 0 && edge.flow > 0) {
                    System.out.println("f(" + edge.from + "," + edge.to + ") = " + edge.flow);
                }
            }
        }
    }
}

