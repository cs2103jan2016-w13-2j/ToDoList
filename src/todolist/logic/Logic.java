package todolist.logic;

import java.text.DecimalFormat;
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

//@@author zhangjiyi
public class Logic {

<<<<<<< Updated upstream
	private MainApp mainApp;
	public DataBase dataBase;
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
		this.setMainApp(mainApp);
		this.dataBase = new DataBase();
		this.mainParser = new MainParser();
		this.uiHandler = new UIHandler(dataBase, mainApp, this);
		this.caseSwitcher = new CaseSwitcher(this);
		this.steps = 0;
	}
    
	public void clean() {
		this.dataBase.clear();
	}
	public UIHandler getUIHandler() {
		return uiHandler;
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
	 * This method returns the current step number
	 *
	 * 
	 * @return int
	 */
	public int checkStep() {
		return this.steps;
	}

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
	 * This method resets the view
	 *
	 * 
	 * @return void
	 */
	public void reset() {
		uiHandler.refresh();
		uiHandler.sendMessage("View refreshed. All search and filter results are cleared!");
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

	public Boolean addRecurringEventLess(String interval, String title, String fuzzyTime, String quantity, String timeUnit) {
		Boolean addResponse = addEventLess(title, fuzzyTime, quantity, timeUnit);
		Boolean setRecurringResponse = setRecurring(title, true, interval);
		return addResponse && setRecurringResponse;
	}

	public Boolean addRecurringDeadlineLess(String interval, String title, String fuzzyTime) {
		Boolean addResponse = addDeadlineLess(title, fuzzyTime);
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
			LocalDateTime start = LocalDateTime.parse(fuzzyParseDate(startDate) + " " + startTime, formatter);
			LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			System.out.println(start);

			if (!start.isAfter(LocalDateTime.now())) {
				logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
				uiHandler.sendMessage(
						"Oh no! [" + title + "] is currently ongoing! Please attend it before it's too late!");
			}

			Task newEvent = new Task(name, start, end, null, null, false, false, null);

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new event [" + newEvent.getName().getName()
					+ "] has been created successfully! [not what you want? try 'undo']");
			return addResponse;
		} else {
			return false;
		}
	}

	public String fuzzyParseDate(String fuzzyDate) {
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

	public LocalDateTime fuzzyParseTime(String fuzzyTime) {
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
				uiHandler.sendMessage("no time or date detected");
			}
		}
		return LocalDateTime.parse(myTime, formatter);
	}

	public Boolean addEventLess(String title, String fuzzyTime, String quantity, String timeUnit) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_EVENT + title + fuzzyTime + quantity + timeUnit);

			LocalDateTime start = fuzzyParseTime(fuzzyTime);
			LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

			Name name = new Name(title);

			if (!start.isAfter(LocalDateTime.now())) {
				logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
				uiHandler.sendMessage(
						"Oh no! [" + title + "] is currently ongoing! Please attend it before it's too late!");
			}

			Task newEvent = new Task(name, start, end, null, null, false, false, null);

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new event [" + newEvent.getName().getName()
					+ "] has been created successfully! [not what you want? try 'undo']");
			return addResponse;
		} else {
			return false;
		}
	}

	/**
	 * This method adds a new deadline.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addDeadline(String title, String endDate, String endTime) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + endDate + endTime);

			Name name = new Name(title);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime end = LocalDateTime.parse(fuzzyParseDate(endDate) + " " + endTime, formatter);
			Task newEvent = new Task(name, null, end, null, null, false, false, null);

			if (!end.isAfter(LocalDateTime.now())) {
				logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
				uiHandler.sendMessage(
						"Oh no! ToDoList currently does not support time travelling! Try creating a task that is due after now.");
			}

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new deadline [" + newEvent.getName().getName()
					+ "] has been created successfully. [not what you want? try 'undo']");
			return addResponse;
		} else {
			return false;
		}
	}

	public Boolean addDeadlineLess(String title, String fuzzyTime) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + fuzzyTime);

			Name name = new Name(title);
			LocalDateTime end = fuzzyParseTime(fuzzyTime);
			Task newEvent = new Task(name, null, end, null, null, false, false, null);

			if (!end.isAfter(LocalDateTime.now())) {
				logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
				uiHandler.sendMessage(
						"Oh no! ToDoList currently does not support time travelling! Try creating a task that is due after now.");
			}

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new deadline [" + newEvent.getName().getName()
					+ "] has been created successfully. [not what you want? try 'undo']");
			return addResponse;
		} else {
			return false;
		}
	}

	/**
	 * This method adds a new floating task.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean addTask(String title) {

		if (noRepeat(title)) {
			logger.log(Level.INFO, LOGGING_ADDING_FLOATING_TASK + title);

			Name name = new Name(title);
			Task newEvent = new Task(name, null, null, null, null, false, false, null);

			Boolean addResponse = dataBaseAdd(newEvent);
			uiHandler.refresh();
			uiHandler.highLight(newEvent);
			uiHandler.sendMessage("A new un-dated task [" + title
					+ "] has been created successfully. [not what you want? try 'undo']");
			return addResponse;
		} else {
			return false;
		}
	}
=======
    private MainApp mainApp;
    public DataBase dataBase;
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
        this.setMainApp(mainApp);
        this.dataBase = new DataBase();
        this.mainParser = new MainParser();
        this.uiHandler = new UIHandler(dataBase, mainApp, this);
        this.caseSwitcher = new CaseSwitcher(this);
        this.steps = 0;
    }

    public UIHandler getUIHandler() {
        return uiHandler;
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
     * This method returns the current step number
     *
     * 
     * @return int
     */
    public int checkStep() {
        return this.steps;
    }

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
     * This method resets the view
     *
     * 
     * @return void
     */
    public void reset() {
        uiHandler.refresh();
        uiHandler.sendMessage("View refreshed. All search and filter results are cleared!", true);
    }

    /**
     * This method adds an recurring event
     *
     * 
     * @return void
     */
    public Boolean addRecurringEvent(String interval, String title, String startDate, String startTime, String quantity,
            String timeUnit) {
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

    public Boolean addRecurringEventLess(String interval, String title, String fuzzyTime, String quantity,
            String timeUnit) {
        Boolean addResponse = addEventLess(title, fuzzyTime, quantity, timeUnit);
        Boolean setRecurringResponse = setRecurring(title, true, interval);
        return addResponse && setRecurringResponse;
    }

    public Boolean addRecurringDeadlineLess(String interval, String title, String fuzzyTime) {
        Boolean addResponse = addDeadlineLess(title, fuzzyTime);
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
            LocalDateTime start = LocalDateTime.parse(fuzzyParseDate(startDate) + " " + startTime, formatter);
            LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

            System.out.println(start);

            if (!start.isAfter(LocalDateTime.now())) {
                logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
                uiHandler.sendMessage(
                        "Oh no! [" + title + "] is currently ongoing! Please attend it before it's too late!", true);
            }

            Task newEvent = new Task(name, start, end, null, null, false, false, null);

            Boolean addResponse = dataBaseAdd(newEvent);
            uiHandler.refresh();
            uiHandler.highLight(newEvent);
            uiHandler.sendMessage("A new event [" + newEvent.getName().getName()
                    + "] has been created successfully! [not what you want? try 'undo']", true);
            return addResponse;
        } else {
            return false;
        }
    }

    public String fuzzyParseDate(String fuzzyDate) {
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

    public LocalDateTime fuzzyParseTime(String fuzzyTime) {
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

    public Boolean addEventLess(String title, String fuzzyTime, String quantity, String timeUnit) {

        if (noRepeat(title)) {
            logger.log(Level.INFO, LOGGING_ADDING_EVENT + title + fuzzyTime + quantity + timeUnit);

            LocalDateTime start = fuzzyParseTime(fuzzyTime);
            LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));

            Name name = new Name(title);

            if (!start.isAfter(LocalDateTime.now())) {
                logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
                uiHandler.sendMessage(
                        "Oh no! [" + title + "] is currently ongoing! Please attend it before it's too late!", true);
            }

            Task newEvent = new Task(name, start, end, null, null, false, false, null);

            Boolean addResponse = dataBaseAdd(newEvent);
            uiHandler.refresh();
            uiHandler.highLight(newEvent);
            uiHandler.sendMessage("A new event [" + newEvent.getName().getName()
                    + "] has been created successfully! [not what you want? try 'undo']", true);
            return addResponse;
        } else {
            return false;
        }
    }

    /**
     * This method adds a new deadline.
     *
     * 
     * @return Boolean
     */
    public Boolean addDeadline(String title, String endDate, String endTime) {

        if (noRepeat(title)) {
            logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + endDate + endTime);

            Name name = new Name(title);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime end = LocalDateTime.parse(fuzzyParseDate(endDate) + " " + endTime, formatter);
            Task newEvent = new Task(name, null, end, null, null, false, false, null);

            if (!end.isAfter(LocalDateTime.now())) {
                logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
                uiHandler.sendMessage(
                        "Oh no! ToDoList currently does not support time travelling! Try creating a task that is due after now.",
                        true);
            }

            Boolean addResponse = dataBaseAdd(newEvent);
            uiHandler.refresh();
            uiHandler.highLight(newEvent);
            uiHandler.sendMessage("A new deadline [" + newEvent.getName().getName()
                    + "] has been created successfully. [not what you want? try 'undo']", true);
            return addResponse;
        } else {
            return false;
        }
    }

    public Boolean addDeadlineLess(String title, String fuzzyTime) {

        if (noRepeat(title)) {
            logger.log(Level.INFO, LOGGING_ADDING_DEADLINE + title + fuzzyTime);

            Name name = new Name(title);
            LocalDateTime end = fuzzyParseTime(fuzzyTime);
            Task newEvent = new Task(name, null, end, null, null, false, false, null);

            if (!end.isAfter(LocalDateTime.now())) {
                logger.log(Level.INFO, LOGGING_TIME_ERROR + title);
                uiHandler.sendMessage(
                        "Oh no! ToDoList currently does not support time travelling! Try creating a task that is due after now.",
                        true);
            }

            Boolean addResponse = dataBaseAdd(newEvent);
            uiHandler.refresh();
            uiHandler.highLight(newEvent);
            uiHandler.sendMessage("A new deadline [" + newEvent.getName().getName()
                    + "] has been created successfully. [not what you want? try 'undo']", true);
            return addResponse;
        } else {
            return false;
        }
    }

    /**
     * This method adds a new floating task.
     *
     * 
     * @return Boolean
     */
    public Boolean addTask(String title) {

        if (noRepeat(title)) {
            logger.log(Level.INFO, LOGGING_ADDING_FLOATING_TASK + title);

            Name name = new Name(title);
            Task newEvent = new Task(name, null, null, null, null, false, false, null);

            Boolean addResponse = dataBaseAdd(newEvent);
            uiHandler.refresh();
            uiHandler.highLight(newEvent);
            uiHandler.sendMessage("A new un-dated task [" + title
                    + "] has been created successfully. [not what you want? try 'undo']", true);
            return addResponse;
        } else {
            return false;
        }
    }
