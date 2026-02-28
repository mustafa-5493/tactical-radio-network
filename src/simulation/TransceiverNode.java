package simulation;

public class TransceiverNode {
    private final String callsign;
    private final int x;
    private final int y;
    private final TransceiverTier tier;
    private double batteryLevel;
    private boolean isJammed; // Our EW flag

    public enum TransceiverTier {
        COMMAND(100.0, 150.0),    
        RELAY(85.0, 100.0),       
        STANDARD(50.0, 50.0),     
        THROTTLED(25.0, 30.0);    

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
        this.isJammed = false; // Default to clear airwaves
    }

    public double calculateDistanceTo(TransceiverNode other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calculateSignalCost(TransceiverNode other) {
        // EW CHECK: If either node is jammed, cost is infinite
        if (this.isJammed || other.isJammed()) {
            return Double.MAX_VALUE;
        }

        double distance = calculateDistanceTo(other);
        
        if (distance > this.tier.maxRange) {
            return Double.MAX_VALUE; 
        }
        
        return (distance * distance) / this.tier.bandwidthAllocation;
    }

    // --- Standard Getters ---
    public String getCallsign() { return callsign; }
    public int getX() { return x; }
    public int getY() { return y; }
    public TransceiverTier getTier() { return tier; }
    public double getBatteryLevel() { return batteryLevel; }
    
    // --- Electronic Warfare Getters & Setters ---
    public void setJammed(boolean jammed) { this.isJammed = jammed; }
    public boolean isJammed() { return isJammed; }
    
    // Simulate battery drain
    public void drainBattery(double transmissionDurationMs) {
        double drainRate = 0.05 * (100.0 / this.tier.bandwidthAllocation);
        this.batteryLevel = Math.max(0, this.batteryLevel - (drainRate * transmissionDurationMs / 1000.0));
    }
}