package simulation;

import engine.MyList;
import engine.MyPriorityQueue;

public class RadioRouter {
    
    // Finds the best path by hopping from radio to radio, ignoring the ground tiles
    public MyList<TransceiverNode> findRoute(MyList<TransceiverNode> nodes, TransceiverNode start, TransceiverNode end) {
        double[] dist = new double[nodes.size()];
        int[] prev = new int[nodes.size()];
        
        // Initialize distances to infinity
        for (int i = 0; i < nodes.size(); i++) {
            dist[i] = Double.MAX_VALUE;
            prev[i] = -1;
        }
        
        int startIndex = getIndex(nodes, start);
        int endIndex = getIndex(nodes, end);
        
        dist[startIndex] = 0.0;
        
        // Use your custom Priority Queue!
        MyPriorityQueue<State> pq = new MyPriorityQueue<>();
        pq.add(new State(startIndex, 0.0));
        
        while (!pq.isEmpty()) {
            State current = pq.poll();
            
            if (current.dist > dist[current.index]) continue;
            if (current.index == endIndex) break; // Reached the destination!
            
            TransceiverNode currNode = nodes.get(current.index);
            
            // Check all other radios in the network
            for (int i = 0; i < nodes.size(); i++) {
                if (i == current.index) continue;
                
                TransceiverNode neighbor = nodes.get(i);
                double cost = currNode.calculateSignalCost(neighbor);
                
                // If it's within range, calculate the path cost
                if (cost != Double.MAX_VALUE) {
                    double newDist = current.dist + cost;
                    if (newDist < dist[i]) {
                        dist[i] = newDist;
                        prev[i] = current.index;
                        pq.add(new State(i, newDist));
                    }
                }
            }
        }
        
        if (dist[endIndex] == Double.MAX_VALUE) return null; // Out of range
        
        // Reconstruct the path backwards
        MyList<TransceiverNode> reversed = new MyList<>();
        int curr = endIndex;
        while (curr != -1) {
            reversed.add(nodes.get(curr));
            curr = prev[curr];
        }
        
        // Flip it to start -> end
        MyList<TransceiverNode> path = new MyList<>();
        for (int i = reversed.size() - 1; i >= 0; i--) {
            path.add(reversed.get(i));
        }
        return path;
    }
    
    private int getIndex(MyList<TransceiverNode> nodes, TransceiverNode target) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) == target) return i;
        }
        return -1;
    }
    
    // Custom state for your Priority Queue
    private static class State implements Comparable<State> {
        int index;
        double dist;
        State(int index, double dist) {
            this.index = index;
            this.dist = dist;
        }
        @Override
        public int compareTo(State other) {
            return Double.compare(this.dist, other.dist);
        }
    }
}