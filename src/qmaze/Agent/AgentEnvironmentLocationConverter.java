package qmaze.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import qmaze.Environment.Location;

/**
 * Agent has a concept of starting at 0,0. So when it passes any locations to the environment,
 * it needs translating into the environmental location, based on where we started the agent.
 * @author katharine
 */
public class AgentEnvironmentLocationConverter {
    
    public static ArrayList<Location> convertArrayList(ArrayList<Location> locations, Location startingState) {
        ArrayList<Location> environmentLocations = new ArrayList<>();
        locations.forEach((agentL) -> {
            environmentLocations.add(convertLocation(agentL, startingState));
        });
        return environmentLocations;
    }
    
    public static Map<Location, Map<Location, Experience>> convertMemoryHashMap(Map<Location, Map<Location, Experience>> memory, Location startingState) {
        Map<Location, Map<Location, Experience>> translatedMemory = new HashMap<>();
        Set<Location> keys = memory.keySet();
        keys.forEach((key) -> {
            HashMap<Location, Experience> newNextStates = new HashMap<>();
            Map<Location, Experience> nextStates = memory.get(key);
            
            nextStates.keySet().forEach((nextLoc) -> {
                Experience exp = nextStates.get(nextLoc);
                
                Location translatedNextLoc = convertLocation(nextLoc, startingState);
                newNextStates.put(translatedNextLoc, exp);
            });
            
            Location translatedKey = convertLocation(key, startingState);
            translatedMemory.put(translatedKey, newNextStates);
        });
        return translatedMemory;
    }
        
    public static Location convertLocation(Location agentLocation, Location startingState) {
        int x = startingState.getCol();
        int y = startingState.getRow();

        int agent_x = agentLocation.getCol();
        int agent_y = agentLocation.getRow();
        return new Location(x+agent_x, y+agent_y);
    }
}
