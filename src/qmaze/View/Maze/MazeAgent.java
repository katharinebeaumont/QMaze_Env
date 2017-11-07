package qmaze.View.Maze;

import javafx.scene.paint.ImagePattern;
import qmaze.Environment.Location;

/**
 *
 * @author katharine
 */
public class MazeAgent {
    
    private final String url;
    private final String name;
    private final ImagePattern agentImage;
    private final ImagePattern agentAtGoal;
    private final ImagePattern agentDeath;
    private Location startingLocation = new Location(0,0);
    
    public MazeAgent(String url, String name, ImagePattern agentImage,
            ImagePattern agentAtGoal, ImagePattern agentDeath) {
        this.url = url;
        this.name = name;
        this.agentImage = agentImage;
        this.agentAtGoal = agentAtGoal;
        this.agentDeath = agentDeath;
    }
   
    /*
     *Getters
     */
    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public ImagePattern getAgentImage() {
        return agentImage;
    }
    
    public ImagePattern getAgentAtGoal() {
        return agentAtGoal;
    }
    
    public ImagePattern getAgentDeath() {
        return agentDeath;
    }
    
    public Location getStartingLocation() {
        return startingLocation;
    }
    
    public void setStartingLocation(Location location) {
        this.startingLocation = location;
    }
}
