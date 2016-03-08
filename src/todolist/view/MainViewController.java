package todolist.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import todolist.MainApp;
import todolist.model.Category;
import todolist.model.Command;
import todolist.model.Logic;
import todolist.model.Name;
import todolist.model.Reminder;
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

    // private static final String MESSAGE_EMPTY_LIST = "No Content To Show";

    /*** MODEL DATA ***/
    private ObservableList<TaskWrapper> tasksToDisplay = null;

    private ArrayList<Task> taskListStub = new ArrayList<Task>();

    /*** MAIN APP ***/
    private MainApp mainApplication = null;

    /*** VIEWS ***/
    @FXML
    private ListView<TaskWrapper> listView = null;

    /*** CORE CONTROLLER FUNCTIONS ***/
    public MainViewController() {

        tasksToDisplay = FXCollections.observableArrayList();

        listView = new ListView<TaskWrapper>();

        taskListStub.add(new Task(new Name("Proposal meeting"), LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                new Category("Meetings"), new Reminder(true, LocalDateTime.now()), false));

        taskListStub.add(new Task(new Name("Scrum discussion"), LocalDateTime.now().plusHours(21),
                LocalDateTime.now().plusHours(22), new Category("New Release Project"),
                new Reminder(true, LocalDateTime.now()), false));

        taskListStub.add(new Task(new Name("Buy coffee"), null, null, new Category("Personal"),
                new Reminder(true, LocalDateTime.now()), false));

        taskListStub.add(new Task(new Name("Module submission"), null, LocalDateTime.now().plusHours(154),
                new Category("Deadlines"), new Reminder(false, LocalDateTime.now()), false));

        taskListStub.add(new Task(new Name("Send email"), null, LocalDateTime.now().minusHours(2),
                new Category("Tasks"), new Reminder(false, LocalDateTime.now()), false));

        setTasks(taskListStub);
    }

    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
    }

    @FXML
    public void initialize() {
        initTaskListView();
    }

    public void setCommandLineCallback(TextField commandField) {
        // Set Callback for TextField
        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String commandString = commandField.getText();
                Command command = new Command(commandString);
                System.out.println(command.getCommand());

                // Pass Command Line input for processing
                mainApplication.handler.process(command);

            }
        };

        commandField.setOnAction(commandHandler);
    }

    /*** VIEW GETTERS-SETTERS-LOADERS ***/

    public ListView<TaskWrapper> getTaskListView() {
        return listView;
    }

    public void populateTaskListView() {
        mainApplication.handler.uiHandler.refresh();
        // listView.setItems(tasksToDisplay);
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

    /*** MODEL GETTERS-SETTERS-RELOADERS ***/

    public ObservableList<TaskWrapper> getTasks() {
        return tasksToDisplay;
    }

    public void setTasks(ArrayList<Task> tasks) {
        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        for (int i = 0; i < tasks.size(); ++i) {
            // ... Convert Task to TaskWrapper
            TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
            arrayOfWrappers.add(wrappedTask);

        }

        listView.getItems().addAll(arrayOfWrappers);

    }

}
