package todolist.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static String EXCEPTION_INVALID_PATH = "Invalid path!";
    public static String EXCEPEPTION_REPEATED_TASK = "The task has already existed!";
    public static String EXCEPTION_TASKNOTEXIST = "The task to delete does not exist!";
    public static String LOGGING_ADDING_TASK = "tring to add task: ";
    public static String LOGGING_DELETING_TASK = "tring to delete task: ";
    public static String LOGGING_REPEATED_TASK = "The task has already existed: ";
    public static String LOGGING_TASK_NOTEXIST = "The task to delete does not exist: ";
    public static String LOGGING_TASK_DELETED = "The task is deleted from database: ";
    
    private static Logger dataBase_Logger = Logger.getLogger("Database logger");
    
    public static enum FilterType {
        VIEW, CATEGORY, NAME, END_DATE, START_DATE;
    }

    public static enum ViewType {
        ARCHIVE, OVERDUE, TODAY;
    }

    private FileHandler fh;
    private ArrayList<Task> taskList;
    private ArrayList<ArrayList<Task>> snapshot;
    
    public DataBase() {
        taskList = null;
        fh = new FileHandler();
        loadFromFile();
        
    }

    // firstly convert every task in the arraylist to string, then call write
    // function in filehandler
    private void writeToFile() {
        sort_StartDate(taskList);
        ArrayList<String> taskList_str = new ArrayList<String>();
        for (Task eachTask : taskList) {
            taskList_str.add(convert_TaskToString(eachTask));
        }
        fh.write(taskList_str);
    }

    // helper method
    private Task convert_StringToTask(String taskStr) {
        String[] taskInfo = taskStr.split(" ");
        Name name = new Name(taskInfo[0]);
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Category category = null;
        Reminder reminder = null;
        Boolean isDone = null;

        if (!taskInfo[1].equals(new String("mynull"))) {
            startTime = LocalDateTime.parse(taskInfo[1]);
        }

        if (!taskInfo[2].equals(new String("mynull"))) {
            endTime = LocalDateTime.parse(taskInfo[2]);
        }

        if (!taskInfo[3].equals(new String("mynull"))) {
            category = new Category(taskInfo[3]);
        }

        if (!taskInfo[4].equals(new String("mynull"))) {
            isDone = Boolean.valueOf(taskInfo[4]);
        }

        if (!taskInfo[5].equals(new String("mynull+mynull"))) {
            if (!taskInfo[4].split("+")[1].equals("mynull")) {
                reminder = new Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]), null);
            } else {
                reminder = new Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]),
                        LocalDateTime.parse(taskInfo[4].split("+")[1]));
            }
        }

        // Reminder reminder = new
        // Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]),
        // LocalDateTime.parse(taskInfo[4].split("+")[1]));

        return new Task(name, startTime, endTime, category, reminder, isDone);
    }

    // helper method
    public String convert_TaskToString(Task currentTask) {
        String task_str = "";
        task_str += currentTask.getName().getName() + " ";

        if (currentTask.getStartTime() == null) {
            task_str += "mynull" + " ";
        } else {
            task_str += currentTask.getStartTime().toString() + " ";
        }

        if (currentTask.getEndTime() == null) {
            task_str += "mynull" + " ";
        } else {
            task_str += currentTask.getEndTime().toString() + " ";
        }

        if (currentTask.getCategory() == null) {
            task_str += "mynull" + " ";
        } else {
            task_str += currentTask.getCategory().toString() + " ";
        }

        if (currentTask.getDoneStatus() == null) {
            task_str += "mynull" + " ";
        } else {
            task_str += currentTask.getDoneStatus().toString() + " ";
        }

        if (currentTask.getReminder() == null) {
            task_str += "mynull" + "+";
            task_str += "mynull";
        } else {
            if (currentTask.getReminder().getStatus() == null) {
                task_str += "mynull" + "+";
                if (currentTask.getReminder().getTime() == null) {
                    task_str += "mynull";
                } else {
                    task_str += currentTask.getReminder().getTime().toString();
                }
            } else {
                task_str += currentTask.getReminder().getStatus().toString() + "+";
                if (currentTask.getReminder().getTime() == null) {
                    task_str += "mynull";
                } else {
                    task_str += currentTask.getReminder().getTime().toString();
                }
            }
        }

        // task_str += currentTask.getReminder().getStatus().toString() + "+";
        // task_str += currentTask.getReminder().getTime().toString();
        return task_str;
    }

    // firstly call read function in filehandler, then convert every string into
    // a task object
    private void loadFromFile() {
        ArrayList<String> taskList_str = fh.read();
        taskList = new ArrayList<Task>();
        for (String eachTask_str : taskList_str) {
            taskList.add(convert_StringToTask(eachTask_str));
        }
    }

    /**
     * This method handles the writing into the text file with the add command.
     * It returns true if the task is successfully written into the file.
     * 
     * @param Task
     *            the task to be added
     * @return  TRUE whether the task is successfully added\
     *   
     * @throws IOException  when task already exist
     */
    public boolean add(Task task) throws IOException {
        if(isExistingTask(task)) {
        	dataBase_Logger.log(Level.INFO, LOGGING_REPEATED_TASK + task.getName().getName());
        	throw new IOException(EXCEPEPTION_REPEATED_TASK) ;
        }
        
        dataBase_Logger.log(Level.INFO, LOGGING_ADDING_TASK + task.getName().getName());
    	taskList.add(0, task);
        writeToFile();       
        return true;
    }
    
    //helper method for add function
    private boolean isExistingTask(Task task) {
    	return taskList.contains(task);    	
    }
    
    /**
     * This method handles the updating of text file when the specified task is
     * to be deleted. Returns true if the task is successfully deleted.
     * 
     * @param Task
     *            the task to be deleted
     * @return boolean true if the task is successfully deleted; 
     *        
     * @throws IOException   if the task to delete does not exist
     */
    public boolean delete(Task taskToDelete) throws IOException {
        loadFromFile();
        
        dataBase_Logger.log(Level.INFO, LOGGING_DELETING_TASK + taskToDelete.getName().getName());
        
        if (taskList.size() == 0) {
            System.out.println(0);
            dataBase_Logger.log(Level.INFO, LOGGING_TASK_NOTEXIST + taskToDelete.getName().getName());
            throw new IOException(EXCEPTION_TASKNOTEXIST);
        }

        System.out.println(taskList.size());

        Integer index = searchForIndexOfTask(taskToDelete);

        System.out.println(index);

        if (index == null) {
            System.out.println("null");
            dataBase_Logger.log(Level.INFO, LOGGING_TASK_NOTEXIST + taskToDelete.getName().getName());
            throw new IOException(EXCEPTION_TASKNOTEXIST);
        }

        taskList.remove(taskList.get(index));
        dataBase_Logger.log(Level.INFO, LOGGING_TASK_DELETED + taskToDelete.getName().getName());
               
        System.out.println(taskList.size());

        writeToFile();
        // snapshot.add(retrieveAll());
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

    // return all tasks
    public ArrayList<Task> retrieveAll() {
        return taskList;
    }

    // keep!!
    // retrieving
    public ArrayList<Task> retrieve(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        FilterType type = getFilterType(command);

        switch (type) {
        case CATEGORY:
            resultList = retrieve_Category(command);
            break;
        case NAME:
            resultList = retrieve_Name(command);
            break;
        case VIEW:
            resultList = retrieve_View(command);
            break;
        default:
            return resultList;
        }
        return resultList;
    }

    private FilterType getFilterType(SearchCommand command) {
        String type = command.getType();
        if (isCategory(type)) {
            return FilterType.CATEGORY;
        }
        if (isView(type)) {
            return FilterType.VIEW;
        }
        if (isName(type)) {
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
        switch (viewToFilter) {
        case OVERDUE:
            resultList = retrieve_ViewOverDue();
            break;
        case ARCHIVE:
            resultList = retrieve_ViewArchive();
            break;
        default:
            return resultList;
        }
        return resultList;
    }

    // helper method for retrieve_View
    private ArrayList<Task> retrieve_ViewArchive() {
        ArrayList<Task> resultList = new ArrayList<Task>();
        for (Task eachTask : taskList) {
            if (eachTask.getDoneStatus()) {
                resultList.add(eachTask);
            }
        }
        return resultList;
    }

    // helper method for retrieve_View
    private ArrayList<Task> retrieve_ViewOverDue() {
        ArrayList<Task> resultList = new ArrayList<Task>();
        for (Task eachTask : taskList) {
            if (isTaskOverdue(eachTask.getEndTime())) {
                resultList.add(eachTask);
            }
        }
        return null;
    }

    private boolean isTaskOverdue(LocalDateTime endTime) {
        if (endTime == null) {
            return false;
        }
        return endTime.isBefore(LocalDateTime.now());
    }

    private ViewType determineViewType(String content) {
        if (isOverdue(content)) {
            return ViewType.OVERDUE;
        }
        if (isArchive(content)) {
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
        for (Task eachTask : taskList) {
            if (isSame(eachTask.getName().getName(), requiredName)) {
                resultList.add(eachTask);
            }
        }
        return resultList;
    }

    private boolean isSame(String str1, String str2) {
        return str1.equalsIgnoreCase(str2);
    }

    private ArrayList<Task> retrieve_Category(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        String requiredCategory = command.getContent();
        for (Task eachTask : taskList) {
            if (isSame(eachTask.getCategory().getCategory(), requiredCategory)) {
                resultList.add(eachTask);
            }
        }
        return resultList;
    }

    // sorting
    // 1.startDate
    private ArrayList<Task> sort_StartDate(ArrayList<Task> currentList) {
        Collections.sort(currentList, new StartDateComparator());
        return currentList;
    }

    private class StartDateComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            LocalDateTime firstDate = ((todolist.model.Task) t1).getStartTime();
            LocalDateTime secondDate = ((todolist.model.Task) t2).getStartTime();
            if (firstDate == null && secondDate == null) {
                String t1_Name = ((todolist.model.Task) t1).getName().getName();
                String t2_Name = ((todolist.model.Task) t2).getName().getName();
                return t1_Name.compareToIgnoreCase(t2_Name);
            } else if (firstDate == null) {
                return -1;
            } else if (secondDate == null) {
                return 1;
            } else {
                return firstDate.compareTo(secondDate);
            }
        }
    }

    // 2.endDate
    private ArrayList<Task> sort_EndDate(ArrayList<Task> currentList) {
        Collections.sort(currentList, new EndDateComparator());
        return currentList;
    }

    private class EndDateComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            LocalDateTime firstDate = ((todolist.model.Task) t1).getEndTime();
            LocalDateTime secondDate = ((todolist.model.Task) t2).getEndTime();
            if (firstDate == null && secondDate == null) {
                String t1_Name = ((todolist.model.Task) t1).getName().getName();
                String t2_Name = ((todolist.model.Task) t2).getName().getName();
                return t1_Name.compareToIgnoreCase(t2_Name);
            } else if (firstDate == null) {
                return -1;
            } else if (secondDate == null) {
                return 1;
            } else {
                return firstDate.compareTo(secondDate);
            }
        }
    }

    // 3.category
    private ArrayList<Task> sort_Category(ArrayList<Task> currentList) {
        Collections.sort(currentList, new CategoryComparator());
        return currentList;
    }

    private class CategoryComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            String firstCategory = ((todolist.model.Task) t1).getCategory().getCategory();
            String secondCategory = ((todolist.model.Task) t2).getCategory().getCategory();
            return firstCategory.compareToIgnoreCase(secondCategory);
        }
    }

    // 4.name
    private ArrayList<Task> sort_Name(ArrayList<Task> currentList) {
        Collections.sort(currentList, new NameComparator());
        return currentList;
    }

    private class NameComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            String firstName = ((todolist.model.Task) t1).getName().getName();
            String secondName = ((todolist.model.Task) t2).getName().getName();
            return firstName.compareToIgnoreCase(secondName);
        }
    }

    /*
     * This method is to set new file for the storage of data.
     * 
     * @param newFile the string that contains the new path and file name
     * 
     * @return true     if the directory exists and the new file is set.
     * @throw Exception if the path is invalid
     */
    public boolean setNewFile(String newFilePath) throws Exception {
        boolean isSet = false;
        isSet = fh.setFile(newFilePath);
        this.loadFromFile();
        if(!isSet) {
        	throw new Exception(EXCEPTION_INVALID_PATH);
        }
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
        System.out.println(convert_TaskToString(taskToDelete));
        for (int i = 0; i < taskList.size(); i++) {
            Task currentTask = taskList.get(i);
            System.out.println(convert_TaskToString(currentTask));

            if (currentTask.getName().getName().equals(taskToDelete.getName().getName())) {
                System.out.println(i);
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
