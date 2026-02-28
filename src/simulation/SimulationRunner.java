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
        TransceiverNode relay2 = new TransceiverNode("RELAY-SOUTH", 140, 160, TransceiverNode.TransceiverTier.RELAY);
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

        // 5. Simulate an initial CLEAR transmission
        System.out.println("\n--- INITIATING TRANSMISSION (CLEAR AIRWAVES) ---");
        System.out.println("Source: " + hq.getCallsign() + " | Dest: " + forwardObserver.getCallsign());
        printRoute(manager.routeTransmission(hq, forwardObserver));

        // 6. DEPLOY THE JAMMER!
        // We drop it right on top of RELAY-NORTH (which is at 150, 100)
        ElectronicWarfare.deployJammer(manager, 145, 105, 20.0);

        // 7. Simulate the transmission again - Dijkstra must recalculate!
        System.out.println("\n--- RE-INITIATING TRANSMISSION (UNDER EW THREAT) ---");
        System.out.println("Source: " + hq.getCallsign() + " | Dest: " + forwardObserver.getCallsign());
        printRoute(manager.routeTransmission(hq, forwardObserver));
        
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    // Extracted printing logic to keep the main method clean
    private static void printRoute(MyList<TransceiverNode> route) {
        if (route == null || route.size() == 0) {
            System.out.println("STATUS: FAILED. No viable signal path found. Network isolated.");
        } else {
            System.out.println("STATUS: SUCCESS. Optimal frequency path established.");
            System.out.print("Path (" + (route.size() - 1) + " hops): ");
            for (int i = 0; i < route.size(); i++) {
                System.out.print(route.get(i).getCallsign());
                if (i < route.size() - 1) System.out.print(" -> ");
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