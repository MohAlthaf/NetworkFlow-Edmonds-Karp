package network;

import java.util.ArrayList;
import java.util.List;

public class FlowNetwork {
    private final int V;// Number of nodes
    private final List<List<Edge>> adj;// Adjacency list for edges

    // Constructor to initialize the network with a given number of nodes

    public FlowNetwork(int vertices) {
        this.V = vertices;
        adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Method to add a directed edge between two nodes with a given capacity
    public void addEdge(int u, int v, int capacity) {
        // Create a forward edge from 'u' to 'v' with given capacity
        Edge forward = new Edge(u, v, capacity);
        // Create a backward (residual) edge from 'v' to 'u' with zero initial capacity
        Edge backward = new Edge(v, u, 0);
        // Set the residuals of the forward and backward edges
        forward.residual = backward;
        backward.residual = forward;
        // Add both edges to the adjacency lists of the respective nodes
        adj.get(u).add(forward);
        adj.get(v).add(backward);
    }

//get the number of vertices (nodes)
    public int getV() {
        return V;
    }

    // Get the list of edges for a specific node
    public List<Edge> getAdj(int v) {
        return adj.get(v);
    }
    // Print out the current state of the network
    public void printNetwork() {
        System.out.println("Flow Network:");
        for (int v = 0; v < V; v++) {
            for (Edge e : adj.get(v)) {
                if (e.capacity > 0) {
                    System.out.println("Edge " + e.from + " -> " + e.to +
                            " | Capacity: " + e.capacity + " | Flow: " + e.flow);
                }
            }
        }
    }
}
