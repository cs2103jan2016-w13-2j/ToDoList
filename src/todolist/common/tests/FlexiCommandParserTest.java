//@@author A0131334W
package todolist.common.tests;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import todolist.model.TokenizedCommand;
import todolist.parser.FlexiCommandParser;

public class FlexiCommandParserTest {
    
	FlexiCommandParser flexiParser;
	
	@Before
	public void setUp() throws Exception {
		flexiParser = new FlexiCommandParser();
	}

	/*
	 * test flexi-command for add deadline
	 */
	@Test
	public void testParseDeadline() {
		//parse the command
		String input = "sumbit proposal tmr 2359";
		LocalDateTime date = LocalDateTime.now();
		DecimalFormat decimalFormatter = new DecimalFormat("00");
	    String deadlineDate = date.getYear() + "-"
				+ decimalFormatter.format(date.getMonthValue()) + "-"
				+ decimalFormatter.format(date.getDayOfMonth()+1);
	    
		TokenizedCommand command = flexiParser.parse(input);
		
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
	
		
	/*
	 * test flexi-command for add event
	 */
	@Test
	public void testParseEvent() {
		//parse the command
		String input = "2013 lecture 4/12/16 4pm to 6pm";
	    String date = "2016-04-12";
	    String startTime = "16:00";
	    String timeInterval = Integer.toString(2*60);
	    String unit = "minute";
		TokenizedCommand command = flexiParser.parse(input);
		
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
	 * test flexi-command for add floating task
	 */
	@Test
	public void testParseTask() {
		//parse the command
		String input = "attend lecture";
		TokenizedCommand command = flexiParser.parse(input);
		
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

}
