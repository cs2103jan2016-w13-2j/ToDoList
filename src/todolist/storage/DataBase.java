//@@author A0131334W
package todolist.storage;

import java.io.IOException;
import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import todolist.common.UtilityLogger;
import todolist.common.UtilityLogger.Component;
import todolist.model.SearchCommand;
import todolist.model.Task;


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
 */
public class DataBase {

	private static String MESSAGE_ADDING_TASK = "tring to add task: ";
	private static String MESSAGE_SUCCESSFULLY_ADD_TASK = "successfully add task: ";
	private static String MESSAGE_DELETING_TASK = "tring to delete task: ";
	private static String MESSAGE_SUCCESSFULLY_DELETE_TASK = "The task is deleted from database: ";
	private static String MESSAGE_RETRIEVE_TASK = "trying to retrieve: ";
	private static String MESSAGE_SETTING_NEW_PATH = "trying to set new path: ";
	private static String MESSAGE_SMART_SEARCH = "tring to smart search for task:  ";
	private static String MESSAGE_SUCCESSFULLY_SMART_SEARCH_TASK = "successfully smart search for task: ";
	private static String MESSAGE_NO_RESULT_SMART_SEARCH_TASK = "no result for smart search for task: ";
	private static String MESSAGE_SUCCESSFULLY_SET_PATH = "successfully set new path: ";
	private static String MESSAGE_FAIL_SET_PATH = "fail to set new path: ";
	private static String MESSAGE_OPEN_NEW_FILE = "trying to open a new file in new directory: ";
	private static String MESSAGE_SUCCESSFULLY_OPEN_NEW_FILE = "successfully open new file in new directory: ";
	private static String MESSAGE_FAIL_OPEN_NEW_FILE = "fail to open new file in the new directory: ";
	private static String MESSAGE_SORT = "trying to sort based on %1$ in %2s order";
	
	private static String ERROR_REPEATED_TASK = "The task has already existed: ";
	private static String ERROR_TASK_NOT_EXIST = "The task to delete does not exist: ";

	protected static Component COMPONENT_STORAGE = UtilityLogger.Component.Storage;

	private FileHandler fh;
	public ArrayList<Task> taskList;
	private TaskRetriever retriever;
	private TaskSorter sorter;
	private DatabaseModifier modifier;
	private UtilityLogger logger = null;

    public DataBase() {
		fh = new FileHandler();
		retriever = new TaskRetriever();
		sorter = new TaskSorter();
		modifier = new DatabaseModifier();
		logger = new UtilityLogger();

		loadFromFile();		
	}

	private void writeToFile() {
		fh.write(taskList);
	}

	public void clear() {
		taskList = new ArrayList<Task>();
		
		writeToFile();
	}

	public void loadFromFile() {
		taskList = fh.read();
	}

	/**
	 * This method handles the writing into the text file with the add command.
	 * It returns true if the task is successfully written into the file.
	 *
	 * @param Task
	 *            the task to be added
	 * @return TRUE whether the task is successfully added\
	 *
	 * @throws IOException
	 *             when task already exist
	 */
	public boolean add(Task task) {		
		assert (task != null);
		
		logger.logAction(COMPONENT_STORAGE, MESSAGE_ADDING_TASK + task.getName().getName());
		
		try {
			taskList = modifier.addTask(taskList, task);
		} catch (IOException e) {
			logger.logError(COMPONENT_STORAGE, ERROR_REPEATED_TASK + task.getName().getName());
			return false;
		}
		
		writeToFile();	
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_ADD_TASK + task.getName().getName());
		
