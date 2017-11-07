package qmaze.View;

import qmaze.View.Maze.MazeConfig;
import qmaze.View.Maze.MazeAgent;
import qmaze.View.LearningGrid.MazeLearning;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.Pane;
import qmaze.Controller.LearningController;
import qmaze.Controller.TrainingInterruptedException;
import qmaze.Environment.Location;
import qmaze.View.Components.AlertPopup;
import qmaze.View.Components.Component;
import qmaze.View.Maze.QMazeGrid;

/**
 *
 * @author katharine
 * 2 types of component: animated and non-animated.
 *  - The non-animated (e.g. buttons, sliders, Q Learning panel) are all treated the same.
 *  - The animated (maze) is a special case, built by the controller and directly managed.
 * The ComponentController:
 *  - Acts as a go between for LearningController and components
 *  - Manages state across the Components.
 */
public class ViewController {
    
    private LearningController learningController;
    public String STATE;

    private MazeConfig config;
    private String heatMapColour = "None";
    
    private ArrayList<Component> components;
    private QMazeGrid maze;
    
    public ViewController() {
        this.STATE = Component.RESET_STATE;
        components = new ArrayList();
    }
    
    public void register(Component component) {
        components.add(component);
    }

    /**
     * State Resets
     */
    public void configurationReset(MazeConfig config) {
        this.config = config;
        this.STATE = Component.ADJUST_PARAM_STATE;
        reset();
    }
    
    public void episodesReset(MazeConfig config) {
        this.config = config;
        this.STATE = Component.ADJUST_MAZE_STATE;
        reset();
    }
    
    public void heatMapReset(String heatMapColour) {
        this.heatMapColour = heatMapColour;
        this.STATE = Component.TRAINED_STATE;
        reset();
    }
        
    private void reset() {
        components.forEach((c) -> {
            c.reset();
        });
    }
    
    public void hardReset() {
        this.STATE = Component.RESET_STATE;
        reset();
    }
    
    public void roomReset() {
        this.STATE = Component.ADJUST_MAZE_STATE;
        reset();
    }
    
    public void agentReset(MazeConfig config) {
        this.config = config;
        this.STATE = Component.AGENT_STATE;
        reset();
    }

    /**
     * Actions
     */
    public void startTraining() {
        System.out.println("Training");
        String previousState = this.STATE;
        try {
           this.STATE = Component.TRAINED_STATE;
           learningController = new LearningController(maze.getRooms(), config);  
           learningController.startLearning();
           reset();
        } catch (TrainingInterruptedException te) {
            showAlert(te.getMessage(), "There's no goal state I can get to. You're killing me!");
            this.STATE = previousState;
        }
    }
    
    public void showOptimalPath() {
        System.out.println("Finding optimal path...");
        HashMap<MazeAgent, ArrayList<Location>> optimalPath = learningController.getOptimalPath();
        maze.animateMap(optimalPath);
    }
    
    public void showAlert(String header, String message) {
        AlertPopup.popup(header, message);
    }
    
    public String validateUrl(String url){
        String name = LearningController.validateUrl(url);
        if (name == null) {
            showAlert("Could not talk to agent at " + url + "!", "Did you give me a phony url?");
        } 
        return name;
    }
    
    /**
     * Getters
     */
    public Location getGoalState() {
        if (maze == null) {
            return null;
        }
        return maze.getGoalState();
    }

    public HashMap<MazeAgent, MazeLearning> getLearnings() {
        if (maze == null) {
            return new HashMap();
        }
        return learningController.getLearnings(maze.getRooms());
    }

    public Pane getMaze() {
        maze = new QMazeGrid(this);
        return maze.build();
    }
    
    public String getHeatMapColour() {
        return heatMapColour;
    }
        
    public HashMap<Location, Integer> getHeatMap() {
         return learningController.getHeatMap();
    }
        
    public MazeConfig getQMazeConfig() {
        return config;
    }

}
