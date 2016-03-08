package todolist.model;

<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> v0.11
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * This class is the storage class, handling the read and write of local file with relative commands.
 * It will be called by the logic.
 * 
 * for the retrieve method, now can support 3 search command
 * 1:"category" + category
 * 2:"name" + keyword 
 * 3:"view"+view (overdue, archived)
 * 
 * for the sorting methods, can now sort by
 * 1:name
 * 2:end date
 * 3:start date
 * 4:category
 * but they are not integrated with the search method yet!!
 * 
 * 
 * @author yuxin
 */
public class DataBase {
	public static String VIEW_TODAY = "today";
	public static String VIEW_ARCHIVED = "archive";
	public static String VIEW_OVERDUE = "overdue";

	public static enum FilterType {
		VIEW, CATEGORY, NAME, END_DATE, START_DATE;
	}
	
	public static enum ViewType {
		ARCHIVE, OVERDUE, TODAY;
	}
	
	private FileHandler fh;
	private ArrayList<Task> taskList;
<<<<<<< HEAD
=======
	private ArrayList<ArrayList<Task>> snapshot;
>>>>>>> v0.11
    
	public DataBase() {
		fh = new FileHandler();
		loadFromFile();
	}
	
    //firstly convert every task in the arraylist to string, then call write function in filehandler
	private void writeToFile() {
		sort_StartDate(taskList);
		ArrayList<String> taskList_str = new ArrayList<String>();
		for(Task eachTask: taskList) {
			taskList_str.add(convert_TaskToString(eachTask));
		}
		fh.write(taskList_str);
	}
	//helper method
	private Task convert_StringToTask(String taskStr) {
		String[] taskInfo = taskStr.split(" ");
		Name name = new Name(taskInfo[0]);
<<<<<<< HEAD
		LocalDateTime startTime = LocalDateTime.parse(taskInfo[1]);
		LocalDateTime endTime = LocalDateTime.parse(taskInfo[2]);
		Category category = new Category(taskInfo[3]);
		Reminder reminder = new Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]), LocalDateTime.parse(taskInfo[4].split("+")[1]));
		Boolean isDone = Boolean.valueOf(taskInfo[6]);
=======
		LocalDateTime startTime = null;
		LocalDateTime endTime = null;
		Category category = null;
		Reminder reminder = null;
		Boolean isDone = null;
		
		if(!taskInfo[1].equals(new String("null"))) {
			startTime = LocalDateTime.parse(taskInfo[1]);
		}
		
		if(!taskInfo[2].equals(new String("null"))) {
			endTime = LocalDateTime.parse(taskInfo[2]);
		}

		if(!taskInfo[3].equals(new String("null"))) {
			category = new Category(taskInfo[3]);
		}
		
		if(!taskInfo[6].equals(new String("null"))) {
			isDone = Boolean.valueOf(taskInfo[6]);
		}
		
		if(!taskInfo[4].equals(new String("null"))) {
			if(!taskInfo[4].split("+")[1].equals("null")) {
				reminder = new Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]), null);
			} else {
				reminder = new Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]), LocalDateTime.parse(taskInfo[4].split("+")[1]));
			}
		}
		
		//Reminder reminder = new Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]), LocalDateTime.parse(taskInfo[4].split("+")[1]));

