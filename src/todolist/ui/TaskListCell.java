package todolist.ui;

import javafx.scene.control.ListCell;
import todolist.ui.controllers.TaskNodeController;

//@@author A0123994W

/*
 * TaskListCell formats task views by assigning every task node to a controller.
 * 
 * @author Huang Lie Jun (A0123994W)
 * 
 */
public class TaskListCell extends ListCell<TaskWrapper> {

    /*
     * (non-Javadoc)
     * 
     * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
     */
    @Override
    public void updateItem(TaskWrapper task, boolean empty) throws IllegalArgumentException {
        // Only displays tasks if they are not empty
        if (task != null) {
            super.updateItem(task, empty);

            // Link task to graphic node
            try {
                
                TaskNodeController taskNode = new TaskNodeController(task, this.getIndex());
                setGraphic(taskNode.getNode());
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
                throw iae;
            }
        } else {
            nullifyItem();
        }
    }

    /*
     * nullifyItem hides empty task views.
     */
    private void nullifyItem() {
        setText(null);
        setGraphic(null);
    }
}
