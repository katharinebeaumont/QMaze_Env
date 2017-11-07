package qmaze.Environment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author katharine
 * The Maze knows:
 *  - It's size
 *  - The rooms it contains
 *  - Where the start and end (goal) is
 * The Maze does not know:
 *  - About the agent
 * Through the Maze we can ask:
 *  - Questions about state for specific locations in the maze.
 * Through the Maze we can control:
 *  - Locations that are opened or closed
 * Shouldn't do:
 *  - Reveal rooms.
 * 
 * COLUMN IS X
 * ROW IS Y
 */
public class Maze {
    
    private final ArrayList<Room> rooms;
    private Room goal;
    
    public Maze(int rows, int columns) {
        this.rooms = new ArrayList();
        buildMaze(rows, columns);
    }
    
    private Room getRoom(Location state) {
        
        Room roomToFind = new Room(state);
        for (Room room: rooms) {
            if (room.hasSameLocation(roomToFind)) {
                return room;
            }
        }
        return null;
    }
    
    public void setOpen(Location state, boolean open) {
        Room r = getRoom(state);
        r.open(open);
    }
    
    public void setGoalState(Location state, int reward) {
        goal = getRoom(state);
        goal.setReward(reward);
    }
    
    public boolean isGoalState(Location state) {
        Room room = getRoom(state);
        return room.equals(goal);
    }
    
    public double getReward(Location action) {
        Room r = getRoom(action);
        return r.getReward();
    }
    
    private void buildMaze(int rows, int columns) {
        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {
                Room r = new Room(new Location(column, row));
                rooms.add(r);
            }
        }
    }
            
   /*
    * TODO: refactor: environment should know when agents are in rooms?
    */
    public Set<Direction> getAdjoiningStatesExcluding(Location state, ArrayList<Location> locationsToExclude) {
        Room r = getRoom(state);
        HashSet<Direction> adjoiningRooms = new HashSet();
        rooms.stream().filter((otherRoom) -> (otherRoom.isOpen() && otherRoom.adjoins(r))).forEachOrdered((otherRoom) -> {
            Location thisLocation = r.getLocation();
            Location otherLocation = otherRoom.getLocation();
            if (!locationsToExclude.contains(otherLocation)) {
               

                int other_x = otherLocation.getCol();
                int this_x = thisLocation.getCol();
                int other_y = otherLocation.getRow();
                int this_y = thisLocation.getRow();
                if (other_x == this_x) {
                    //x is same, so if y is higher, it's down
                    if (this_y < other_y) {
                        adjoiningRooms.add(Direction.DOWN);
                    }
                    if (this_y > other_y) {
                        adjoiningRooms.add(Direction.UP);
                    }
                }

                if (other_y == this_y) {
                    //y is same, so if x is less, it's left
                    if (this_x < other_x) { //e.g. currently at 3, can go to 4
                        adjoiningRooms.add(Direction.RIGHT);
                    } 
                    if (this_x > other_x) { //e.g currently at 3, can go to 2
                        adjoiningRooms.add(Direction.LEFT);
                    }
                }
            }
        });
        return adjoiningRooms;
   }   
}
