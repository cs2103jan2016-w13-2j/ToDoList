//@@author A0131334W
package todolist.common.tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import todolist.model.Category;
import todolist.model.Name;
import todolist.model.SearchCommand;
import todolist.model.Task;
import todolist.storage.DataBase;

public class DataBaseTest {

	/**
	 * @throws java.lang.Exception
	 */

	private DataBase db;
	private ArrayList<String> name;
	private ArrayList<String> date;
    private ArrayList<Task> eventList;

	@Before
	public void setUp() throws Exception {
		
		db = new DataBase();
		name = new ArrayList<String>();
		date = new ArrayList<String>();
		eventList = new ArrayList<Task>();
		
		initialiseName();
		initialiseDate();
				
	}
	   
	private void initialiseDate() {
		date.add(0, "2030-11-01 14:00");
		date.add(1, "2030-01-01 15:00");
		date.add(2, "2030-01-01 10:00");
		date.add(3, "2030-01-01 10:00");
		date.add(4, "2009-01-01 10:00");
		date.add(5, "2016-01-01 10:00");
		date.add(6, "2012-03-01 10:00");
		date.add(7, "2020-01-22 10:00");
		date.add(8, "2030-01-01 10:00");
		date.add(9, "2011-01-01 10:00");
	}

	private void initialiseName() {
		name.add(0, "hello jenny");
		name.add(1, "hello world");
		name.add(2, "some random title");
		name.add(3, "finish proposal");
		name.add(4, "do laundry");
		name.add(5, "buy lunch for girfriend");
		name.add(6, "buy jay chou cd");
		name.add(7, "do homework");
		name.add(8, "have date with girlfriend");
		name.add(9, "rewrite taskstorage");
	}

	private void initialiseEventList() {
		
		for(int i = 0; i < name.size(); i++) {
			Task eachTask = createTask(name.get(i), date.get(i), date.get(i), "cat1");
			eventList.add(eachTask);			
		}
	}
	
	private void addEvents() {
		
		if(eventList.size() == 0) {
			initialiseEventList();
		}
		
		for(int i = 0; i < eventList.size(); i++) {
			db.add(eventList.get(i));
		}
	}
	
	private Task createTask(String taskName, String taskStart, String taskEnd, String cat) {
		
		Name name = new Name(taskName);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		LocalDateTime start = null;
		if(taskStart != null) {
			start = LocalDateTime.parse(taskStart, formatter);			
		}
				
		LocalDateTime end = null;
		if(taskEnd != null) {
			end = LocalDateTime.parse(taskEnd, formatter);
		}
			
		Category category = null;
		if(cat != null) {
			category = new Category(cat);
		}
		
		Task newEvent = new Task(name, start, end, category, null, false, false, null);
		
		return newEvent;
	}
	
	@Test
	/**
	 * test add event to the database
	 * 
	 */
	public void testAdd1() {
		db.clear();
		
		eventList = new ArrayList<Task>();
		// test whether can add an event to database
		for(int i = 0; i < eventList.size(); i++) {
			assertTrue(db.add(eventList.get(i)));
		}
		
		// test whether it is really written into the file
		db.loadFromFile();
		ArrayList<Task> listReadFromFile = db.taskList;
		assertEquals(listReadFromFile.size(), eventList.size());
		
		//assert the title of task written into file
		for(int i = 0; i < listReadFromFile.size(); i++) {
			boolean isEqual = listReadFromFile.get(0).getName().getName().equals(name.get(i));
			assertTrue(isEqual);
		}
		
	}
    
	/**
	 * test delete an (existing) event from database
	 */
	@Test
	public void testDelete1() {
		db.clear();
		
		addEvents();

		for(int i = 0; i < eventList.size(); i++) {
			assertTrue(db.delete(eventList.get(i)));
		}

		// check whether it is really deleted from the file
		db.loadFromFile();
		assertTrue(db.taskList.isEmpty());
	}
	
	/**
	 * test delete an (not existing) event from database
	 */
	@Test
	public void testDelete2() {
		db.clear();
		
		addEvents();
		
		Task taskToDelete = createTask("title not exit", date.get(0), date.get(0), null);
		assertFalse(db.delete(taskToDelete));
	}
	
