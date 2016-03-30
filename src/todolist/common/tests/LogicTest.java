package todolist.common.tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import todolist.MainApp;
import todolist.logic.Logic;
import todolist.logic.MainAppStub;
import todolist.model.Name;
import todolist.model.SearchCommand;
import todolist.model.Task;

//@@author yuxin
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
