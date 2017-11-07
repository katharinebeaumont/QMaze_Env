package qmaze.View.Components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import qmaze.View.LearningGrid.LearningGridPopup;
import qmaze.View.LearningGrid.MazeLearning;
import qmaze.View.Maze.MazeAgent;
import qmaze.View.ViewController;

/**
 *
 * @author katharine
 */
public class ShowLearningsPopupComponent extends Component {

    private static final int WIDTH = 350;
    private static final int HEIGHT = 600;
    private Button btnShowLearnings;
    private ArrayList<LearningGridPopup> components;
    
    public ShowLearningsPopupComponent(ViewController controller) {
        super(controller);
    }
        
    @Override
    public Pane build() {
        HBox hbox = new HBox();
        btnShowLearnings = new Button();
        btnShowLearnings.setText("Show Learnings");
        hbox.getChildren().add(btnShowLearnings);
        
        btnShowLearnings.setOnAction((ActionEvent event) -> {
            ArrayList<LearningGridPopup> components = new ArrayList<>();
            HashMap<MazeAgent, MazeLearning> learnings = controller.getLearnings();
        
            for (MazeAgent agent: learnings.keySet()) {
                LearningGridPopup component = new LearningGridPopup(agent.getName(), 
                        learnings.get(agent), controller.getGoalState(), assets);
                component.build();
                components.add(component);
            }
            
        });
        
        return hbox;
    }

    @Override
    public void reset() {
        if (controller.STATE.equals(TRAINED_STATE)) {
            btnShowLearnings.setDisable(false);
        } else {
            btnShowLearnings.setDisable(true);
            if (components != null) {
                for (LearningGridPopup grid: components) {
                    grid.reset();
                }
           }
        }
    }
  
}
