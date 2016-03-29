# zhangjiyi
###### src/todolist/logic/Sorter.java
``` java
public class Sorter {
    
}
```
###### src/todolist/logic/CaseSwitcher.java
``` java
public class CaseSwitcher {

    private Logic logic;

    public CaseSwitcher(Logic logic) {
        this.logic = logic;
    }

    public void execute(TokenizedCommand command) {
        String action = command.getAction();
        String arg[] = command.getArgs();

        switch (action) {

        case "add":
            String type = "null";
            if (arg.length == 0) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To create a task, you will first need to specify the type of task that you wish to create.");
            } else {
                type = arg[0];
            }

            switch (type) {
            case "event":
                if (arg.length != 6 && arg.length != 5) {
                    logic.getUIHandler().sendMessage(
                            "Your command was incomplete! To add an event, try: add event [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day].");
                } else {
                	if(arg.length == 6) {
                		logic.addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
                        logic.stepForward(1);
                	} else {
                		logic.addEventLess(arg[1], arg[2], arg[3], arg[4]);
                        logic.stepForward(1);
                	}
                }
                break;
            case "deadline":
                if (arg.length != 4 && arg.length != 3) {
                    logic.getUIHandler().sendMessage(
                            "Your command was incomplete! To add a deadline, try: add deadline [title] [YYYY-MM-DD] [HH:MM]");
                } else {
                	if(arg.length == 4) {
                        logic.addDeadline(arg[1], arg[2], arg[3]);
                        logic.stepForward(1);
                	} else {
                        logic.addDeadlineLess(arg[1], arg[2]);
                        logic.stepForward(1);
                	}
                }
                break;
            case "task":
                if (arg.length != 2) {
                    logic.getUIHandler()
                            .sendMessage("Your command was incomplete! To add an un-dated task: add task [title]");
                } else {
                    logic.addTask(arg[1]);
                    logic.stepForward(1);
                }
                break;
            case "recurring":
                switch (arg[1]) {
                case "event":
                    if (arg.length != 8 && arg.length != 7 ) {
                        logic.getUIHandler().sendMessage(
                                "Your command was incomplete! To add a recurring event, try: add recurring event [7-day] [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day]");
                    } else {
                    	if(arg.length == 8) {
                    		logic.addRecurringEvent(arg[2], arg[3], arg[4], arg[5], arg[6], arg[7]);
                            logic.stepForward(3);
                    	} else {
                    		logic.addRecurringEventLess(arg[2], arg[3], arg[4], arg[5], arg[6]);
                            logic.stepForward(3);
                    	}
                    }
                    break;
                case "deadline":
                    if (arg.length != 6 && arg.length != 5) {
                        logic.getUIHandler().sendMessage(
                                "Your command was incomplete! To add a recurring deadline, try: add recurring deadline [7-day] [title] [YYYY-MM-DD] [HH:MM]");
                    } else {
                    	if(arg.length == 6) {
                            logic.addRecurringDeadline(arg[2], arg[3], arg[4], arg[5]);
                            logic.stepForward(3);
                    	} else {
                            logic.addRecurringDeadlineLess(arg[2], arg[3], arg[4]);
                            logic.stepForward(3);
                    	}
                    }
                    break;
                default:
                    logic.getUIHandler().sendMessage("Opps! I don't understand this command! Please try again.");

                }
                break;
            default:
                logic.getUIHandler().sendMessage("Opps! I don't understand this command! Please try again.");
            }
            break;
        case "edit":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To edit a task, try: edit [title] [field-name] [new-value]");
            } else {
                logic.edit(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "delete":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage("Your command was incomplete! To delete a task, try: delete [title]");
            } else {
                logic.delete(arg[0]);
                logic.stepForward(1);
            }
            break;
        case "search":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To search for something, try: search [search-term] (You are searching task names!)");
            } else {
                logic.search(arg[0]);
            }
            break;
        case "filter":
            if (arg.length != 1) {
                logic.getUIHandler()
                        .sendMessage("Your command was incomplete! To apply a filter, try: filter [category-name]");
            } else {
                logic.filter(arg[0]);
            }
            break;
        case "sort":
            if (arg.length != 2) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To sort, try: sort ['start' | 'end' | 'category' | 'name'] [ascending | descending]");
            } else {
                logic.sort(arg[0], arg[1]);
                logic.stepForward(1);
            }
            break;
        case "insert":
            // logic.insert(arg[0], arg[1], arg[2]);
            break;
        case "switchPosition":
            // logic.switchPosition(arg[0], arg[1]);
            break;
        case "label":
            if (arg.length != 2) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To categorise or label a task, try: label [title] [category-name]");
            } else {
                logic.label(arg[0], arg[1]);
                logic.stepForward(2);
            }
            break;
        case "set-recurring":
            if (arg.length != 2) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To set a task to repeat, try: set-recurring [title] [interval]");
            } else {
                logic.setRecurring(arg[0], true, arg[1]);
                logic.stepForward(2);
            }
            break;
        case "remove-recurring":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To stop a task from repeating, try: remove-recurring [title]");
            } else {
                logic.setRecurring(arg[0], false, null);
                logic.stepForward(2);
            }
            break;
        case "postpone":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To postpone a task, try: postpone [title] [number] [hour | day]");
            } else {
                logic.postpone(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "forward":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To forward a task, try: forward [title] [number] [hour | day]");
            } else {
                logic.forward(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "add-remind":
            try {
                // need to handle exceptions here
                logic.addRemind(arg);
                logic.stepForward(3);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            break;
        case "remind":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To get ToDoList to remind you on a task, try: remind [title]");
            } else {
                logic.remind(arg[0]);
                logic.stepForward(2);
            }
            break;
        case "add-remind-bef":
            // need to handle exceptions here
            String[] restOfArgs = new String[arg.length - 2];
            for (int i = 0; i < arg.length; i++) {
                restOfArgs[i] = arg[i + 2];
            }
            try {
                logic.addRemindBef(arg[0], arg[1], restOfArgs);
                logic.stepForward(3);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case "remind-bef":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To get ToDoList to remind you on a task sometime before it is due, try: remind-bef [title] [number] [hour | day]");
            } else {
                logic.remindBef(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "done":
            if (arg.length != 1) {
                logic.getUIHandler()
                        .sendMessage("Your command was incomplete! To archive a completed task, try: done [title]");
            } else {
                logic.done(arg[0]);
                logic.stepForward(2);
            }
            break;
        case "undone":
            if (arg.length != 1) {
                logic.getUIHandler()
                        .sendMessage("Your command was incomplete! To un-archive an ongoing task, try: undone [title]");
            } else {
                logic.undone(arg[0]);
                logic.stepForward(2);
            }
            break;
        case "exit":
            logic.exit();
            break;
        case "undo":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To undo a few action(s), try: undo [number-of-actions]");
            } else {
                logic.undo(Integer.parseInt(arg[0]));
            }
            break;
        case "redo":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To redo a few action(s), try: redo [number-of-actions]");
            } else {
                logic.redo(Integer.parseInt(arg[0]));
            }
            break;
        case "reset":
            logic.reset();
            break;
        case "save":
        	logic.setNewFile(arg[0]);
        	break;
        case "tab":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To navigate to a certain page, try: tab [page-name] (as reflected on the tab bar)");
            } else {
                logic.tab(arg[0]);
            }
            break;
        default:
            logic.getUIHandler().sendMessage("Opps! I don't understand this command! Please try again.");
        }
    }
}
```
###### src/todolist/logic/MainAppStub.java
``` java
public class MainAppStub extends MainApp{
	public MainAppStub() {
		super();
	}
	
	@Override
	public void setDisplayTasks(ArrayList<Task> listOfTasks) {
		
	}
	
	@Override
	public void notifyWithText(String message) {
		
	}
}
```
###### src/todolist/logic/UIHandler.java
``` java
public class UIHandler {

    private DataBase dataBase;
    private MainApp mainApp;
    private Logic logic;

    public UIHandler(DataBase dataBase, MainApp mainApp, Logic logic) {
        this.dataBase = dataBase;
        this.mainApp = mainApp;
        this.logic = logic;
    }

    public void process(String input) {
        logic.process(input);
    }

    public void refresh() {
        mainApp.setDisplayTasks(dataBase.retrieveAll());
    }

    public void sendMessage(String message) {
        mainApp.notifyWithText(message);
    }

    public void highLight(Task task) {
        mainApp.highLight(task);
    }

    public void display(ArrayList<Task> taskList) {
        mainApp.setDisplayTasks(taskList);
    }

    public void tab(int index) {
        mainApp.loadPage(index);
    }
}
```
###### src/todolist/logic/Logic.java
``` java
public class Logic {

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

	/**
	 * This method takes in the title of a task and marks it as done.
	 *
	 * 
	 * @return Boolean
	 */
	public Boolean done(String title) {

		logger.log(Level.INFO, LOGGING_EDITING_TASK + title);

		Task tempTask = dataBase.retrieve(new SearchCommand("NAME", title)).get(0);
		Boolean deleteResponse = dataBaseDelete(tempTask);

		tempTask.setDoneStatus(true);
		Boolean addResponse = dataBaseAdd(tempTask);

		uiHandler.refresh();
		uiHandler.highLight(tempTask);
		uiHandler.sendMessage("[" + title
				+ "] has been marked as completed! Woohoo another one down! [not what you want? try 'undo']");

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
}
```
###### src/todolist/parser/FlexiCommandParser.java
``` java
public class FlexiCommandParser {

    public FlexiCommandParser() {

    }

    public TokenizedCommand parse(String input) {

        Parser parser = new Parser(TimeZone.getDefault());
        List<DateGroup> groups = parser.parse(input);
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
//            int line = group.getLine();
//            int column = group.getPosition();
//            String matchingValue = group.getText();
//            String syntaxTree = group.getSyntaxTree().toStringTree();
//            Map parseMap = group.getParseLocations();
//            boolean isRecurreing = group.isRecurring();
//            Date recursUntil = group.getRecursUntil();
            System.out.println(dates);

        }

        String action = null;
        String args[] = null;
        return new TokenizedCommand(action, args);
    }
}
```
###### src/todolist/parser/MainParser.java
``` java
public class MainParser {

    private FlexiCommandParser flexiCommandParser;
    private NormalCommandParser normalCommandParser;

    public MainParser() {
        this.setFlexiCommandParser(new FlexiCommandParser());
        this.normalCommandParser = new NormalCommandParser();
    }

    /**
     * This method takes in a string and parse it.
     *
     * 
     * @return TokenizedCommand
     */
    public TokenizedCommand parse(String input) {
        if (checkType(input)) {
            return normalCommandParser.parse(input);
        } else {
            return normalCommandParser.parse(input);
            // return flexiCommandParser.parse(input);
        }
    }

    /**
     * This method takes in a string and check whether it is a flexi command.
     *
     * 
     * @return Boolean
     */
    private Boolean checkType(String input) {
        String temp[] = input.split(" ");
        String head = temp[0];
        Boolean type = head.equals("add") || head.equals("edit") || head.equals("delete") || head.equals("search")
                || head.equals("filter") || head.equals("sort") || head.equals("insert")
                || head.equals("switchposition") || head.equals("label") || head.equals("postpone")
                || head.equals("forward") || head.equals("add-remind") || head.equals("remind")
                || head.equals("add-remind-bef") || head.equals("remind-bef") || head.equals("done")
                || head.equals("undone") || head.equals("exit") || head.equals("undo") || head.equals("redo")
                || head.equals("reset") || head.equals("tab") || head.equals("set-recurring")
                || head.equals("remove-recurring");
        return type;
    }

    public FlexiCommandParser getFlexiCommandParser() {
        return flexiCommandParser;
    }

    public void setFlexiCommandParser(FlexiCommandParser flexiCommandParser) {
        this.flexiCommandParser = flexiCommandParser;
    }
}
```
###### src/todolist/parser/NormalCommandParser.java
``` java
public class NormalCommandParser {

	public NormalCommandParser() {

	}

	public TokenizedCommand parse(String input) {

		String temp[] = input.split(" ");
		String action = temp[0];
		
		ArrayList<String> myList = new ArrayList<String>();

		String name = null;
		Boolean generateName = false;
		for (int i = 0; i < temp.length - 1; i++) {
	
			
			if (generateName == true) {
;
				if (temp[i+1].contains("\"")) {
					name = name + " " + temp[i+1].replace("\"", "");
					generateName = false;
					myList.add(name);
					//args[counter] = name;
					//counter++;
					name = null;

				} else {
					name = name + " " + temp[i+1];
				}
			} else {
				if (temp[i+1].contains("\"")) {
					name = temp[i+1].replace("\"", "");
					generateName = true;
					int count = temp[i+1].length() - temp[i+1].replace("\"", "").length();
					if(count == 2) {
						generateName = false;
						myList.add(name);
						//args[counter] = name;
						//counter++;
						name = null;
					}
				} else {
					myList.add(temp[i+1]);
					//args[counter] = temp[i+1];
					//counter++;
				}
			}
		}
		
		String [] args = myList.toArray(new String[0]); 

		/*
		 * String temp0[] = input.split("\" "); String temp1[] = temp0[1].split(
		 * " \"");
		 * 
		 * String first[] = temp0[0].split(" "); String third[] =
		 * temp1[1].split(" ");
		 * 
		 * String second = temp1[0];
		 * 
		 * int length = first.length + third.length + 1;
		 * 
		 * 
		 * String temp[] = new String[length];
		 * 
		 * for(int i=0; i< length;i++) { if(i < first.length) { temp[i] =
		 * first[i]; } else { if(i > first.length) { temp[i] = third[i -
		 * first.length - 1]; } else { temp[i] = second; } } }
		 * 
		 * String action = temp[0]; String args[] = new String[temp.length - 1];
		 * for(int i=0; i<temp.length-1; i++) { args[i] = temp[i + 1]; }
		 */

		return new TokenizedCommand(action, args);
	}
}
```
###### src/todolist/ui/controllers/MainViewController.java
``` java
	public String path = "demo.txt";

```
###### src/todolist/ui/controllers/MainViewController.java
``` java
	public int demoCounter = 0;

```
###### src/todolist/ui/controllers/MainViewController.java
``` java
	public ArrayList<String> demoFileHandler(String path) {
		ArrayList<String> myList = new ArrayList<String>();
		try {

			File file = new File(path);
			Scanner scr = new Scanner(file);
			while (scr.hasNextLine()) {
				String temp = scr.nextLine();
				myList.add(temp);
				System.out.println(temp);
			}
			scr.close();
		} catch (Exception e) {

		}
		return myList;
	}

```
###### src/todolist/ui/controllers/MainViewController.java
``` java
	void append(TextField field, String newText) {
		field.setText(field.getText() + newText);
	}

```
###### src/todolist/ui/controllers/MainViewController.java
``` java
	public void setCommandLineCallbackDemo(TextField commandField) {
		// Set Callback for TextField
		EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ArrayList<String> demoList = demoFileHandler(path);
				String commandString = demoList.get(demoCounter);
				demoCounter++;
				// Command command = new Command(commandString);
				// System.out.println(command.getCommand());

				// Pass Command Line input for processing
				try {
					commandField.clear();
					commandField.setText(commandString);
					mainApplication.uiHandlerUnit.process(commandString);
				} catch (Exception e) {

				}
			}
		};

		commandField.setOnAction(commandHandler);
	}

	/*** VIEW GETTERS-SETTERS-LOADERS ***/

	public ListView<TaskWrapper> getTaskListView() {
		return listView;
	}

	/*** MODEL GETTERS-SETTERS-RELOADERS ***/

	public ObservableList<TaskWrapper> getTasks() {
		return tasksToDisplay;
	}

	public void setTasks(ArrayList<Task> tasks) {

		// List provided by logic must be valid
		assert (tasks != null);

		ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
		listView.getItems().clear();

		// Convert Task to TaskWrapper for display handling
		for (int i = 0; i < tasks.size(); ++i) {
			if (!tasks.get(i).getDoneStatus()) {
				TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
				arrayOfWrappers.add(wrappedTask);
			}
		}

		listView.getItems().addAll(arrayOfWrappers);
	}

	public void highLight(Task task) {
		// TODO Auto-generated method stub
		int index = searchInList(task);

		if (index != -1) {
			listView.getSelectionModel().select(index);
			listView.getFocusModel().focus(index);
			listView.scrollTo(index);
		}
	}

	private int searchInList(Task task) {

		for (int i = 0; i < listView.getItems().size(); ++i) {
			if (listView.getItems().get(i).getTaskObject().equals(task)) {
				return i;
			}
		}
		return -1;
	}

}
```
###### src/todolist/model/Reminder.java
``` java
public class Reminder {
    
    private Boolean switcher = null;
    private LocalDateTime time = null;
    
    public Reminder(Boolean switcher, LocalDateTime time) {
        this.switcher = switcher;
        this.time = time;
    }
        
    public Boolean getStatus() {
        return switcher;
    }
    
    public LocalDateTime getTime() {
        return time;
    }
}
```
###### src/todolist/model/Task.java
``` java
public class Task {
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
		if (isRecurring && startTime != null) {
			String temp[] = interval.split("-");
			String length = temp[0];
			String unit = temp[1];
			LocalDateTime now = LocalDateTime.now();
			while (now.isAfter(startTime)) {
				startTime = startTime.plus(Long.parseLong(length), generateTimeUnit(unit));
			}
			return startTime;
		} else {
			return startTime;
		}
	}

	public LocalDateTime getEndTime() {
		if (isRecurring) {
			String temp[] = interval.split("-");
			String length = temp[0];
			String unit = temp[1];

			if (startTime != null) {
				while (startTime.isAfter(endTime)) {
					endTime = endTime.plus(Long.parseLong(length), generateTimeUnit(unit));
				}
				return endTime;
			} else {
				LocalDateTime now = LocalDateTime.now();
				while (now.isAfter(endTime)) {
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
}
```
###### src/todolist/model/SearchCommand.java
``` java
public class SearchCommand {
	private String type = null;
	private String content = null;
	
	public SearchCommand(String type, String content) {
		this.type = type;
		this.content = content;
	}

    public String getType() {
    	return type;
    }
    
    public String getContent() {
    	return content;
    }

}
```
###### src/todolist/model/TokenizedCommand.java
``` java
public class TokenizedCommand {
	
  private String action = null;
  private String args[];

  public TokenizedCommand(String action, String[] args) {
	  this.action = action;
	  this.args = args;
  }
    
  public String getAction() {
      return action;
  }
  
  public String[] getArgs() {
      return args;
  }
}
```
###### src/todolist/model/Category.java
``` java
public class Category implements Comparable<Category>{
    private String category;
	public Category(String category) {
    	this.category = category;
    }
	@Override
	public int compareTo(Category o) {
		return this.category.compareToIgnoreCase(o.getCategory());
	}
	
	public String getCategory() {
		return category;
	}
}
```
###### src/todolist/model/Name.java
``` java
public class Name implements Comparable<Name>{

	private String name;
	
	public Name(String name) {
	    this.name = name;
	}
	
	@Override
	public int compareTo(Name o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getName() {
	    return name;
	}
}
```
