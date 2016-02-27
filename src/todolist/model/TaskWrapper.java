package todolist.model;

//import java.sql.Time;
import java.time.LocalDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskWrapper {
	
//	private Name name;
//	private Time startTime;
//	private Time endTime;
//	private Category category;
//	private Priority priority;
//	private Reminder reminder;
	
	private StringProperty taskTitle;
	private ObjectProperty<LocalDateTime> startTime;
	private ObjectProperty<LocalDateTime> endTime;
	private ObjectProperty<Category> category;
	private ObjectProperty<Reminder> reminder;
	
//	public TaskWrapper(String task, LocalDateTime start, LocalDateTime end, Category category, Reminder reminder) {
//		this.taskTitle = new SimpleStringProperty(task);
//		this.startTime = new SimpleObjectProperty<LocalDateTime>(start);
//		this.endTime = new SimpleObjectProperty<LocalDateTime>(end);
//		this.category = new SimpleObjectProperty<Category>(category);
//		this.reminder = new SimpleObjectProperty<Reminder>(reminder);
//	}
	
	   public TaskWrapper(Task task) {
	        this.taskTitle = new SimpleStringProperty(task.getName().getName());
	        this.startTime = new SimpleObjectProperty<LocalDateTime>(task.getStartTime());
	        this.endTime = new SimpleObjectProperty<LocalDateTime>(task.getEndTime());
	        this.category = new SimpleObjectProperty<Category>(task.getCategory());
	        this.reminder = new SimpleObjectProperty<Reminder>(task.getReminder());
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
	
}