>>>>>>> v0.11
		return new Task(name, startTime, endTime, category, reminder, isDone);
	}
	//helper method
    private String convert_TaskToString(Task currentTask) {
    	String task_str = "";
    	task_str += currentTask.getName().getName() + " ";
<<<<<<< HEAD
    	task_str += currentTask.getStartTime().toString() + " ";
    	task_str += currentTask.getEndTime().toString() + " ";
    	task_str += currentTask.getCategory().getCategory() + " ";
    	task_str += currentTask.getReminder().getStatus().toString() + "+";
    	task_str += currentTask.getReminder().getTime().toString();
=======
    	
    	if(currentTask.getStartTime() == null) {
    		task_str += "null" + " ";
    	} else {
    		task_str += currentTask.getStartTime().toString() + " ";
    	}
    	
    	if(currentTask.getEndTime() == null) {
    		task_str += "null" + " ";
    	} else {
    		task_str += currentTask.getEndTime().toString() + " ";
    	}
    	
    	if(currentTask.getCategory() == null) {
    		task_str += "null" + " ";
    	} else {
    		task_str += currentTask.getCategory().toString() + " ";
    	}
    	
    	if(currentTask.getReminder() == null) {
    		task_str += "null" + "+";
    		task_str += "null";
    	} else {
    		if(currentTask.getReminder().getStatus() == null) {
    			task_str += "null" + "+";
    			if(currentTask.getReminder().getTime() == null) {
    				task_str += "null";
    			} else {
    				task_str += currentTask.getReminder().getTime().toString();
    			}
    		} else {
    			task_str += currentTask.getReminder().getStatus().toString() + "+";
    			if(currentTask.getReminder().getTime() == null) {
    				task_str += "null";
    			} else {
    				task_str += currentTask.getReminder().getTime().toString();
    			}
    		}
    	}
    	
    	//task_str += currentTask.getReminder().getStatus().toString() + "+";
    	//task_str += currentTask.getReminder().getTime().toString();
>>>>>>> v0.11
    	return task_str;
    }
    
    //firstly call read function in filehandler, then convert every string into a task object
	private void loadFromFile() {
		ArrayList<String> taskList_str = fh.read();
		taskList = new ArrayList<Task>();
		for(String eachTask_str: taskList_str) {
			taskList.add(convert_StringToTask(eachTask_str));		
		}
	}

	/**
	 * This method handles the writing into the text file with the add command.
	 * It returns true if the task is successfully written into the file.
	 * 
	 * @param Task
	 *            the task to be added
	 * @return boolean whether the task is successfully added
	 */
	public boolean add(Task task) {
		taskList.add(task);
		writeToFile();
<<<<<<< HEAD
=======
		snapshot.add(retrieveAll());
>>>>>>> v0.11
		return true;
	}

	/**
	 * This method handles the updating of text file when the specified task is
	 * to be deleted. Returns true if the task is successfully deleted.
	 * 
	 * @param Task
	 *            the task to be deleted
	 * @return boolean true if the task is successfully deleted; false if the
	 *         task cannot be found
	 */
	public boolean delete(Task taskToDelete) {
		if (taskList.size() == 0) {
			return false;
		}
		Integer index = searchForIndexOfTask(taskToDelete);
		if (index == null) {
			return false;
		}
		taskList.remove(index);
		writeToFile();
<<<<<<< HEAD
=======
		snapshot.add(retrieveAll());
>>>>>>> v0.11
		return true;
	}

	/**
	 * This method returns whether a task is in the text file.
	 * 
	 * @param Task
	 *            task to search
	 * @return boolean returns true if the task is found; false if not.
	 */
	public boolean checkExistence(Task taskToCheck) {
		Integer index = searchForIndexOfTask(taskToCheck);
		if (index == null) {
			return false;
		}
		return true;
	}
    
	//return all tasks
	public ArrayList<Task> retrieveAll() {
		return taskList;
	}
	
	//keep!!
	//retrieving
	public ArrayList<Task> retrieve(SearchCommand command) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		FilterType type = getFilterType(command);

		switch (type) {
			case CATEGORY :
				resultList = retrieve_Category(command);
				break;
			case NAME :
				resultList = retrieve_Name(command);
				break;
			case VIEW :
				resultList = retrieve_View(command);
				break;
		    default :
		    	return resultList;
		}
		return resultList;
	}
	
	private FilterType getFilterType(SearchCommand command) {
		String type = command.getType();
		if(isCategory(type)) {
			return FilterType.CATEGORY;
		}
		if(isView(type)) {
			return FilterType.VIEW;
		}
		if(isName(type)) {
			return FilterType.NAME;
		}
		return null;
	}

	private boolean isName(String type) {
		return type.equalsIgnoreCase("name");
	}
	private boolean isView(String type) {
		return type.equalsIgnoreCase("view");
	}
	private boolean isCategory(String type) {
		return type.equalsIgnoreCase("category");
	}

	private ArrayList<Task> retrieve_View(SearchCommand command) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		ViewType viewToFilter = determineViewType(command.getContent());
		switch(viewToFilter) {
		case OVERDUE :
			resultList = retrieve_ViewOverDue();
			break;
		case ARCHIVE :
			resultList = retrieve_ViewArchive();
			break;
		default :
			return resultList;
		}
		return resultList;
	}
    //helper method for retrieve_View
	private ArrayList<Task> retrieve_ViewArchive() {
		ArrayList<Task> resultList = new ArrayList<Task>();
		for(Task eachTask: taskList) {
			if(eachTask.getDoneStatus()) {
				resultList.add(eachTask);
			}
		}
		return resultList;
	}
    //helper method for retrieve_View
	private ArrayList<Task> retrieve_ViewOverDue() {
		ArrayList<Task> resultList = new ArrayList<Task>();
		for(Task eachTask: taskList) {
			if(isTaskOverdue(eachTask.getEndTime())) {
				resultList.add(eachTask);
			}
		}
		return null;
	}

	private boolean isTaskOverdue(LocalDateTime endTime) {
		if(endTime == null) {
			return false;
		}
		return endTime.isBefore(LocalDateTime.now());
	}

	private ViewType determineViewType(String content) {
		if(isOverdue(content)) {
			return ViewType.OVERDUE;
		}
		if(isArchive(content)) {
			return ViewType.ARCHIVE;
		}
	    return null;
	}

	private boolean isArchive(String content) {
		return content.equalsIgnoreCase("archive");
	}

	private boolean isOverdue(String content) {
		return content.equalsIgnoreCase("overdue");
	}
   
	private ArrayList<Task> retrieve_Name(SearchCommand command) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		String requiredName = command.getContent();
		for(Task eachTask: taskList) {
			if(isSame(eachTask.getName().getName(), requiredName)) {
				resultList.add(eachTask);
			}
		}
		return resultList;