		return true;
	}

	/**
	 * This method handles the updating of text file when the specified task is
	 * to be deleted. Returns true if the task is successfully deleted.
	 *
	 * @param Task
	 *            the task to be deleted
	 * @return boolean true if the task is successfully deleted; false if the
	 *         task to delete does not exist
	 */
	public boolean delete(Task taskToDelete) {
		
		if(taskToDelete == null) {
			return false;
		}
		
		loadFromFile();

		logger.logAction(COMPONENT_STORAGE, MESSAGE_DELETING_TASK + taskToDelete.getName().getName());

		try {
			taskList = modifier.deleteTask(taskList, taskToDelete);
		} catch (IOException e) {
			logger.logError(COMPONENT_STORAGE, ERROR_TASK_NOT_EXIST + taskToDelete.getName().getName());
			return false;
		}

		writeToFile();
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_DELETE_TASK + taskToDelete.getName().getName());
		
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
		return retriever.isTaskExisting(taskList, taskToCheck);
	}

	/**
	 * This method returns all the tasks in the database.
	 *
	 * @return an arraylist of all the tasks in database
	 */
	public ArrayList<Task> retrieveAll() {        
		return fh.read();
	}
	
	public Boolean recover(ArrayList<Task> backup) {
		fh.write(backup);
		
		loadFromFile();
		
		return true;
	}

	/**
	 * This method search for and then return tasks from the database according
	 * to the command.
	 * 
	 * @param command
	 *            command to search for the tasks in the database
	 * @return an arraylist of all the tasks in the search result; if the search
	 *         result is empty, returns an empty arraylist
	 */
	public ArrayList<Task> retrieve(SearchCommand command) {
		assert (command != null);
		
		logger.logAction(COMPONENT_STORAGE, MESSAGE_RETRIEVE_TASK + command.getType());
		
		return retriever.retrieveHandler(taskList, command);
	}

	/**
	 * search for tasks whose names containing the passed in string as a
	 * substring
	 * 
	 * @param command
	 * @return arraylist list of result tasks
	 */
	public ArrayList<Task> smartSearch(String[] keywords) {
		assert (keywords != null);
		
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SMART_SEARCH + keywords.toString());
		
		ArrayList<Task> resultList = retriever.smartRetrieve(taskList, keywords);
		
		if(resultList.isEmpty()) {
			logger.logAction(COMPONENT_STORAGE, MESSAGE_NO_RESULT_SMART_SEARCH_TASK + keywords.toString());
		}else {
			logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_SMART_SEARCH_TASK + keywords.toString());
		}
		
		return resultList;
	}

	/**
	 * This method is to set new file for the storage of data.
	 *
	 * @param newFile
	 *            the string that contains the new path and file name
	 *
	 * @return true if the directory exists and the new file is set.
	 * @throw Exception if the path is invalid
	 */
	public boolean setNewFile(String newFilePath) {
		assert (newFilePath != null);
		
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SETTING_NEW_PATH + newFilePath);
		
		boolean isSet = false;
		isSet = fh.setFile(newFilePath);

		if (!isSet) {
			
			logger.logAction(COMPONENT_STORAGE, MESSAGE_FAIL_SET_PATH + newFilePath);
			
			return false;
		}
		
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_SET_PATH + newFilePath);
		
		this.loadFromFile();
		
		return isSet;
	}
    
	/**
	 * open a new file for task storage in another directory
	 * @param newFilePath  the new directory to store the tasks
	 * @return  boolean   true if the a new file is created in the new directory;
	 */
	public boolean openNewFile(String newFilePath) {
		assert (newFilePath != null);
		
		logger.logAction(COMPONENT_STORAGE, MESSAGE_OPEN_NEW_FILE + newFilePath);
		
		boolean isOpen = false;
		isOpen = fh.openFile(newFilePath);
		
		if (!isOpen) {
			
			logger.logAction(COMPONENT_STORAGE, MESSAGE_FAIL_OPEN_NEW_FILE + newFilePath);
			return false;
		}
		
		logger.logAction(COMPONENT_STORAGE, MESSAGE_SUCCESSFULLY_OPEN_NEW_FILE + newFilePath);
		
		this.loadFromFile();
		
		return isOpen;
	}

	/**
	 * This method returns the path of the local text file that stores all the
	 * tasks.
	 *
	 * @return a string that represents the path
	 */
	public String getPath() {
		return fh.getPath();
	}


	/**
	 * Sort the tasks in the text file in the specific order of the specific
	 * field.
	 *
	 * @param fieldName
	 *            the name of the field to sort on
	 * @param order
	 *            the order of the task list after sorting, either in ascending
	 *            order or descending order
	 */
	public void sort(String fieldName, String order) {
		
		logger.logAction(COMPONENT_STORAGE, String.format(MESSAGE_SORT, fieldName, order));
		
		taskList = sorter.sortHandler(taskList, fieldName, order);
	}
}
