package todolist.common.tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import todolist.MainApp;
import todolist.logic.Logic;
import todolist.logic.MainAppStub;
import todolist.model.Name;
import todolist.model.Task;

public class LogicTest {
	
	private MainApp mainAppStub = new MainAppStub();
	
	private Logic logic = new Logic(mainAppStub);
	
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
		int original = logic.checkStep();
		logic.stepForward(1);
		assertEquals(logic.checkStep(), original + 1);
	}

	public void testAddRecurringEvent() {
		fail("Not yet implemented");
	}

	public void testAddRecurringDeadline() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddEvent() {
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("1970-01-01" + " " + "12:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		
		logic.addEvent("title", "1970-01-01", "12:00", "1", "day");
		Boolean isEqual = logic.dataBase.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assert(isEqual);
	}

	@Test
	public void testAddDeadline() {
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime end = LocalDateTime.parse("1970-01-01" + " " + "12:00", formatter);
		Task newEvent = new Task(name, null, end, null, null, false, false, null);
		
		logic.addDeadline("title", "1970-01-01", "12:00");
		Boolean isEqual = logic.dataBase.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assert(isEqual);
	}

	@Test
	public void testAddTask() {
		Name name = new Name("title");
		Task newEvent = new Task(name, null, null, null, null, false, false, null);
		logic.addTask("title");
		Boolean isEqual = logic.dataBase.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assert(isEqual);
	}

	@Test
	public void testDone() {
		logic.addTask("title");
		logic.done("title");
		Name name = new Name("title");
		Task newEvent = new Task(name, null, null, null, null, true, false, null);
		Boolean isEqual = logic.dataBase.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assert(isEqual);
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
		assert(isEqual);
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
