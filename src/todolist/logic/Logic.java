package todolist.logic;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import todolist.MainApp;
import todolist.model.Category;
import todolist.model.Name;
import todolist.model.Reminder;
import todolist.model.SearchCommand;
import todolist.model.Task;
import todolist.model.TokenizedCommand;
import todolist.parser.MainParser;
import todolist.storage.DataBase;

public class Logic {

	private MainApp mainApp;
	private DataBase dataBase;
	private UIHandler uiHandler;
	private MainParser mainParser;
	private CaseSwitcher caseSwitcher;
	private int steps;
	private Logger logger = Logger.getLogger("Logic Logger");

	private static String LOGGING_ADDING_FLOATING_TASK = "tring to add floating task: ";
	private static String LOGGING_ADDING_EVENT = "tring to add event: ";
	private static String LOGGING_ADDING_DEADLINE = "tring to add deadline: ";
	private static String LOGGING_EDITING_TASK = "tring to edit task: ";
	private static String LOGGING_SEARCHING_TASK = "tring to search task: ";
	private static String LOGGING_DELETING_TASK = "tring to delete task: ";
	private static String LOGGING_REPEATED_TASK = "The task has already existed: ";
	private static String LOGGING_TIME_ERROR = "This time was in the past";

	public Logic(MainApp mainApp) {
		this.mainApp = mainApp;
		this.dataBase = new DataBase();
		this.mainParser = new MainParser();
		this.uiHandler = new UIHandler(dataBase, mainApp, this);
		this.caseSwitcher = new CaseSwitcher(this);
		this.steps = 0;
	}

	/**
	 * This method takes in raw user input and process it by calling parser
	 *
	 * @param String
	 *            take in the user input string
	 * @return void
	 */
	
	public UIHandler getUIHandler() {
		return uiHandler;
	}
	
	public void process(String input) {
		TokenizedCommand tokenizedCommand = mainParser.parse(input);
		caseSwitcher.execute(tokenizedCommand);
	}

	/**
	 * This method takes in an integer and increment the internal step counter
	 *
	 * @param int
	 * @return void
	 */
	public void stepForward(int increment) {
		this.steps = steps + increment;
	}

	/**
	 * This method resets the view
	 *
	 * 
	 * @return void
	 */
	public void reset() {
		uiHandler.refresh();
		uiHandler.sendMessage("View reseted");
	}
	
	/**
	 * This method adds an recurring event
	 *
	 * 
	 * @return void
	 */
	public Boolean addRecurringEvent(String interval, String title, String startDate, String startTime, String quantity, String timeUnit) {
		Boolean addResponse = addEvent(title, startDate, startTime, quantity, timeUnit);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}
	
	/**
	 * This method adds an recurring deadline
	 *
	 * 
	 * @return void
	 */
	public Boolean addRecurringDeadline(String interval, String title, String endDate, String endTime) {
		Boolean addResponse = addDeadline(title, endDate, endTime);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}

