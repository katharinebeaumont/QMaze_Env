package qmaze.View.Maze;

import qmaze.View.Maze.MazeAgent;
import java.util.ArrayList;

/**
 *
 * @author katharine
 */
public class MazeConfig {
    private final int episodes;
    private final int rows;
    private final int columns;
    private final ArrayList<MazeAgent> agents;
    
    public MazeConfig(int episodes, int rows, int columns, ArrayList<MazeAgent> agents) {
        this.episodes = episodes;
        this.rows = rows;
        this.columns = columns;
        this.agents = agents;
    }
    
    public int getEpisodes() {
        return episodes;
    }
    public int getRows() {
        return rows;
    } 
    public int getColumns() {
        return columns;
    } 
    public ArrayList<MazeAgent> getAgents() {
        return agents;
    } 
    
    @Override
    public String toString() {
        return "Episodes: " + episodes + ", for " + agents.size() + " agents "
                + "\n and " + rows + " rows by " + columns + " columns.";
        
    }
}
