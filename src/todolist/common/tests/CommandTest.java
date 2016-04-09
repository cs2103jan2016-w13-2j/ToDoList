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
    private ArrayList<String> input;
	private ArrayList<LocalDateTime> startdate;
    private ArrayList<LocalDateTime> enddate;
    private ArrayList<String> name;
    
	@Before
	public void setUp() throws Exception {
        mainAppStub = new MainAppStub();
        logic = new Logic(mainAppStub);
        input = new ArrayList<String>();
        startdate = new ArrayList<LocalDateTime>();
        enddate = new ArrayList<LocalDateTime>();
        name = new ArrayList<String>();
        
        initialiseInput();
        initialiseName();
        initialiseStartdate();
        initialiseEnddate();
	}
    
	private void initialiseInput() {
		input.add(0, "add event firsttitle 2017-01-01 14:00 1 day");
		input.add(1, "add event secondtitle 2016-01-01 14:00 1 day");
		input.add(2, "add event \"cs2103 lecture\" 2016-08-01 23:00 2 hour");
		input.add(3, "i want to attend lecture tmr 4pm to 6pm");
		input.add(4, "i want to add money to my account tmr 4pm to 6pm");
		input.add(5, "i want to eat lunch tmr");
		input.add(6, "add deadline title 1970-01-01 12:00");
		input.add(7, "add deadline title2 2100-01-01 12:00");
		input.add(8, "sumbit project proposal by tmr 2359");
		input.add(9, "sumbit proposal by today");
		input.add(10, "add task \"a floating task\"");
		input.add(11, "try tutorial question");
		input.add(12, "try to add amount to bank account");
		input.add(13, "delete firsttitle");
		input.add(14, "edit firsttitle title newtitle");

	}
	
	private void initialiseName() {
		name.add(0, "firsttitle");
		name.add(1, "secondtitle");
		name.add(2, "cs2103 lecture");
		name.add(3, "i want to attend lecture");
		name.add(4, "i want to add money to my account");
		name.add(5, "i want to eat lunch");
		name.add(6, "title");
		name.add(7, "title2");
		name.add(8, "sumbit project proposal");
		name.add(9, "sumbit proposal");
		name.add(10, "a floating task");
		name.add(11, "try tutorial question");
		name.add(12, "try to add amount to bank account");
		//name.add(13, "try tutorial question");		
	}
	
	private void initialiseStartdate() {
		startdate.add(0, parseDate("2017-01-01 14:00"));
		startdate.add(1, parseDate("2016-01-01 14:00"));
		startdate.add(2, parseDate("2016-08-01 23:00"));
		startdate.add(3, parseDate(tmr_string() + " " + "16:00"));
		startdate.add(4, parseDate(tmr_string() + " " + "16:00"));
		
		
	}
	
	private void initialiseEnddate() {	
		enddate.add(0, startdate.get(0).plus(Long.parseLong("1"), ChronoUnit.DAYS));
		enddate.add(1, startdate.get(1).plus(Long.parseLong("1"), ChronoUnit.DAYS));
		enddate.add(2, startdate.get(2).plus(Long.parseLong("2"), ChronoUnit.HOURS));
		enddate.add(3, startdate.get(3).plus(Long.parseLong("2"), ChronoUnit.HOURS));
		enddate.add(4, startdate.get(4).plus(Long.parseLong("2"), ChronoUnit.HOURS));
		enddate.add(5, parseDate(tmr_string() + " " + "13:00"));
		enddate.add(6, parseDate("1970-01-01 12:00"));
		enddate.add(7, parseDate("2100-01-01 12:00"));
		enddate.add(8, parseDate(tmr_string() + " " + "23:59"));
		enddate.add(9, parseDate(today_string() + " " + "23:59"));
	}
	
	
	private LocalDateTime parseDate(String date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatedDate = LocalDateTime.parse(date, formatter);
        return formatedDate;
	}
	
	private String today_string() {
		
		DecimalFormat decimalFormatter = new DecimalFormat("00");
		String str = LocalDateTime.now().getYear() + "-"
				+ decimalFormatter.format(LocalDateTime.now().getMonthValue()) + "-"
				+ decimalFormatter.format(LocalDateTime.now().getDayOfMonth());
		
		return str;
		
	}
	
    private String tmr_string() {
    	
    	DecimalFormat decimalFormatter = new DecimalFormat("00");
		String str = LocalDateTime.now().getYear() + "-"
				+ decimalFormatter.format(LocalDateTime.now().getMonthValue()) + "-"
				+ decimalFormatter.format(LocalDateTime.now().getDayOfMonth()+1);
		
		return str;
    }

	/*
     * 0. test add function: event, normal input,start time in the future
     */
    @Test
    public void testAddCommand0() {
    	logic.clean();

    	logic.process(input.get(0));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().equals(name.get(0));
    	assertTrue(isEqual);

    	// check start time
    	isEqual = thisTask.getStartTime().isEqual(startdate.get(0));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(0));
    	assertTrue(isEqual);             
    }
    
	/*
     * 1. test add function: event, normal input,start time in the past
     */
    @Test
    public void testAddCommand1() {
    	logic.clean();

    	logic.process(input.get(1));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().equals(name.get(1));
    	assertTrue(isEqual);

    	// check start time
    	isEqual = thisTask.getStartTime().isEqual(startdate.get(1));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(1));
    	assertTrue(isEqual);             
    }
    
	/*
     * 2. test add function:event,normal input,start time in the future;
     *    with event across two days
     */
    @Test
    public void testAddCommand2() {
    	logic.clean();

    	logic.process(input.get(2));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().equals(name.get(2));
    	assertTrue(isEqual);

    	// check start time
    	isEqual = thisTask.getStartTime().isEqual(startdate.get(2));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(2));
    	assertTrue(isEqual);             
    }

	/*
     * 3. test add function:add event, flexi-command input; start time in the future;
     */
    @Test
    public void testAddCommand3() {
    	logic.clean();

    	logic.process(input.get(3));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(3));
    	assertTrue(isEqual);

    	// check start time
    	isEqual = thisTask.getStartTime().isEqual(startdate.get(3));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(3));
    	assertTrue(isEqual);             
    }
    
	/*
     * 4. test add function:add event flexi-command input; 
     *    start time in the future;with key word 'add' in the title
     */
    @Test
    public void testAddCommand4() {
    	logic.clean();

    	logic.process(input.get(4));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(4));
    	assertTrue(isEqual);

    	// check start time
    	isEqual = thisTask.getStartTime().isEqual(startdate.get(4));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(4));
    	assertTrue(isEqual);             
    }
    
	/*
     * 5. test add function: add deadline flexi-command ;with key word 'lunch'
     */
    @Test
    public void testAddCommand5() {
    	logic.clean();

    	logic.process(input.get(5));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(5));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(5));
    	assertTrue(isEqual);             
    }
    
	/*
     * 6. test add function: add deadline,normal input;start time in the future
     */
    @Test
    public void testAddCommand6() {
    	logic.clean();

    	logic.process(input.get(6));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(6));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(6));
    	assertTrue(isEqual);             
    }
    
	/*
     * 7. test add function:add deadline,normal input;start time in the past
     */
    @Test
    public void testAddCommand7() {
    	logic.clean();

    	logic.process(input.get(7));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(7));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(7));
    	assertTrue(isEqual);             
    }
    
	/*
     * 8. test add function: add deadline,flexi input;start time in the future
     */
    @Test
    public void testAddCommand8() {
    	logic.clean();

    	logic.process(input.get(8));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(8));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(8));
    	assertTrue(isEqual);             
    }
    
	/*
     * 9. test add function: add deadline, flexi-command,with key word today
     */
    @Test
    public void testAddCommand9() {
    	logic.clean();

    	logic.process(input.get(9));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(9));
    	assertTrue(isEqual);

    	// check end time
    	isEqual = thisTask.getEndTime().isEqual(enddate.get(9));
    	assertTrue(isEqual);             
    }
    
	/*
     * 10. test add function:add floating task, normal input
     */
    @Test
    public void testAddCommand10() {
    	logic.clean();

    	logic.process(input.get(10));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(10));
    	assertTrue(isEqual);             
    }
    
	/*
     * 11. test add function:add floating task, flexi input
     */
    @Test
    public void testAddCommand11() {
    	logic.clean();

    	logic.process(input.get(11));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(11));
    	assertTrue(isEqual);             
    }
    
	/*
     * 12. test add function: add floating task command with flexi command; 
     *     with 'add' keyword in the middle
     */
    @Test
    public void testAddCommand12() {
    	logic.clean();

    	logic.process(input.get(12));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	Boolean isEqual = taskList.size() == 1;
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	isEqual = thisTask.getName().getName().trim().equals(name.get(12));
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
    
    
    /*
     * 13. test delete function
     */
    @Test
    public void testDelete() {
        logic.clean();
        
        //add a task
        logic.process(input.get(0));
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
        assertTrue(taskList.size() == 1);
        
        //delete the task
        logic.process(input.get(13));
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.isEmpty());        
    }
    
    /*
     * 14. test edit function
     */
    @Test
    public void testEdit() {
        logic.clean();
        
        //add a task
        logic.process(input.get(0));
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
        assertTrue(taskList.size() == 1);
        
        //edit the title
        logic.process(input.get(14));
        
        //check that the task is still there 
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check name of the task
        boolean isEqual = taskList.get(0).getName().getName().equals("newtitle");
        assertTrue(isEqual);     
    }
    
    /*
     * 15. test edit function
     */
    @Test
    public void testEdit2() {
        logic.clean();
        
        //add a task
        logic.process(input.get(0));
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
        assertTrue(taskList.size() == 1);
        
        //edit the title
        logic.process(input.get(14));
        
        //check that the task is still there 
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check name of the task
        boolean isEqual = taskList.get(0).getName().getName().equals("newtitle");
        assertTrue(isEqual);
        
    }

    

}
