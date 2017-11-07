package qmaze.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import qmaze.View.Maze.MazeAgent;
import qmaze.Agent.AgentController;
import qmaze.Agent.SpatialExperienceMemory;
import qmaze.Environment.Location;
import qmaze.Environment.Maze;
import qmaze.View.Maze.MazeConfig;
import qmaze.View.Maze.QMazeRoom;
import qmaze.View.LearningGrid.MazeLearning;

/**
 *
 * @author katharine
 * Needs rewriting: needs to act as central clock... one tick moves each agent through the enviroment
 */
public class LearningController {

    private static final int EXCEPTION_THRESHOLD = 20;
    
    private HashMap<MazeAgent, AgentController> agents;
    private Maze maze;
    private final int episodes;
    
    private HashMap<Location, Integer> heatMap;
    
    public LearningController(ArrayList<QMazeRoom> rooms, MazeConfig mazeConfig) {
        this.agents = new HashMap<>();
        this.episodes = mazeConfig.getEpisodes();
        
        initialiseMaze(rooms, mazeConfig.getRows(), mazeConfig.getColumns());
        initialiseAgents(mazeConfig.getAgents());
    }
    
    /*
     * TODO: Need a complete rewrite
     */
    public void startLearning() throws TrainingInterruptedException {
        
        String agentNames = "";
        for (MazeAgent agent: agents.keySet()) {
            if (!agentNames.isEmpty()) {
                agentNames += " and ";
            }
            agentNames += agent.getName();
        }
        
        int exceptionCount = 0;
        heatMap = new HashMap();
        
        for (int i=1; i<episodes; i++) {

            System.out.println("**Training episode " + i + " for " + agentNames);
            Episode e = new Episode(agents, maze);
            try { 
                e.play();
            } catch (EpisodeInterruptedException ex) {
                System.out.println(ex.getMessage());
                exceptionCount++;
                if (exceptionCount > EXCEPTION_THRESHOLD) {
                    throw new TrainingInterruptedException("I've exceeded the failure threshold.");
                }
            }
            buildHeatMap(e.getEpisodeSteps());
        }
    }
    
    public void initialiseMaze(ArrayList<QMazeRoom> rooms, int rows, int columns) {
        maze = new Maze(rows, columns);
        rooms.forEach((r) -> {
            Location roomLocation = new Location(r.getLocation());
            boolean open = r.getOpen();
            maze.setOpen(roomLocation, open);
            if (r.getReward() > 0) {
                maze.setGoalState(roomLocation, r.getReward());
            }
        });
    }
    
    public void initialiseAgents(List<MazeAgent> mazeAgents) {
        for (MazeAgent mazeAgent: mazeAgents) {
            AgentController agentController = new AgentController(mazeAgent.getStartingLocation(), mazeAgent.getUrl());
            agents.put(mazeAgent, agentController);
        }
    }
    
    public HashMap<MazeAgent, MazeLearning> getLearnings(ArrayList<QMazeRoom> rooms) {
       HashMap<MazeAgent, MazeLearning> learnings = new HashMap<>();
       for (MazeAgent agent: agents.keySet()) {
           MazeLearning mazeLearning = getLearningsForAgent(rooms, agent);
           learnings.put(agent, mazeLearning);
       }
       return learnings;
    }
    
    private MazeLearning getLearningsForAgent(ArrayList<QMazeRoom> rooms, MazeAgent agent) {
         
        MazeLearning learning = new MazeLearning();
        
        AgentController ac = agents.get(agent);
        
        SpatialExperienceMemory memory = ac.whatDoYouRemember();
        rooms.forEach((r) -> {
            Location roomLocation = r.getLocation();
            if (r.getOpen()) {
                HashMap<Location,Double> rewardFromRoom = new HashMap();
                ArrayList<Location> potentialActions = memory.actionsForState(roomLocation);
                for (Location action: potentialActions) {
                    double reward = memory.rewardFromAction(roomLocation, action);
                    rewardFromRoom.put(action, reward);
                }
                learning.put(roomLocation, rewardFromRoom);
            }
        });
        
        return learning;
    }
    
    public HashMap<MazeAgent, ArrayList<Location>> getOptimalPath() {
       HashMap<MazeAgent, ArrayList<Location>> agentLocations = new HashMap<MazeAgent, ArrayList<Location>>();
       for (MazeAgent agent: agents.keySet()) {
           AgentController ac = agents.get(agent);
           ArrayList<Location> optimalPath = ac.whatIsYourOptimalPath();
           agentLocations.put(agent, optimalPath);
       }
       return agentLocations;
    }

    private void buildHeatMap(ArrayList<Location> episodeSteps) {
        for (Location roomVisited: episodeSteps) {
            
            Integer roomVisitCount = heatMap.get(roomVisited);
            if (roomVisitCount == null) {
                roomVisitCount = 0;
            }
            roomVisitCount++;
            heatMap.put(roomVisited, roomVisitCount);
        }
    }
    
    public HashMap<Location, Integer> getHeatMap() {
        return heatMap;
    }
    
    //TODO: should be somewhere else
    public static String validateUrl(String url) {
        AgentController test = new AgentController(url);
        return test.hello();
    }
}
