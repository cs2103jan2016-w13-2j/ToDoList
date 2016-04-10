//@@author A0130620B
package todolist.logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
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
	public DataBase dataBase;
	private UIHandler uiHandler;
	private MainParser mainParser;
	private CaseSwitcher caseSwitcher;
	private int steps;
	private Logger logger = Logger.getLogger("Logic Logger");
	ArrayList<Task>[] snapshot;

	private static String LOGGING_ADDING_FLOATING_TASK = "tring to add floating task: ";
	private static String LOGGING_ADDING_EVENT = "tring to add event: ";
	private static String LOGGING_ADDING_DEADLINE = "tring to add deadline: ";
	private static String LOGGING_EDITING_TASK = "tring to edit task: ";
	private static String LOGGING_SEARCHING_TASK = "tring to search task: ";
	private static String LOGGING_DELETING_TASK = "tring to delete task: ";
	
	@SuppressWarnings("unchecked")
	public Logic(MainApp mainApp) {
		this.setMainApp(mainApp);
		this.dataBase = new DataBase();
		this.mainParser = new MainParser();
		this.uiHandler = new UIHandler(dataBase, mainApp, this);
		this.caseSwitcher = new CaseSwitcher(this, dataBase);
		this.steps = 0;
		snapshot = new ArrayList[1000];
		snapshot[0] = dataBase.retrieveAll();
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
	 * This method adds a new floating task.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addTask(String title) {
		logger.log(Level.INFO, LOGGING_ADDING_FLOATING_TASK + title);

		Name name = new Name(title);
		Task newEvent = new Task(name, null, null, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage(
				"A new un-dated task [" + title + "] has been created successfully. [not what you want? try 'undo']",
				true);
		return addResponse;
	}

	/**
	 * This method adds a new event with start date and duration
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addEvent(String title, String startDate, String startTime, String quantity, String timeUnit) {
		logger.log(Level.INFO, LOGGING_ADDING_EVENT + title + startDate + startTime + quantity + timeUnit);

		Name name = new Name(title);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse(fuzzyParseDate(startDate) + " " + startTime, formatter);
		LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

		if (!start.isAfter(LocalDateTime.now())) {
			uiHandler.sendMessage("Oh no! [" + title + "] is currently ongoing! Please attend it before it's too late!",
					true);
		}

		if (!end.isAfter(LocalDateTime.now())) {
			uiHandler.sendMessage("[" + title + "] is a task in the past", true);
		}

		Task newEvent = new Task(name, start, end, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage("A new event [" + newEvent.getName().getName()
				+ "] has been created successfully! [not what you want? try 'undo']", true);
		return addResponse;
	}

	/**
	 * This method adds a new event with start date and duration(less argument
	 * and fuzzy time)
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addEventLess(String title, String fuzzyTime, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_ADDING_EVENT + title + fuzzyTime + quantity + timeUnit);

		LocalDateTime start = fuzzyParseTime(fuzzyTime);
		LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

		Name name = new Name(title);

		if (!start.isAfter(LocalDateTime.now())) {
			uiHandler.sendMessage("Oh no! [" + title + "] is currently ongoing! Please attend it before it's too late!",
					true);
		}
		if (!end.isAfter(LocalDateTime.now())) {
			uiHandler.sendMessage("[" + title + "] is a task in the past", true);
		}

		Task newEvent = new Task(name, start, end, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage("A new event [" + newEvent.getName().getName()
				+ "] has been created successfully! [not what you want? try 'undo']", true);
		return addResponse;
	}

	/**
	 * This method adds a new deadline.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addDeadline(String title, String endDate, String endTime) {

		logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + endDate + endTime);

		Name name = new Name(title);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime end = LocalDateTime.parse(fuzzyParseDate(endDate) + " " + endTime, formatter);
		Task newEvent = new Task(name, null, end, null, null, false, false, null);

		if (!end.isAfter(LocalDateTime.now())) {
			uiHandler.sendMessage("[" + title + "] is a task in the past", true);
		}

		Boolean addResponse = dataBase.add(newEvent);
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage("A new deadline [" + newEvent.getName().getName()
				+ "] has been created successfully. [not what you want? try 'undo']", true);
		return addResponse;
	}

	/**
	 * This method adds a new deadline.(less argument and fuzzy time)
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addDeadlineLess(String title, String fuzzyTime) {

			logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + fuzzyTime);

			Name name = new Name(title);
			LocalDateTime end = fuzzyParseTime(fuzzyTime);
			Task newEvent = new Task(name, null, end, null, null, false, false, null);

			if (!end.isAfter(LocalDateTime.now())) {
				uiHandler.sendMessage("[" + title + "] is a task in the past", true);
			}

			Boolean addResponse = dataBase.add(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new deadline [" + newEvent.getName().getName()
					+ "] has been created successfully. [not what you want? try 'undo']", true);
			return addResponse;
	}
	
	/**
	 * This method adds an recurring event
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addRecurringEvent(String interval, String title, String startDate, String startTime, String quantity,
			String timeUnit) {
		Boolean addResponse = addEvent(title, startDate, startTime, quantity, timeUnit);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}
	
	/**
	 * This method adds an recurring event.(less argument and fuzzy time)
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addRecurringEventLess(String interval, String title, String fuzzyTime, String quantity,
			String timeUnit) {
		Boolean addResponse = addEventLess(title, fuzzyTime, quantity, timeUnit);
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
	 * This method adds an recurring deadline.(less argument and fuzzy time)
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addRecurringDeadlineLess(String interval, String title, String fuzzyTime) {
		Boolean addResponse = addDeadlineLess(title, fuzzyTime);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}
	
	/**
	 * This method edits a task.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean edit(String title, String fieldName, String newValue) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

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
				tempTask.setStartTime(start);
			} else {
				start = LocalDateTime.parse(newValue, formatter);
				if(tempTask.getEndTime() == null) {
					tempTask.setStartTime(start);
					tempTask.setEndTime(start);
				} else {
					tempTask.setStartTime(start);
				}
			}
			break;
		case "end-time":
			LocalDateTime end = null;

			if (newValue.equals("remove")) {
				end = null;
				start = null;
				tempTask.setStartTime(start);
				tempTask.setEndTime(end);

			} else {
				end = LocalDateTime.parse(newValue, formatter);
				tempTask.setEndTime(end);
			}
			break;
		}

		Boolean addResponse = dataBase.add(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage("[" + title + "] has been edited successfully! [not what you want? try 'undo']", true);

		return deleteResponse && addResponse;
	}
	
	/**
	 * This method takes in the title of a task and deletes it.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean delete(String title) {

		logger.log(Level.INFO, LOGGING_DELETING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		uiHandler.refresh();
		uiHandler.sendMessage("[" + title + "] has been deleted successfully! [not what you want? try 'undo']", true);

		return deleteResponse;
	}
	
	/**
	 * This method takes in the title of a task and displays it.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean search(String[] keyword) {
		String input = keyword[0];

		for (int i = 1; i < keyword.length; i++) {
			input = input + " " + keyword[i];
		}

		logger.log(Level.INFO, LOGGING_SEARCHING_TASK + input);

		ArrayList<Task> tempTaskList = dataBase.smartSearch(keyword);

		uiHandler.display(tempTaskList);
		uiHandler.sendMessage("Here are your search results for '" + input + "'! [to clear this search, type 'reset']",
				true);
		
		return true;
	}
	
	/**
	 * This method takes in the name of a category and displays tasks of that
	 * category.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean filter(String category) {

		logger.log(Level.INFO, LOGGING_SEARCHING_TASK + category);

		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("CATEGORY", category));

		uiHandler.display(tempTaskList);
		uiHandler.sendMessage(
				"Here are the related tasks under " + category.toUpperCase() + "! [to clear this filter, type 'reset']",
				true);
		return true;
	}
	
	/**
	 * This method sorts all tasks in according to the field name and order.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean sort(String fieldName, String order) {
		dataBase.sort(fieldName, order);
		uiHandler.refresh();
		uiHandler.sendMessage(
				"Ta-da! Your tasks have been sorted by " + fieldName + "! [not what you want? try 'undo']", true);
		return true;
	}
	
	/**
	 * This method takes in the title of a task and labels it with a category.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean label(String title, String category) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		tempTask.setCategory(new Category(category));
		Boolean addResponse = dataBase.add(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage("You have categorised [" + title + "] under " + category.toUpperCase()
				+ " ! [not what you want? try 'undo']", true);

		return deleteResponse && addResponse;
	}
	
	/**
	 * This method edits the recurring status of a task.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean setRecurring(String title, Boolean status, String interval) {
		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);
		if (tempTask.getEndTime() == null) {
			throw new RuntimeException();
		}
		tempTask.setRecurring(status);
		tempTask.setInterval(interval);
		Boolean addResponse = dataBase.add(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);

		if (status) {
			uiHandler.sendMessage("[" + title + "] is now a recurring task! [not what you want? try 'undo']", true);
		} else {
			uiHandler.sendMessage("[" + title + "] is now an ad-hoc task! [not what you want? try 'undo']", true);
		}

		return deleteResponse && addResponse;
	}
	
	/**
	 * This method postpones a task by a duration.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean postpone(String title, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		Boolean addResponse = false;
		
		if(tempTask.getReminder() != null && tempTask.getReminder().getStatus()) {
			LocalDateTime oldReminderTime = tempTask.getReminder().getTime();
			LocalDateTime newReminderTime = oldReminderTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempTask.setReminder(new Reminder(true, newReminderTime));
		}

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setEndTime(tempEndTime);
			addResponse = dataBase.add(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);

			addResponse = dataBase.add(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
			uiHandler.sendMessage("[" + title + "] has been postponed! [not what you want? try 'undo']", true);
		}
		
		return deleteResponse && addResponse;
	}
	
	/**
	 * This method forwards a task by a duration.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean forward(String title, String quantity, String timeUnit) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		Boolean addResponse = false;
		
		if(tempTask.getReminder() != null && tempTask.getReminder().getStatus()) {
			LocalDateTime oldReminderTime = tempTask.getReminder().getTime();
			LocalDateTime newReminderTime = oldReminderTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempTask.setReminder(new Reminder(true, newReminderTime));
		}

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setEndTime(tempEndTime);
			addResponse = dataBase.add(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);

			addResponse = dataBase.add(tempTask);

			uiHandler.refresh();
			uiHandler.highLight(tempTask);
			uiHandler.sendMessage("[" + title + "] has been rescheduled forward! [not what you want? try 'undo']",
					true);
		}

		return deleteResponse && addResponse;
	}
	
	/**
	 * This method adds a task with remind and triggers the remind at the
	 * deadline.
	 *
	 * 
	 * @return Boolean
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
	 * This method adds remind to an existing task and triggers the remind at
	 * the deadline.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean remind(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		return remindBef(title, "0", "minute");
	}
	
	/**
	 * This method adds remind to an existing task and triggers the remind a
	 * duration before the deadline.
	 *
	 * 
	 * @return Boolean
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
			if (tempTask.getStartTime() != null) {
				reminderTime = tempTask.getStartTime().minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			} else {
				reminderTime = tempTask.getEndTime().minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			}
		}

		Boolean deleteResponse = dataBase.delete(tempTask);

		Reminder newReminder = new Reminder(true, reminderTime);

		tempTask.setReminder(newReminder);

		Boolean addResponse = dataBase.add(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);

		return deleteResponse && addResponse;
	}
	
	/**
	 * This method adds a task with remind and triggers the remind a duration
	 * before the deadline.
	 *
	 * 
	 * @return Boolean
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
	 * This method takes in the title of a task and marks it as done.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean done(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		tempTask.setDoneStatus(true);
		String tempName = tempTask.getName().getName();
		
		if (tempTask.getRecurringStatus()) {
			tempTask.setName(new Name(tempName + " finished on " + getCurrentTimeStamp()));
		}

		Boolean addResponse = dataBase.add(tempTask);

		if (tempTask.getRecurringStatus()) {

			String interval = tempTask.getInterval();
			String temp[] = interval.split("-");
			String length = temp[0];
			String unit = temp[1];

			LocalDateTime oldEndTime = tempTask.getEndTime();
			LocalDateTime newEndTime = oldEndTime.plus(Long.parseLong(length), generateTimeUnit(unit));
			LocalDateTime newStartTime = null;
			if (tempTask.getStartTime() != null) {
				LocalDateTime oldStartTime = tempTask.getStartTime();
				newStartTime = oldStartTime.plus(Long.parseLong(length), generateTimeUnit(unit));
			}

			Task newTempTask = new Task(new Name(tempName), newStartTime, newEndTime, tempTask.getCategory(),
					tempTask.getReminder(), false, true, interval);
			addResponse = dataBase.add(newTempTask);

		}

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(
				"[" + title
						+ "] has been marked as completed! Woohoo another one down! [not what you want? try 'undo']",
				true);
		uiHandler.sendMessage(
				"[" + title
						+ "] has been marked as completed! Woohoo another one down! [not what you want? try 'undo']",
				true);

		return deleteResponse && addResponse;
	}

	/**
	 * This method takes in the title of a task and marks it as undone.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean undone(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		tempTask.setDoneStatus(false);
		Boolean addResponse = dataBase.add(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(
				"[" + title + "] has been marked as ongoing! Go get it TIGER! [not what you want? try 'undo']", true);

		return deleteResponse && addResponse;
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
	 * @return Boolean
	 */
	public Boolean undo(int undostep) {
		Boolean undoResponse = false;
		steps = steps - undostep;
		undoResponse = dataBase.recover(snapshot[steps]);
		uiHandler.refresh();
		return undoResponse;
	}

	/**
	 * This method takes in an integer and redo that number of steps.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean redo(int redostep) {
		Boolean redoResponse = false;
		steps = steps + redostep;
		redoResponse = dataBase.recover(snapshot[steps]);
		uiHandler.refresh();
		return redoResponse;
	}
	
	/**
	 * This method resets the view
	 *
	 *
	 * @return Boolean
	 */
	public Boolean reset() {
		uiHandler.refresh();
		uiHandler.sendMessage("View refreshed. All search and filter results are cleared!", true);
		return true;
	}
	
	/**
	 * This method save all the tasks to a new file path
	 *
	 * 
	 * @return Boolean
	 */
	public boolean setNewFile(String path) {
		return dataBase.setNewFile(path);
	}
	
	/**
	 * This method opens a file from a given new file path
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean openNewFile(String path) {
		return dataBase.openNewFile(path);
	}
	
	/**
	 * This method call UIHandler to change tab
	 *
	 * 
	 * @return Boolean
	 */
	public void tab(String workplace) {
		switch (workplace) {
		case "home":
			uiHandler.tab(1);
			break;
		case "expired":
			uiHandler.tab(2);
			break;
		case "today":
			uiHandler.tab(3);
			break;
		case "week":
			uiHandler.tab(4);
			break;
		case "done":
			uiHandler.tab(5);
			break;
		case "options":
			uiHandler.tab(6);
			break;
		case "help":
			uiHandler.tab(7);
			break;
		}
	}
	
	/**
	 * This method call UIHandler to display a message when flexi command cannot be parsed
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean invalid(String keyword) {
		uiHandler.sendMessage("Sorry! I don't understand what you are talking about!", true);
		return true;
	}
	
	/**
	 * This method clears all the tasks
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean clean() {
		this.dataBase.clear();
		return true;
	}
	
	/**
	 * This method returns the current UIHandler
	 *
	 * 
	 * @return Boolean
	 */
	public UIHandler getUIHandler() {
		return uiHandler;
	}
	
	/**
	 * This method returns the current main app
	 *
	 * 
	 * @return Boolean
	 */
	public MainApp getMainApp() {
		return mainApp;
	}

	/**
	 * This method sets the main app
	 *
	 * 
	 * @return Boolean
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * This method takes in an integer, increases the internal step counter and takes a snapshot
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean stepForward() {
		this.steps++;
		snapshot[steps] = dataBase.retrieveAll();
		return true;
	}

	/**
	 * This method returns the current step number
	 *
	 * 
	 * @return int
	 */
	public int checkStep() {
		return this.steps;
	}
	
	/**
	 * This method returns the snapshot array
	 *
	 * 
	 * @return ArrayList<Task>[]
	 */
	public ArrayList<Task>[] getSnapshot() {
		return this.snapshot;
	}

	/**
	 * This method process a fuzzy date
	 *
	 * 
	 * @return Boolean
	 */
	private String fuzzyParseDate(String fuzzyDate) {
		String myDate = null;
		int count = fuzzyDate.length() - fuzzyDate.replace("-", "").length();
		if (count == 1) {
			myDate = LocalDateTime.now().getYear() + "-" + fuzzyDate;
		} else {
			if (count == 2) {
				myDate = fuzzyDate;
			}
		}
		return myDate;
	}

	/**
	 * This method process a fuzzy time
	 *
	 * 
	 * @return Boolean
	 */
	private LocalDateTime fuzzyParseTime(String fuzzyTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String myTime = null;

		DecimalFormat decimalFormatter = new DecimalFormat("00");

		if (fuzzyTime.contains("-")) {
			int count = fuzzyTime.length() - fuzzyTime.replace("-", "").length();
			if (count == 1) {
				myTime = LocalDateTime.now().getYear() + "-" + fuzzyTime + " " + "00:00";
			} else {
				if (count == 2) {
					myTime = fuzzyTime + " " + "00:00";
				}
			}
		} else {
			if (fuzzyTime.contains(":")) {
				myTime = LocalDateTime.now().getYear() + "-"
						+ decimalFormatter.format(LocalDateTime.now().getMonthValue()) + "-"
						+ decimalFormatter.format(LocalDateTime.now().getDayOfMonth()) + " " + fuzzyTime;
				// System.out.println(myTime);
			} else {
				uiHandler.sendMessage("no time or date detected", true);
			}
		}
		return LocalDateTime.parse(myTime, formatter);
	}
	
	/**
	 * This method gets the current time stamp
	 *
	 * 
	 * @return Boolean
	 */
	private String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	/**
	 * This method gets time unit
	 *
	 * 
	 * @return Boolean
	 */
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
}