	/**
	 * This method adds a new event with start date and duration
	 *
	 * 
	 * @return void
	 */
	public Boolean addEvent(String title, String startDate, String startTime, String quantity, String timeUnit) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_EVENT + title + startDate + startTime + quantity + timeUnit);

			Name name = new Name(title);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime start = LocalDateTime.parse(startDate + " " + startTime, formatter);
			LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			if (!start.isAfter(LocalDateTime.now())) {
				logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
				uiHandler.sendMessage("start time of task is before now");
			}

			Task newEvent = new Task(name, start, end, null, null, false, false, null);

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new event is successfully added");
			return addResponse;
		} else {
			return false;
		}
	}

	/**
	 * This method adds a new deadline.
	 *
	 * 
	 * @return void
	 */
	public Boolean addDeadline(String title, String endDate, String endTime) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + endDate + endTime);

			Name name = new Name(title);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime end = LocalDateTime.parse(endDate + " " + endTime, formatter);
			Task newEvent = new Task(name, null, end, null, null, false, false, null);

			if (!end.isAfter(LocalDateTime.now())) {
				logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
				uiHandler.sendMessage("deadline of task is before now");
			}

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new deadline is successfully added");
			return addResponse;
		} else {
			return false;
		}
	}

	/**
	 * This method adds a new floating task.
	 *
	 * 
	 * @return void
	 */
	public Boolean addTask(String title) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_FLOATING_TASK + title);

			Name name = new Name(title);
			Task newEvent = new Task(name, null, null, null, null, false, false, null);

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new floating task is successfully added");
			return addResponse;
		} else {
			return false;
		}
	}

	/**
	 * This method takes in the title of a task and marks it as done.
	 *
	 * 
	 * @return void
	 */
	public Boolean done(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setDoneStatus(true);
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(title + " is marked done!");
		
		return deleteResponse && addResponse;
	}
	
	public Boolean undone(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setDoneStatus(false);
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(title + " is marked not done!");
		
		return deleteResponse && addResponse;
	}

	/**
	 * This method edits a task.
	 *
	 * 
	 * @return void
	 */
	public Boolean edit(String title, String fieldName, String newValue) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

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

		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage("Task: " + title + " is successfully changed");
		
		return deleteResponse && addResponse;

	}

	/**
	 * This method takes in the title of a task and deletes it.
	 *
	 * 
	 * @return void
	 */
	public Boolean delete(String title) {

		logger.log(Level.INFO, LOGGING_DELETING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		uiHandler.refresh();
		uiHandler.sendMessage(title + " is successfully deleted");
		
		return deleteResponse;
	}

	/**
	 * This method takes in the title of a task and displays it.
	 *
	 * 
	 * @return void
	 */
	public void search(String title) {

		logger.log(Level.INFO, LOGGING_SEARCHING_TASK + title);

		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("NAME", title));
		
		uiHandler.display(tempTaskList);
		uiHandler.sendMessage("Here are your search results");
	}

	/**
	 * This method takes in the name of a category and displays tasks of that
	 * category.
	 *
	 * 
	 * @return void
	 */
	public void filter(String category) {

		logger.log(Level.INFO, LOGGING_SEARCHING_TASK + category);
		
		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("CATEGORY", category));

		uiHandler.display(tempTaskList);
		uiHandler.sendMessage("Here are your filter results");
	}

	/**
	 * This method sorts all tasks in according to the field name and order.
	 *
	 * 
	 * @return void
	 */
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

	/**
	 * This method takes in the title of a task and labels it with a category.
	 *
	 * 
	 * @return void
	 */
	public Boolean label(String title, String category) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setCategory(new Category(category));
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage("The category of " + title + " is set to " + category + " !");
		
		return deleteResponse && addResponse;
	}
	
	/**
	 * This method edits the recurring status of a task.
	 *
	 * 
	 * @return void
	 */
	public Boolean setRecurring(String title, Boolean status, String interval) {
		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setRecurring(status);
		tempTask.setInterval(interval);
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		
		if(status) {
			uiHandler.sendMessage(title + " is recurring!");
		} else {
			uiHandler.sendMessage(title + " is not recurring!");
		}
		
		return deleteResponse && addResponse;
	}

	/**
	 * This method postpones a task by a duration.
	 *
	 * 
	 * @return void
	 */
	public Boolean postpone(String title, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);
		
		Boolean addResponse = false;

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setEndTime(tempEndTime);
			addResponse = dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);

			addResponse = dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
			uiHandler.sendMessage(title + " is successfully postponed");
		}
		
		return deleteResponse && addResponse;
	}

	/**
	 * This method forwards a task by a duration.
	 *
	 * 
	 * @return void
	 */
	public Boolean forward(String title, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);
		
		Boolean addResponse = false;

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setEndTime(tempEndTime);
			addResponse = dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);

			addResponse = dataBaseAdd(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
			uiHandler.sendMessage(title + " is successfully forwarded");
		}
		
		return deleteResponse && addResponse;
	}

	/**
	 * This method adds a task with remind and triggers the remind at the
	 * deadline.
	 *
	 * 
	 * @return void
	 */
	public Boolean addRemind(String[] arg) {
		
		Boolean addResponse = false;

		String type = arg[0];
		switch (type) {
		case "event":
			addResponse = addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
			break;
		case "deadline":
			addResponse = addDeadline(arg[1], arg[2], arg[3]);
			break;
		case "task":
			addResponse = addTask(arg[1]);
			break;
		}

		remind(arg[1]);
		
		return addResponse;
	}

	/**
	 * This method adds a task with remind and triggers the remind a duration
	 * before the deadline.
	 *
	 * 
	 * @return void
	 */
	public Boolean addRemindBef(String quantity, String timeUnit, String[] arg) {

		String type = arg[0];
		
		Boolean addResponse = false;
		
		switch (type) {
		case "event":
			addResponse = addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
			break;
		case "deadline":
			addResponse = addDeadline(arg[1], arg[2], arg[3]);
			break;
		case "task":
			addResponse = addTask(arg[1]);
			break;
		}

		remindBef(arg[1], quantity, timeUnit);
		
		return addResponse;
	}

	/**
	 * This method adds remind to an existing task and triggers the remind a
	 * duration before the deadline.
	 *
	 * 
	 * @return void
	 */
	public Boolean remindBef(String title, String quantity, String timeUnit) {

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

		Boolean deleteResponse = dataBaseDelete(tempTask);

		Reminder newReminder = new Reminder(true, reminderTime);

		tempTask.setReminder(newReminder);

		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		
		return deleteResponse && addResponse;
	}

	/**
	 * This method adds remind to an existing task and triggers the remind at
	 * the deadline.
	 *
	 * 
	 * @return void
	 */
	public Boolean remind(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		return remindBef(title, null, null);
	}

	/**
	 * This method terminates the application.
	 *
	 * 
	 * @return void
	 */
	public void exit() {
		System.exit(0);
	}

	/**
	 * This method takes in an integer and undo that number of steps.
	 *
	 * 
	 * @return void
	 */
	public Boolean undo(int undostep) {
		Boolean undoResponse = dataBase.retrieveHistory(steps - undostep);
		steps = steps - undostep;
		uiHandler.refresh();
		return undoResponse;
	}

	/**
	 * This method takes in an integer and redo that number of steps.
	 *
	 * 
	 * @return void
	 */
	public Boolean redo(int redostep) {
		Boolean redoResponse = dataBase.retrieveHistory(steps + redostep);
		steps = steps + redostep;
		uiHandler.refresh();
		return redoResponse;
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

	private Boolean dataBaseAdd(Task task) {
		return dataBase.add(task);

	}

	private Boolean dataBaseDelete(Task task) {
		return dataBase.delete(task);
	}

	private Boolean noRepeat(String title) {
		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("NAME", title));
		System.out.println(tempTaskList.size());
		if (tempTaskList.size() > 0) {
			logger.log(Level.INFO, LOGGING_REPEATED_TASK + title);
			uiHandler.sendMessage("Failure. Existing task with same name detected!");
			return false;
		} else {
			return true;
		}
	}
}