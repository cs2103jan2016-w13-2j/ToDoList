package todolist.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import todolist.model.Name;
import todolist.model.SearchCommand;
import todolist.model.Task;

public class DataBaseTest {

	/**
	 * @throws java.lang.Exception
	 */

	DataBase db;

	@Before
	public void setUp() throws Exception {
		db = new DataBase();
	}

	@Test
	public void testAdd() {
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

	@Test
	public void testDelete() {
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

	@Test
	public void testCheckExistence() {
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

	@Test
	public void testRetrieve() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// check the arraylist only has one element
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("name", "title"));
		boolean expected = true;
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
		// check the element is the task we add in
		isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);
	}

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

}
