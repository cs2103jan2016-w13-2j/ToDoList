//@@author A0130620B
package todolist.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		if (noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}

	public InputException addEventChecker(String title, String startDate, String startTime, String quantity,
			String timeUnit) {
		if (noRepeat(title)) {
			if (validFuzzyDate(startDate)) {
				if (validTime(startTime)) {
					if (validQuantity(quantity)) {
						if (validUnit(timeUnit)) {
							return new InputException();
						} else {
							return new InputException("ADD EVENT", "INVALID TIME UNIT");
						}
					} else {
						return new InputException("ADD EVENT", "INVALID QUANTITY");
					}
				} else {
					return new InputException("ADD EVENT", "INVALID START TIME");
				}
			} else {
				return new InputException("ADD EVENT", "INVALID START DATE");
			}
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}

	public InputException addEventLessChecker(String title, String fuzzyTime, String quantity, String timeUnit) {
		if (noRepeat(title)) {
			if (validFuzzyTime(fuzzyTime)) {
				if (validQuantity(quantity)) {
					if (validUnit(timeUnit)) {
						return new InputException();
					} else {
						return new InputException("ADD EVENT", "INVALID TIME UNIT");
					}
				} else {
					return new InputException("ADD EVENT", "INVALID QUANTITY");
				}
			} else {
				return new InputException("ADD EVENT", "INVALID START TIME");
			}
		} else {
			return new InputException("ADD EVENT", "INVLAID TIME");
		}
	}

	public InputException addDeadlineChecker(String title, String endDate, String endTime) {
		if (noRepeat(title)) {
			if (validFuzzyDate(endDate)) {
				if (validTime(endTime)) {
					return new InputException();
				} else {
					return new InputException("ADD DEADLINE", "INVALID END TIME");
				}
			} else {
				return new InputException("ADD DEADLINE", "INVLAID END DATE");
			}
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}

	public InputException addDeadlineLessChecker(String title, String fuzzyTime) {
		if (noRepeat(title)) {
			if (validFuzzyTime(fuzzyTime)) {
				return new InputException();
			} else {
				return new InputException("ADD DEADLINE", "INVALID END TIME");
			}
		} else {
			return new InputException("ADD", "REPEAT TITLE");
		}
	}

	public InputException addRecurringEventChecker(String interval, String title, String startDate, String startTime,
			String quantity, String timeUnit) {
		if (validInterval(interval)) {
			return addEventChecker(title, startDate, startTime, quantity, timeUnit);
		} else {
			return new InputException("ADD RECURRING EVENT", "INVALID INTERVAL");
		}
	}

	public InputException addRecurringEventLessChecker(String interval, String title, String fuzzyTime, String quantity,
			String timeUnit) {
		if (validInterval(interval)) {
			return addEventLessChecker(title, fuzzyTime, quantity, timeUnit);
		} else {
			return new InputException("ADD RECURRING EVENT", "INVALID INTERVAL");
		}
	}

	public InputException addRecurringDeadlineChecker(String interval, String title, String endDate, String endTime) {
		if (validInterval(interval)) {
			return addDeadlineChecker(title, endDate, endTime);
		} else {
			return new InputException("ADD RECURRING DEADLINE", "INVALID INTERVAL");
		}
	}

	public InputException addRecurringDeadlineLessChecker(String interval, String title, String fuzzyTime) {
		if (validInterval(interval)) {
			return addDeadlineLessChecker(title, fuzzyTime);
		} else {
			return new InputException("ADD RECURRING DEADLINE", "INVALID INTERVAL");
		}
	}

	public InputException remindBefChecker(String title, String quantity, String timeUnit) {
		if (!noRepeat(title)) {
			if (validQuantity(quantity)) {
				if (validUnit(timeUnit)) {
					if(isFloating(title)) {
						return new InputException("REMIND BEF", "FLOATING TASK");
					} else {
						return new InputException();
					}
				} else {
					return new InputException("REMIND BEF", "INVALID TIME UNIT");
				}
			} else {
				return new InputException("REMIND BEF", "INVALID QUANTITY");
			}
		} else {
			return new InputException("REMIND BEF", "TASK NOT EXIST");
		}
	}

	public InputException addRemindBefChecker(String quantity, String timeUnit, String[] arg) {
		if (validQuantity(quantity)) {
			if (validUnit(timeUnit)) {
				String type = arg[0];
				switch (type) {
				case "event":
					return addEventChecker(arg[1], arg[2], arg[3], arg[4], arg[5]);
				case "deadline":
					return addDeadlineChecker(arg[1], arg[2], arg[3]);
				case "task":
					return addTaskChecker(arg[1]);
				default:
					return new InputException("ADD REMIND BEF", "INVALID TYPE");
				}
			} else {
				return new InputException("ADD REMIND BEF", "INVALID TIME UNIT");
			}
		} else {
			return new InputException("ADD REMIND BEF", "INVALID QUANTITY");
		}
	}

	public InputException remindChecker(String title) {
		if (!noRepeat(title)) {
			if(isFloating(title)) {
				return new InputException("REMIND", "FLOATING TASK");
			} else {
				return new InputException();
			}
		} else {
			return new InputException("REMIND", "TASK NOT EXIST");
		}
	}

	public InputException forwardChecker(String title, String quantity, String timeUnit) {
		if (!noRepeat(title)) {
			if (validQuantity(quantity)) {
				if (validUnit(timeUnit)) {
					if(isFloating(title)) {
						return new InputException("FORWARD", "FLOATING TASK");
					} else {
						return new InputException();
					}
				} else {
					return new InputException("FORWARD", "INVALID TIME UNIT");
				}
			} else {
				return new InputException("FORWARD", "INVALID QUANTITY");
			}
		} else {
			return new InputException("FORWARD", "NOT EXIST");
		}
	}

	public InputException postponeChecker(String title, String quantity, String timeUnit) {
		if (!noRepeat(title)) {
			if (validQuantity(quantity)) {
				if (validUnit(timeUnit)) {
					if(isFloating(title)) {
						return new InputException("POSTPONE", "FLOATING TASK");
					} else {
						return new InputException();
					}
				} else {
					return new InputException("POSTPONE", "INVALID TIME UNIT");
				}
			} else {
				return new InputException("POSTPONE", "INVALID QUANTITY");
			}
		} else {
			return new InputException("POSTPONE", "NOT EXIST");
		}
	}

	public InputException redoChecker(String redostep) {
		if (isInteger(redostep)) {
			if (Integer.parseInt(redostep) > 0) {
				if (logic.getSnapshot()[logic.checkStep() + Integer.parseInt(redostep)] == null) {
					return new InputException("REDO", "NO ACTION TO REDO");
				} else {
					return new InputException();
				}
			} else {
				return new InputException("REDO", "STEP NOT POSITIVE");
			}
		} else {
			return new InputException("REDO", "AUGUMENT NOT INTEGER");
		}
	}

	public InputException undoChecker(String undostep) {
		if (isInteger(undostep)) {
			if (Integer.parseInt(undostep) > 0) {
				if (logic.checkStep() - Integer.parseInt(undostep) < 0) {
					return new InputException("UNDO", "NO ACTION TO UNDO");
				} else {
					return new InputException();
				}
			} else {
				return new InputException("UNDO", "STEP NOT POSITIVE");
			}
		} else {
			return new InputException("UNDO", "AUGUMENT NOT INTEGER");
		}
	}

	public InputException undoneChecker(String title) {
		if (!noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("UNDONE", "NOT EXIST");
		}
	}

	public InputException doneChecker(String title) {
		if (!noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("DONE", "NOT EXIST");
		}
	}

	public InputException openChecker(String path) {
		return new InputException();
	}

	public InputException saveChecker(String path) {
		return new InputException();
	}

	public InputException invalidChecker(String arg) {
		return new InputException();
	}

	public InputException tabChecker(String workplace) {
		switch (workplace) {
		case "home":
			return new InputException();
		case "expired":
			return new InputException();
		case "today":
			return new InputException();
		case "week":
			return new InputException();
		case "done":
			return new InputException();
		case "options":
			return new InputException();
		case "help":
			return new InputException();
		default:
			return new InputException("TAB", "WORDPLACE NOT EXIST");
		}
	}

	public InputException setRecurringChecker(String title, Boolean status, String interval) {
		if (!noRepeat(title)) {
			if ((status && validInterval(interval)) || (!status)) {
				if(isFloating(title)) {
					return new InputException("SET RECURRING", "FLOATING TASK");
				} else {
					return new InputException();
				}
			} else {
				return new InputException("SET RECURRING", "INVALID INTERVAL");
			}
		} else {
			return new InputException("SET RECURRING", "TASK NOT EXIST");
		}
	}

	public InputException labelChecker(String title, String category) {
		if (!noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("LABEL", "TASK NOT EXIST");
		}
	}

	public InputException sortChecker(String fieldName, String order) {
		if(validOrder(order)) {
			switch(fieldName) {
			case "title":
				return new InputException();
			case "category":
				return new InputException();
			case "start":
				return new InputException();
			case "end":
				return new InputException();
				default :
					return new InputException("SORT", "INVALID FIELDNAME");
			}
		} else {
			return new InputException("SORT", "INVALID ORDER");
		}
	}

	public InputException filterChecker(String[] arg) {
		return new InputException();
	}

	public InputException searchChecker(String[] arg) {
		return new InputException();
	}

	public InputException editChecker(String title, String fieldName, String newValue) {
		if (!noRepeat(title)) {
			switch (fieldName) {
			case "title":
				return new InputException();
			case "done":
				return new InputException();
			case "undone":
				return new InputException();
			case "start-time":
				return new InputException();
			case "end-time":
				return new InputException();
			default:
				return new InputException("EDIT", "FIELD NOT EXIST");
			}
		} else {
			return new InputException("EDIT", "TASK NOT EXIST");
		}
	}

	public InputException deleteChecker(String title) {
		if (!noRepeat(title)) {
			return new InputException();
		} else {
			return new InputException("DELETE", "TASK NOT EXIST");
		}
	}

	public InputException resetChecker() {
		return new InputException();
	}

	public InputException exitChecker(String[] arg) {
		return new InputException();
	}

	public InputException addRemindChecker(String[] arg) {
		String type = arg[0];
		switch (type) {
		case "event":
			return addEventChecker(arg[1], arg[2], arg[3], arg[4], arg[5]);
		case "deadline":
			return addDeadlineChecker(arg[1], arg[2], arg[3]);
		case "task":
			return addTaskChecker(arg[1]);
		default:
			return new InputException("ADD REMIND", "INVALID TYPE");
		}
	}

	private Boolean noRepeat(String title) {
		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("NAME", title));
		if (tempTaskList.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unused")
	private Boolean validDateTime(String date, String time) {
		String dateTime = date + " " + time;
		if (date == null) {
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
				@SuppressWarnings("unused")
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
					@SuppressWarnings("unused")
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
			@SuppressWarnings("unused")
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
		if (isInteger(length) && Integer.parseInt(length) > 0) {
			return validUnit(unit);
		} else {
			return false;
		}
	}

	private boolean validUnit(String unit) {
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
	}

	private Boolean validQuantity(String quantity) {
		if (isInteger(quantity) && Integer.parseInt(quantity) > 0) {
			return true;
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
	
	private boolean isFloating(String title) {
		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		if(tempTask.getEndTime() == null) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean validOrder(String order) {
		if(order.equalsIgnoreCase("ascending") || order.equalsIgnoreCase("descending")) {
			return true;
		} else {
			return false;
		}
	}
}
