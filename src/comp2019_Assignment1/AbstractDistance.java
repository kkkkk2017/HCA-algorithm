package comp2019_Assignment1;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * This class finds the abstract distance from any location to the agent's goal.
 * This distance is used as the heuristic estimate in the HCA* search.
 * The entry point for your code is in method distance(Location).
 *
 * DO NOT MODIFY THE SIGNATURE OF EXISTING METHODS AND VARIABLES.
 * Otherwise, JUnit tests will fail and you will receive no credit for your code.
 * Of course, you can add additional methods and classes in your implementation.
 *
 */
public class AbstractDistance {

    public static final int INFINITY = Integer.MAX_VALUE;

    RectangularMap map;
    Location agentGoal, agentInitialLoc;
    PriorityQueue<Location> openList = new PriorityQueue<>();
    HashSet<Location> closeList = new HashSet<Location>();


    public AbstractDistance(RectangularMap map, Location agentGoal, Location agentInitialLoc) {
        this.map = map;
        this.agentGoal = agentGoal;
        this.agentInitialLoc = agentInitialLoc;

        //initialise
        this.agentGoal.setG(0);
        this.agentGoal.setH(manthattan(agentGoal, agentInitialLoc));
        this.agentGoal.setF(agentGoal.getG() + agentGoal.getH());
        openList.add(agentGoal);
    }

    public double distance(Location loc) {
        // TODO: Question 2:
        //  Implement the Reverse Resumable A* to calculate the heuristic distance from this.agentGoal to loc.
        //  Return INFINITY if there there is no feasible path fro agentGoal to loc.
        if (closeList.contains(loc)) {
            for (Location location : closeList) {
                if (location.equals(loc)) return location.getG();
            }
        }

        while(!openList.isEmpty()) {
            Location current = openList.poll();

            if (closeList.contains(current)) continue;

            closeList.add(current);

            for (Location neighbour : map.getNeighbours(current)) {
                if (map.getValueAt(neighbour) == 1 || closeList.contains(neighbour)) continue;

                neighbour.setH(manthattan(neighbour, agentInitialLoc));
                neighbour.setG(current.getG() + 1);
                neighbour.setF(neighbour.getG() + neighbour.getH());

                if (!openList.contains(neighbour)){
                    openList.add(neighbour);
                }
            }

            //if exists, return true
            if (current.equals(loc)) {
                return current.getG();
            }
        }

        return INFINITY;

        //throw new UnsupportedOperationException();
    }

    private double manthattan(Location current, Location goal) {
        return Math.abs(current.getColumn() - goal.getColumn())
                + Math.abs(current.getRow() - goal.getRow());
    }
}
