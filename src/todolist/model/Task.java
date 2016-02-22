package todolist.model;

import java.sql.Time;

public class Task {
	private Name name;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Category category;
	private Priority priority;
	private Reminder reminder;
	private Boolean isDone;

	public Task(Name name, LocalDateTime startTime, LocalDateTime endTime, Category category, Priority priority, Reminder reminder, Boolean isDone) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.category = category;
		this.priority = priority;
		this.reminder = reminder;
		this.isDone = isDone;
	}

    public void setName(Name name) {
		this.name = name;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public void setNameetEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public void setReminder(Reminder reminder) {
	    this.reminder = reminder;
	}
	
	public void setDoneStatus(Boolean isDone) {
	    this.isDone = isDone;
	}
	
	///////////////////////////////////////////
	
	public void getName() {
		return name;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public Category getCategory() {
		return category;
	}

	public Priority getPriority() {
		return priority;
	}
	
	public Reminder getReminder() {
	    return reminder;
	}
	
	public Boolean getDoneStatus() {
	    return isDone;
	}
}