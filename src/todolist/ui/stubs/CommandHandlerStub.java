package todolist.ui.stubs;

import java.time.LocalDateTime;
import java.util.ArrayList;

import todolist.MainApp;
//import todolist.model.Feedback.Page;
import todolist.model.Category;
import todolist.model.Reminder;
import todolist.ui.TaskWrapper;

public class CommandHandlerStub {

	private static String status = "200: Okay";
	private static ArrayList<Category> categoriesToDisplay = new ArrayList<Category>();
	private static Category currentCategory = new Category("SAMPLE CATEGORY");
	private static ArrayList<TaskWrapper> tasksToDisplay = new ArrayList<TaskWrapper>();
	private static ArrayList<Reminder> remindersToTrack = new ArrayList<Reminder>();
	private static String currentSearch = "";
//	private static Page page = Page.Home;
	
	private MainApp main = null;
	
	public CommandHandlerStub(MainApp main) {
	    this.main = main;
	}

	//public void execute(Command command) {
		
//		Feedback feedback= new Feedback(status, page,
//			categoriesToDisplay, currentCategory,
//			tasksToDisplay,
//			remindersToTrack, currentSearch);
		
		// ... Do something 
		
		//ArrayList<TaskWrapper> tasksToDisplay = new ArrayList<TaskWrapper>();
		
//		tasksToDisplay.add(new TaskWrapper("Do UI Handler (CHANGED)", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
//                new Category("CS2103T Project (CHANGED)"), new Reminder(LocalDateTime.now().plusHours(3))));
//        tasksToDisplay.add(new TaskWrapper("Setup Trello (THIS ALSO CHANGED)", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
//                new Category("CS2103T Project (CHANGED)"), new Reminder(LocalDateTime.now().plusHours(3))));
//        tasksToDisplay.add(new TaskWrapper("Prepare CV (CHANGED)", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
//                new Category("Personal (CHANGED)"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));
//        tasksToDisplay.add(new TaskWrapper("Buy leather shoes (CHANGED)", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
//                new Category("Personal (CHANGED)"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));
//        tasksToDisplay.add(new TaskWrapper("Send emails (CHANGED)", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
//                new Category("18th MC (CHANGED)"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));
                
        // Call my functions ...
        
//		main.setDisplayTasks(tasksToDisplay);
        
//		return feedback;
	//}
}
