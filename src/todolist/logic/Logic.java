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

import todolist.MainApp;
import todolist.common.UtilityLogger;
import todolist.common.UtilityLogger.Component;
import todolist.model.Category;
import todolist.model.Name;
import todolist.model.Reminder;
import todolist.model.SearchCommand;
import todolist.model.Task;
import todolist.model.TokenizedCommand;
import todolist.parser.MainParser;
import todolist.storage.DataBase;

public class Logic {

	private static final String TITLE_TRUNCATION = "...";
	private static final int TITLE_CAP_SIZE = 25;

	private MainApp mainApp;
	public DataBase dataBase;
	private UIHandler uiHandler;
	private MainParser mainParser;
	private CaseSwitcher caseSwitcher;
	private int steps;
	private UtilityLogger logger;
	ArrayList<Task>[] snapshot;

	protected static Component COMPONENT_LOGIC = UtilityLogger.Component.Logic;

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
		logger = new UtilityLogger();
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
	 * @param String
	 *            title of the task to add
	 * 
	 * @return Boolean return true if the task is successfully added
	 */
	public Boolean addTask(String title) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_FLOATING_TASK + title);

		Name name = new Name(title);
		Task newEvent = new Task(name, null, null, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_ADD_TASK, truncateTitle(title)), true);

		return addResponse;
	}

	/**
	 * This method adds a new event with start date and duration
	 * 
	 * @param title
	 *            the title of the event
	 * @param startDate
	 *            start date of the event
	 * @param startTime
	 *            start time of the event
	 * @param quantity
	 *            quantity of the duration of the event
	 * @param timeUnit
	 *            unit of the duration of the event
	 * @return Boolean true if the event is successfully added
	 */
	public Boolean addEvent(String title, String startDate, String startTime, String quantity, String timeUnit) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_EVENT + title);

		Name name = new Name(title);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse(fuzzyParseDate(startDate) + " " + startTime, formatter);
		LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

		Task newEvent = new Task(name, start, end, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_ADD_EVENT, truncateTitle(title)), true);

		return addResponse;
	}

	/**
	 * This method adds a new event with start date and duration(less argument
	 * and fuzzy time)
	 * 
	 * @param title
	 *            title of the event
	 * @param fuzzyTime
	 *            date or time of the event
	 * @param quantity
	 *            quantity of the duration of the event
	 * @param timeUnit
	 *            unit of the duration of the event
	 * @return boolean true if the event is successfully added
	 */
	public Boolean addEventLess(String title, String fuzzyTime, String quantity, String timeUnit) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_EVENT + title);

		LocalDateTime start = fuzzyParseTime(fuzzyTime);
		LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

		Name name = new Name(title);

		Task newEvent = new Task(name, start, end, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_ADD_EVENT, truncateTitle(title)), true);

		return addResponse;
	}

	/**
	 * 
	 * @param title
	 *            title of the deadline to add
	 * @param endDate
	 *            end date of the deadline to add
	 * @param endTime
	 *            end time of the deadline to add
	 * @return Boolean true if the deadline is successfully added
	 */
	public Boolean addDeadline(String title, String endDate, String endTime) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_DEADLINE + title);

		Name name = new Name(title);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime end = LocalDateTime.parse(fuzzyParseDate(endDate) + " " + endTime, formatter);
		Task newEvent = new Task(name, null, end, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_ADD_DEADLINE, truncateTitle(title)), true);

		return addResponse;
	}

	/**
	 * This method adds a new deadline.(less argument and fuzzy time)
	 * 
	 * @param title
	 *            title of the deadline to add
	 * @param fuzzyTime
	 *            date or time of the deadline to add
	 * @return Boolean true if the deadline is successfully added
	 */
	public Boolean addDeadlineLess(String title, String fuzzyTime) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_DEADLINE + title);

		Name name = new Name(title);
		LocalDateTime end = fuzzyParseTime(fuzzyTime);
		Task newEvent = new Task(name, null, end, null, null, false, false, null);

		Boolean addResponse = dataBase.add(newEvent);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(newEvent);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_ADD_DEADLINE, truncateTitle(title)), true);

		return addResponse;
	}

	/**
	 * This method adds an recurring event
	 * 
	 * @param interval
	 *            interval of the recurring event to add
	 * @param title
	 *            title of the event to add
	 * @param startDate
	 *            start date of the event to add
	 * @param startTime
	 *            start time of the event to add
	 * @param quantity
	 *            quantity of the duration of the event
	 * @param timeUnit
	 *            unit of the duration of the event
	 * @return Boolean true if the recurring event is successfully added
	 */
	public Boolean addRecurringEvent(String interval, String title, String startDate, String startTime, String quantity,
			String timeUnit) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_RECURRING_EVENT + title);

		Boolean addResponse = addEvent(title, startDate, startTime, quantity, timeUnit);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}

	/**
	 * This method adds an recurring event.(less argument and fuzzy time)
	 * 
	 * @param interval
	 *            interval of the recurring event to add
	 * @param title
	 *            title of the event to add
	 * @param fuzzyTime
	 *            start date or time of the event to add
	 * @param quantity
	 *            quantity of the duration of the event
	 * @param timeUnit
	 *            unit of the duration of the event
	 * @return Boolean true if the recurring event is successfully added
	 */
	public Boolean addRecurringEventLess(String interval, String title, String fuzzyTime, String quantity,
			String timeUnit) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_RECURRING_EVENT + title);

		Boolean addResponse = addEventLess(title, fuzzyTime, quantity, timeUnit);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}

	/**
	 * This method adds an recurring deadline
	 * 
	 * @param interval
	 *            interval of the recurring deadline to add
	 * @param title
	 *            title of the deadline to add
	 * @param endDate
	 *            end date of the deadline to add
	 * @param endTime
	 *            end time of the deadline to add
	 * @return Boolean true if the recurring deadline is successfully added
	 */
	public Boolean addRecurringDeadline(String interval, String title, String endDate, String endTime) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_RECURRING_DEADLINE + title);

		Boolean addResponse = addDeadline(title, endDate, endTime);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}

	/**
	 * This method adds an recurring deadline.(less argument and fuzzy time)
	 * 
	 * @param interval
	 *            interval of the recurring deadline to add
	 * @param title
	 *            title of the deadline to add
	 * @param fuzzyTime
	 *            end date or time of the deadline
	 * @return Boolean true if the recurring deadline is successfully added
	 */
	public Boolean addRecurringDeadlineLess(String interval, String title, String fuzzyTime) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_ADDING_RECURRING_DEADLINE + title);

		Boolean addResponse = addDeadlineLess(title, fuzzyTime);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}

	/**
	 * This method edits a task.
	 * 
	 * @param title
	 *            title of the task to edit
	 * @param fieldName
	 *            the field to edit
	 * @param newValue
	 *            the new value to put
	 * @return Boolean true if the task is successfully edited
	 */
	public Boolean edit(String title, String fieldName, String newValue) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		switch (fieldName) {
		case "title":
			tempTask.setName(new Name(newValue));
			break;
		case "start-time":
			LocalDateTime start = null;
			if (newValue.equals("remove")) {
				start = null;
				tempTask.setStartTime(start);
			} else {
				start = LocalDateTime.parse(newValue, formatter);
				if (tempTask.getEndTime() == null) {
					tempTask.setStartTime(start);
					tempTask.setEndTime(start);
				} else {
					tempTask.setStartTime(start);
					if(tempTask.getEndTime().isBefore(start)) {
						tempTask.setEndTime(start);
					}
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
				if(tempTask.getStartTime().isAfter(end)) {
					tempTask.setStartTime(end);
				}
				tempTask.setEndTime(end);
			}
			break;
		}

		Boolean addResponse = dataBase.add(tempTask);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_EDIT, truncateTitle(title)), true);

		return deleteResponse && addResponse;
	}

	/**
	 * This method takes in the title of a task and deletes it.
	 * 
	 * @param title
	 *            title of the task to delete
	 * @return Boolean return true if the task is successfully deleted
	 */
	public Boolean delete(String title) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_DELETING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		// UI handling
		uiHandler.refresh();
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_DELETE, truncateTitle(title)), true);

		return deleteResponse;
	}

	/**
	 * This method takes in the title of a task and displays it.
	 * 
	 * @param keyword
	 *            the keyword in title to search for
	 * @return Boolean return true if there is at one search result
	 */
	public Boolean search(String input) {
		
		String[] keyword = input.split(" ");

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_SEARCHING_TASK + input);

		ArrayList<Task> tempTaskList = dataBase.smartSearch(keyword);

		// UI handling
		uiHandler.display(tempTaskList);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_SEARCH, truncateTitle(keyword)), false);

		return true;
	}

	/**
	 * This method takes in the name of a category and displays tasks of that
	 * category.
	 * 
	 * @param category
	 *            the category of tasks to filter base on
	 * @return Boolean if there is at one task found under this category
	 */
	public Boolean filter(String category) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_SEARCHING_TASK + category);

		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("CATEGORY", category));

		// UI handling
		uiHandler.display(tempTaskList);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_FILTER, truncateTitle(category)), false);

		return true;
	}

	/**
	 * This method sorts all tasks in according to the field name and order.
	 * 
	 * @param fieldName
	 *            the field to sort based on
	 * @param order
	 *            the order to sort the list of task
	 * @return Boolean return true if the list of task is successfully sorted
	 */
	public Boolean sort(String fieldName, String order) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_SORTING_TASK + fieldName);

		dataBase.sort(fieldName, order);

		// UI handling
		uiHandler.refresh();
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_SORT, truncateTitle(fieldName)), true);

		return true;
	}

	/**
	 * This method takes in the title of a task and labels it with a category.
	 * 
	 * @param title
	 *            title of the task to label
	 * @param category
	 *            category of the task to put under
	 * @return Boolean return true if the task is successfully labeled under the
	 *         category
	 */
	public Boolean label(String title, String category) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		tempTask.setCategory(new Category(category));
		Boolean addResponse = dataBase.add(tempTask);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(
				String.format(ResponseMessage.MESSAGE_SUCCESS_LABEL, truncateTitle(title), truncateTitle(category)),
				true);

		return deleteResponse && addResponse;
	}

	/**
	 * This method edits the recurring status of a task.
	 * 
	 * @param title
	 *            title of the task to set
	 * @param status
	 *            true if it is to set as recurring, false if it is to remove
	 *            the recurring
	 * @param interval
	 *            the interval to repeat the task
	 * @return Boolean true if the task is successfully set as recurring or
	 *         removed the recurring
	 */
	public Boolean setRecurring(String title, Boolean status, String interval) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);
		if (tempTask.getEndTime() == null) {
			throw new RuntimeException();
		}
		tempTask.setRecurring(status);
		tempTask.setInterval(interval);
		Boolean addResponse = dataBase.add(tempTask);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		if (status) {
			uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_RECURRING, truncateTitle(title)), true);
		} else {
			uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_NON_RECURRING, truncateTitle(title)),
					true);
		}

		return deleteResponse && addResponse;
	}

	/**
	 * This method postpones a task by a duration
	 * 
	 * @param title
	 *            title of the task to postpone
	 * @param quantity
	 *            quantity of the duration to postpone
	 * @param timeUnit
	 *            unit of the duration to postpone
	 * @return Boolean return true if the task is successfully postponed
	 */
	public Boolean postpone(String title, String quantity, String timeUnit) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		Boolean addResponse = false;

		if (tempTask.getReminder() != null && tempTask.getReminder().getStatus()) {
			LocalDateTime oldReminderTime = tempTask.getReminder().getTime();
			LocalDateTime newReminderTime = oldReminderTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempTask.setReminder(new Reminder(true, newReminderTime));
		}

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setEndTime(tempEndTime);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);
		}
		addResponse = dataBase.add(tempTask);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_POSTPONE, truncateTitle(title)), true);

		return deleteResponse && addResponse;
	}

	/**
	 * This method forwards a task by a duration.
	 * 
	 * @param title
	 *            title of the task to forward
	 * @param quantity
	 *            quantity of the duration to forward
	 * @param timeUnit
	 *            unit of the duration to forward
	 * @return Boolean return true if the task is successfully forward
	 */
	public Boolean forward(String title, String quantity, String timeUnit) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		Boolean addResponse = false;

		if (tempTask.getReminder() != null && tempTask.getReminder().getStatus()) {
			LocalDateTime oldReminderTime = tempTask.getReminder().getTime();
			LocalDateTime newReminderTime = oldReminderTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempTask.setReminder(new Reminder(true, newReminderTime));
		}

		if (tempTask.getStartTime() == null) {
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempTask.setEndTime(tempEndTime);
		} else {
			LocalDateTime tempStartTime = tempTask.getStartTime();
			LocalDateTime tempEndTime = tempTask.getEndTime();
			tempStartTime = tempStartTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempEndTime = tempEndTime.minus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
			tempTask.setStartTime(tempStartTime);
			tempTask.setEndTime(tempEndTime);
		}

		addResponse = dataBase.add(tempTask);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_FORWARD, truncateTitle(title)), true);

		return deleteResponse && addResponse;
	}

	/**
	 * This method adds a task with remind and triggers the remind at the
	 * deadline.
	 * 
	 * @param arg
	 *            the information of the task to set reminder for
	 * @return Boolean return true if a reminder is successfully for this task
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
	 * @param title
	 *            title of the task to set reminder for
	 * @return Boolean return true if a reminder is successfully set for this
	 *         task
	 */
	public Boolean remind(String title) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		return remindBef(title, "0", "minute");
	}

	/**
	 * This method adds remind to an existing task and triggers the remind a
	 * duration before the deadline.
	 * 
	 * @param title
	 *            title of the task to set reminder for
	 * @param quantity
	 *            quantity of the duration to remind before the end time of the
	 *            task
	 * @param timeUnit
	 *            unit of the duration to remind before the end time of the task
	 * @return Boolean return true if a reminder is successfully set for this
	 *         task
	 */
	public Boolean remindBef(String title, String quantity, String timeUnit) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

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

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM h:mma");

		String remindTimeString = reminderTime.format(formatter);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_REMIND, truncateTitle(title),
				truncateTitle(remindTimeString)), true);

		return deleteResponse && addResponse;
	}

	/**
	 * This method adds a task with remind and triggers the remind a duration
	 * before the deadline.
	 * 
	 * @param quantity
	 *            quantity of the duration to remind before the end time of the
	 *            task
	 * @param timeUnit
	 *            unit of the duration to remind before the end time of the task
	 * @param arg
	 *            the information of the task to set reminder for
	 * @return Boolean return true if a reminder is successfully set for this
	 *         task
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
	 * This method takes in the title of a task and removes its reminder.
	 * 
	 * @param title
	 *            title of the task to remove the reminder
	 * @return Boolean return true if the reminder is successfully removed
	 */
	public Boolean removeRemind(String title) {
		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);

		Boolean deleteResponse = dataBase.delete(tempTask);

		tempTask.setReminder(new Reminder(false, null));

		Boolean addResponse = dataBase.add(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_REMOVE_REMIND, truncateTitle(title)), true);

		return addResponse && deleteResponse;
	}

	/**
	 * This method takes in the title of a task and marks it as done.
	 * 
	 * @param title
	 *            title of the task to set the done status
	 * @return Boolean return true if the task is successfully set as done
	 */
	public Boolean done(String title) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		tempTask.setDoneStatus(true);
		String tempName = tempTask.getName().getName();
		
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
			
			Task newTempTask = null;
			if(tempTask.getReminder() != null && tempTask.getReminder().getStatus()) {
				Reminder newReminder = new Reminder(true, tempTask.getReminder().getTime().plus(Long.parseLong(length), generateTimeUnit(unit)));
				newTempTask = new Task(new Name(tempName), newStartTime, newEndTime, tempTask.getCategory(),
						newReminder, false, true, interval);
			} else {
				newTempTask = new Task(new Name(tempName), newStartTime, newEndTime, tempTask.getCategory(),
						tempTask.getReminder(), false, true, interval);
			}
			
			@SuppressWarnings("unused")
			Boolean addResponse = dataBase.add(newTempTask);

		}
		
		if (tempTask.getRecurringStatus()) {
			tempTask.setName(new Name(tempName + " finished on " + getCurrentTimeStamp()));
			tempTask.setRecurring(false);
			tempTask.setInterval(null);
		}
		
		tempTask.setReminder(new Reminder(false, null));

		Boolean addResponse = dataBase.add(tempTask);


		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_ARCHIVE, truncateTitle(title)), true);

		return deleteResponse && addResponse;
	}

	/**
	 * This method takes in the title of a task and marks it as undone.
	 * 
	 * @param title
	 *            title of the task to set the done status
	 * @return Boolean return true if the task is successfully set as undone
	 */
	public Boolean undone(String title) {

		logger.logAction(COMPONENT_LOGIC, ResponseMessage.MESSAGE_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBase.delete(tempTask);

		tempTask.setDoneStatus(false);
		Boolean addResponse = dataBase.add(tempTask);

		// UI handling
		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_UNARCHIVE, truncateTitle(title)), true);

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
	 * @param undostep
	 *            number of steps to undo
	 * @return Boolean return true if successfully go back to the number of
	 *         steps before
	 */
	public Boolean undo(int undostep) {
		Boolean undoResponse = false;
		steps = steps - undostep;
		undoResponse = dataBase.recover(snapshot[steps]);

		// UI handling
		uiHandler.refresh();
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_UNDO, undostep), true);

		return undoResponse;
	}

	/**
	 * This method takes in an integer and redo that number of steps.
	 * 
	 * @param undostep
	 *            number of steps to redo
	 * @return Boolean return true if successfully go back to the number of
	 *         steps later
	 */
	public Boolean redo(int redostep) {
		Boolean redoResponse = false;
		steps = steps + redostep;
		redoResponse = dataBase.recover(snapshot[steps]);

		// UI handling
		uiHandler.refresh();
		uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_REDO, redostep), true);

		return redoResponse;
	}

	/**
	 * This method resets the view
	 *
	 *
	 * @return Boolean
	 */
	public Boolean reset() {

		// UI handling
		uiHandler.refresh();
		uiHandler.sendMessage(ResponseMessage.MESSAGE_SUCCESS_REFRESH, true);

		return true;
	}

	/**
	 * This method save all the tasks to a new file path
	 * 
	 * @param path
	 *            new path to save the tasks
	 * @return Boolean return true if the path successfully set
	 */
	public boolean setNewFile(String path) {
		boolean success = dataBase.setNewFile(path);

		// UI handling
		if (success) {
			uiHandler.refresh();
			uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_MOVE_DIR, path), true);
		} else {
			uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_FAILURE_MOVE_DIR, path), true);
		}

		return success;
	}

	/**
	 * This method opens a file from a given new file path
	 * 
	 * @param path
	 *            new path to create a new file to store the task
	 * @return Boolean return true if the a new file is successfully created in
	 *         the new path
	 */
	public Boolean openNewFile(String path) {
		boolean success = dataBase.openNewFile(path);

		// UI handling
		if (success) {
			uiHandler.refresh();
			uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_SUCCESS_OPEN_DIR, path), true);
		} else {
			uiHandler.sendMessage(String.format(ResponseMessage.MESSAGE_FAILURE_OPEN_DIR, path), true);
		}

		return success;
	}

	/**
	 * This method call UIHandler to change tab
	 * 
	 * @param workplace
	 *            workplace in UI
	 */
	public void tab(String workplace) {
		switch (workplace) {
		case "all":
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
	 * This method call UIHandler to display a message when flexi command cannot
	 * be parsed
	 * 
	 * @param keyword
	 *            the key
	 * @return Boolean return true if the message is successfully sent
	 */
	public Boolean invalid(String keyword) {
		uiHandler.sendMessage("Sorry but I don't understand! I will scold my makers for you.", true);
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
		uiHandler.refresh();
		uiHandler.sendMessage("Schedule cleaned! All the tasks are deleted from your schedule. (not what you want? try 'undo 1')", true);
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
	 * @return mainApp return the current mainApp
	 */
	public MainApp getMainApp() {
		return mainApp;
	}

	/**
	 * This method sets the main app
	 * 
	 * @param mainApp
	 *            the MainApp to set
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * This method increases the internal step counter and takes a snapshot
	 * 
	 * @return Boolean return true if the step is successfully incremented and
	 *         the snapshot is taken
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
	 * @return steps the current step number
	 */
	public int checkStep() {
		return this.steps;
	}

	/**
	 * This method returns the snapshot array
	 * 
	 * @return snapshot the snapshot of all the steps
	 */
	public ArrayList<Task>[] getSnapshot() {
		return this.snapshot;
	}

	/**
	 * This method process a fuzzy date
	 * 
	 * @param fuzzyDate
	 *            date to be parsed
	 * @return myDate the date after parsing
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
	 * @param fuzzyTime
	 *            time to be parsed
	 * @return myTime time after parsing
	 */
	private LocalDateTime fuzzyParseTime(String fuzzyTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String myTime = null;

		if (fuzzyTime.contains("-")) {
			myTime = fuzzyParseDate(fuzzyTime) + " " + "00:00";
		} else {
			if (fuzzyTime.contains(":")) {
				myTime = getCurrentDate() + " " + fuzzyTime;
			}
		}
		return LocalDateTime.parse(myTime, formatter);
	}

	private String getCurrentDate() {
		DecimalFormat decimalFormatter = new DecimalFormat("00");
		return LocalDateTime.now().getYear() + "-" + decimalFormatter.format(LocalDateTime.now().getMonthValue()) + "-"
				+ decimalFormatter.format(LocalDateTime.now().getDayOfMonth());
	}

	private String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
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

	/**
	 * This method truncates the tile of a task
	 * 
	 * @param title
	 *            tile to be truncated
	 * @return title title after truncation
	 */
	private String truncateTitle(String title) {
		if (title.length() > TITLE_CAP_SIZE) {
			title = TITLE_TRUNCATION + title.substring(title.length() - TITLE_CAP_SIZE);
		}

		return title;
	}

	/**
	 * This method is to truncate the title
	 * 
	 * @param title
	 *            array of string to be truncated
	 * @return title string of title after truncation
	 */
	private String truncateTitle(String[] title) {
		String titleString = "";
		for (String section : title) {
			titleString += section + " ";
		}

		titleString = titleString.trim();
		if (titleString.length() > TITLE_CAP_SIZE) {
			titleString = TITLE_TRUNCATION + titleString.substring(titleString.length() - TITLE_CAP_SIZE);
		}

		return titleString;
	}
}
