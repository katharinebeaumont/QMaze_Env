package qmaze.View.Components;

import qmaze.View.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author katharine
 */
public class StartTrainingComponent extends Component {

    private final Button btnTraining = new Button();
    
    public StartTrainingComponent(ViewController controller) {
        super(controller);
    }
    
    @Override
    public Pane build() {
        HBox hbox = new HBox();
        btnTraining.setDisable(true);
        btnTraining.setText("Start training");
        btnTraining.setOnAction((ActionEvent event) -> {
            controller.startTraining();
        });
        hbox.getChildren().add(btnTraining);
        return hbox;
    }

    @Override
    public void reset() {
        if (controller.STATE.equals(AGENT_STATE)) {
            btnTraining.setDisable(false);
        } else if (controller.STATE.equals(RESET_STATE)){
            btnTraining.setDisable(true);
        }
    }
}
