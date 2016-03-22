package todolist.ui;

import java.time.LocalDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import todolist.model.Category;
import todolist.model.Reminder;
import todolist.model.Task;

public class TaskWrapper {

    private StringProperty taskTitle;
    private ObjectProperty<LocalDateTime> startTime;
    private ObjectProperty<LocalDateTime> endTime;
    private ObjectProperty<Category> category;
    private ObjectProperty<Reminder> reminder;
    private ObjectProperty<Boolean> isDone;

    public TaskWrapper(Task task) {
        this.taskTitle = new SimpleStringProperty(task.getName().getName());
        this.startTime = new SimpleObjectProperty<LocalDateTime>(task.getStartTime());
        this.endTime = new SimpleObjectProperty<LocalDateTime>(task.getEndTime());
        this.category = new SimpleObjectProperty<Category>(task.getCategory());
        this.reminder = new SimpleObjectProperty<Reminder>(task.getReminder());
        this.isDone = new SimpleObjectProperty<Boolean>(task.getDoneStatus());
    }

    public StringProperty taskTitleProperty() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle.set(taskTitle);
    }

    public String getTaskTitle() {
        return taskTitle.get();
    }

    public ObjectProperty<LocalDateTime> startTimeProperty() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    public ObjectProperty<LocalDateTime> endTimeProperty() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    public ObjectProperty<Category> categoryProperty() {
        return category;
    }

    public void setCategory(Category category) {
        this.category.set(category);
    }

    public Category getCategory() {
        return category.get();
    }

    public ObjectProperty<Reminder> reminderProperty() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder.set(reminder);
    }

    public Reminder getReminder() {
        return reminder.get();
    }

    public ObjectProperty<Boolean> getIsDoneProperty() {
        return isDone;
    }

    public void setIsDone(ObjectProperty<Boolean> isDone) {
        this.isDone = isDone;
    }

    public Boolean getIsDone() {
        return isDone.get();
    }

}
