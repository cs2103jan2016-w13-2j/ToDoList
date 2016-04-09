package todolist.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import todolist.model.InputException;
import todolist.model.SearchCommand;
import todolist.model.Task;
import todolist.storage.DataBase;

public class FunctionChecker {
	private Logic logic;
	private DataBase dataBase;
	
	public FunctionChecker(Logic logic, DataBase dataBase) {
		this.logic = logic;
		this.dataBase = dataBase;
	}
	
	public InputException addTaskChecker(String title) {
		if(noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}
	
	public InputException addEventChecker(String title, String startDate, String startTime, String quantity, String timeUnit) {
		if(noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}
	
	public InputException addEventLessChecker(String title, String fuzzyTime, String quantity, String timeUnit) {
		if(noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}
	
	public InputException addDeadlineChecker(String title, String endDate, String endTime) {
		if(noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}
	
	public InputException addDeadlineLessChecker(String title, String fuzzyTime) {
		if(noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}
	
	public InputException addRecurringEventChecker(String interval, String title, String startDate, String startTime, String quantity,
			String timeUnit) {
		// TODO Auto-generated method stub
		return new InputException();
	}
	
	public InputException addRecurringEventLessChecker(String interval, String title, String fuzzyTime, String quantity,
			String timeUnit) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRecurringDeadlineChecker(String interval, String title, String endDate, String endTime) {
		// TODO Auto-generated method stub
		return new InputException();
	}
	
	public InputException addRecurringDeadlineLessChecker(String interval, String title, String fuzzyTime) {
		// TODO Auto-generated method stub
		return new InputException();
	}
	
	public InputException remindBefChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRemindBefChecker(String string, String string2, String[] restOfArgs) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException remindChecker(String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException forwardChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException postponeChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}
	
	public InputException redoChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException undoChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException undoneChecker(String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException doneChecker(String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	
	public InputException openChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException saveChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException invalidChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException tabChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException setRecurringChecker(String string, boolean b, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException labelChecker(String name, String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException sortChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException filterChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException searchChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException editChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}
	
	public InputException deleteChecker(String name) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	
	public InputException resetChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException exitChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRemindChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException removeRecurringChecker(String name, boolean b, String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}
	
	private Boolean noRepeat(String title) {
		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("NAME", title));
		if (tempTaskList.size() > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	private Boolean validDateTime(String date, String time) {
		String dateTime = date + " " + time;
		if(date == null){
			return false;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setLenient(false);
		
		try {
			Date newDateTime = sdf.parse(dateTime);
		} catch (ParseException e) {
			
			e.printStackTrace();
			return false;
		}
		return true;		
	}
	
	private Boolean validFuzzyDate(String fuzzyDate) {
		int count = fuzzyDate.length() - fuzzyDate.replace("-", "").length();
		if (count == 1) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			sdf.setLenient(false);
			try {
				Date newFuzzyDate = sdf.parse(fuzzyDate);
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			if (count == 2) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false);
				try {
					Date newFuzzyDate = sdf.parse(fuzzyDate);
				} catch (ParseException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
			return false;
		}
	}
	
	private Boolean validFuzzyTime(String fuzzyTime) {
		if (fuzzyTime.contains("-")) {
			return validFuzzyTime(fuzzyTime);
		} else {
			if (fuzzyTime.contains(":")) {
				return validTime(fuzzyTime);
			} else {
				return false;
			}
		}
	}
	
	private Boolean validTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sdf.setLenient(false);
		try {
			Date newTime = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Boolean validInterval(String interval) {
		String temp[] = interval.split("-");
		String length = temp[0];
		String unit = temp[1];
		if(isInteger(length) && Integer.parseInt(length) > 0) {
			switch (unit) {
			case "day":
				return true;
			case "hour":
				return true;
			case "minute":
				return true;
			case "week":
				return true;
			case "month":
				return true;
			case "year":
				return true;
			default:
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean isInteger(String s) {
		try {
			@SuppressWarnings("unused")
			int i = Integer.parseInt(s);
			return true;
		} catch (NumberFormatException er) {
			return false;
		}
	}
}