<<<<<<< HEAD
	}

	private boolean isSame(String str1, String str2) {
		return str1.equalsIgnoreCase(str2);
	}

=======
	}

	private boolean isSame(String str1, String str2) {
		return str1.equalsIgnoreCase(str2);
	}

>>>>>>> v0.11
	private ArrayList<Task> retrieve_Category(SearchCommand command) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		String requiredCategory = command.getContent();
		for(Task eachTask: taskList) {
			if(isSame(eachTask.getCategory().getCategory(), requiredCategory)) {
				resultList.add(eachTask);
			}
		}
		return resultList;
	}
	
	//sorting
	//1.startDate
	private ArrayList<Task> sort_StartDate(ArrayList<Task> currentList) {
		Collections.sort(currentList, new StartDateComparator<Task>());
		return currentList;
	}
   
	private class StartDateComparator<Task> implements Comparator<Task> {
    	public int compare(Task t1, Task t2) {
    		LocalDateTime firstDate = ((todolist.model.Task) t1).getStartTime();
    		LocalDateTime secondDate = ((todolist.model.Task) t2).getStartTime();
    		if(firstDate == null && secondDate == null) {
    			String t1_Name = ((todolist.model.Task) t1).getName().getName();
    			String t2_Name = ((todolist.model.Task) t2).getName().getName();
    			return t1_Name.compareToIgnoreCase(t2_Name);
    		}else if(firstDate == null) {
    			return -1; 			
    		}else if(secondDate == null) {
    			return 1;
    		}else {
    			return firstDate.compareTo(secondDate);
    		}
    	}
    }
	
	//2.endDate
	private ArrayList<Task> sort_EndDate(ArrayList<Task> currentList) {
		Collections.sort(currentList, new EndDateComparator<Task>());
		return currentList;
	}
   
	private class EndDateComparator<Task> implements Comparator<Task> {
    	public int compare(Task t1, Task t2) {
    		LocalDateTime firstDate = ((todolist.model.Task) t1).getEndTime();
    		LocalDateTime secondDate = ((todolist.model.Task) t2).getEndTime();
    		if(firstDate == null && secondDate == null) {
    			String t1_Name = ((todolist.model.Task) t1).getName().getName();
    			String t2_Name = ((todolist.model.Task) t2).getName().getName();
    			return t1_Name.compareToIgnoreCase(t2_Name);
    		}else if(firstDate == null) {
    			return -1; 			
    		}else if(secondDate == null) {
    			return 1;
    		}else {
    			return firstDate.compareTo(secondDate);
    		}
    	}
    }
	
	//3.category
	private ArrayList<Task> sort_Category(ArrayList<Task> currentList) {
		Collections.sort(currentList, new CategoryComparator<Task>());
		return currentList;
	}
   
	private class CategoryComparator<Task> implements Comparator<Task> {
    	public int compare(Task t1, Task t2) {
    		String firstCategory = ((todolist.model.Task) t1).getCategory().getCategory();
    		String secondCategory = ((todolist.model.Task) t2).getCategory().getCategory();
    	    return firstCategory.compareToIgnoreCase(secondCategory);
    	}
    }
	//4.name
	private ArrayList<Task> sort_Name(ArrayList<Task> currentList) {
		Collections.sort(currentList, new NameComparator<Task>());
		return currentList;
	}
   
	private class NameComparator<Task> implements Comparator<Task> {
    	public int compare(Task t1, Task t2) {
    		String firstName = ((todolist.model.Task) t1).getName().getName();
    		String secondName = ((todolist.model.Task) t2).getName().getName();
    	    return firstName.compareToIgnoreCase(secondName);
    	}
    }
	
	/*
	 * This method is to set new file for the storage of data.
	 * 
	 * @param newFile
	 *            the string that contains the new path and file name
	 * @return true if the directory exists and the new file is set.
	 */
	public boolean setNewFile(String newFilePath) {
		boolean isSet = false;
		isSet = fh.setFile(newFilePath);
		this.loadFromFile();
		return isSet;
	}

	public String getPath() {
		return fh.getPath();
	}

	public String getFileName() {
		return fh.getFileName();
	}

	// helper methods	
	private Integer searchForIndexOfTask(Task taskToDelete) {
		for (int i = 0; i < taskList.size(); i++) {
			Task currentTask = taskList.get(i);
			if (currentTask.equals(taskToDelete)) {
				return i;
			}
		}
		return null;
	}

	public void retrieveHistory(int steps) {
		taskList = snapshot.get(steps);
		writeToFile();
	}
}