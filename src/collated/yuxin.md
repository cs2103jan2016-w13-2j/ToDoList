# yuxin
###### \todolist\common\tests\DataBaseTest.java
``` java
public class DataBaseTest {

	/**
	 * @throws java.lang.Exception
	 */

	private DataBase db;

	@Before
	public void setUp() throws Exception {
		db = new DataBase();
	}

	@Test
	/**
	 * test add an event to the database
	 * 
	 */
	public void testAdd1() {
		db.clear();
		// create a event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);

		// test whether can add an event to database
		boolean expected = true;
		assertEquals(db.add(newEvent), expected);

		// test whether it is really written into the file
		db.loadFromFile();
		boolean isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(isEqual, expected);
	}
    
	/**
	 * test delete an (existing) event from database
	 */
	@Test
	public void testDelete1() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// delete the task
		boolean expected = true;
		assertEquals(db.delete(newEvent), expected);

		// check whether it is really deleted from the file
		db.loadFromFile();
		assertEquals(db.taskList.isEmpty(), expected);
	}
	
	/**
	 * test delete an (not existing) event from database
	 */
	@Test
	public void testDelete2() {
		db.clear();
		//add an event to the database
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);
		
		//delete an event not in the database
		name = new Name("title2");
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		newEvent = new Task(name, start, end, null, null, false, false, null);
		
		boolean expected = false;
		assertEquals(db.delete(newEvent), expected);
	}
    /**
     * test check existence for an event (existing) in the database
     */
	@Test
	public void testCheckExistence1() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// check the existence
		boolean expected = true;
		assertEquals(db.checkExistence(newEvent), expected);
	}
	/**
     * test check existence for an event (not existing) in the database
     */
	@Test
	public void testCheckExistence2() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);
		
		//check the existence for another event
		name = new Name("title2");
        newEvent = new Task(name, start, end, null, null, false, false, null);
        boolean expected = false;
		assertEquals(db.checkExistence(newEvent), expected);
	}
    
	/**
	 * test retrieve by (existing) name
	 */
	@Test
	public void testRetrieve1() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the name of the (existing) task
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("name", "title"));
		boolean expected = true;
		//check the size of the resultant list
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
		// check the element is the task we add in
		isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);
	}
	
	/**
	 * test retrieve by (not existing) name
	 */
	@Test
	public void testRetrieve2() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the name of the ( not existing) task
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("name", "different-name"));
		boolean expected = false;
		//check the size of the resultant list
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
	}
	
	/**
	 * test retrieve by (existing) category
	 */
	@Test
	public void testRetrieve3() {
		db.clear();
		// add one event with category
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Category cat = new Category("cat1");
		Task newEvent = new Task(name, start, end, cat, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the specific (existing) category
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("category", "cat1"));
		boolean expected = true;
		//check the size of the resultant list
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
		//check the task in the resultant list 
		isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);
	}
	
	/**
	 * test retrieve by (not existing) category
	 */
	@Test
	public void testRetrieve4() {
		db.clear();
		// add one event with category
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Category cat = new Category("cat1");
		Task newEvent = new Task(name, start, end, cat, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the specific (existing) category
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("category", "not_existing_cat"));
		boolean expected = false;
		//check the size of the resultant list
		boolean isEqual = taskList.size() >= 1;
		assertEquals(expected, isEqual);
	}
    
	/**
	 * test retrieveAll function
	 */
	@Test
	public void testRetrieveAll() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// check the arraylist only has one element
		ArrayList<Task> taskList = db.retrieveAll();
		boolean expected = true;
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
		// check the element is the task we add in
		isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);

		// add one more event
		name = new Name("title2");
		Task newEvent2 = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent2);

		// check the size the the taskList
		taskList = db.retrieveAll();
		isEqual = taskList.size() == 2;
		assertEquals(expected, isEqual);
		// check the two tasks really are the two tasks
		isEqual = db.taskList.get(1).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);

		isEqual = db.taskList.get(0).getName().getName().equals(newEvent2.getName().getName());
		assertEquals(expected, isEqual);
	}
	
	/**
	 * test change directory method
	 */
	@Test 
	public void testChangeDir() {
		db.clear();
		//add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);
		//add another event
		name = new Name("another-event");
        newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);
		
		//change the directory
		assertTrue(db.setNewFile("/Users/Xyx/Desktop/jim"));
	}

}
```
###### \todolist\common\tests\LogicTest.java
``` java
public class LogicTest {

    private MainApp mainAppStub;
    private Logic logic;

    @Before
    public void setUp() throws Exception {
        mainAppStub = new MainAppStub();
        logic = new Logic(mainAppStub);
    }

    /**
     * test process method with add (event) command from user
     */
    @Test
    public void testProcess1() {
        logic.clean();
        boolean expected = true;

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
        // add in the command to add a new event
        logic.process("add event title 2017-01-01 14:00 1 day");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertEquals(isEqual, expected);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertEquals(isEqual, expected);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertEquals(isEqual, expected);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertEquals(isEqual, expected);
    }

    /**
     * test process method with add (deadline) command from user
     */
    @Test
    public void testProcess2() {
        logic.clean();
        boolean expected = true;

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime end = LocalDateTime.parse("1970-01-01" + " " + "12:00", formatter);

        // pass in the command to add a new deadline
        logic.process("add deadline title 1970-01-01 12:00");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertEquals(isEqual, expected);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertEquals(isEqual, expected);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertEquals(isEqual, expected);
    }

    /**
     * test process method with add (floating task) command from user
     */
    @Test
    public void testProcess3() {
        logic.clean();
        boolean expected = true;
        String name = "title";

        // pass in command to add a floating task
        logic.process("add task title");
        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertEquals(isEqual, expected);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertEquals(isEqual, expected);
    }

    /**
     * test add recurring event function
     */
    @Test
    public void testAddRecurringEvent() {
        logic.clean();
        boolean expected = true;

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
        // add in the command to add a new event
        logic.addRecurringEvent("7-day", "title", "2017-01-01", "14:00", "1", "day");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertEquals(isEqual, expected);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertEquals(isEqual, expected);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertEquals(isEqual, expected);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertEquals(isEqual, expected);
        // check whether it is set to be recurring
        isEqual = taskList.get(0).getRecurringStatus();
        assertEquals(taskList.get(0).getRecurringStatus(), expected);
        // check the interval of recurring
        isEqual = taskList.get(0).getInterval().equals("7-day");
    }

    /**
     * test add recurring deadline function
     */
    @Test
    public void testAddRecurringDeadline() {
        logic.clean();
        boolean expected = true;

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime end = LocalDateTime.parse("2016-04-01" + " " + "12:00", formatter);

        // pass in the command to add a new deadline
        logic.addRecurringDeadline("7-day", "title", "2016-03-25", "12:00");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertEquals(isEqual, expected);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertEquals(isEqual, expected);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertEquals(isEqual, expected);
        // check whether it is set to be recurring
        isEqual = taskList.get(0).getRecurringStatus();
        assertEquals(taskList.get(0).getRecurringStatus(), expected);
        // check the interval of recurring
        isEqual = taskList.get(0).getInterval().equals("7-day");
    }

    /**
     * test the archive function with existing task
     */
    @Test
    public void testDone1() {
        logic.clean();
        boolean expected = true;
        // add a new event
        Name name = new Name("title");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
        Task newEvent = new Task(name, start, end, null, null, false, false, null);
        logic.dataBase.add(newEvent);

        // archive this event
        assertEquals(logic.done("title"), expected);
        // check the status of the task
        newEvent = logic.dataBase.retrieve(new SearchCommand("name", "title")).get(0);
        Boolean isEqual = newEvent.getDoneStatus().equals(true);
        assertEquals(isEqual, expected);
    }

    /**
     * test the archive function with non-existing task
     */
    @Test
    public void testDone2() {
        logic.clean();
        boolean expected = true;
        // add a new event
        Name name = new Name("title");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
        Task newEvent = new Task(name, start, end, null, null, false, false, null);
        logic.dataBase.add(newEvent);

        // archive a non-existing event
        expected = false;
        assertEquals(logic.done("non-existing-task"), expected);
    }

    @Test
    public void testStepForward() {
        int original = logic.checkStep();
        logic.stepForward(1);
        assertEquals(logic.checkStep(), original + 1);
    }

    @Test
    public void testUndone() {
        logic.addTask("title");
        logic.done("title");
        logic.undone("title");
        Name name = new Name("title");
        Task newEvent = new Task(name, null, null, null, null, false, false, null);
        logic.addTask("title");
        Boolean isEqual = logic.dataBase.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
        assert (isEqual);
    }

    public void testEdit() {
        fail("Not yet implemented");
    }

    public void testDelete() {
        fail("Not yet implemented");
    }

    public void testSearch() {
        fail("Not yet implemented");
    }

    public void testLabel() {
        fail("Not yet implemented");
    }

    public void testSetRecurring() {
        fail("Not yet implemented");
    }

    public void testPostpone() {
        fail("Not yet implemented");
    }

    public void testForward() {
        fail("Not yet implemented");
    }

    public void testAddRemind() {
        fail("Not yet implemented");
    }

    public void testAddRemindBef() {
        fail("Not yet implemented");
    }

    public void testRemindBef() {
        fail("Not yet implemented");
    }

    public void testRemind() {
        fail("Not yet implemented");
    }

    public void testExit() {
        fail("Not yet implemented");
    }

    public void testUndo() {
        fail("Not yet implemented");
    }

    public void testRedo() {
        fail("Not yet implemented");
    }

}
```
###### \todolist\storage\DataBase.java
``` java
/*
 * This class is the storage class, handling the read and write of local file with relative commands.
 * It will be called by the logic.
 *
 * for the retrieve method, now can support 3 search command
 * 1:"category" + category
 * 2:"name" + keyword
 * 3:"view"+view (overdue, archived)
 *
 * for the sorting methods, can now sort by
 * 1:name
 * 2:end date
 * 3:start date
 * 4:category
 * but they are not integrated with the search method yet!!
 *
 *
 * @author yuxin
 */
public class DataBase {

	private static String MESSAGE_ADDING_TASK = "tring to add task: ";
	private static String MESSAGE_SUCCESSFULLY_ADD_TASK = "successfully add task: ";
	private static String MESSAGE_DELETING_TASK = "tring to delete task: ";
	private static String MESSAGE_SUCCESSFULLY_DELETE_TASK = "The task is deleted from database: ";
	private static String MESSAGE_RETRIEVE_TASK = "trying to retrieve: ";
	private static String MESSAGE_SETTING_NEW_PATH = "trying to set new path: ";
	private static String MESSAGE_SUCCESSFULLY_SET_PATH = "successfully set new path: ";
	private static String ERROR_REPEATED_TASK = "The task has already existed: ";
	private static String ERROR_TASK_NOT_EXIST = "The task to delete does not exist: ";

	protected static Component COMPONENT_STORAGE = UtilityLogger.Component.Storage;

	private FileHandler fh;
	public ArrayList<Task> taskList;
	//private ArrayList<ArrayList<Task>> snapshot;
	ArrayList<Task>[] snapshot;
	private TaskRetriever retriever;
	private TaskSorter sorter;
	private DatabaseModifier modifier;
	private UtilityLogger logger = null;
	private int counter;

	@SuppressWarnings("unchecked")
    public DataBase() {
		// taskList = null;
		fh = new FileHandler();
		retriever = new TaskRetriever();
		sorter = new TaskSorter();
		modifier = new DatabaseModifier();
		loadFromFile();
		//snapshot = new ArrayList<ArrayList<Task>>();
		snapshot = new ArrayList[1000];
		snapshot[0] = fh.read();
		counter = 0;
		logger = new UtilityLogger();
	}

	private void writeToFile() {
		fh.write(taskList);
	}

	public void clear() {
		taskList = new ArrayList<Task>();
		writeToFile();
	}

	public void loadFromFile() {
		taskList = fh.read();
	}

	/**
	 * This method handles the writing into the text file with the add command.
	 * It returns true if the task is successfully written into the file.
	 *
	 * @param Task
	 *            the task to be added
	 * @return TRUE whether the task is successfully added\
	 *
	 * @throws IOException
	 *             when task already exist
	 */
	public boolean add(Task task) {
		assert (task != null);
		logger.logAction(COMPONENT_STORAGE, MESSAGE_ADDING_TASK + task.getName().getName());
		try {
			taskList = modifier.addTask(taskList, task);
		} catch (IOException e) {
			logger.logError(COMPONENT_STORAGE, ERROR_REPEATED_TASK + task.getName().getName());
			return false;
		}
		@SuppressWarnings("unused")
        ArrayList<Task> temp = fh.read();
		//snapshot.add(temp);
		writeToFile();
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_ADD_TASK + task.getName().getName());
		return true;
	}

	/**
	 * This method handles the updating of text file when the specified task is
	 * to be deleted. Returns true if the task is successfully deleted.
	 *
	 * @param Task
	 *            the task to be deleted
	 * @return boolean true if the task is successfully deleted; false if the
	 *         task to delete does not exist
	 */
	public boolean delete(Task taskToDelete) {
		assert (taskToDelete != null);
		loadFromFile();

		logger.logAction(COMPONENT_STORAGE, MESSAGE_DELETING_TASK + taskToDelete.getName().getName());
		// dataBase_Logger.log(Level.INFO, LOGGING_DELETING_TASK +
		// taskToDelete.getName().getName());
		//ArrayList<Task> temptemp = taskList;
		try {
			taskList = modifier.deleteTask(taskList, taskToDelete);
		} catch (IOException e) {
			logger.logError(COMPONENT_STORAGE, ERROR_TASK_NOT_EXIST + taskToDelete.getName().getName());
			return false;
		}

		// dataBase_Logger.log(Level.INFO, LOGGING_TASK_DELETED +
		// taskToDelete.getName().getName());
		@SuppressWarnings("unused")
        ArrayList<Task> temp = fh.read();
		//ArrayList<Task> temp = temptemp;
		//snapshot.add(temp);
		writeToFile();
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_DELETE_TASK + taskToDelete.getName().getName());
		return true;
	}
 	
	public boolean takeSnapshot() {
	    counter++;
	    System.out.println("snapshot" + counter);
		snapshot[counter] = fh.read();
		for(Task each: snapshot[counter-1]) {
		    System.out.println(each.getName().getName());
		}
		//System.out.println("hello" + snapshot.length);
		return true;
	}

	/**
	 * This method returns whether a task is in the text file.
	 *
	 * @param Task
	 *            task to search
	 * @return boolean returns true if the task is found; false if not.
	 */
	public boolean checkExistence(Task taskToCheck) {
		return retriever.isTaskExisting(taskList, taskToCheck);
	}

	/**
	 * This method returns all the tasks in the database.
	 *
	 * @return an arraylist of all the tasks in database
	 */
	public ArrayList<Task> retrieveAll() {
		return taskList;
	}

	/**
	 * This method search for and then return tasks from the database according
	 * to the command.
	 * 
	 * @param command
	 *            command to search for the tasks in the database
	 * @return an arraylist of all the tasks in the search result; if the search
	 *         result is empty, returns an empty arraylist
	 */
	public ArrayList<Task> retrieve(SearchCommand command) {
		assert (command != null);
		logger.logAction(COMPONENT_STORAGE, MESSAGE_RETRIEVE_TASK + command.getType());
		return retriever.retrieveHandler(taskList, command);
	}

	/**
	 * search for tasks whose names containing the passed in string as a
	 * substring
	 * 
	 * @param command
	 * @return arraylist list of result tasks
	 */
	public ArrayList<Task> smartSearch(SearchCommand command) {
		assert (command != null);
		logger.logAction(COMPONENT_STORAGE, MESSAGE_RETRIEVE_TASK + command.getType());
		return retriever.smartRetrieve(taskList, command);
	}

	/**
	 * This method is to set new file for the storage of data.
	 *
	 * @param newFile
	 *            the string that contains the new path and file name
	 *
	 * @return true if the directory exists and the new file is set.
	 * @throw Exception if the path is invalid
	 */
	public boolean setNewFile(String newFilePath) {
		assert (newFilePath != null);
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SETTING_NEW_PATH + newFilePath);
		boolean isSet = false;
		isSet = fh.setFile(newFilePath);

		if (!isSet) {
			return false;
		}
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_SET_PATH + newFilePath);
		this.loadFromFile();
		return isSet;
	}
    
	/**
	 * load 
	 * @param newFilePath
	 * @return
	 */
	public boolean openNewFile(String newFilePath) {
		assert (newFilePath != null);
		boolean isOpen = false;
		isOpen = fh.openFile(newFilePath);
		if (!isOpen) {
			return false;
		}
		this.loadFromFile();
		return isOpen;
	}

	/**
	 * This method returns the path of the local text file that stores all the
	 * tasks.
	 *
	 * @return a string that represents the path
	 */
	public String getPath() {
		return fh.getPath();
	}

	/**
	 * Goes back to a number of steps ago according to the number of steps pass
	 * in.
	 *
	 * @param steps
	 *            the number of steps to go back
	 */
	public Boolean retrieveHistory(int steps) {
		taskList = snapshot[steps];
		System.out.println("database" + counter);
		this.counter = steps;
		//System.out.println("goodbye" + steps);
		writeToFile();
		System.out.println("size:::::::" + taskList.size() + "step:::" + steps);
		return true;
	}

	/**
	 * Sort the tasks in the text file in the specific order of the specific
	 * field.
	 *
	 * @param fieldName
	 *            the name of the field to sort on
	 * @param order
	 *            the order of the task list after sorting, either in ascending
	 *            order or descending order
	 */
	public void sort(String fieldName, String order) {
		taskList = sorter.sortHandler(taskList, fieldName, order);
		//ArrayList<Task> temp = fh.read();
		//snapshot.add(temp);
	}
}
```
###### \todolist\storage\FileHandler.java
``` java
/*
 * This class is to read and write from the file directly. It will be called by the database.
 * 
 * 
 * @author Xyx
 *
 */
public class FileHandler {
	private static String PATH_UPDATEDDIRECTORY = "updatedDirectory.txt";
	private String fileName = "/taskStorage.txt";
	private String filePath = "taskStorage.txt";

	private Gson gson = new Gson();

	public FileHandler() {
		checkForUpdatedDirectory();
	}

	/**
	 * This method reads from the local file. It returns a ArrayList containing
	 * all the Strings in the file.
	 * 
	 * @return taskList the list of tasks stored in file if no such file or the
	 *         file is empty, return an empty arraylist.
	 */
	public ArrayList<Task> read() {
		ArrayList<Task> taskList = new ArrayList<Task>();
		File path = new File(filePath);
		System.out.println(filePath);
		if (isFileReady(path) && !isFileEmpty(path)) {
			try {
				// System.out.println("kjkkkkkk" + filePath);
				FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr);
				String input = null;
				input = br.readLine();

				while (input != null && !input.equals("null")) {
					taskList.add(gson.fromJson(input, Task.class));
					input = br.readLine();
				}
				br.close();
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return taskList;
	}

	/**
	 * This method write directly the list of tasks to the local file.
	 * 
	 * @param taskList
	 *            the list of tasks to be written
	 * @return true if it is successfully written; false if the target file
	 *         cannot be found
	 */
	public boolean write(ArrayList<Task> taskList) {
		try {
			FileWriter fw = new FileWriter(filePath);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Task eachTask : taskList) {
				System.out.println("filehandler writing into file: " + gson.toJson(eachTask));
				bw.write(gson.toJson(eachTask) + "\n");
			}
			bw.close();
			System.out.println("filehandler writing into file: successfully ");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * The method is to set a new direction to store the tasks in the defined
	 * path.
	 * 
	 * @param newFilePath
	 *            the path of the new file (the path + the file name); it is not
	 *            null.
	 * @return true if the file is set; false if the path is not a correct path
	 */
	public boolean setFile(String newFilePath) {
		//check whether the directory is valid
		if(!isPathCorrect(newFilePath)) {
			return false;
		}
		
		newFilePath = newFilePath + fileName;
		Path from = Paths.get(filePath);
		Path to = Paths.get(newFilePath);
		CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES };

		try {
			Files.copy(from, to, options);
			
		} catch (IOException e1) {
			return false;
		}

		filePath = newFilePath;
        
		//write the new file path into local txt file
		try {
			FileWriter fw = new FileWriter(PATH_UPDATEDDIRECTORY);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(filePath + "\n");// store the new path in the local file
			bw.close();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public boolean openFile(String newFilePath) {
		//check whether the directory exist		
		String tempFilePath = newFilePath + fileName;
		if(!isPathCorrect(tempFilePath)) {
			return false;
		}
		//check whether the txt file contains the correct format (gson format)
		if(read() == null) {			
			return false;
		}
		
		filePath = tempFilePath;
		return true;
	}

	public String getPath() {
		return filePath;
	}

	// helper methods
	private void checkForUpdatedDirectory() {
		File updatedDirectory = new File(PATH_UPDATEDDIRECTORY);

		try {
			if (!isFileEmpty(updatedDirectory)) {
				FileReader fr = new FileReader(updatedDirectory);
				BufferedReader br = new BufferedReader(fr);
				filePath = br.readLine();
				br.close();
			}
		} catch (Exception e) {
			return;
		}
	}

	private boolean isFileReady(File filePath) {
		return filePath.exists();
	}

	private boolean isFileEmpty(File filePath) {
		if (isFileReady(filePath)) {
			return filePath.length() == 0;
		}
		return true;
	}

    private boolean isPathCorrect(String pathName) {
		if (pathName.length() == 0) {

			return true;
		}
		File pathToCheck = new File(pathName);
		if (pathToCheck.isDirectory() || pathToCheck.isFile()) {
			return true;
		}
		return false;
	}




}
```
###### \todolist\storage\TaskRetriever.java
``` java
public class TaskRetriever {

    private static enum FilterType {
        VIEW, CATEGORY, NAME, END_DATE, START_DATE;
    }

    private static enum ViewType {
        ARCHIVE, OVERDUE, TODAY;
    }

    private ArrayList<Task> taskList;

    public TaskRetriever() {
        taskList = new ArrayList<Task>();
    }

    public ArrayList<Task> retrieveHandler(ArrayList<Task> tasks, SearchCommand command) {
        // System.out.println("is retrieving");
        assert (command instanceof SearchCommand);
        // System.out.println("not even here");
        taskList = tasks;

        ArrayList<Task> resultList = new ArrayList<Task>();
        FilterType type = getFilterType(command);
        // dataBase_Logger.log(Level.INFO, LOGGING_RETRIEVE_TASK + type);
        switch (type) {
        case CATEGORY:
            // System.out.println("is retrieving 2222");
            resultList = retrieve_Category(command);
            break;
        case NAME:
            System.out.println("name");
            resultList = retrieve_Name(command);
            break;
        case VIEW:
            resultList = retrieve_View(command);
            break;
        default:
            // System.out.println("is retrieving default");
            return resultList;
        }

        // System.out.println(Arrays.toString(resultList.toArray()));

        return resultList;
    }

    private FilterType getFilterType(SearchCommand command) {
        String type = command.getType();
        if (isCategory(type)) {
            return FilterType.CATEGORY;
        }
        if (isView(type)) {
            return FilterType.VIEW;
        }
        if (isName(type)) {
            return FilterType.NAME;
        }
        return null;
    }

    private boolean isName(String type) {
        return type.equalsIgnoreCase("name");
    }

    private boolean isView(String type) {
        return type.equalsIgnoreCase("view");
    }

    private boolean isCategory(String type) {
        return type.equalsIgnoreCase("category");
    }

    private ArrayList<Task> retrieve_View(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        ViewType viewToFilter = determineViewType(command.getContent());
        switch (viewToFilter) {
        case OVERDUE:
            resultList = retrieve_ViewOverDue();
            break;
        case ARCHIVE:
            resultList = retrieve_ViewArchive();
            break;
        default:
            return resultList;
        }
        return resultList;
    }

    // helper method for retrieve_View
    private ArrayList<Task> retrieve_ViewArchive() {
        ArrayList<Task> resultList = new ArrayList<Task>();
        for (Task eachTask : taskList) {
            if (eachTask.getDoneStatus()) {
                resultList.add(eachTask);
            }
        }
        return resultList;
    }

    // helper method for retrieve_View
    private ArrayList<Task> retrieve_ViewOverDue() {
        ArrayList<Task> resultList = new ArrayList<Task>();
        for (Task eachTask : taskList) {
            if (isTaskOverdue(eachTask.getEndTime())) {
                resultList.add(eachTask);
            }
        }
        return null;
    }

    private boolean isTaskOverdue(LocalDateTime endTime) {
        if (endTime == null) {
            return false;
        }
        return endTime.isBefore(LocalDateTime.now());
    }

    private ViewType determineViewType(String content) {
        if (isOverdue(content)) {
            return ViewType.OVERDUE;
        }
        if (isArchive(content)) {
            return ViewType.ARCHIVE;
        }
        return null;
    }

    private boolean isArchive(String content) {
        return content.equalsIgnoreCase("archive");
    }

    private boolean isOverdue(String content) {
        return content.equalsIgnoreCase("overdue");
    }

    private ArrayList<Task> retrieve_Name(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        String requiredName = command.getContent();
        for (Task eachTask : taskList) {
            if (eachTask.getName().getName().equalsIgnoreCase(requiredName)) {
                resultList.add(eachTask);
            }
        }
        System.out.println(Arrays.toString(resultList.toArray()));
        return resultList;
    }
    
    protected ArrayList<Task> smartRetrieve(ArrayList<Task> taskList, SearchCommand command) {
    	ArrayList<Task> resultList = new ArrayList<Task>();
        String requiredName = command.getContent().trim();
        
        for (Task eachTask : taskList) {
        	boolean isMatching = false;
        	String eachName = eachTask.getName().getName();
        	String temp[] = eachName.split(" ");
        	if(temp.length == 1) {
        		isMatching = retrieveByInitial(requiredName, eachName);
        	}else {
        		isMatching = retrieveByTokenizedString(requiredName, temp);
        	}
        	
            if (isMatching) {
                resultList.add(eachTask);
            }
        }
        
        System.out.println(Arrays.toString(resultList.toArray()));
        return resultList;
    }

    private boolean retrieveByTokenizedString(String requiredName, String[] temp) {
		boolean isMatching = false;
		for(int i = 0; i< temp.length; i++) {
			if(temp[i].equalsIgnoreCase(requiredName)) {
				isMatching = true;
			}
		}
		return isMatching;
	}

	private boolean retrieveByInitial(String requiredName, String eachName) {
		boolean isMatching = false;
		int length = requiredName.length();
		if(length > eachName.length()) {
			isMatching = false;
		} else {
			String initialPart = eachName.substring(0, length);
			isMatching = initialPart.equalsIgnoreCase(requiredName);
		}
		return isMatching;
	}

	private boolean isSame(String str1, String str2) {
        return str1.equalsIgnoreCase(str2);
    }


    private ArrayList<Task> retrieve_Category(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        String requiredCategory = command.getContent();

        System.out.println(requiredCategory);

        for (Task eachTask : taskList) {
            if (eachTask.getCategory() != null && isSame(eachTask.getCategory().getCategory(), requiredCategory)) {
                System.out.println(eachTask.getName().getName());
                resultList.add(eachTask);
            }
        }
        return resultList;
    }

    /**
     * check whether the required task exist in the local storage file
     * 
     */
    public boolean isTaskExisting(ArrayList<Task> taskList, Task taskToCheck) {
        return taskList.contains(taskToCheck);
    }

}
```
###### \todolist\storage\TaskSorter.java
``` java

public class TaskSorter {
	
	private ArrayList<Task> taskList;
	
    public TaskSorter() {
    	taskList = new ArrayList<Task>();
    }
    
    
	public ArrayList<Task> sortHandler(ArrayList<Task> tasks, String fieldName, String order) {
		
        taskList = tasks;
        
		switch (fieldName) {
		case "start":
			sort_StartDate(taskList);
			break;
		case "end":
			sort_EndDate(taskList);
			break;
		case "category":
			sort_Category(taskList);
			break;
		case "name":
			sort_Name(taskList);
			break;
		default:
			
		}
		
		if(order.equals("descending")) {
			Collections.reverse(taskList);
		}
		
		return taskList;
	}
    // sorting
    // 1.startDate
    private ArrayList<Task> sort_StartDate(ArrayList<Task> currentList) {
        Collections.sort(currentList, new StartDateComparator());
        return currentList;
    }

    private class StartDateComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            LocalDateTime firstDate = ((todolist.model.Task) t1).getStartTime();
            LocalDateTime secondDate = ((todolist.model.Task) t2).getStartTime();
            if (firstDate == null && secondDate == null) {
                String t1_Name = ((todolist.model.Task) t1).getName().getName();
                String t2_Name = ((todolist.model.Task) t2).getName().getName();
                return t1_Name.compareToIgnoreCase(t2_Name);
            } else if (firstDate == null) {
                return -1;
            } else if (secondDate == null) {
                return 1;
            } else {
                return firstDate.compareTo(secondDate);
            }
        }
    }

    // 2.endDate
    private ArrayList<Task> sort_EndDate(ArrayList<Task> currentList) {
        Collections.sort(currentList, new EndDateComparator());
        return currentList;
    }

    private class EndDateComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            LocalDateTime firstDate = ((todolist.model.Task) t1).getEndTime();
            LocalDateTime secondDate = ((todolist.model.Task) t2).getEndTime();
            if (firstDate == null && secondDate == null) {
                String t1_Name = ((todolist.model.Task) t1).getName().getName();
                String t2_Name = ((todolist.model.Task) t2).getName().getName();
                return t1_Name.compareToIgnoreCase(t2_Name);
            } else if (firstDate == null) {
                return -1;
            } else if (secondDate == null) {
                return 1;
            } else {
                return firstDate.compareTo(secondDate);
            }
        }
    }

    // 3.category
    private ArrayList<Task> sort_Category(ArrayList<Task> currentList) {
        Collections.sort(currentList, new CategoryComparator());
        return currentList;
    }

    private class CategoryComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            String firstCategory = ((todolist.model.Task) t1).getCategory().getCategory();
            String secondCategory = ((todolist.model.Task) t2).getCategory().getCategory();
            return firstCategory.compareToIgnoreCase(secondCategory);
        }
    }

    // 4.name
    private ArrayList<Task> sort_Name(ArrayList<Task> currentList) {
        Collections.sort(currentList, new NameComparator());
        return currentList;
    }

    private class NameComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            String firstName = ((todolist.model.Task) t1).getName().getName();
            String secondName = ((todolist.model.Task) t2).getName().getName();
            return firstName.compareToIgnoreCase(secondName);
        }
    }
}
```
