package todolist.ui.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import todolist.common.UtilityLogger.Component;
import todolist.model.Task;
import todolist.ui.TaskWrapper;

//@@author A0123994W

/* 
 * OverdueController controls and manipulates data for display on the main display area, for the overdue tab.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class OverdueController extends MainViewController {

    // Logger messages
    private static final String MESSAGE_UPDATED_OVERDUE_TASKLIST = "Updated display task list [OVERDUE].";

    /*
     * Constructor overrides super constructor and intializes the display task
     * list and list view.
     * 
     */
    public OverdueController() {
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
            // Only display overdue tasks that are not yet completed
            if (isOverdue(task) && !isCompleted(task)) {
                TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
        logger.logAction(Component.UI, MESSAGE_UPDATED_OVERDUE_TASKLIST);
    }

    /*
     * isOverdue is a utility function that checks if a given task is expired by
     * checking that the end date-time is not past yet.
     * 
     * @param Task task is the task to check for expiry
     * 
     * @return boolean isOverdue is overdue flag
     * 
     */
    private boolean isOverdue(Task task) {
        return task.getEndTime() != null && task.getEndTime().isBefore(LocalDateTime.now());
    }
}
