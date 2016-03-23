package todolist.ui.controllers;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import todolist.model.Task;
import todolist.ui.TaskWrapper;

public class ArchiveController extends MainViewController {

    public ArchiveController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    @FXML
    public void initialize() {
        initTaskListView();
    }

    @Override
    public void setTasks(ArrayList<Task> tasks) {

        // List provided by logic must be valid
        assert (tasks != null);

        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            Task task = tasks.get(i);
            if (task.getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(task);
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
    }
}