    /**
     * test check existence for an event (existing) in the database
     */
	@Test
	public void testCheckExistence1() {
		db.clear();
		
		addEvents();

		// check the existence
		for(int i = 0; i < eventList.size(); i++) {
			assertTrue(db.checkExistence(eventList.get(i)));
		}
		
	}
	/**
     * test check existence for an event (not existing) in the database
     */
	@Test
	public void testCheckExistence2() {
		db.clear();

		addEvents();
		
		//check the existence for another event
		Task taskToCheck = createTask("new task", date.get(0), date.get(0), null);
		assertFalse(db.checkExistence(taskToCheck));
	}
    
	/**
	 * test retrieve by (existing) name
	 */
	@Test
	public void testRetrieve1() {
		db.clear();
		
		addEvents();
        
		for(int i = 0; i < name.size(); i++) {
			ArrayList<Task> taskList = db.retrieve(new SearchCommand("name", name.get(0)));
			
			//check the size of the resultant list
			boolean isEqual = taskList.size() == 1;
			assertTrue(isEqual);
			
			// check the element is the task we add in
			isEqual = taskList.get(0).getName().getName().equals(name.get(0));
			assertTrue(isEqual);
		}		
	}
	
	/**
	 * test retrieve by (not existing) name
	 */
	@Test
	public void testRetrieve2() {
		db.clear();
		
		addEvents();

		// retrieve the task for the name of the ( not existing) task
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("name", "different-name"));
		//check the size of the resultant list
		boolean isEqual = taskList.size() == 0;
		assertTrue(isEqual);
	}
	
	/**
	 * test retrieve by (existing) category
	 */
	@Test
	public void testRetrieve3() {
		db.clear();
		
		addEvents();

		// retrieve the task for the specific (existing) category
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("category", "cat1"));
		//check the size of the resultant list
		assertEquals(taskList.size(), 10);
	}
	
	/**
	 * test retrieve by (not existing) category
	 */
	@Test
	public void testRetrieve4() {
		db.clear();
		
		addEvents();

		// retrieve the task for the specific (existing) category
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("category", "not_existing_cat"));
		boolean expected = false;
		//check the size of the resultant list
		boolean isEqual = taskList.size() >= 1;
		assertEquals(expected, isEqual);
	}
    
	/**
	 * test retrieveSmartSearch function
	 */
	@Test
	public void testSmartSearch() {
		db.clear();
		
        addEvents();

        // retrieve the task for the keywords
        String[] keywords = ("hello word").split(" ");
        ArrayList<Task> taskList = db.smartSearch(keywords);

        //check the size of the resultant list
        assertEquals(2, taskList.size());
        boolean isEqual = taskList.get(0).getName().getName().equals(name.get(0));
        assertTrue(isEqual);
        isEqual = taskList.get(1).getName().getName().equals(name.get(1));
        assertTrue(isEqual);
	}
	
	/**
	 * test retrieveSmartSearch function
	 */
	@Test
	public void testSmartSearch2() {
		db.clear();
		
        addEvents();

        // retrieve the task for the keywords
        String[] keywords = ("hello").split(" ");
        ArrayList<Task> taskList = db.smartSearch(keywords);

        //check the size of the resultant list      
        assertEquals(2, taskList.size());      
      
        boolean isEqual = taskList.get(0).getName().getName().equals(name.get(0));
        assertTrue(isEqual);
        
        isEqual = taskList.get(1).getName().getName().equals(name.get(1));
        assertTrue(isEqual);
	}
	
	/**
	 * test retrieveAll function
	 */
	@Test
	public void testRetrieveAll() {
		db.clear();
		
        addEvents();

		// check size of the database
		ArrayList<Task> taskList = db.retrieveAll();
		assertEquals(taskList.size(), 10);
		
		// check the element is the task we add in
		for(int i = 0; i < eventList.size(); i++) {
			boolean isEqual = taskList.get(i).getName().getName().equals(name.get(9-i));
			assertTrue(isEqual);
		}
	}
	
	
	/**
	 * test change directory method
	 */
	@Test 
	public void testChangeDir() {
		db.clear();
		
        addEvents();
		
		//change the directory
		assertTrue(db.setNewFile("/Users/Xyx/Desktop/jim"));
	}

}
