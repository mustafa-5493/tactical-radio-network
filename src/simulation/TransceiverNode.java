package simulation;

public class TransceiverNode {
    private final String callsign;
    private final int x;
    private final int y;
    private final TransceiverTier tier;
    private double batteryLevel;

    // We use an Enum to strictly define the capabilities of each radio type.
    // This replaces the messy "type 1, type 2" integers from the other project.
    public enum TransceiverTier {
        COMMAND(100.0, 150.0),    // Tier 0: 100% bandwidth, 150 unit range
        RELAY(85.0, 100.0),       // Tier 1: 85% bandwidth, 100 unit range
        STANDARD(50.0, 50.0),     // Tier 2: 50% bandwidth, 50 unit range
        THROTTLED(25.0, 30.0);    // Tier 3: 25% bandwidth, 30 unit range

        public final double bandwidthAllocation;
        public final double maxRange;

        TransceiverTier(double bandwidthAllocation, double maxRange) {
            this.bandwidthAllocation = bandwidthAllocation;
            this.maxRange = maxRange;
        }
    }

    public TransceiverNode(String callsign, int x, int y, TransceiverTier tier) {
        this.callsign = callsign;
        this.x = x;
        this.y = y;
        this.tier = tier;
        this.batteryLevel = 100.0;
    }

    // 1. Calculate physical distance using Euclidean math
    public double calculateDistanceTo(TransceiverNode other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 2. THE MAGIC: Calculate Signal Degradation (Acts as Dijkstra Weight)
    // Lower weight = clearer signal. High degradation = high routing cost.
    public double calculateSignalCost(TransceiverNode other) {
        double distance = calculateDistanceTo(other);
        
        // If it's physically out of range, the "travel time" is infinite.
        if (distance > this.tier.maxRange) {
            return Double.MAX_VALUE; 
        }
        
        // Simplified Path Loss: Cost increases exponentially with distance.
        // We factor in the tier's bandwidth (better tier = lower cost penalty).
        return (distance * distance) / this.tier.bandwidthAllocation;
    }

    // --- Getters ---
    public String getCallsign() { return callsign; }
    public int getX() { return x; }
    public int getY() { return y; }
    public TransceiverTier getTier() { return tier; }
    public double getBatteryLevel() { return batteryLevel; }
    
    // Simulate battery drain during transmission
    public void drainBattery(double transmissionDurationMs) {
        // Higher tier radios drain battery slower due to better hardware
        double drainRate = 0.05 * (100.0 / this.tier.bandwidthAllocation);
        this.batteryLevel = Math.max(0, this.batteryLevel - (drainRate * transmissionDurationMs / 1000.0));
    }
}