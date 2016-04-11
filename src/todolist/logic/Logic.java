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
    private static final String MESSAGE_FAILURE_OPEN_DIR = "I encountered a problem importing your new schedule. Sorry!";
    private static final String MESSAGE_SUCCESS_OPEN_DIR = "A new schedule has been imported.";
    private static final String MESSAGE_FAILURE_MOVE_DIR = "I encountered a problem migrating your schedule. Sorry!";
    private static final String MESSAGE_SUCCESS_MOVE_DIR = "Schedule has been saved to %1$s";
    private static final String MESSAGE_SUCCESS_REFRESH = "View refreshed. All searches and filters are cleared!";
    private static final String MESSAGE_SUCCESS_REDO = "You have reverted the last %d called-back action(s).";
    private static final String MESSAGE_SUCCESS_UNDO = "You have called back the last %d action(s).";
    private static final String MESSAGE_SUCCESS_UNARCHIVE = "[%1$s] has been unarchived! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_ARCHIVE = "[%1$s] has been archived! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_REMIND = "[%1$s] is set to trigger a reminder on %2$s. (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_FORWARD = "[%1$s] has been brought forward! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_POSTPONE = "[%1$s] has been postponed! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_NON_RECURRING = "[%1$s] is now a one-time-off task. (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_RECURRING = "[%1$s] is now a recurring task. (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_LABEL = "You have tagged [%1$s] as '%2$s'! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_SORT = "Your tasks are now sorted by '%1$s'. (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_FILTER = "Here are the related tasks under: '%1$s'. (to clear a filter, type 'reset')";
    private static final String MESSAGE_SUCCESS_SEARCH = "Here are your search results for: '%1$s'. (to clear a search, type 'reset')";
    private static final String MESSAGE_SUCCESS_DELETE = "[%1$s] has been deleted! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_EDIT = "[%1$s] has been edited! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_ADD_DEADLINE = "A new deadline [%1$s] has been created! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_ADD_EVENT = "A new event [%1$s] has been created! (not what you want? try 'undo 1')";
    private static final String MESSAGE_SUCCESS_ADD_TASK = "A new task [%1$s] has been created! (not what you want? try 'undo 1')";
    private static final String TITLE_TRUNCATION = "...";
    private static final int TITLE_CAP_SIZE = 15;
    private MainApp mainApp;
    public DataBase dataBase;
    private UIHandler uiHandler;
    private MainParser mainParser;
    private CaseSwitcher caseSwitcher;
    private int steps;
    private UtilityLogger logger;
    ArrayList<Task>[] snapshot;

    private static String MESSAGE_ADDING_FLOATING_TASK = "tring to add floating task: ";
    private static String MESSAGE_ADDING_EVENT = "tring to add event: ";
    private static String MESSAGE_ADDING_DEADLINE = "tring to add deadline: ";
    private static String MESSAGE_ADDING_RECURRING_EVENT = "tring to add recurring event: ";
    private static String MESSAGE_ADDING_RECURRING_DEADLINE = "tring to add reucrring deadline: ";
    private static String MESSAGE_EDITING_TASK = "tring to edit task: ";
    private static String MESSAGE_SEARCHING_TASK = "tring to search task: ";
    private static String MESSAGE_SORTING_TASK = "tring to sort task: ";
    private static String MESSAGE_DELETING_TASK = "tring to delete task: ";
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
     * 
     * @return Boolean
     */
    public Boolean addTask(String title) {
        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_FLOATING_TASK + title);

        Name name = new Name(title);
        Task newEvent = new Task(name, null, null, null, null, false, false, null);

        Boolean addResponse = dataBase.add(newEvent);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(newEvent);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_ADD_TASK, truncateTitle(title)), true);

        return addResponse;
    }

    private String truncateTitle(String title) {
        if (title.length() > TITLE_CAP_SIZE) {
            title = TITLE_TRUNCATION + title.substring(title.length() - TITLE_CAP_SIZE);
        }

        return title;
    }

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

    /**
     * This method adds a new event with start date and duration
     *
     * 
     * @return Boolean
     */
    public Boolean addEvent(String title, String startDate, String startTime, String quantity, String timeUnit) {
        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_EVENT + title);

        Name name = new Name(title);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(fuzzyParseDate(startDate) + " " + startTime, formatter);
        LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

        // if (!start.isAfter(LocalDateTime.now())) {
        // uiHandler.sendMessage("[" + title + "] is currently ongoing! Please
        // attend it before it's too late!",
        // true);
        // }
        //
        // if (!end.isAfter(LocalDateTime.now())) {
        // uiHandler.sendMessage("[" + title + "] is a task in the past", true);
        // }

        Task newEvent = new Task(name, start, end, null, null, false, false, null);

        Boolean addResponse = dataBase.add(newEvent);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(newEvent);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_ADD_EVENT, truncateTitle(title)), true);

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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_EVENT + title);

        LocalDateTime start = fuzzyParseTime(fuzzyTime);
        LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

        Name name = new Name(title);

        // if (!start.isAfter(LocalDateTime.now())) {
        // uiHandler.sendMessage("Oh no! [" + title + "] is currently ongoing!
        // Please attend it before it's too late!",
        // true);
        // }
        // if (!end.isAfter(LocalDateTime.now())) {
        // uiHandler.sendMessage("[" + title + "] is a task in the past", true);
        // }

        Task newEvent = new Task(name, start, end, null, null, false, false, null);

        Boolean addResponse = dataBase.add(newEvent);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(newEvent);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_ADD_EVENT, truncateTitle(title)), true);

        return addResponse;
    }

    /**
     * This method adds a new deadline.
     *
     * 
     * @return Boolean
     */
    public Boolean addDeadline(String title, String endDate, String endTime) {

        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_DEADLINE + title);

        Name name = new Name(title);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime end = LocalDateTime.parse(fuzzyParseDate(endDate) + " " + endTime, formatter);
        Task newEvent = new Task(name, null, end, null, null, false, false, null);

        // if (!end.isAfter(LocalDateTime.now())) {
        // uiHandler.sendMessage("[" + title + "] is a task in the past", true);
        // }

        Boolean addResponse = dataBase.add(newEvent);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(newEvent);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_ADD_DEADLINE, truncateTitle(title)), true);

        return addResponse;
    }

    /**
     * This method adds a new deadline.(less argument and fuzzy time)
     *
     * 
     * @return Boolean
     */
    public Boolean addDeadlineLess(String title, String fuzzyTime) {

        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_DEADLINE + title);

        Name name = new Name(title);
        LocalDateTime end = fuzzyParseTime(fuzzyTime);
        Task newEvent = new Task(name, null, end, null, null, false, false, null);

        // if (!end.isAfter(LocalDateTime.now())) {
        // uiHandler.sendMessage("[" + title + "] is a task in the past", true);
        // }

        Boolean addResponse = dataBase.add(newEvent);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(newEvent);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_ADD_DEADLINE, truncateTitle(title)), true);

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
        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_RECURRING_EVENT + title);

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
        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_RECURRING_EVENT + title);

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
        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_RECURRING_DEADLINE + title);

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
        logger.logAction(COMPONENT_LOGIC, MESSAGE_ADDING_RECURRING_DEADLINE + title);

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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

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
                if (tempTask.getEndTime() == null) {
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

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(tempTask);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_EDIT, truncateTitle(title)), true);

        return deleteResponse && addResponse;
    }

    /**
     * This method takes in the title of a task and deletes it.
     *
     * 
     * @return Boolean
     */
    public Boolean delete(String title) {

        logger.logAction(COMPONENT_LOGIC, MESSAGE_DELETING_TASK + title);

        Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
        Boolean deleteResponse = dataBase.delete(tempTask);

        // UI handling
        uiHandler.refresh();
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_DELETE, truncateTitle(title)), true);

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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_SEARCHING_TASK + input);

        ArrayList<Task> tempTaskList = dataBase.smartSearch(keyword);

        // UI handling
        uiHandler.display(tempTaskList);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_SEARCH, truncateTitle(keyword)), true);

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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_SEARCHING_TASK + category);

        ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("CATEGORY", category));

        // UI handling
        uiHandler.display(tempTaskList);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_FILTER, truncateTitle(category)), true);

        return true;
    }

    /**
     * This method sorts all tasks in according to the field name and order.
     *
     * 
     * @return Boolean
     */
    public Boolean sort(String fieldName, String order) {
        logger.logAction(COMPONENT_LOGIC, MESSAGE_SORTING_TASK + fieldName);

        dataBase.sort(fieldName, order);

        // UI handling
        uiHandler.refresh();
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_SORT, truncateTitle(fieldName)), true);

        return true;
    }

    /**
     * This method takes in the title of a task and labels it with a category.
     *
     * 
     * @return Boolean
     */
    public Boolean label(String title, String category) {

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

        Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
        Boolean deleteResponse = dataBase.delete(tempTask);

        tempTask.setCategory(new Category(category));
        Boolean addResponse = dataBase.add(tempTask);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(tempTask);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_LABEL, truncateTitle(title), truncateTitle(category)),
                true);

        return deleteResponse && addResponse;
    }

    /**
     * This method edits the recurring status of a task.
     *
     * 
     * @return Boolean
     */
    public Boolean setRecurring(String title, Boolean status, String interval) {

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

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
            uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_RECURRING, truncateTitle(title)), true);
        } else {
            uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_NON_RECURRING, truncateTitle(title)), true);
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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

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
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_POSTPONE, truncateTitle(title)), true);

        return deleteResponse && addResponse;
    }

    /**
     * This method forwards a task by a duration.
     *
     * 
     * @return Boolean
     */
    public Boolean forward(String title, String quantity, String timeUnit) {

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

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
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_FORWARD, truncateTitle(title)), true);

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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM h:ma");

        String remindTimeString = reminderTime.format(formatter);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(tempTask);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_REMIND, truncateTitle(title), truncateTitle(remindTimeString)), true);

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

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

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

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(tempTask);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_ARCHIVE, truncateTitle(title)), true);

        return deleteResponse && addResponse;
    }

    /**
     * This method takes in the title of a task and marks it as undone.
     *
     * 
     * @return Boolean
     */
    public Boolean undone(String title) {

        logger.logAction(COMPONENT_LOGIC, MESSAGE_EDITING_TASK + title);

        Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
        Boolean deleteResponse = dataBase.delete(tempTask);

        tempTask.setDoneStatus(false);
        Boolean addResponse = dataBase.add(tempTask);

        // UI handling
        uiHandler.refresh();
        uiHandler.highLight(tempTask);
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_UNARCHIVE, truncateTitle(title)), true);

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
        
        // UI handling
        uiHandler.refresh();
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_UNDO, undostep), true);

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
        
        // UI handling
        uiHandler.refresh();
        uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_REDO, redostep), true);

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
        uiHandler.sendMessage(MESSAGE_SUCCESS_REFRESH, true);
        
        return true;
    }

    /**
     * This method save all the tasks to a new file path
     *
     * 
     * @return Boolean
     */
    public boolean setNewFile(String path) {
        boolean success = dataBase.setNewFile(path);
        
        // UI handling
        if (success) {
            uiHandler.refresh();
            uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_MOVE_DIR, path), true);
        } else {
            uiHandler.sendMessage(String.format(MESSAGE_FAILURE_MOVE_DIR, path), true);
        }
        
        return success;
    }

    /**
     * This method opens a file from a given new file path
     *
     * 
     * @return Boolean
     */
    public Boolean openNewFile(String path) {
        boolean success = dataBase.openNewFile(path);
        
        // UI handling
        if (success) {
            uiHandler.refresh();
            uiHandler.sendMessage(String.format(MESSAGE_SUCCESS_OPEN_DIR, path), true);
        } else {
            uiHandler.sendMessage(String.format(MESSAGE_FAILURE_OPEN_DIR, path), true);
        }
        
        return success;
    }

    /**
     * This method call UIHandler to change tab
     *
     * 
     * @return Boolean
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
     * 
     * @return Boolean
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
     * This method takes in an integer, increases the internal step counter and
     * takes a snapshot
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
                // Use logger here
//                uiHandler.sendMessage("no time or date detected", true);
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
