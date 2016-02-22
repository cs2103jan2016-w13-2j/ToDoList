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

	public Name getName() {
		return name;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
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
