package qmaze.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import qmaze.Environment.Direction;
import qmaze.Agent.AgentController;
import qmaze.Agent.NoWhereToGoException;
import qmaze.Environment.Location;
import qmaze.Environment.Maze;
import qmaze.View.Maze.MazeAgent;

/**
 *
 * @author katharine
 * The events that join the agent with the environment
 */
public class Episode {
    
    public final HashMap<MazeAgent, AgentController> agents;
    public final Maze maze;
    public final int STEP_LIMIT = 5000;
    
    public ArrayList<Location> episodeSteps;
    
    public Episode(HashMap<MazeAgent, AgentController> agents, Maze maze) {
        this.agents = agents;
        this.maze = maze;
        this.episodeSteps = new ArrayList<>();
    }
    
    public void play() throws EpisodeInterruptedException {
        
        System.out.println("Playing episode");
        
        while(goalNotReached()) {
            //Agent can't move into a location that another agent is in 
            ArrayList<Location> otherAgentActions = new ArrayList<Location>();
            
            for (MazeAgent agent: agents.keySet()) {
                AgentController controller = agents.get(agent);
                Location action = getNextAction(controller, otherAgentActions);
                otherAgentActions.add(action);
                 //Did the maze give a reward?
                double reward = maze.getReward(action);
                controller.giveRewardAndPromptMove(reward);
                recordAction(action);
            }
        }
        System.out.println("Finished episode in " + episodeSteps + " steps.");
    }
    
    public void recordAction(Location action) throws EpisodeInterruptedException {
        episodeSteps.add(action);
        if (episodeSteps.size() == STEP_LIMIT) {
            throw new EpisodeInterruptedException("taking too long!", episodeSteps.size());
        }
    }

    public Location getNextAction(AgentController agent, ArrayList<Location> otherAgentLocations) throws EpisodeInterruptedException {
        //Where is the agent?
        Location currentState = agent.whereAreYou();
        //Have a look around the maze
        Set<Direction> adjoiningStates = maze.getAdjoiningStatesExcluding(currentState, otherAgentLocations);
        //Decide on action
        try {
            Direction selectedDirection = agent.chooseNextAction(adjoiningStates);
            return currentState.getAdjoining(selectedDirection);
        } catch (NoWhereToGoException e) {
            throw new EpisodeInterruptedException(e, episodeSteps.size());
        }
    }

    private boolean goalNotReached() {
        for (MazeAgent agent: agents.keySet()) {
            AgentController controller = agents.get(agent);
            if (controller.isGoalReached()) {
                return false;
            }
        }
        return true;
    }
    
    public ArrayList<Location> getEpisodeSteps(){
        return episodeSteps;
    }
}
