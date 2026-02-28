package simulation;

import engine.MyList;

public class ElectronicWarfare {
    
    // Deploys a signal jammer at a specific coordinate
    public static void deployJammer(RadioNetworkManager manager, int targetX, int targetY, double radius) {
        System.out.println("\n[WARNING] ENEMY EW JAMMER DEPLOYED AT (" + targetX + ", " + targetY + ") - RADIUS: " + radius);
        
        MyList<TransceiverNode> nodes = manager.getActiveNodes();
        boolean jammerHit = false;

        for (int i = 0; i < nodes.size(); i++) {
            TransceiverNode node = nodes.get(i);
            
            // Calculate Euclidean distance from jammer to the radio node
            double dx = node.getX() - targetX;
            double dy = node.getY() - targetY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance <= radius) {
                node.setJammed(true);
                System.out.println("  -> [EW-ALERT] " + node.getCallsign() + " caught in jamming radius! Signal lost.");
                jammerHit = true;
            }
        }

        if (!jammerHit) {
            System.out.println("  -> [EW-REPORT] Jammer missed all friendly nodes.");
        }
    }
}