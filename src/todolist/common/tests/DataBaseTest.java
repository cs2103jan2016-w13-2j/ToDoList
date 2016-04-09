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
		
       String commandDate = "2017-01-0";
	   
       for(int i = 0; i < 10; i++) {
    	   date.add(commandDate + i + " " + "14:00");
       }
	}

	private void initialiseName() {
		
		String commonName = "title";
		
		for(int i = 0; i < 10; i++) {
			name.add(commonName + i);
		}		
	}

	private void initialiseEventList() {
		
		for(int i = 0; i < name.size(); i++) {
			
			Task eachTask = createTask(name.get(i), date.get(i), date.get(i), "cat");
			eventList.add(eachTask);			
		}
	}
	
	private void addEvents() {
		
		if(eventList == null) {
			
			for(int i = 0; i < eventList.size(); i++) {
				db.add(eventList.get(i));
			}
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
		
		eventList = null;
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
		
		// add one event
		addEvents();

		// delete the task
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
		
		//add event to the database
		addEvents();
		
		//delete an event not in the database
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
