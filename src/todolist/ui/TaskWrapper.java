package todolist.ui;

import java.time.LocalDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import todolist.model.Category;
import todolist.model.Reminder;
import todolist.model.Task;

//@@author A0123994W

/*
 * TaskWrapper is the wrapper class for Task class. It wraps each attribute into a property for display.
 * 
 * @author Huang Lie Jun (A0123994W)
 * 
 */
public class TaskWrapper {

    private Task task;
    private StringProperty taskTitle;
    private ObjectProperty<LocalDateTime> startTime;
    private ObjectProperty<LocalDateTime> endTime;
    private ObjectProperty<Category> category;
    private ObjectProperty<Reminder> reminder;
    private ObjectProperty<Boolean> isDone;
    private ObjectProperty<Boolean> isRecurring;
    private ObjectProperty<String> interval;

    /*
     * Constructor builds the TaskWrapper class with the given task.
     * 
     * @param Task task is the task being wrapped around
     * 
     */
    public TaskWrapper(Task task) {
        this.taskTitle = new SimpleStringProperty(task.getName().getName());
        this.startTime = new SimpleObjectProperty<LocalDateTime>(task.getStartTime());
        this.endTime = new SimpleObjectProperty<LocalDateTime>(task.getEndTime());
        this.category = new SimpleObjectProperty<Category>(task.getCategory());
        this.reminder = new SimpleObjectProperty<Reminder>(task.getReminder());
        this.isDone = new SimpleObjectProperty<Boolean>(task.getDoneStatus());
        this.isRecurring = new SimpleObjectProperty<Boolean>(task.getRecurringStatus());
        this.interval = new SimpleObjectProperty<String>(task.getInterval());
        this.task = task;
    }

    /*** GETTER-SETTER FUNCTIONS ***/

    /*
     * getTaskTitleProperty returns the string property of the title.
     * 
     * @return StringProperty taskTitle
     */
    public StringProperty getTaskTitleProperty() {
        return taskTitle;
    }

    /*
     * setTaskTitle takes in a string and sets it as the task title.
     * 
     * @param String taskTitle
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle.set(taskTitle);
    }

    /*
     * getTaskTitle returns the task title.
     * 
     * @return String taskTitle
     */
    public String getTaskTitle() {
        return taskTitle.get();
    }

    /*
     * getStartTimeProperty returns the property of the start time.
     * 
     * @return ObjectProperty<LocalDateTime> startTime
     */
    public ObjectProperty<LocalDateTime> getStartTimeProperty() {
        return startTime;
    }

    /*
     * setStartTime sets the given startTime as the task start time.
     * 
     * @param LocalDateTime startTime
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    /*
     * getStartTime returns the task start time.
     * 
     * @return LocalDateTime startTime
     * 
     */
    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    /*
     * getEndTimeProperty returns the property of the end time.
     * 
     * @return ObjectProperty<LocalDateTime> endTime
     */
    public ObjectProperty<LocalDateTime> getEndTimeProperty() {
        return endTime;
    }

    /*
     * setEndTime sets the given endTime as the task end time.
     * 
     * @param LocalDateTime endTime
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    /*
     * getEndTime returns the task end time.
     * 
     * @return LocalDateTime endTime
     * 
     */
    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    /*
     * getCategoryProperty returns the property of the category.
     * 
     * @return ObjectProperty<Category> category
     */
    public ObjectProperty<Category> getCategoryProperty() {
        return category;
    }

    /*
     * setCategory sets the task category as the given category.
     * 
     * @param Category category
     * 
     */
    public void setCategory(Category category) {
        this.category.set(category);
    }

    /*
     * getCategory returns the category of the task.
     * 
     * @return Category category
     * 
     */
    public Category getCategory() {
        return category.get();
    }

    /*
     * getReminderProperty returns the property of the reminder.
     * 
     * @return ObjectProperty<Reminder> reminder
     */
    public ObjectProperty<Reminder> getReminderProperty() {
        return reminder;
    }

    /*
     * setReminder sets the task reminder as the given reminder.
     * 
     * @param Reminder reminder
     * 
     */
    public void setReminder(Reminder reminder) {
        this.reminder.set(reminder);
    }

    /*
     * getReminder returns the task reminder.
     * 
     * @return Reminder reminder
     * 
     */
    public Reminder getReminder() {
        return reminder.get();
    }

    /*
     * getIsCompletedProperty returns the property of the completion status.
     * 
     * @return ObjectProperty<Boolean> isDone
     */
    public ObjectProperty<Boolean> getIsCompletedProperty() {
        return isDone;
    }

    /*
     * setIsCompleted sets the completion status of the task.
     * 
     * @param ObjectProperty<Boolean> isDone
     */
    public void setIsCompleted(ObjectProperty<Boolean> isDone) {
        this.isDone = isDone;
    }

    /*
     * getIsCompleted returns the completion status of the task.
     * 
     * @return Boolean isDone
     */
    public Boolean getIsCompleted() {
        return isDone.get();
    }

    /*
     * getRecurringStatusProperty returns the property of the recurrence status.
     * 
     * @return ObjectProperty<Boolean> isRecurring
     */
    public ObjectProperty<Boolean> getRecurringStatusProperty() {
        return isRecurring;
    }

    /*
     * setIsRecurring sets the recurrence status of the task.
     * 
     * @param ObjectProperty<Boolean> isRecurring
     * 
     */
    public void setIsRecurring(ObjectProperty<Boolean> isRecurring) {
        this.isRecurring = isRecurring;
    }

    /*
     * getIsRecurring returns the recurrence status of the task.
     * 
     * @return Boolean isRecurring
     */
    public Boolean getIsRecurring() {
        return isRecurring.get();
    }

    /*
     * getIntervalProperty returns the property of the recurrence interval.
     * 
     * @return ObjectProperty<String> interval
     */
    public ObjectProperty<String> getIntervalProperty() {
        return interval;
    }

    /*
     * setInterval sets the recurrence interval of the task.
     * 
     * @param ObjectProperty<String> interval
     * 
     */
    public void setInterval(ObjectProperty<String> interval) {
        this.interval = interval;
    }

    /*
     * getInterval returns the recurrence interval of the task.
     * 
     * @return String interval
     * 
     */
    public String getInterval() {
        return interval.get();
    }

    /*
     * getTaskObject returns the task object reference.
     * 
     * @return Task task
     * 
     */
    public Task getTaskObject() {
        return task;
    }

    public boolean getIsExpired() {
        return getEndTime() != null && getEndTime().isBefore(LocalDateTime.now());
    }
}
