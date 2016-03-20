package todolist.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import todolist.MainApp;

public class Logic {

	private MainApp mainApp;
	private DataBase dataBase;
	public UIHandler uiHandler;
	private Parser parser;
	private CaseSwitcher caseSwitcher;
	private int steps;
	private Logger logger = Logger.getLogger("Logic Logger");

	public static String LOGGING_ADDING_FLOATING_TASK = "tring to add floating task: ";
	public static String LOGGING_ADDING_EVENT = "tring to add event: ";
	public static String LOGGING_ADDING_DEADLINE = "tring to add deadline: ";
	public static String LOGGING_EDITING_TASK = "tring to edit task: ";
	public static String LOGGING_SEARCHING_TASK = "tring to search task: ";
	public static String LOGGING_DELETING_TASK = "tring to delete task: ";
	public static String LOGGING_REPEATED_TASK = "The task has already existed: ";
	public static String LOGGING_TASK_NOTEXIST = "The task does not exist: ";
	public static String LOGGING_TASK_DELETED = "The task is deleted from database: ";
	public static String EXCEPTION_TIME_ERROR = "This time was in the past";

	public Logic(MainApp mainApp) {
		this.mainApp = mainApp;
		this.dataBase = new DataBase();
		this.parser = new Parser();
		this.uiHandler = new UIHandler(dataBase, mainApp);
		this.caseSwitcher = new CaseSwitcher(this);
		this.steps = 0;
	}

	public void process(String input) {
		TokenizedCommand tokenizedCommand = parser.parse(input);
		caseSwitcher.execute(tokenizedCommand);
	}

	
	public void stepForward(int increment) {
		this.steps = steps + increment;
	}
	
	public void reset() {
		uiHandler.refresh();
		uiHandler.sendMessage("View reseted");
	}

	public void addEvent(String title, String startDate, String startTime, String quantity, String timeUnit)
			throws Exception {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_FLOATING_TASK + title + startDate + startTime + quantity + timeUnit);

			Name name = new Name(title);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime start = LocalDateTime.parse(startDate + " " + startTime, formatter);
			LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			if (!start.isAfter(LocalDateTime.now())) {
				throw new Exception(EXCEPTION_TIME_ERROR);
			}

			Task newEvent = new Task(name, start, end, null, null, false);

			dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new event is successfully added");
		}
	}

	public void addDeadline(String title, String endDate, String endTime) throws Exception {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + endDate + endTime);

			Name name = new Name(title);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime end = LocalDateTime.parse(endDate + " " + endTime, formatter);
			Task newEvent = new Task(name, null, end, null, null, false);

			if (!end.isAfter(LocalDateTime.now())) {
				throw new Exception(EXCEPTION_TIME_ERROR);
			}

			dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new deadline is successfully added");
		}
	}

	public void addTask(String title) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_FLOATING_TASK + title);

			Name name = new Name(title);
			Task newEvent = new Task(name, null, null, null, null, false);

			dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new floating task is successfully added");
		}
	}

	public void done(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		dataBaseDelete(tempTask);

		tempTask.setDoneStatus(true);
		dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(title + " is marked done!");
	}

	public void edit(String title, String fieldName, String newValue) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		dataBaseDelete(tempTask);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");

		switch (fieldName) {
		case "title":
			tempTask.setName(new Name(newValue));
			break;
		case "done":
			tempTask.setDoneStatus(true);
			break;
		case "undone":
			tempTask.setDoneStatus(false);
			break;
		case "start-time":
			LocalDateTime start = null;

			if (newValue.equals("remove")) {
				start = null;
			} else {
				start = LocalDateTime.parse(newValue, formatter);
			}
			tempTask.setStartTime(start);
			break;
		case "end-time":
			LocalDateTime end = null;

			if (newValue.equals("remove")) {
				end = null;
			} else {
				end = LocalDateTime.parse(newValue, formatter);
			}
			tempTask.setEndTime(end);
			break;
		}

		dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage("Task: " + title + " is successfully changed");

	}

	public void delete(String title) {

		logger.log(Level.INFO, LOGGING_DELETING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		System.out.println(dataBase.convert_TaskToString(tempTask));
		dataBaseDelete(tempTask);

		uiHandler.refresh();
		uiHandler.sendMessage(title + " is successfully deleted");
	}

	public void search(String title) {

		logger.log(Level.INFO, LOGGING_SEARCHING_TASK + title);

		uiHandler.search(title);
		uiHandler.sendMessage("Here are your search results");
	}

	public void filter(String category) {

		logger.log(Level.INFO, LOGGING_SEARCHING_TASK + category);
		uiHandler.filter(category);
		uiHandler.sendMessage("Here are your filter results");
	}

	public void sort(String fieldName, String order) {
		dataBase.sort(fieldName, order);
		uiHandler.refresh();
		uiHandler.sendMessage("Sorted!");
	}
	//
	// public void insert(String title, String befaft, String title) {
	// uiHandler.insert(title, befaft, title);
	// }
	//

	/*
	 * public void switchPosition(String title1, String title2) {
	 * uiHandler.insert(title1, "aft", title2); }
	 */

	public void label(String title, String category) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		dataBaseDelete(tempTask);

		tempTask.setCategory(new Category(category));
		dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
	}

	public void postpone(String title, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		dataBaseDelete(tempTask);

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setEndTime(tempEndTime);
			dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);

			dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
			uiHandler.sendMessage(title + " is successfully postponed");
		}
	}

	public void forward(String title, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		dataBaseDelete(tempTask);

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setEndTime(tempEndTime);
			dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);

			dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
			uiHandler.sendMessage(title + " is successfully forwarded");
		}
	}

	public void addRemind(String[] arg) throws Exception {

		String type = arg[0];
		switch (type) {
		case "event":
			addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
		case "deadline":
			addDeadline(arg[1], arg[2], arg[3]);
		case "task":
			addTask(arg[1]);
		}

		remind(arg[1]);
	}

	public void addRemindBef(String quantity, String timeUnit, String[] arg) throws Exception {

		String type = arg[0];
		switch (type) {
		case "event":
			addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
		case "deadline":
			addDeadline(arg[1], arg[2], arg[3]);
		case "task":
			addTask(arg[1]);
		}

		remindBef(arg[1], quantity, timeUnit);
	}

	public void remindBef(String title, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		LocalDateTime reminderTime = null;

		if (quantity == null) {
			if (tempTask.getStartTime() == null) {
				reminderTime = tempTask.getStartTime();
			} else {
				reminderTime = tempTask.getEndTime();
			}
		} else {
			if (tempTask.getStartTime() == null) {
				reminderTime = tempTask.getStartTime().minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			} else {
				reminderTime = tempTask.getEndTime().minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			}
		}

		dataBaseDelete(tempTask);

		Reminder newReminder = new Reminder(true, reminderTime);

		tempTask.setReminder(newReminder);

		dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
	}

	public void remind(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		remindBef(title, null, null);
	}

	public void exit() {
		uiHandler.exit();
	}

	public void undo(int undostep) {
		dataBase.retrieveHistory(steps - undostep);
		steps = steps - undostep;
		uiHandler.refresh();
	}

	public void redo(int redostep) {
		dataBase.retrieveHistory(steps + redostep);
		steps = steps + redostep;
		uiHandler.refresh();
	}

	private TemporalUnit generateTimeUnit(String unit) {
		switch (unit) {
		case "day":
			return ChronoUnit.DAYS;
		case "hour":
			return ChronoUnit.HOURS;
		case "minute":
			return ChronoUnit.MINUTES;
		default:
			return null;
		}
	}

	private void dataBaseAdd(Task task) {
		try {
			dataBase.add(task);
		} catch (IOException e) {
			logger.log(Level.INFO, "IOException");
		}
	}

	private void dataBaseDelete(Task task) {
		try {
			dataBase.delete(task);
		} catch (IOException e) {
			logger.log(Level.INFO, "IOException");
		}
	}

	private boolean noRepeat(String title) {
		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("NAME", title));
		System.out.println(tempTaskList.size());
		if (tempTaskList.size() > 0) {
			uiHandler.sendMessage("Failure. Existing task with same name detected!");
			return false;
		} else {
			return true;
		}
	}
}