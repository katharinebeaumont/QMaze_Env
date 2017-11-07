package qmaze.View.Components;

import java.util.ArrayList;
import qmaze.View.ViewController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.util.StringConverter;
import qmaze.View.Maze.MazeAgent;
import qmaze.View.Maze.MazeConfig;

/**
 *
 * @author katharine
 */
public class MazeConfigComponent extends Component {
    
    private MazeConfig config;
    
    private final int MAX_HEIGHT = 150;
    private final int MAX_EPISODES = 250;
    
    private final int initialRows = 4;
    private final int initialColumns = 4;
    private final int initialEpisodes = 50;
    final SpinnerValueFactory.IntegerSpinnerValueFactory mazeSpinnerRows = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,16,initialRows);
    final SpinnerValueFactory.IntegerSpinnerValueFactory mazeSpinnerColumns = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,16,initialColumns);
    final SpinnerValueFactory.IntegerSpinnerValueFactory mazeSpinnerEpisodes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,MAX_EPISODES,initialEpisodes);
    private TextArea agentNameList;
    
    private ArrayList<MazeAgent> agents;
    
    public MazeConfigComponent(ViewController controller) {
        super(controller);
        resetConfig();
        agents = new ArrayList<MazeAgent>();
    }
    
    private void resetConfig() {
        config = new MazeConfig(mazeSpinnerEpisodes.getValue(), 
                mazeSpinnerRows.getValue(), mazeSpinnerColumns.getValue(), 
                agents);
        controller.configurationReset(config);
    }

    private void resetEpisodes() {
        config = new MazeConfig(mazeSpinnerEpisodes.getValue(), 
                mazeSpinnerRows.getValue(), mazeSpinnerColumns.getValue(),
                agents);
        controller.episodesReset(config);
    }
    
    private void resetAgents() {
        config = new MazeConfig(mazeSpinnerEpisodes.getValue(), 
                mazeSpinnerRows.getValue(), mazeSpinnerColumns.getValue(),
                agents);
        controller.agentReset(config);
    }
    
    @Override
    public Pane build() {
        FlowPane flow = new FlowPane(Orientation.HORIZONTAL);
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setStyle(assets.getRichGreenBackground());
        flow.setMaxHeight(MAX_HEIGHT);
        
        HBox hboxsp = buildSpinners();
        HBox hboxUrl = buildUrl();
        flow.getChildren().addAll(hboxsp,hboxUrl);
        
        return flow;
    }
    
    private HBox buildSpinners() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        Spinner rowSpinner = new Spinner(mazeSpinnerRows);
        Spinner columnSpinner = new Spinner(mazeSpinnerColumns);
        Spinner episodeSpinner = new Spinner(mazeSpinnerEpisodes);
        episodeSpinner.setEditable(true);
        addEpisodeValidation();
        
        final Label labelRows = new Label("Rows");
        final Label labelCols = new Label("Columns");
        final Label labelEpisodes = new Label("Episodes");
        rowSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            resetConfig();   
        });
        columnSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            resetConfig();   
        });
        episodeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (validEpisodeInput(newValue)) {
                mazeSpinnerEpisodes.setValue((int)newValue);
            } else {
                mazeSpinnerEpisodes.setValue((int)oldValue);
            }
            resetEpisodes();
        });
        
        
        hbox.getChildren().addAll(labelRows, rowSpinner, labelCols, columnSpinner, labelEpisodes, episodeSpinner);
        return hbox;
    }

    private boolean validEpisodeInput(Object newValue) {
        return 0 < (int)newValue && (int)newValue <= MAX_EPISODES;
    }
    
    private void addEpisodeValidation() {
    
        StringConverter<Integer> sc = new StringConverter<Integer>() {
           @Override
           public Integer fromString(String value)
           {
              try {
                 return Integer.parseInt(value);
              }
              catch (NumberFormatException nfe) {
                 System.out.println("Bad integer: " + value);
                 return initialEpisodes;
              }
           }

           @Override
           public String toString(Integer value) {
              return value.toString();
           }
        };
        mazeSpinnerEpisodes.setConverter(sc);
    }
    
    private HBox buildUrl() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        
        Label label = new Label("Add agents (url): ");
        TextField text = new TextField();
        text.setText("http://localhost:4567");
        Button btnSubmit = new Button("Submit");
        agentNameList = new TextArea();
        agentNameList.setMaxHeight(50);
        agentNameList.setMinWidth(200);
        agentNameList.setMaxWidth(200);
        agentNameList.setEditable(false);
        
        btnSubmit.setOnAction((ActionEvent eventOpt) -> {
            String url = text.getText();
            String name = controller.validateUrl(url);
            if (!name.isEmpty() && validateAgentName(name)) {
                MazeAgent agent = buildAgent(url, name);
                agents.add(agent);

                System.out.println("Adding agent " + name + " from " + url);
                String existingNames = agentNameList.getText();
                if (existingNames.isEmpty()) {
                    existingNames += name;
                } else {
                    existingNames += ", " + name;
                }
                agentNameList.setText(existingNames);
                text.setText("");
                resetAgents();
            }
        });
        hbox.getChildren().addAll(label, text, btnSubmit, agentNameList); 
        return hbox;
        
    }
    
    private boolean validateAgentName(String name) {
        for (MazeAgent agent: agents) {
            if (agent.getName().equals(name)) {
                controller.showAlert("Name already in use", "Sorry, " + name + " is taken.");
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void reset() {
        if (controller.STATE.equals(RESET_STATE)) {
            mazeSpinnerEpisodes.setValue(initialEpisodes);
            mazeSpinnerRows.setValue(initialRows);
            mazeSpinnerColumns.setValue(initialColumns);
            agentNameList.setText("");
            agents = new ArrayList<>();
            resetConfig();    
        }
    }

    private MazeAgent buildAgent(String url, String name) {
        ImagePattern agentImage = new ImagePattern(new Image(url + "/agent.png"));
        ImagePattern agentAtGoal = new ImagePattern(new Image(url + "/agentAtGoal.png"));
        ImagePattern agentDeath = new ImagePattern(new Image(url + "/agentDeath.png"));
        return new MazeAgent(url, name, agentImage, agentAtGoal, agentDeath);
    }
}
