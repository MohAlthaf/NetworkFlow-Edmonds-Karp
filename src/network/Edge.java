package network;

public class Edge {
    final int from; // The starting node
    final int to; // The ending node
    final int capacity; // Maximum capacity of the edge
    int flow; // Current flow through the edge
    Edge residual; // Residual edge for reverse flow

//    Constructor to create an edge with a capacity and no flow
    public Edge(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0;
    }
    // Method to calculate the remaining capacity on the edge
    public int remainingCapacity() {
        return capacity - flow;
    }
    // Method to add flow to the edge and update the residual edge
    public void addFlow(int additionalFlow) {
        flow += additionalFlow; // Increase the flow
        residual.flow -= additionalFlow; // Decrease the flow on the reverse edge (residual)
    }
}
