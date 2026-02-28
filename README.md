# Tactical Radio Network Simulation

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Algorithms](https://img.shields.io/badge/Algorithm-Dijkstra-blue?style=for-the-badge)
![Data Structures](https://img.shields.io/badge/Data_Structures-Custom-success?style=for-the-badge)

A highly optimized, graph-based tactical radio network simulation. This project demonstrates the decoupling of physical grid terrain from wireless airwave routing, utilizing custom-built data structures (Priority Queues, Hash Maps, Linked Lists) to calculate real-time signal degradation and fault tolerance.



## Key Features

* **Dynamic Signal Cost Calculation:** Replaces standard distance-based pathfinding with a Path Loss algorithm. Edge weights are dynamically calculated based on `TransceiverTier` bandwidth allocations and Euclidean distance.
* **Graph-Based Dijkstra Routing:** Uses a custom `MyPriorityQueue` to achieve $O((E+V) \log V)$ routing efficiency across the airwaves, ignoring physical terrain limitations.
* **Electronic Warfare (EW) Simulation:** Features real-time fault tolerance. When an EW Jammer is deployed to a coordinate, affected transceiver nodes are neutralized (Signal Cost = `Double.MAX_VALUE`), forcing the network to dynamically recalculate secondary routing paths.

## Custom Data Structures
This engine runs entirely independent of standard Java collections (`java.util.*`), utilizing bespoke implementations for maximum performance control:
* `MyPriorityQueue<T>`: Min-heap implementation for Dijkstra state evaluation.
* `MyHash<K, V>`: Dynamic capacity hash table for topology mapping.
* `MyList<T>`: Array-backed dynamic list for active node management.

## Simulation Output Example
```text
--- INITIATING TRANSMISSION (CLEAR AIRWAVES) ---
Source: COMMAND-ALPHA | Dest: OBSERVER-BRAVO
STATUS: SUCCESS. Optimal frequency path established.
Path (2 hops): COMMAND-ALPHA -> RELAY-NORTH -> OBSERVER-BRAVO

[WARNING] ENEMY EW JAMMER DEPLOYED AT (145, 105) - RADIUS: 20.0
  -> [EW-ALERT] RELAY-NORTH caught in jamming radius! Signal lost.

--- RE-INITIATING TRANSMISSION (UNDER EW THREAT) ---
Source: COMMAND-ALPHA | Dest: OBSERVER-BRAVO
STATUS: SUCCESS. Optimal frequency path established.
Path (2 hops): COMMAND-ALPHA -> RELAY-SOUTH -> OBSERVER-BRAVO

##  How to Run
1. Navigate to the `src` directory.
2. Compile the engine and simulation modules: `javac engine/*.java simulation/*.java`
3. Execute the command center: `java simulation.SimulationRunner`
