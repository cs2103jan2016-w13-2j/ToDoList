package todolist.ui.controllers;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import todolist.common.UtilityLogger.Component;
import todolist.model.Task;
import todolist.ui.TaskWrapper;

//@@author huangliejun

/* 
 * ArchiveController controls and manipulates data for display on the main display area, for the archive tab.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class ArchiveController extends MainViewController {

    // Logger messages
    private static final String MESSAGE_UPDATED_ARCHIVED_TASKLIST = "Updated display task list [DONE].";

    /*
     * Constructor overrides super constructor and intializes the display task
     * list and list view.
     * 
     */
    public ArchiveController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    /*
     * (non-Javadoc)
     * @see todolist.ui.controllers.MainViewController#initialize()
     */
    @FXML
    public void initialize() {
        initTaskListView();
    }

    /*
     * (non-Javadoc)
     * @see todolist.ui.controllers.MainViewController#setTasks(java.util.ArrayList)
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
            if (task.getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(task);
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
        logger.logAction(Component.UI, MESSAGE_UPDATED_ARCHIVED_TASKLIST);
    }
}
