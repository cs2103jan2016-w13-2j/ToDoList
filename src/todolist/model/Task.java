package todolist.model;

import java.time.LocalDateTime;

public class Task {
	private Name name = null;
	private LocalDateTime startTime = null;
	private LocalDateTime endTime = null;
	private Category category = null;
	private Reminder reminder = null;
	private Boolean isDone = null;

	public Task(Name name, LocalDateTime startTime, LocalDateTime endTime, Category category, Reminder reminder, Boolean isDone) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.category = category;
		this.reminder = reminder;
		this.isDone = isDone;
	}

    public void setName(Name name) {
		this.name = name;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public void setReminder(Reminder reminder) {
	    this.reminder = reminder;
	}
	
	public void setDoneStatus(Boolean isDone) {
	    this.isDone = isDone;
	}
	
	///////////////////////////////////////////
	
	public Name getName() {
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
	
	public Reminder getReminder() {
	    return reminder;
	}
	
	public Boolean getDoneStatus() {
	    return isDone;
	}
	
	/////////////////////////////////////////////
	
	public void changeByField(String fieldName, String newContent) {
	    
	}
}
