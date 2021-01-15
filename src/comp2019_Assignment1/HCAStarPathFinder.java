package comp2019_Assignment1;

import java.util.*;

/**
 * This class finds the best path for multiple agents using the Hierarchical Cooperative A* algorithm.
 * The entry point for your code is in method findPaths().
 *
 * DO NOT MODIFY THE SIGNATURE OF EXISTING METHODS AND VARIABLES.
 * Otherwise, JUnit tests will fail and you will receive no credit for your code.
 * Of course, you can add additional methods and classes in your implementation.
 *
 */
public class HCAStarPathFinder {

    private RectangularMap map;   // the map
    private List<Agent> agents;     // the list of agents
    private int maxTimeSteps;     // max time steps to consider in each path

    public HCAStarPathFinder(RectangularMap map, List<Agent> agents, int maxTimeSteps) {
        this.map = map;
        this.agents = agents;
        this.maxTimeSteps = maxTimeSteps;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public RectangularMap getMap() {
        return map;
    }

    public int maxTimeSteps() {
        return maxTimeSteps;
    }

    /* DO NOT CHANGE THE CODE ABOVE */
    /* adding imports and variables is okay. */
  
    /* Question 2
     * add your code below. 
     * you can add extra methods.
     */

   
    public List<Path> findPaths() {
        //
        //TODO:
        // Question2
        // Implement HCA* algorithm and use class AbstractDistance as the heuristic distance estimator
        // You will need to implement the RRA* algorithm in AbstractDistance.
        List<Path> pathList = new ArrayList<>();

        HashMap<Key, Agent> reservationTb = new HashMap<>();

        for (Agent agent : agents) {
            //a map store the time step that the agent wait at the same location, each agent has one
            HashMap<Integer, Location> waitMap = new HashMap<>();
            Path path = CA(agent, reservationTb, waitMap);
            if (path != null){
                putInReservationTable(agent, path, reservationTb);
                pathList.add(path);
            }
        }

        if (pathList.isEmpty()){
            return null;
        }

        return pathList;
        // not yet implemented
        //throw new UnsupportedOperationException();
    }

    private Path CA(Agent agent, HashMap<Key, Agent> reservationTb, HashMap<Integer, Location> waitMap){
       // System.out.println("CA ing");
        Location start = agent.getStart();
        Location goal = agent.getGoal();
        // have a last current to check if it wait at the same location for a time step
        Location lastCurrent = null;

        if(start == null || goal == null || map.getValueAt(start) != 0 || map.getValueAt(goal) != 0){
            return null;
        }

        AbstractDistance abstractDistance = new AbstractDistance(map, goal, start);

        PriorityQueue<Location> openList = new PriorityQueue<Location>();
        HashSet<Location> closeList = new HashSet<Location>();

        start.setH(abstractDistance.distance(start));
        start.setG(0);
        start.setF(start.getG() + start.getH());
        start.setParent(null);
        openList.add(start);
        int time = 0;

        while(!openList.isEmpty()) {

            if (time == maxTimeSteps){
                return null;
            }

//            for (Location location : openList) {
//                System.out.println(location.toString() + " h: " + location.getH() + " g: " + location.getG() + " f: " + location.getF() + " at time " + time);
//            }

            Location current = openList.poll();
            closeList.add(current);


//            System.out.println("GET:\t" + current.toString() +  " which f: " + current.getF());
//            System.out.println("*********************** FINISHED *****************************");

            if (current.equals(goal)) {
                boolean ifEnd = true;
                for (Key key : reservationTb.keySet()) {
                    if (key.getX() == current.getRow() && key.getY() == current.getColumn()){
                        if (key.getT() >= time){
                            ifEnd = false;
                            break;
                        }
                    }
                }
                if (ifEnd){
                    return constructPath(start, current, time, waitMap);
                }
            }

            //if it wait at the same location, notify the program time and location
            if (current.equals(lastCurrent)){
               // System.out.println("wait " + current.toString() + " at time " + time);
                waitMap.put(time, current);
            }

            //if the search continues, the time step grows as it is checking next move to
            time ++;
            for(Location neighbour: map.getNeighbours(current)) {
                // check obstruction
                if(map.getValueAt(neighbour) == 1){
                    continue;
                }
                //collision done. but (1,3) has higher f than (3,3)

                //check collision
                if (agent.getPriority() != 0){
                    //check if location is occupied at this time step
                    if (reservationTb.get(new Key(neighbour.getRow(), neighbour.getColumn(), time)) != null){
                        continue;
                    }
                    //check if collides with other agent (higher priority)
                    //check if this location is the last location of previous agent
                    int prevTime = time -1;
                    Agent prevAgent = reservationTb.get(new Key(neighbour.getRow(), neighbour.getColumn(), prevTime));
                    Agent currentAgent = reservationTb.get(new Key(current.getRow(), current.getColumn(), time));
                    if (prevAgent != null && currentAgent != null){
                        if (prevAgent.equals(currentAgent)){
                            continue;
                        }
                    }
                }

                neighbour.setH(abstractDistance.distance(neighbour));
                neighbour.setG(current.getG() + 1);
                neighbour.setF(neighbour.getG() + neighbour.getH());

               // System.out.println(neighbour.toString() + " h: " + neighbour.getH() + " g: " + neighbour.getG());

                if (!neighbour.equals(current)) {
                    neighbour.setParent(current);
                }

                if (openList.contains(neighbour)){
                    openList.remove(neighbour);
                }
                openList.add(neighbour);
            }

            //store it
            lastCurrent = current;
        }
        return null;
    }

    private HashMap<Key, Agent> putInReservationTable(Agent agent, Path path, HashMap<Key, Agent> reservationTable){
        for (int i = 0; i < path.getLength(); i++) {
            Location location = path.getLocation(i);
            Key key = new Key(location.getRow(), location.getColumn(), i);
            if (reservationTable.get(key) == null){
                reservationTable.put(key, agent);
            }
        }
        return reservationTable;
    }

    /**
     * construct the path
     * @param start
     * @param goal
     * @return
     */
    private Path constructPath(Location start, Location goal, int endTime, HashMap<Integer, Location> waitMap){
        List<Location> reversePath = new ArrayList<Location>();
        Location back = goal;
        int time = 0;
        while (back != null || time < endTime){
            reversePath.add(back);
            back = back.getParent();
            time++;
        }

        Path path = new Path(start);
        for (int i = reversePath.size()-2; i >= 0; i--) {
            //System.out.println("Adding at time " + (endTime-i-1) + " " + reversePath.get(i).toString());
            if (waitMap.get(endTime-i-1) != null){
                path.moveTo(waitMap.get(endTime-i-1));
            }
            path.moveTo(reversePath.get(i));
        }

        return path;
    }

    private Path constructPath(HashMap<Key, Agent> reservationTb, Agent agent){
        Path path = new Path(agent.getStart());

        HashMap<Integer, Location> reversePath = new HashMap<>();
        for (Map.Entry<Key, Agent> entry : reservationTb.entrySet()) {
            if (entry.getValue().equals(agent)){
                Location location = new Location(entry.getKey().getX(), entry.getKey().getY());
                if (entry.getKey().getT() != 0){
                    reversePath.put(entry.getKey().getT(), location);
                }
            }
        }

        int time = 1;
        while (!reversePath.isEmpty()){
            Location location = reversePath.remove(time);
            path.moveTo(location);
            time++;
        }
        return path;
    }

}

class Key{
    private int x,y,t;

    public Key(int x, int y, int t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "Key{" +
                "x=" + x +
                ", y=" + y +
                ", t=" + t +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Key other = (Key) obj;
        return this == obj || other.x ==  x && other.y == y && other.t == t;
    }

    @Override
    public int hashCode() {
        return 1000 + x * 100 + y * 10 + t;
    }

}
