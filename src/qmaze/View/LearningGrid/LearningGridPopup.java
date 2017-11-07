package qmaze.View.LearningGrid;

import java.util.HashMap;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qmaze.Environment.Location;
import qmaze.View.Assets;

/**
 *
 * @author katharine
 */
public class LearningGridPopup {

    private final BorderPane border = new BorderPane();
    private final String name;
    private final MazeLearning learning;
    private final Location goalState;
    private final Assets assets;

    public LearningGridPopup(String name, MazeLearning learning, Location goalState, Assets assets) {
        this.name = name;
        this.learning = learning;
        this.goalState = goalState;
        this.assets = assets;
    }
    
    
    public Pane build() {
        HashMap<Location, HashMap<Location,Double>> roomLearnings = learning.getLearnings();
        addQTable(roomLearnings, goalState);
        
        final Stage learningGridPopup = new Stage();
        learningGridPopup.initModality(Modality.NONE);
        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 300, 300);
        root.getChildren().add(border);
        
        learningGridPopup.setTitle("Learnings for " + name);
        learningGridPopup.setScene(scene);
        learningGridPopup.show();
        return null;
    }
    
    public void reset() {
         Node qTable = border.getCenter();
         border.getChildren().remove(qTable);
    }
    
    private Pane addQTable(HashMap<Location, HashMap<Location,Double>> roomLearnings, Location goalState) {
        
        if (roomLearnings.isEmpty()) {
            return border;
        }
        ScrollPane sp = new ScrollPane();
        
        GridPane grid = new GridPane();
        grid.setBorder(Border.EMPTY);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setStyle(assets.getLightGreenBackground()); 
        
        Set<Location> roomCoordinates = roomLearnings.keySet();
        for (Location roomCoordinate: roomCoordinates) {
            Pane textPane = new Pane();
            
            int columnIndex = roomCoordinate.getCol();
            int rowIndex = roomCoordinate.getRow();
            StringBuilder sb = new StringBuilder();
            sb.append("Room ");
            sb.append(rowIndex);
            sb.append(",");
            sb.append(columnIndex);
            sb.append("\n");
            HashMap<Location,Double> actions = roomLearnings.get(roomCoordinate);
            String toolTipText = "";
            if (goalState.equals(roomCoordinate)) {
                sb.append("GOAL");
                toolTipText = "Yay!";
                textPane.setStyle(assets.getGoalRoomBackground());
            } else if (actions == null || actions.isEmpty()) {
                sb.append("No info");
                toolTipText = "Maybe we didn't visit this room?";
                textPane.setStyle(assets.getUnvisitedRoomBackground());
            }
            else {
                for (HashMap.Entry<Location,Double> entry : actions.entrySet()) {
                    Location nextRoom = entry.getKey();
                    String qValueForText = String.format("%.2f", entry.getValue());
                    sb.append(qValueForText);
                    sb.append(getArrowDirection(roomCoordinate, nextRoom));
                    sb.append("\n");
                    textPane.setStyle(assets.getWhiteBackground());
                    String qValueForToolTip = String.format("%.6f", entry.getValue());
                    toolTipText = toolTipText + "Moving " + getDirectionDesc(roomCoordinate, nextRoom) + " for " + qValueForToolTip + "\n";
                }
            }
            Text t = new Text(sb.toString());
            
            Tooltip tp = new Tooltip(toolTipText);
            Tooltip.install(textPane, tp);
                    
            textPane.getChildren().add(t);
            textPane.setMinSize(60,60);
            textPane.setMaxSize(70,70);
            
            grid.add(textPane, columnIndex, rowIndex);
        }
        sp.setContent(grid);
        Text title = new Text("Q Table");
        border.setTop(title);
        border.setCenter(sp);
        return border;
    }
    
    private String getArrowDirection(Location currentRoom, Location nextRoom) {
        int currentRow = currentRoom.getRow();
        int currentColumn = currentRoom.getCol();
        int nextRow = nextRoom.getRow();
        int nextColumn = nextRoom.getCol();
        if (currentRow == nextRow && currentColumn > nextColumn) {
            return " <- ";
        } else if (currentRow == nextRow && currentColumn < nextColumn) {
            return " -> ";
        } else if (currentRow > nextRow && currentColumn == nextColumn) {
            return " ^ ";
        } else if (currentRow < nextRow && currentColumn == nextColumn) {
            return " v ";
        }
        return nextRoom.toString();
    }
    
    private String getDirectionDesc(Location currentRoom, Location nextRoom) {
        int currentRow = currentRoom.getRow();
        int currentColumn = currentRoom.getCol();
        int nextRow = nextRoom.getRow();
        int nextColumn = nextRoom.getCol();
        if (currentRow == nextRow && currentColumn > nextColumn) {
            return "left";
        } else if (currentRow == nextRow && currentColumn < nextColumn) {
            return "right";
        } else if (currentRow > nextRow && currentColumn == nextColumn) {
            return "up";
        } else if (currentRow < nextRow && currentColumn == nextColumn) {
            return "down";
        }
        return nextRoom.toString();
    }
    
}
