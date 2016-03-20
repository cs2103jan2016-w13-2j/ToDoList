package todolist.view;

import java.util.ArrayList;

import todolist.MainApp;
import todolist.model.Task;
import todolist.model.TaskWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MainViewController {

    /*** MODEL DATA ***/
    private ObservableList<TaskWrapper> tasksToDisplay = null;

    /*** MAIN APP ***/
    private MainApp mainApplication = null;

    /*** VIEWS ***/
    @FXML
    private ListView<TaskWrapper> listView = null;

    
    /*** CORE CONTROLLER FUNCTIONS ***/
    
    public MainViewController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
    }

    @FXML
    public void initialize() {
        initTaskListView();
    }

    public void initTaskListView() {
        listView.setCellFactory(new Callback<ListView<TaskWrapper>, javafx.scene.control.ListCell<TaskWrapper>>() {
            @Override
            public ListCell<TaskWrapper> call(ListView<TaskWrapper> listView) {
                return new TaskListCell();
            }
        });

        VBox.setVgrow(listView, Priority.ALWAYS);
        HBox.setHgrow(listView, Priority.ALWAYS);

    }

    public void setCommandLineCallback(TextField commandField) {
        // Set Callback for TextField
        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String commandString = commandField.getText();
                //Command command = new Command(commandString);
                //System.out.println(command.getCommand());

                // Pass Command Line input for processing
                try {
                    mainApplication.logicUnit.process(commandString);
                } catch (Exception e) {

                }

            }
        };

        commandField.setOnAction(commandHandler);
    }

    
    /*** VIEW GETTERS-SETTERS-LOADERS ***/

    public ListView<TaskWrapper> getTaskListView() {
        return listView;
    }

    public void populateTaskListView() {
        mainApplication.logicUnit.uiHandler.refresh();
    }

    
    /*** MODEL GETTERS-SETTERS-RELOADERS ***/

    public ObservableList<TaskWrapper> getTasks() {
        return tasksToDisplay;
    }

    public void setTasks(ArrayList<Task> tasks) {
        
        // List provided by logic must be valid
        assert(tasks != null);
        
        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
            arrayOfWrappers.add(wrappedTask);
        }

        listView.getItems().addAll(arrayOfWrappers);
    }

}
