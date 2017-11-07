package qmaze.Agent;

import qmaze.Environment.Location;

/**
 *
 * @author katharine
 */
public class NoWhereToGoException extends Exception {
    
    public NoWhereToGoException(Location state) {
        super("I have no-where to go from here: " + state.toString());
    }
    
        
    public NoWhereToGoException(String message) {
        super(message);
    }
    
}
