package qmaze.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import qmaze.Environment.Location;
        
/**
 *
 * @author katharine
 * The Agent learns as it moves through the maze:
 *  - What room am I in? Co-ordinates.
 *  - Is there a reward for moving into this room?
 * Then it can recall:
 *  - What was the reward for moving into this room?
 *  - What are the best rooms I remember, the next step on?
 * 
 * Have to: initialise class
 *  then set starting state before anything else can happen.
 *  Why not do this in the constructor? We use the memory for multiple episodes.
 */
public class SpatialExperienceMemory {
    
    private Map<Location, Map<Location, Experience>> memory;
    
    public SpatialExperienceMemory(Map<Location, Map<Location, Experience>> memory) {
        this.memory = memory;
    }
    
    //What do I remember about future actions>
    public ArrayList<Location> actionsForState(Location state) {
        Map nextSteps = memory.get(state);
        if (nextSteps == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(nextSteps.keySet());
    }
    
    public double rewardFromAction(Location state, Location action) {
        Map nextSteps = memory.get(state);
        //Nope, no memory of next steps
        if (nextSteps == null) {
            return 0;
        }
       
        Experience exp = (Experience)nextSteps.get(action);
        //Nope, no memory of moving into this room.
        if (exp == null) {
            return 0;
        }
        return exp.getReward();
    }
}
