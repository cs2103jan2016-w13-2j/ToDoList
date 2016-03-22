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
	public UIHandler uiHandler;
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
		this.uiHandler = new UIHandler(dataBase, mainApp);
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
	 * This method adds a new event with start date and duration
	 *
	 * 
	 * @return void
	 */
	public void addEvent(String title, String startDate, String startTime, String quantity, String timeUnit) {

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

			Task newEvent = new Task(name, start, end, null, null, false, false);

			dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new event is successfully added");
		}
	}

	/**
	 * This method adds a new deadline.
	 *
	 * 
	 * @return void
	 */
	public void addDeadline(String title, String endDate, String endTime) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + endDate + endTime);

			Name name = new Name(title);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime end = LocalDateTime.parse(endDate + " " + endTime, formatter);
			Task newEvent = new Task(name, null, end, null, null, false, false);

			if (!end.isAfter(LocalDateTime.now())) {
				logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
				uiHandler.sendMessage("deadline of task is before now");
			}

			dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new deadline is successfully added");
		}
	}

	/**
	 * This method adds a new floating task.
	 *
	 * 
	 * @return void
	 */
	public void addTask(String title) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_FLOATING_TASK + title);

			Name name = new Name(title);
			Task newEvent = new Task(name, null, null, null, null, false, false);

			dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new floating task is successfully added");
		}
	}

	/**
	 * This method takes in the title of a task and marks it as done.
	 *
	 * 
	 * @return void
	 */
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

	/**
	 * This method edits a task.
	 *
	 * 
	 * @return void
	 */
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

	/**
	 * This method takes in the title of a task and deletes it.
	 *
	 * 
	 * @return void
	 */
	public void delete(String title) {

		logger.log(Level.INFO, LOGGING_DELETING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		System.out.println(dataBase.convert_TaskToString(tempTask));
		dataBaseDelete(tempTask);

		uiHandler.refresh();
		uiHandler.sendMessage(title + " is successfully deleted");
	}

	/**
	 * This method takes in the title of a task and displays it.
	 *
	 * 
	 * @return void
	 */
	public void search(String title) {

		logger.log(Level.INFO, LOGGING_SEARCHING_TASK + title);

		uiHandler.search(title);
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
		uiHandler.filter(category);
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
	public void label(String title, String category) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		dataBaseDelete(tempTask);

		tempTask.setCategory(new Category(category));
		dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
	}

	/**
	 * This method postpones a task by a duration.
	 *
	 * 
	 * @return void
	 */
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

	/**
	 * This method forwards a task by a duration.
	 *
	 * 
	 * @return void
	 */
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

	/**
	 * This method adds a task with remind and triggers the remind at the
	 * deadline.
	 *
	 * 
	 * @return void
	 */
	public void addRemind(String[] arg) {

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

	/**
	 * This method adds a task with remind and triggers the remind a duration
	 * before the deadline.
	 *
	 * 
	 * @return void
	 */
	public void addRemindBef(String quantity, String timeUnit, String[] arg) {

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

	/**
	 * This method adds remind to an existing task and triggers the remind a
	 * duration before the deadline.
	 *
	 * 
	 * @return void
	 */
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

	/**
	 * This method adds remind to an existing task and triggers the remind at
	 * the deadline.
	 *
	 * 
	 * @return void
	 */
	public void remind(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		remindBef(title, null, null);
	}

	/**
	 * This method terminates the application.
	 *
	 * 
	 * @return void
	 */
	public void exit() {
		uiHandler.exit();
	}

	/**
	 * This method takes in an integer and undo that number of steps.
	 *
	 * 
	 * @return void
	 */
	public void undo(int undostep) {
		dataBase.retrieveHistory(steps - undostep);
		steps = steps - undostep;
		uiHandler.refresh();
	}

	/**
	 * This method takes in an integer and redo that number of steps.
	 *
	 * 
	 * @return void
	 */
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
		dataBase.delete(task);
	}

	private boolean noRepeat(String title) {
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