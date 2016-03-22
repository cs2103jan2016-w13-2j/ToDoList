package todolist.ui.controllers;

import javafx.scene.control.ListCell;
import todolist.ui.TaskWrapper;

public class TaskListCell extends ListCell<TaskWrapper> {

    @Override
    public void updateItem(TaskWrapper task, boolean empty) throws IllegalArgumentException {
        if (task != null) {
            super.updateItem(task, empty);
            
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

    private void nullifyItem() {
        setText(null);
        setGraphic(null);
    }
}
