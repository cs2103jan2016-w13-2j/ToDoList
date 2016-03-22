package todolist.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Task {
<<<<<<< Updated upstream
	private Name name = null;
	private LocalDateTime startTime = null;
	private LocalDateTime endTime = null;
	private Category category = null;
	private Reminder reminder = null;
	private Boolean isDone = null;
	private Boolean isRecurring = null;
	private String interval = null;

	public Task(Name name, LocalDateTime startTime, LocalDateTime endTime, Category category, Reminder reminder,
			Boolean isDone, Boolean recurring, String interval) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.category = category;
		this.reminder = reminder;
		this.isDone = isDone;
		this.isRecurring = recurring;
		this.interval = interval;
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

	public void setRecurring(Boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	///////////////////////////////////////////

	public Name getName() {
		return name;
	}

	public LocalDateTime getStartTime() {
		if (isRecurring) {
			String temp[] = interval.split(" ");
			String length = temp[0];
			String unit = temp[1];
			LocalDateTime now = LocalDateTime.now();
			while(now.isAfter(startTime)) {
				startTime = startTime.plus(Long.parseLong(length), generateTimeUnit(unit));
			}
			return startTime;
		} else {
			return startTime;
		}
	}

	public LocalDateTime getEndTime() {
		if (isRecurring) {
			String temp[] = interval.split(" ");
			String length = temp[0];
			String unit = temp[1];
			
			if(startTime != null) {
				while(startTime.isAfter(endTime)) {
					endTime = endTime.plus(Long.parseLong(length), generateTimeUnit(unit));
				}
				return endTime;
			} else {
				LocalDateTime now = LocalDateTime.now();
				while(now.isAfter(endTime)) {
					endTime = endTime.plus(Long.parseLong(length), generateTimeUnit(unit));
				}
				return endTime;
			}
		} else {
			return endTime;
		}
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

	public Boolean getRecurringStatus() {
		return isRecurring;
	}

	public String getInterval() {
		return interval;
	}
	
	private TemporalUnit generateTimeUnit(String unit) {
		switch (unit) {
		case "day":
			return ChronoUnit.DAYS;
		case "hour":
			return ChronoUnit.HOURS;
		case "minute":
			return ChronoUnit.MINUTES;
		case "week":
			return ChronoUnit.WEEKS;
		case "month":
			return ChronoUnit.MONTHS;
		case "year":
			return ChronoUnit.YEARS;
		default:
			return null;
		}
	}
=======
    private Name name = null;
    private LocalDateTime startTime = null;
    private LocalDateTime endTime = null;
    private Category category = null;
    private Reminder reminder = null;
    private Boolean isDone = null;
    private Boolean recurring = null;

    public Task(Name name, LocalDateTime startTime, LocalDateTime endTime, Category category, Reminder reminder,
            Boolean archive, Boolean recurring) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.reminder = reminder;
        this.isDone = archive;
        this.recurring = recurring;
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
        System.out.println(isDone);
        return isDone;
    }

    public Boolean getRecurringStatus() {
    	return recurring;
    }
>>>>>>> Stashed changes
}