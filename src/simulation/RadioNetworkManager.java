package simulation;

import engine.Grid;
import engine.Node;
import engine.Pathfinder;
import engine.MyList;

public class RadioNetworkManager {
    private final Grid tacticalGrid;
    private final MyList<TransceiverNode> activeNodes;
    private final Pathfinder pathfinder;

    public RadioNetworkManager(int gridWidth, int gridHeight) {
        this.tacticalGrid = new Grid(gridWidth, gridHeight);
        this.activeNodes = new MyList<>();
        this.pathfinder = new Pathfinder();
    }

    // 1. Deploy a radio unit to the battlefield
    public void deployNode(TransceiverNode node) {
        activeNodes.add(node);
        // Mark the grid at this location as a passable communication point (type 0)
        tacticalGrid.setNodeType(node.getX(), node.getY(), 0);
    }

    // 2. THE BRIDGE: Map signal costs to your Dijkstra Grid
    public void establishNetworkTopology() {
        // Loop through all unique pairs of radios to calculate interference and range
        for (int i = 0; i < activeNodes.size(); i++) {
            for (int j = i + 1; j < activeNodes.size(); j++) {
                TransceiverNode a = activeNodes.get(i);
                TransceiverNode b = activeNodes.get(j);

                double signalCost = a.calculateSignalCost(b);

                // If they are within range, map the cost to your Grid's custom Hash
                if (signalCost != Double.MAX_VALUE) {
                    tacticalGrid.setTravelTime(a.getX(), a.getY(), b.getX(), b.getY(), signalCost);
                }
            }
        }
    }

    // 3. Find the clearest frequency route using your custom Pathfinder
    public MyList<Node> routeTransmission(TransceiverNode start, TransceiverNode destination) {
        return pathfinder.findShortestPath(tacticalGrid, start.getX(), start.getY(), destination.getX(), destination.getY());
    }

    public Grid getGrid() { return tacticalGrid; }
    public MyList<TransceiverNode> getActiveNodes() { return activeNodes; }
}