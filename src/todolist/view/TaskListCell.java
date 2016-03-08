package todolist.view;

import javafx.scene.control.ListCell;
import todolist.model.TaskWrapper;

public class TaskListCell extends ListCell<TaskWrapper> {

    @Override
    public void updateItem(TaskWrapper task, boolean empty) {
        if (task != null) {
            super.updateItem(task, empty);
            TaskNode taskNode = new TaskNode(task, this.getIndex());
            setGraphic(taskNode.getNode());
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
