package todolist.logic;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import todolist.MainApp;
import todolist.model.Name;
import todolist.model.Task;

public class LogicTest {
	
	private MainApp mainApp;
	Logic logic = new Logic(mainApp);
	
	@Test
	public void testProcess() {
		boolean expected = true;
		
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
	
		logic.process("add event title 2017-01-01 14:00 1 day");
		
		Boolean isEqual = logic.dataBase.taskList.get(0).getName().getName().equals(newEvent.getName().getName());

		assertEquals(isEqual, expected);
	}

	@Test
	public void testStepForward() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddRecurringEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddRecurringDeadline() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDeadline() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testDone() {
		fail("Not yet implemented");
	}

	@Test
	public void testUndone() {
		fail("Not yet implemented");
	}

	@Test
	public void testEdit() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearch() {
		fail("Not yet implemented");
	}

	@Test
	public void testLabel() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetRecurring() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostpone() {
		fail("Not yet implemented");
	}

	@Test
	public void testForward() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddRemind() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddRemindBef() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemindBef() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemind() {
		fail("Not yet implemented");
	}

	@Test
	public void testExit() {
		fail("Not yet implemented");
	}

	@Test
	public void testUndo() {
		fail("Not yet implemented");
	}

	@Test
	public void testRedo() {
		fail("Not yet implemented");
	}

}
