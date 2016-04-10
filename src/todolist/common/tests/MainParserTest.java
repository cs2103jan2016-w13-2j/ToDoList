//@@author A0131334W
package todolist.common.tests;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import todolist.model.TokenizedCommand;
import todolist.parser.MainParser;

public class MainParserTest {
    MainParser mainparser = null;
    
	@Before
	public void setUp() throws Exception {
		mainparser = new MainParser();
	}

	/*
	 * 1. test normal-command for add event
	 */
	@Test
	public void testAddEvent1() {
		//parse the command
		String input = "add event \"cs2013 lecture\" 2019-05-01 16:00 2 hour";
	    String date = "2019-05-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = mainparser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("add");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("event"));
		//check the title of the task
		assertTrue(tokonizedCommand[1].trim().equals("cs2013 lecture".trim()));
		//check the date
		assertTrue(tokonizedCommand[2].trim().equals(date));
		//check the start time
		assertTrue(tokonizedCommand[3].trim().equals(startTime));
		//check timeInterval
		assertTrue(tokonizedCommand[4].trim().equals(timeInterval));
		//check unit
		assertTrue(tokonizedCommand[5].trim().equals(unit));
	}
	
	/*
	 * 2. test normal-command for add event (with keyword 'add')
	 */
	@Test
	public void testAddEvent2() {
		//parse the command
		String input = "add event \"add amount to bank account\" 2017-01-01 16:00 2 hour";
	    String date = "2017-01-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = mainparser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("add");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("event"));
		//check the title of the task
		assertTrue(tokonizedCommand[1].trim().equals("add amount to bank account".trim()));
		//check the date
		assertTrue(tokonizedCommand[2].trim().equals(date));
		//check the start time
		assertTrue(tokonizedCommand[3].trim().equals(startTime));
		//check timeInterval
		assertTrue(tokonizedCommand[4].trim().equals(timeInterval));
		//check unit
		assertTrue(tokonizedCommand[5].trim().equals(unit));
	}
	
	/*
	 * 3. test normal-command for add deadline
	 */
	@Test
	public void testAddDeadline() {
		//parse the command
		String input = "add deadline \"submit proposal\" 2017-01-01 23:59";
	    String date = "2017-01-01";
	    String startTime = "23:59";
		TokenizedCommand command = mainparser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("add");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("deadline"));
		//check the title of the task
		assertTrue(tokonizedCommand[1].trim().equals("submit proposal".trim()));
		//check the date
		assertTrue(tokonizedCommand[2].trim().equals(date));
		//check the start time
		assertTrue(tokonizedCommand[3].trim().equals(startTime));
	}
	
	/*
	 * 3. test normal-command for add floating task
	 */
	@Test
	public void testAddFloating() {
		//parse the command
		String input = "add deadline \"submit proposal\" 2017-01-01 23:59";
	    String date = "2017-01-01";
	    String startTime = "23:59";
		TokenizedCommand command = mainparser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("add");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("deadline"));
		//check the title of the task
		assertTrue(tokonizedCommand[1].trim().equals("submit proposal".trim()));
		//check the date
		assertTrue(tokonizedCommand[2].trim().equals(date));
		//check the start time
		assertTrue(tokonizedCommand[3].trim().equals(startTime));
	}
	
	/*
	 * 5. test flexi-command for add event
	 */
	@Test
	public void testFlexiAddEvent() {
		//parse the command
		String input = "2013 lecture 4/12/16 4pm to 6pm";
	    String date = "2016-04-12";
	    String startTime = "16:00";
	    String timeInterval = Integer.toString(2*60);
	    String unit = "minute";
		TokenizedCommand command = mainparser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("add");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("event"));
		//check the title of the task
		assertTrue(tokonizedCommand[1].trim().equals("2013 lecture".trim()));
		//check the date
		assertTrue(tokonizedCommand[2].trim().equals(date));
		//check the start time
		assertTrue(tokonizedCommand[3].trim().equals(startTime));
		//check timeInterval
		assertTrue(tokonizedCommand[4].trim().equals(timeInterval));
		//check unit
		assertTrue(tokonizedCommand[5].trim().equals(unit));
	}
	
	
	/*
	 * 6. test flexi-command for add floating task
	 */
	@Test
	public void testFlexiAddTask() {
		//parse the command
		String input = "attend lecture";
		TokenizedCommand command = mainparser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("add");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("task"));
		//check the title of the task
		assertTrue(tokonizedCommand[1].trim().equals("attend lecture".trim()));
	}
	
	/*
	 * 7. test flexi-command for add deadline
	 */
	@Test
	public void testFlexiAddDeadline() {
		//parse the command
		String input = "sumbit proposal tmr 2359";
		LocalDateTime date = LocalDateTime.now();
		DecimalFormat decimalFormatter = new DecimalFormat("00");
	    String deadlineDate = date.getYear() + "-"
				+ decimalFormatter.format(date.getMonthValue()) + "-"
				+ decimalFormatter.format(date.getDayOfMonth()+1);
	    
		TokenizedCommand command = mainparser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("add");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("deadline"));
		//check the title of the task
		assertTrue(tokonizedCommand[1].trim().equals("sumbit proposal".trim()));
		//check the date
		assertTrue(tokonizedCommand[2].trim().equals(deadlineDate));
		//check the time
		assertTrue(tokonizedCommand[3].trim().equals("23:59"));
	}
	

}
