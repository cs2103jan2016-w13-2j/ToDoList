package todolist.view;

import java.io.IOException;
import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import todolist.MainApp;
import todolist.model.TaskWrapper;

public class TaskNode {
    
    private static enum Priority {
        URGENT, NORMAL, CASUAL
    };
    
    private TaskWrapper task = null;
    private int index = -1;
    
    @FXML
    private HBox root = null;
    
    
    @FXML
    private Rectangle priorityLabel = null;
    
    
    @FXML
    private StackPane numberLabel = null;
    @FXML
    private Circle numLabelBase = null;
    @FXML
    private Label number = null;
    
    
    @FXML
    private VBox details = null;
    
    @FXML
    private HBox titleBox = null;
    @FXML
    private Label title = null;
    @FXML
    private ImageView reminderIcon = null;
    
    @FXML
    private HBox dateRangeBox = null;
    @FXML
    private Circle overdueFlag = null;
    @FXML
    private Label dateRange = null;
    
    @FXML
    private HBox categoryBox = null;
    @FXML
    private Circle categorySprite = null;
    @FXML
    private Label category = null;
    
    
    @FXML
    private CheckBox checkbox = null;

    public TaskNode(TaskWrapper task, int index) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainApp.DIRECTORY_TASKITEM));
        fxmlLoader.setController(this);
        
        this.task = task;
        this.index = index;
        
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        // Assignment TaskWrapper to HBox Layout
        mapTaskToNode();
        
    }

    public void mapTaskToNode() {
        number.setText(Integer.toString(index + 1));
        title.setText(task.getTaskTitle());
        dateRange.setText(parseDateTimeRange(task));
        category.setText(task.getCategory().getCategory());
    }

    public HBox getNode() {
        return root;
    }
    
    public String parseDateTimeRange(TaskWrapper task) {
        return new String("date to be parsed");
    }
    
}