>>>>>>> Stashed changes

    /**
     * This method takes in the title of a task and marks it as done.
     *
     * 
     * @return Boolean
     */
    public Boolean done(String title) {

        logger.log(Level.INFO, LOGGING_EDITING_TASK + title);
<<<<<<< Updated upstream
        
        if(noRepeat(title)) {
        	return false;
        }
=======

>>>>>>> Stashed changes
        Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
        Boolean deleteResponse = dataBaseDelete(tempTask);

        tempTask.setDoneStatus(true);
        Boolean addResponse = dataBaseAdd(tempTask);

        uiHandler.refresh();
        uiHandler.highLight(tempTask);
<<<<<<< Updated upstream
        uiHandler.sendMessage("[" + title
                + "] has been marked as completed! Woohoo another one down! [not what you want? try 'undo']");
=======
        uiHandler.sendMessage(
                "[" + title
                        + "] has been marked as completed! Woohoo another one down! [not what you want? try 'undo']",
                true);
>>>>>>> Stashed changes

        return deleteResponse && addResponse;
    }

<<<<<<< Updated upstream
	public Boolean undone(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setDoneStatus(false);
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage(
				"[" + title + "] has been marked as ongoing! Go get it TIGER! [not what you want? try 'undo']");

		return deleteResponse && addResponse;
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
		uiHandler.sendMessage("[" + title + "] has been edited successfully! [not what you want? try 'undo']");

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
		Boolean deleteResponse = dataBaseDelete(tempTask);

		uiHandler.refresh();
		uiHandler.sendMessage("[" + title + "] has been deleted successfully! [not what you want? try 'undo']");

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
		uiHandler.sendMessage("Here are your search results for '" + title + "'! [to clear this search, type 'reset']");
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
		uiHandler.sendMessage("Here are the related tasks under " + category.toUpperCase()
				+ "! [to clear this filter, type 'reset']");
	}

	/**
	 * This method sorts all tasks in according to the field name and order.
	 *
	 * 
	 * @return void
	 */
	public void sort(String fieldName, String order) {
		if (fieldName.isEmpty()) {
			uiHandler.sendMessage(
					"Please specify a sorting order! Try sort ['start' | 'end' | 'category' | 'name'], followed by [ascending | descending].");
		}
		dataBase.sort(fieldName, order);
		uiHandler.refresh();
		uiHandler.sendMessage(
				"Ta-da! Your tasks have been sorted by " + fieldName + "! [not what you want? try 'undo']");
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
	 * @return Boolean
	 */
	public Boolean label(String title, String category) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setCategory(new Category(category));
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage("You have categorised [" + title + "] under " + category.toUpperCase()
				+ " ! [not what you want? try 'undo']");

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
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setRecurring(status);
		tempTask.setInterval(interval);
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);

		if (status) {
			uiHandler.sendMessage("[" + title + "] is now a recurring task! [not what you want? try 'undo']");
		} else {
			uiHandler.sendMessage("[" + title + "] is now an ad-hoc task! [not what you want? try 'undo']");
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
			uiHandler.sendMessage("[" + title + "] has been postponed! [not what you want? try 'undo']");
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
			uiHandler.sendMessage("[" + title + "] has been rescheduled forward! [not what you want? try 'undo']");
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
	 * @return Boolean
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
	 * @return Boolean
	 */
	public Boolean undo(int undostep) {

		if (steps <= 0) {
			uiHandler.sendMessage("Undo was unsuccessful. No actions to undo!");
		}

		Boolean undoResponse = dataBase.retrieveHistory(steps - undostep);
		steps = steps - undostep;

		if (undoResponse) {
			uiHandler.sendMessage("Undo #" + undostep + " step(s) successfully!");
		} else {
			uiHandler.sendMessage("Undo was unsuccessful. Try again!");
		}

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
		redoResponse = dataBase.retrieveHistory(steps + redostep);
		steps = steps + redostep;
		uiHandler.refresh();
		return redoResponse;
	}

	public boolean setNewFile(String path) {
		return dataBase.setNewFile(path);
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
			uiHandler.sendMessage("You have added a task with same name before! Try another name!");
			return false;
		} else {	
			return true;	
		}
	}

	public MainApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
