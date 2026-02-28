package simulation;

import engine.Node;
import engine.MyList;

public class SimulationRunner {
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("TACTICAL RADIO NETWORK SIMULATION INITIALIZED");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        // 1. Initialize a 500x500 operational grid
        RadioNetworkManager manager = new RadioNetworkManager(500, 500);

        // 2. Create Transceiver Nodes (Callsign, X, Y, Tier)
        TransceiverNode hq = new TransceiverNode("COMMAND-ALPHA", 50, 50, TransceiverNode.TransceiverTier.COMMAND);
        TransceiverNode relay1 = new TransceiverNode("RELAY-NORTH", 150, 100, TransceiverNode.TransceiverTier.RELAY);
        TransceiverNode relay2 = new TransceiverNode("RELAY-SOUTH", 100, 150, TransceiverNode.TransceiverTier.RELAY);
        TransceiverNode forwardObserver = new TransceiverNode("OBSERVER-BRAVO", 220, 150, TransceiverNode.TransceiverTier.STANDARD);

        // 3. Deploy nodes to the grid
        System.out.println("[SYSTEM] Deploying units to the grid...");
        manager.deployNode(hq);
        manager.deployNode(relay1);
        manager.deployNode(relay2);
        manager.deployNode(forwardObserver);

        // 4. Establish topology (maps signal degradation to your MyHash)
        System.out.println("[SYSTEM] Calculating signal degradation and establishing topology...");
        manager.establishNetworkTopology();

        // 5. Simulate a transmission using YOUR Dijkstra's algorithm
        System.out.println("\n--- INITIATING TRANSMISSION ---");
        System.out.println("Source: " + hq.getCallsign());
        System.out.println("Destination: " + forwardObserver.getCallsign());

        MyList<Node> route = manager.routeTransmission(hq, forwardObserver);

        // 6. Output the tactical results
        System.out.println("-------------------------------");
        if (route == null || route.size() == 0) {
            System.out.println("STATUS: FAILED. No viable signal path found. Out of range.");
        } else {
            System.out.println("STATUS: SUCCESS. Optimal frequency path established.");
            System.out.println("Hops required: " + (route.size() - 1));
            
            System.out.print("Path: ");
            for (int i = 0; i < route.size(); i++) {
                Node n = route.get(i);
                // Match grid coordinates back to callsigns for a clean display
                String callsign = getCallsignAt(manager, n.getX(), n.getY());
                System.out.print(callsign);
                if (i < route.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    // Helper method to make the output look like a real military radio log
    private static String getCallsignAt(RadioNetworkManager manager, int x, int y) {
        MyList<TransceiverNode> nodes = manager.getActiveNodes();
        for (int i = 0; i < nodes.size(); i++) {
            TransceiverNode tn = nodes.get(i);
            if (tn.getX() == x && tn.getY() == y) {
                return tn.getCallsign();
            }
        }
        return "[" + x + "," + y + "](Unknown)";
    }
}