package qmaze.View.Components;

import javafx.scene.control.Alert;
import javafx.scene.shape.Rectangle;
import qmaze.View.Assets;

/**
 *
 * @author katharine
 */
public class AlertPopup {

    private final Assets assets = Assets.getInstance();
    
    private AlertPopup() {
        
    }
            
    private void build(String header, String message) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle("Bad News");
       alert.setHeaderText(header);
       Rectangle r = new Rectangle(50,50);
       r.setFill(assets.getAgentDeathImage());
       alert.setGraphic(r);
       alert.setContentText(message);
       alert.showAndWait();
    }
    
    public static void popup(String header, String message) {
        AlertPopup popup = new AlertPopup();
        popup.build(header, message);
    }
}
