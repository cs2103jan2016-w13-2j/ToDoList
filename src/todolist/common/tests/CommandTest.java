//@@author A0131334W
package todolist.common.tests;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import todolist.MainApp;
import todolist.logic.Logic;
import todolist.logic.MainAppStub;
import todolist.model.Task;
import todolist.model.TokenizedCommand;

public class CommandTest {
	
	private MainApp mainAppStub;
	private Logic logic;

	@Before
	public void setUp() throws Exception {
        mainAppStub = new MainAppStub();
        logic = new Logic(mainAppStub);
	}
    
	/*
     * 1. test add event command with normal input;with start time in the future
     */
    @Test
    public void testAddEvent1() {
        logic.clean();

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
        // add in the command to add a new event
        logic.process("add event title 2017-01-01 14:00 1 day");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
    /*
     * 2. test add event command with normal input; start time in the past
     */
    @Test
    public void testAddEvent2() {
        logic.clean();

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2016-01-01" + " " + "14:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
        // add in the command to add a new event
        logic.process("add event title 2016-01-01 14:00 1 day");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
    /*
     * 3. test add event command with normal input; start time in the future;with event across two days
     */
    @Test
    public void testAddEvent3() {
        logic.clean();

        String name = "cs2103 lecture";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2016-08-01" + " " + "23:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("2"), ChronoUnit.HOURS);
        // add in the command to add a new event
        logic.process("add event \"cs2103 lecture\" 2016-08-01 23:00 2 hour");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
    /*
     * 4. test add event command with flexi-command input; start time in the future;
     */
    @Test
    public void testAddEvent4() {
        logic.clean();

        String name = "i want to attend lecture";
        DecimalFormat decimalFormatter = new DecimalFormat("00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String start_string = LocalDateTime.now().getYear() + "-"
				+ decimalFormatter.format(LocalDateTime.now().getMonthValue()) + "-"
				+ decimalFormatter.format(LocalDateTime.now().getDayOfMonth()+1)  + " " + "16:00";
        
        LocalDateTime start = LocalDateTime.parse(start_string, formatter);
        LocalDateTime end = start.plus(Long.parseLong("2"), ChronoUnit.HOURS);
        // add in the command to add a new event
        logic.process("i want to attend lecture tmr 4pm to 6pm");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        
        isEqual = taskList.get(0).getName().getName().trim().equals(name);
        assertTrue(isEqual);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
    /*
     * 5. test add event command with flexi-command input; start time in the future;with key word 'add' in the title
     */
    @Test
    public void testAddEvent5() {
        logic.clean();

        String name = "i want to add money to my account";
        DecimalFormat decimalFormatter = new DecimalFormat("00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String start_string = LocalDateTime.now().getYear() + "-"
				+ decimalFormatter.format(LocalDateTime.now().getMonthValue()) + "-"
				+ decimalFormatter.format(LocalDateTime.now().getDayOfMonth()+1)  + " " + "16:00";
        
        LocalDateTime start = LocalDateTime.parse(start_string, formatter);
        LocalDateTime end = start.plus(Long.parseLong("2"), ChronoUnit.HOURS);
        // add in the command to add a new event
        logic.process("i want to add money to my account tmr 4pm to 6pm");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        
        isEqual = taskList.get(0).getName().getName().trim().equals(name);
        assertTrue(isEqual);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
    
    
    /*
	 * 6. test flexi-command for add deadline (with key word 'lunch')
	 */
	@Test
	public void testAddDeadline1() {
		logic.clean();
		
		String input = "i want to eat lunch tmr";//should be by 1300 tmr
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DecimalFormat decimalFormatter = new DecimalFormat("00");
	    String deadlineDate = date.getYear() + "-"
				+ decimalFormatter.format(date.getMonthValue()) + "-"
				+ decimalFormatter.format(date.getDayOfMonth()+1) + " " + "13:00";
	    LocalDateTime deadline = LocalDateTime.parse(deadlineDate, formatter);
		
	    // add in the command to add a new deadline
        logic.process(input);
        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().trim().equals("i want to eat lunch");
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(deadline);
        assertTrue(isEqual);

	}
	
	/*
     * 6. test add deadline command with normal input;start time in the past
     */
    @Test
    public void testAddDeadline2() {
        logic.clean();

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime end = LocalDateTime.parse("1970-01-01" + " " + "12:00", formatter);

        // pass in the command to add a new deadline
        logic.process("add deadline title 1970-01-01 12:00");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
	/*
     * 7. test add deadline command with normal input;start time in the future
     */
    @Test
    public void testAddDeadline3() {
        logic.clean();

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime end = LocalDateTime.parse("2100-01-01" + " " + "12:00", formatter);

        // pass in the command to add a new deadline
        logic.process("add deadline title 2100-01-01 12:00");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
	/*
     * 8. test add deadline command with flexi input;start time in the future
     */
    @Test
    public void testAddDeadline4() {
        logic.clean();
   
        String name = "sumbit proposal";
        DecimalFormat decimalFormatter = new DecimalFormat("00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String deadline_string = LocalDateTime.now().getYear() + "-"
				+ decimalFormatter.format(LocalDateTime.now().getMonthValue()) + "-"
				+ decimalFormatter.format(LocalDateTime.now().getDayOfMonth()+1)  + " " + "23:59";
        
        LocalDateTime deadline = LocalDateTime.parse(deadline_string, formatter);

        // pass in the command to add a new deadline
        logic.process("sumbit proposal by tmr 2359");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().trim().equals(name);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(deadline);
        assertTrue(isEqual);
    }
    
	/*
	 * test flexi-command for add deadline (with key word today)
	 */
	@Test
	public void test_ParseDeadline2() {
		logic.clean();
		//parse the command
		String input = "sumbit proposal by today";//should be by 2359 today
		LocalDateTime date = LocalDateTime.now();
		DecimalFormat decimalFormatter = new DecimalFormat("00");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    String deadlineDate = date.getYear() + "-"
				+ decimalFormatter.format(date.getMonthValue()) + "-"
				+ decimalFormatter.format(date.getDayOfMonth()) + " " + "23:59";
	    
	    LocalDateTime deadline = LocalDateTime.parse(deadlineDate, formatter);
		// pass in the command to add a new deadline
        logic.process(input);

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().trim().equals("sumbit proposal");
        assertTrue(isEqual);
        // check end time
        //System.out.println("endddd: " + taskList.get(0).getEndTime());
        //System.out.println("endddd: " + deadline);
        isEqual = taskList.get(0).getEndTime().isEqual(deadline);
        assertTrue(isEqual);
	 }
    
    
    /*
     * 9. test add floating task command with normal input
     */
    @Test
    public void testAddFloating1() {
        logic.clean();
        
        String name = "title";

        // pass in command to add a floating task
        logic.process("add task title");
        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
    }
    
    /*
     * 10. test add floating task command with flexi input
     */
    @Test
    public void testAddFloating2() {
        logic.clean();
        
        String name = "try tutorial question";

        // pass in command to add a floating task
        logic.process("try tutorial question");
        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
    }
    
    /*
     * 11. test add floating task command with flexi command; with 'add' keyword in the middle
     */
    @Test
    public void testAddFloating3() {
        logic.clean();
        
        String name = "try to add amount to bank account";

        // pass in command to add a floating task
        logic.process("try to add amount to bank account");
        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
    }
    
    
	/*
     * 12. test add recurring event command with start time in the future
     */
    /*@Test
    public void testAddRecurring_Event1() {
        logic.clean();

        String name = "title";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
        LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
        // add in the command to add a new event
        logic.process("add-recurring event title 2017-01-01 14:00 ");

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
        // check start time
        isEqual = taskList.get(0).getStartTime().isEqual(start);
        assertTrue(isEqual);
        // check end time
        isEqual = taskList.get(0).getEndTime().isEqual(end);
        assertTrue(isEqual);
    }
    
*/
    /*
     * 11. test add floating task command with flexi command; with 'add' keyword in the middle
     */
    @Test
    public void testAddFloating() {
        logic.clean();
        
        String name = "try to add amount to bank account";

        // pass in command to add a floating task
        logic.process("try to add amount to bank account");
        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        Boolean isEqual = taskList.size() == 1;
        assertTrue(isEqual);
        // check name of the task
        isEqual = taskList.get(0).getName().getName().equals(name);
        assertTrue(isEqual);
    }
    

}
