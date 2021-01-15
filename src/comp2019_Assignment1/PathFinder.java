package comp2019_Assignment1;


import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class finds the best path from a start location to the goal location given the map.
 * The entry point for your code is in method findPath().
 *
 * DO NOT MODIFY THE SIGNATURE OF EXISTING METHODS AND VARIABLES.
 * Otherwise, JUnit tests will fail and you will receive no credit for your code.
 * Of course, you can add additional methods and classes in your implementation.
 *
 */
public class PathFinder {

    private Location start;       // start location
    private Location goal;        // goal location
    private RectangularMap map;   // the map

    public PathFinder(RectangularMap map, Location start, Location goal) {
        this.map = map;
        this.start = start;
        this.goal = goal;
    }

    public RectangularMap getMap() {
        return map;
    }

    public Location getStart() {
        return start;
    }

    public Location getGoal() {
        return goal;
    }

    /* DO NOT CHANGE THE CODE ABOVE */
    /* adding imports and variables is okay. */

    /* Question 1:
     * add your code below.
     * you can add extra methods.
     */

    public Path findPath() {
        //
        //TODO Question1
        // Implement A* search that finds the best path from start to goal.
        // Return a Path object if a solution was found; return null otherwise.
        // Refer to the assignment specification for details about the desired path.

        if(start == null || goal == null || map.getValueAt(start) != 0 || map.getValueAt(goal) != 0){
            return null;
        }

        PriorityQueue<Location> openList = new PriorityQueue<Location>();
        HashSet<Location> closeList = new HashSet<Location>();

        start.setH(h(start));
        start.setG(0);
        start.setF(start.getG() + start.getH());
        start.setParent(null);
        openList.add(start);

        while(!openList.isEmpty()){
            Location current = openList.poll();

            if(closeList.contains(current)) continue;

            closeList.add(current);


            if (current.equals(goal)){
                return constructPath(current);
            }

            for (Location neighbour : map.getNeighbours(current)) {
                if (map.getValueAt(neighbour) == 1 || closeList.contains(neighbour)) continue;

                neighbour.setH(h(neighbour));
                neighbour.setG(current.getG() + distance_between(neighbour, current));
                neighbour.setF(neighbour.getG() + neighbour.getH());
                neighbour.setParent(current);

                openList.add(neighbour);
            }
        }
        return null;
    }

    private Path constructPath(Location goal){
        List<Location> reversePath = new ArrayList<Location>();
        Location back = goal;

        while (back != null){
            reversePath.add(back);
            back = back.getParent();
        }

        Path shortestPath = new Path(start);
        for (int i = reversePath.size()-2; i >= 0; i--) {
            shortestPath.moveTo(reversePath.get(i));
        }

        return shortestPath;
    }

    private double h(Location neighbour){
        return  Math.abs(neighbour.getColumn() - goal.getColumn())
                + Math.abs(neighbour.getRow() - goal.getRow());
    }

    private double distance_between(Location l1, Location l2){
        return (Math.abs(l1.getRow() - l2.getRow()) + Math.abs(l1.getColumn() - l2.getColumn()));
    }
}