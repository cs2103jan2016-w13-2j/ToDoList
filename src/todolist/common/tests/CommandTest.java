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
		input.add(15, "edit firsttitle start-time 2017-01-02-13:00");
		input.add(16, "edit firsttitle end-time 2017-01-03-13:00");
		input.add(17, "add-remind deadline \"to be reminded\" 05-01 14:00");
		input.add(18, "add recurring deadline 2-hour title 2017-01-01 14:00");		
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
		name.add(13, "to be reminded");		
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
     * 13. test add function: add a deadline with reminder
     */
    @Test
    public void testAddCommand13() {
    	logic.clean();

    	logic.process(input.get(17));

    	//check size of database
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();
    	assertEquals(1, taskList.size());

    	Task thisTask = taskList.get(0);
    	// check name of the task
    	boolean isEqual;
    	isEqual = thisTask.getName().getName().trim().equals(name.get(13));
    	assertTrue(isEqual);
    	
    	//check the status of reminder
    	assertTrue(thisTask.getReminder().getStatus()); 
    }
        
	/*
     * 14. test add recurring deadline
     */
    @Test
    public void testAddCommand14() {
        logic.clean();

        logic.process(input.get(18));

        // check size of database
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();
        assertEquals(1, taskList.size());
        
        //check recurring status
        assertTrue(taskList.get(0).getRecurringStatus());
        
    }
    
    
    /*
     * 15. test delete function
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
     * 16. test edit function: edit title of the task
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
     * 17/18. test edit function: edit the start/end time of the task
     */
    @Test
    public void testEdit2() {
        logic.clean();
        
        //add a task
        logic.process(input.get(0));
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
        assertTrue(taskList.size() == 1);
        
        //edit the start time
        logic.process(input.get(15));
        
        //check that the task is still there 
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check start time of the task
        boolean isEqual = taskList.get(0).getStartTime().toString().equals("2017-01-02T13:00");
        assertTrue(isEqual); 
        
        //edit the end time
        logic.process(input.get(16));
        
        //check end time of the task
        taskList = logic.dataBase.retrieveAll();
        isEqual = taskList.get(0).getEndTime().toString().equals("2017-01-03T13:00");
        assertTrue(isEqual);      
    }
    
    /*
     * 19/20. test edit function: remove start/end time
     */
    @Test
    public void testEdit3() {
        logic.clean();
        
        //add a task
        logic.process(input.get(0));
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
        assertTrue(taskList.size() == 1);
        
        //edit the start time
        logic.process("edit firsttitle start-time remove");
        
        //check that the task is still there 
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check start time of the task
        boolean isEqual = taskList.get(0).getStartTime() == null;
        assertTrue(isEqual); 
        
        //edit the end time
        logic.process("edit firsttitle end-time remove");
        
        //check end time of the task
        taskList = logic.dataBase.retrieveAll();
        isEqual = taskList.get(0).getEndTime() == null;
        assertTrue(isEqual);      
    }

    /*
     * 21. test label function: label the task to certain category
     */
    @Test
    public void testLabel() {
    	logic.clean();

    	//add a task
    	logic.process(input.get(0));
    	ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
    	assertTrue(taskList.size() == 1);

    	//edit the start time
    	logic.process("label firsttitle cat1");

    	//check that the task is still there 
    	taskList = logic.dataBase.retrieveAll();
    	assertTrue(taskList.size() == 1);

    	//check label of the task
    	boolean isEqual = taskList.get(0).getCategory().getCategory().equals("cat1");;
    	assertTrue(isEqual); 
    }

    /*
     * 22. test remind function: set reminder to an event
     */
    @Test
    public void testRemind() {
        logic.clean();
        
        //add a task
        logic.process(input.get(0));
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
        assertTrue(taskList.size() == 1);
        
        //edit the start time
        logic.process("remind firsttitle");
        
        //check that the task is still there 
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check label of the task
        assertTrue(taskList.get(0).getReminder().getStatus()); 
    }
    
    /*
     * 23. test remind function: set reminder to an event
     */
    @Test
    public void testDone() {
        logic.clean();
        
        //add a task
        logic.process(input.get(0));
        ArrayList<Task> taskList = logic.dataBase.retrieveAll();       
        assertTrue(taskList.size() == 1);
        
        //edit the start time
        logic.process("done firsttitle");
        
        //check that the task is still there 
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check done status of the task
        assertTrue(taskList.get(0).getDoneStatus()); 
        
        logic.process("undone firsttitle");
        taskList = logic.dataBase.retrieveAll();
        assertFalse(taskList.get(0).getDoneStatus());       
    }
    
    /*
     * 24. test undo/redo function
     */
    @Test
    public void testUndo() {
        logic.clean();
        
        //add tasks
        logic.process(input.get(0));
        logic.process(input.get(1));        
        ArrayList<Task> taskList = null;
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 2);
        
        //delete tasks         
        logic.process(input.get(13));
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);

        logic.process("undo 1");
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 2);
        
        logic.process("redo 1");
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1); 
        
        //add another two task
        logic.process(input.get(2));
        logic.process(input.get(3));
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 3);
        
        logic.process("undo 2");
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        logic.process("redo 2");
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 3);      
    }
    
    /*
     * 25. test set-recurring function
     */
    @Test
    public void testSetRemoveRecurring() {
        logic.clean();
        
        //add tasks
        logic.process(input.get(0));        
        ArrayList<Task> taskList = null;
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
           
        logic.process("set-recurring " + name.get(0) + " 3-month");
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        //check the recurring status and interval
        assertTrue(taskList.get(0).getRecurringStatus());
        assertTrue(taskList.get(0).getInterval().equals("3-month")); 
        
        logic.process("remove-recurring  " + name.get(0));
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        //check the recurring status and interval
 
        assertFalse(taskList.get(0).getRecurringStatus());
        assertTrue(taskList.get(0).getInterval() == null); 
    }
    
    /*
     * 26. test set-recurring function
     */
    @Test
    public void testRemoveRecurring() {
        logic.clean();
        
        //add tasks
        logic.process(input.get(0));        
        ArrayList<Task> taskList = null;
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
           
        logic.process("remove-recurring " + name.get(0));
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        //check the recurring status and interval
        assertFalse(taskList.get(0).getRecurringStatus());
        assertTrue(taskList.get(0).getInterval() == null); 
    }
    
    /*
     * 27/28. test postpone/forward function
     */
    @Test
    public void testPostponeForward() {
        logic.clean();
        
        //add tasks
        logic.process(input.get(0));        
        ArrayList<Task> taskList = null;
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
           
        logic.process("postpone " + name.get(0) + " 2 day");
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check the start and end time        
        assertTrue(taskList.get(0).getStartTime().equals(startdate.get(0).plus(Long.parseLong("2"), ChronoUnit.DAYS)));
        assertTrue(taskList.get(0).getEndTime().equals(enddate.get(0).plus(Long.parseLong("2"), ChronoUnit.DAYS))); 
        
        logic.process("forward " + name.get(0) + " 2 day");
        taskList = logic.dataBase.retrieveAll();
        assertTrue(taskList.size() == 1);
        
        //check the start and end time        
        assertTrue(taskList.get(0).getStartTime().equals(startdate.get(0)));
        assertTrue(taskList.get(0).getEndTime().equals(enddate.get(0))); 
    }
}
