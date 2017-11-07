package qmaze.View.LearningGrid;

import java.util.HashMap;
import qmaze.Environment.Location;

/**
 *
 * @author katharine
 * Wrapper class around data structure that represents what an agent learns
 * from moving from one location into the next.
 * Represents a Q table, which maps the rewards for taking an action, a
 * from state, s
 * so:
 *               action (a)
 *             ___________
 *            |           |
 *  state (s) |  reward   |
 *             ___________
 */
public class MazeLearning {
    private final HashMap<Location, HashMap<Location,Double>> learnings;
    
    public MazeLearning() {
       learnings = new HashMap<>();
    }
    
    public void put(Location location, HashMap<Location,Double> learning) {
        learnings.put(location, learning);
    }
    
    public HashMap<Location, HashMap<Location,Double>> getLearnings() {
        return learnings;
    }
}
