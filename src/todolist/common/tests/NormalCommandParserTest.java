//@@author A0131334W
package todolist.common.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import todolist.model.TokenizedCommand;
import todolist.parser.NormalCommandParser;

public class NormalCommandParserTest {
    NormalCommandParser normalCommandParser = null;

    @Before
    public void initNormalCommandParser() {
        normalCommandParser = new NormalCommandParser();
    }
    
	/*
	 * test normal-command for add event (in the future)
	 */
	@Test
	public void testParseAdd1() {
		//parse the command
		String input = "add event \"cs2013 lecture\" 2019-05-01 16:00 2 hour";
	    String date = "2019-05-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
	 * test normal-command for add event (in the future)
	 */
	@Test
	public void testParseAdd2() {
		//parse the command
		String input = "add event \"cs2013 lecture\" 2019-05-01 16:00 2 hour";
	    String date = "2019-05-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
	 * test normal-command for add event (in the past)
	 */
	@Test
	public void testParseAdd3() {
		//parse the command
		String input = "add event \"cs2013 lecture\" 2100-05-01 16:00 2 hour";
	    String date = "2100-05-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
	 * test normal-command for add event (in the past)
	 */
	@Test
	public void testParseEvent4() {
		//parse the command
		String input = "add event \"cs2013 lecture\" 1970-01-01 16:00 2 hour";
	    String date = "1970-01-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
	 * test normal-command for add event (with keyword 'add')
	 */
	@Test
	public void testParseEvent5() {
		//parse the command
		String input = "add event \"add amount to bank account\" 2017-01-01 16:00 2 hour";
	    String date = "2017-01-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
	 * test normal-command for add event (without the 'year')
	 */
	@Test
	public void testParseEvent6() {
		//parse the command
		String input = "add event \"cs2013 lecture\" 05-01 16:00 2 hour";
	    String date = "05-01";
	    String startTime = "16:00";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
	 * test normal-command for add event (without the 'year' & 'start time') ,
	 */
	@Test
	public void testParseEvent7() {
		//parse the command
		String input = "add event \"cs2013 lecture\" 05-01 2 hour";
	    String date = "05-01";
	    String timeInterval = "2";
	    String unit = "hour";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
		//check timeInterval
		assertTrue(tokonizedCommand[3].trim().equals(timeInterval));
		//check unit
		assertTrue(tokonizedCommand[4].trim().equals(unit));
	}
	
	/*
	 * test normal-command for add deadline
	 */
	@Test
	public void testDeadline1() {
		//parse the command
		String input = "add deadline \"submit proposal\" 2017-01-01 23:59";
	    String date = "2017-01-01";
	    String startTime = "23:59";
		TokenizedCommand command = normalCommandParser.parse(input);
		
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
	 * test normal-command for delete a task
	 */
	@Test
	public void testDelete() {
		//parse the command
		String input = "delete \"title\"";
		TokenizedCommand command = normalCommandParser.parse(input);
		
		//check the action
		boolean isEqual = command.getAction().equals("delete");
		assertTrue(isEqual);
		//check the array of string
		String[] tokonizedCommand = command.getArgs();
		//check the type of the task
		assertTrue(tokonizedCommand[0].equals("title"));
	}	
    
}
