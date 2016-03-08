package todolist.view;

import java.util.ArrayList;
import todolist.MainApp;
import todolist.model.Command;
import todolist.model.Logic;
import todolist.model.Task;
import todolist.model.TaskWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainViewController {

    /*** MODEL DATA ***/
    private ObservableList<TaskWrapper> tasksToDisplay = null;
    private ObservableList<String> tasksToDisplayStub = null;

    
    /*** MAIN APP ***/
    private MainApp mainApplication = null;
    
    
    /*** VIEWS ***/
    @FXML
    private ListView<String> listView = null;

    
    /*** CORE CONTROLLER FUNCTIONS ***/
    public MainViewController() {
        tasksToDisplay = FXCollections.observableArrayList();
        tasksToDisplayStub = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
        
        listView = new ListView<String>();
    }

    @FXML
    private void initialize() {

    }
    
    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
    }
    
    public void setCommandLineCallback(TextField commandField) {
        // Set Callback for TextField
        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {
            Logic handler = new Logic(mainApplication);

            @Override
            public void handle(ActionEvent event) {
                String commandString = commandField.getText();
                Command command = new Command(commandString);
                System.out.println(command.getCommand());

                // Pass Command Line input for processing
                handler.process(command);

            }
        };

        commandField.setOnAction(commandHandler);
    }

    
    /*** VIEW GETTERS-SETTERS-RELOADERS ***/
    
    public ListView<String> getListView() {
        return listView;
    }

    public void reloadTaskListView() {
        listView.setItems(tasksToDisplayStub);
    }
    
    
    /*** MODEL GETTERS-SETTERS-RELOADERS ***/

    public ObservableList<TaskWrapper> getTasks() {
        return tasksToDisplay;
    }

    public void setTasks(ArrayList<Task> tasks) {
        tasksToDisplay.clear();
        for (int i = 0; i < tasks.size(); ++i) {

            // ... Convert Task to TaskWrapper
            TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));

            tasksToDisplay.add(wrappedTask);
        }
    }
    
}
