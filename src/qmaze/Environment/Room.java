package qmaze.Environment;

/**
 *
 * @author katharine
 * A Room knows:
 *  - Where it is (Location)
 *  - If it is open or closed
 *  - What reward (if any) it contains
 *  - If another room is adjoining/ neighboring
 * The Room does not know:
 *  - About the agent
 *  - About the maze
 * 
 */
public class Room {
    
    private boolean open = true;
    private double reward = 0;
    private final Location location;
    
    public Room(Location location) {
        this.location = location;
    }

    public void open(boolean open) {
        this.open = open;
    }
    
    public boolean isOpen() {
        return open;
    }
    
    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    } 
    
    public Location getLocation() {
        return location;
    }
 
    public boolean hasSameLocation(Room otherRoom) {
        return otherRoom.getLocation().equals(location);
    }
    
    public boolean adjoins(Room otherRoom) {
        return location.adjoins(otherRoom.getLocation());
    }
}
