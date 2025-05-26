package network;

import java.util.Scanner;
import java.io.FileNotFoundException;

public class NetworkFlowApp {
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            //  input file name
            System.out.print("\nEnter input file name (ladder_1.txt/bridge_1.txt) or enter -1 to exit: ");
            String filename = inputScanner.nextLine();

            if (filename.equals("-1")) {
                // Exit the program if the user types -1
                System.out.println("Exiting program. Goodbye!");
                break;
            }

            FlowNetwork network = null;

            try {
                // Parse the network from the input file
                network = MaxFlowSolver.parseNetworkFromFile(filename);
                System.out.println("Loaded network with " + network.getV() + " nodes.");

                int source = 0; // The source node
                int sink = network.getV() - 1; // The sink node (last node)

                // Run the Edmonds-Karp algorithm to find the maximum flow
                System.out.println("Running Edmonds-Karp algorithm...");
                int maxFlow = MaxFlowSolver.edmondsKarp(network, source, sink);

                System.out.println("\nFinal Network State:");
                network.printNetwork(); // Print the final network with flows

                System.out.println("\nFinal Results:");
                MaxFlowSolver.printFlowDetails(network, source, sink, maxFlow); // Print the flow details

            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found. Please try again.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        inputScanner.close(); // Close input scanner
    }
}
