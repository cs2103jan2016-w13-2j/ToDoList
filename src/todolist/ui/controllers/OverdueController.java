package todolist.ui.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import todolist.model.Task;
import todolist.ui.TaskWrapper;

//@@author Huang Lie Jun

public class OverdueController extends MainViewController {

    public OverdueController() {
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
            if (task.getEndTime() != null && task.getEndTime().isBefore(LocalDateTime.now()) && !task.getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
    }
}
