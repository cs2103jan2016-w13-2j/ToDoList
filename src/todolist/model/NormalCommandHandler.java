package todolist.model;

public class NormalCommandHandler {
	
	public static DataBase dataBase = new dataBase();
	
	public static void addEvent(String title, String startDate, String startTime, String quantity, String timeUnit) {
	    Name name = new Name(title);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    LocalDateTime start = LocalDateTime.parse(startDate + " " + startTime, formatter);
	    LocalDateTime end = start.plus(Long.parseLong(quantity), generateTimeUnit(timeUnit));
	    Task newEvent = new Task(name, start, end, null, null, null, false);
	    
	    dataBase.add(newEvent);
	    UIHandler.add(newEvent);
	}
	
	public static void addDeadline(String title, String endDate, String endTime) {
	    Name name = new Name(title);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    LocalDateTime end = LocalDateTime.parse(endDate + " " + endTime, formatter);
	    Task newEvent = new Task(name, null, end, null, null, null, false);
	    
	    dataBase.add(newEvent);
	    UIHandler.add(newEvent);
	}
	
	public static void addTask(String title) {
	    Name name = new Name(title);
	    Task newEvent = new Task(name, null, null, null, null, null, false);
	    
	    dataBase.add(newEvent);
	    UIHandler.add(newEvent);
	}
	
	public static void done(String title) {
	    
	}
	
	public static void edit(String title, String fieldName, String newValue) {
	    
	}
	
	public static void delete(String title) {
	    
	}
	
	public static void search(String title) {
	    
	}
	
	public static void filter(String view, String category) {
	    
	}
	
	public static void sort(String fieldName, String order) {
	    
	}
	
	public static void insert(String fieldName, String befaft, String title) {
	    
	}
	
	public static void switchPosition(String title1, String title2) {
	    
	}
	
	public static void label(String title, String Category) {
	    
	}
	
	public static void postpone(String title, String quantity, String timeUnit) {
	    
	}
	
	public static void forward(String title, String quantity, String timeUnit) {
	    
	}
	
	public static void addRemind(String[] args) {
	    
	}
	
	public static void addRemindBef(String[] args) {
	    
	}
	
	public static void remindBef(String title, String duration) {
	    
	}
	
	public static void exit() {
	    
	}
	
	
	
	public static void undo(int steps) {
	    
	}
	
	public static void redo(int steps) {
	    
	}
	
	public static void generateTimeUnit(String unit) {
	    switch(unit) {
	        case "day": return TimeUnit.DAYS;
	        case "hour": return TimeUnit.HOURS;
	        case "minute": return TimeUnit.MINUTES;
	    }
	}
	
	public static void execute(NormalCommand normalCommand) {
		// TODO Auto-generated method stub
		String action = normalCommand.getAction();
		String arg[] = normalCommand.getArgs();
		switch(action) {
		    case "add":
		        String type = arg[0];
		        switch(type) {
		            case "event": addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
		            case "deadline": addDeadline(arg[1], arg[2], arg[3]);
		            case "task": addTask(arg[1]);
		        }
		    case "edit": edit(arg[0], arg[1], arg[2]);
		    case "delete": delete(arg[0]);
		    case "search": search(arg[0]);
		    case "filter": filter(arg[0], arg[1]);
		    case "sort": sort(arg[0], arg[1]);
		    case "insert": insert(arg[0], arg[1], arg[2]);
		    case "switchPosition": switchPosition(arg[0], arg[1]);
		    case "label": label(arg[0], arg[1]);
		    case "postpone": postpone(arg[0], arg[1], arg[2]);
		    case "forward": forward(arg[0], arg[1], arg[2]);
		    case "add-remind": addRemind(arg);
		    case "remind": remind(arg);
		    case "add-remind-bef": addRemindBef(arg);
		    case "remind-bef": remindBef(arg[0], arg[1]);
		    case "exit": exit();
		    case "undo": undo(Integer.parseInt(arg[0]));
		    case "redo": redo(Integer.parseInt(arg[0]));
		}
	}
}

