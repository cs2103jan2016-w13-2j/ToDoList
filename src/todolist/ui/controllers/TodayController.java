package todolist.ui.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import todolist.model.Task;
import todolist.ui.TaskWrapper;

//@@author huangliejun

public class TodayController extends MainViewController {

    /*
     * TodayController controls and formats data for the Today page
     * 
     * @author Huang Lie Jun A0123994W
     */
    public TodayController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see todolist.ui.controllers.MainViewController#initialize()
     */
    @FXML
    public void initialize() {
        initTaskListView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * todolist.ui.controllers.MainViewController#setTasks(java.util.ArrayList)
     */
    @Override
    public void setTasks(ArrayList<Task> tasks) {

        // List provided by logic must be valid
        assert (tasks != null);

        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            Task task = tasks.get(i);
            if (isToday(task) && !isCompleted(task)) {
                TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
    }

    /*
     * isToday is a utility function that checks if a given task is due today.
     * 
     * @param Task task is the given task to check
     * 
     * @return boolean isToday
     * 
     */
    private boolean isToday(Task task) {
        return task.getEndTime() != null && task.getEndTime().getYear() == LocalDateTime.now().getYear()
                && task.getEndTime().getDayOfYear() == LocalDateTime.now().getDayOfYear();
    }
}
