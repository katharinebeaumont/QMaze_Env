package qmaze.Agent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import qmaze.Environment.Direction;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import qmaze.Environment.Location;

/**
 *
 * @author katharine
 * 
 * Tells the agent:
 *  - What actions are available to it
 *  - If there is a reward from taking that action
 *  - When it has reached it's goal
 * 
 * Asks the agent:
 *  - Where it is at any point
 *  - What action it will take next
 *  - What it's memory of rooms and rewards is
 * 
 * Needs to know:
 * - What the maze starting state is 
 *  in order to translate the agent's memory of where it is
 *  to the maze-relative location
 * 
 */
public class AgentController {
    
    private final Location startingState;
    private final HttpClient httpClient;
    private final Gson gson;
    private final String url;
    
    public AgentController(Location startingState, String url) {
        this.startingState = startingState;
        
        this.httpClient = createHttpClient();
        
        this.gson = new Gson();
        this.url = url;
        initaliseAgent();
    }
    
    private HttpClient createHttpClient() {
        SocketConfig socketConfig = SocketConfig.copy(SocketConfig.DEFAULT)
                .setSoTimeout(300)
                .build();
        BasicHttpClientConnectionManager manager = new BasicHttpClientConnectionManager();
        manager.setConnectionConfig(ConnectionConfig.DEFAULT);
        manager.setSocketConfig(socketConfig);
        return HttpClients.custom()
                .setConnectionManager(manager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler())
                .build();
        
    }
    
    /**
     * NB dirty code, just for testing agent is there
     * TODO refactor
     */
    public AgentController(String url) { 
        this.startingState = null;
        this.httpClient = HttpClients.createDefault();
        this.gson = new Gson();
        this.url = url;
    }
    
    public Location whereAreYou() {
        try {
            System.out.println("asking where agent is");
            String response = getFromUrl("/location");
            Location agentRelativeLocation = gson.fromJson(response, Location.class);
            return AgentEnvironmentLocationConverter.convertLocation(agentRelativeLocation, startingState);
        } catch (Exception e) {
            System.out.println("Exception getting location: " + e.getMessage());
            throw new RuntimeException("Problem getting next location " + e.getMessage());
        }
    }
    
    public Direction chooseNextAction(Set<Direction> adjoiningStates) throws NoWhereToGoException {
        try {
            String json = gson.toJson(adjoiningStates);
            String response = postToUrl("/chooseAction", json);
            Direction selected = gson.fromJson(response, Direction.class);
            if (selected == null) {
                throw new NoWhereToGoException("Problem getting next action from " + json);
            }
            return selected;
        } catch (Exception e) {
            throw new NoWhereToGoException("Problem getting next action " + e.getMessage());
        }
    }
    
    public void giveRewardAndPromptMove(double reward) {
        String json = gson.toJson(reward);
        postToUrl("/takeAction", json);
    }
    
    public boolean isGoalReached() {
        try {
            System.out.println("Asking if goal has been reached.");
            String response = getFromUrl("/isGoalReached");
            
            if (response.equals("Yes")) {
                System.out.println("Goal reached!");
                return true;
            } 
        }
        catch (Exception e) {
            System.out.println("Did not understand response. " + e.getMessage());
        }
        return false;
    }
    
    public SpatialExperienceMemory whatDoYouRemember() {
        try {
            String response = getFromUrl("/memory");
            System.out.println("Response is: " + response);
            Type typeOfHashMap = new TypeToken<Map<Location, Map<Location, Experience>>>() { }.getType();
            Map<Location, Map<Location, Experience>> newMap = gson.fromJson(response, typeOfHashMap); // This type must match TypeToken
            Map<Location, Map<Location, Experience>> mappedMap = AgentEnvironmentLocationConverter.convertMemoryHashMap(newMap, startingState);
            return new SpatialExperienceMemory(mappedMap);
        }
        catch (Exception e) {
            System.out.println("Did not understand response. " + e.getMessage());
        }
        return null;
    }
    
    public ArrayList<Location> whatIsYourOptimalPath() {
        try {
            String response = getFromUrl("/optimalPath");
            System.out.println("Response is: " + response);
            Type typeOfList = new TypeToken<ArrayList<Location>>() { }.getType();
            ArrayList<Location> list = gson.fromJson(response, typeOfList); 
            return AgentEnvironmentLocationConverter.convertArrayList(list, startingState);
        }
        catch (Exception e) {
            System.out.println("Did not understand response. " + e.getMessage());
        }
        return null;
    }
    

    public String hello() {
        try {
            String name = getFromUrl("/hello");
            System.out.println("Response is: " + name);
            return name;
        }
        catch (Exception e) {
            System.out.println("Did not understand response. " + e.getMessage());
        }
        return "";
    }
    
    private String getFromUrl(String urlString) throws Exception {
        HttpResponse httpResponse = null;
        HttpGet getMethod = new HttpGet(url + urlString);
        
        try {
            httpResponse = httpClient.execute(getMethod);
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity);
            
        } catch (IOException e) {
            System.out.println("Problem reading from client: " + e.getMessage());
        }
        return null;
    }
    
    private String postToUrl(String urlString, String jsonString) {
        
        StringEntity requestEntity = new StringEntity(
            jsonString,
            ContentType.APPLICATION_JSON);

        HttpPost postMethod = new HttpPost(url + urlString);
        postMethod.setEntity(requestEntity);
        try {
            HttpResponse httpResponse = httpClient.execute(postMethod);
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            System.out.println("Problem reading from client: " + e.getMessage());
        }
        return null;
    }

    private void initaliseAgent() {
        try {
            String response = getFromUrl("/init");
            System.out.println("Response is: " + response);
        }
        catch (Exception e) {
            System.out.println("Did not understand response. " + e.getMessage());
        }
    }
}