=======
    public Boolean undone(String title) {

        logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

        Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
        Boolean deleteResponse = dataBaseDelete(tempTask);

        tempTask.setDoneStatus(false);
        Boolean addResponse = dataBaseAdd(tempTask);

        uiHandler.refresh();
        uiHandler.highLight(tempTask);
        uiHandler.sendMessage(
                "[" + title + "] has been marked as ongoing! Go get it TIGER! [not what you want? try 'undo']", true);

        return deleteResponse && addResponse;
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
        Boolean deleteResponse = dataBaseDelete(tempTask);

        uiHandler.refresh();
        uiHandler.sendMessage("[" + title + "] has been deleted successfully! [not what you want? try 'undo']", true);

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
        uiHandler.sendMessage("Here are your search results for '" + title + "'! [to clear this search, type 'reset']",
                true);
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
        uiHandler.sendMessage(
                "Here are the related tasks under " + category.toUpperCase() + "! [to clear this filter, type 'reset']",
                true);
    }

    /**
     * This method sorts all tasks in according to the field name and order.
     *
     * 
     * @return void
     */
    public void sort(String fieldName, String order) {
        if (fieldName.isEmpty()) {
            uiHandler.sendMessage(
                    "Please specify a sorting order! Try sort ['start' | 'end' | 'category' | 'name'], followed by [ascending | descending].",
                    true);
        }
        dataBase.sort(fieldName, order);
        uiHandler.refresh();
        uiHandler.sendMessage(
                "Ta-da! Your tasks have been sorted by " + fieldName + "! [not what you want? try 'undo']", true);
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
     * @return Boolean
     */
    public Boolean label(String title, String category) {

        logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

        Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
        Boolean deleteResponse = dataBaseDelete(tempTask);

        tempTask.setCategory(new Category(category));
        Boolean addResponse = dataBaseAdd(tempTask);

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
        Boolean deleteResponse = dataBaseDelete(tempTask);

        tempTask.setRecurring(status);
        tempTask.setInterval(interval);
        Boolean addResponse = dataBaseAdd(tempTask);

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
     * @return Boolean
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
     * @return Boolean
     */
    public Boolean undo(int undostep) {

        if (steps <= 0) {
            uiHandler.sendMessage("Undo was unsuccessful. No actions to undo!", true);
        }

        Boolean undoResponse = dataBase.retrieveHistory(steps - undostep);
        steps = steps - undostep;

        if (undoResponse) {
            uiHandler.sendMessage("Undo #" + undostep + " step(s) successfully!", true);
        } else {
            uiHandler.sendMessage("Undo was unsuccessful. Try again!", true);
        }

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
        redoResponse = dataBase.retrieveHistory(steps + redostep);
        steps = steps + redostep;
        uiHandler.refresh();
        return redoResponse;
    }

    public boolean setNewFile(String path) {
        return dataBase.setNewFile(path);
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
            uiHandler.sendMessage("You have added a task with same name before! Try another name!", true);
            return false;
        } else {
            return true;
        }
    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
>>>>>>> Stashed changes
}