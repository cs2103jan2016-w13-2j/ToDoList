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
//@@author yuxin
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
		
		assertFalse(db.delete(newEvent));
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
