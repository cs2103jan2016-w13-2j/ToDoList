package todolist;

import java.sql.Time;

public class Task {
	private Name name;
	private Time startTime;
	private Time endTime;
	private Category category;
	private Priority priority;

	public Task(Name name, Time startTime, Time endTime, Category category, Priority priority, Reminder reminder) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.category = category;
		this.priority = priority;
		this.reminder = reminder;
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
}
