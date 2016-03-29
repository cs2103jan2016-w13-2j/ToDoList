package todolist.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import todolist.model.SearchCommand;
import todolist.model.Task;
//@@author yuxin
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

	public static String LOGGING_ADDING_TASK = "tring to add task: ";
	public static String LOGGING_DELETING_TASK = "tring to delete task: ";
	public static String LOGGING_RETRIEVE_TASK = "trying to retrieve: ";
	public static String LOGGING_REPEATED_TASK = "The task has already existed: ";
	public static String LOGGING_TASK_NOTEXIST = "The task to delete does not exist: ";
	public static String LOGGING_TASK_DELETED = "The task is deleted from database: ";

	private static Logger dataBase_Logger = Logger.getLogger("Database logger");

	private FileHandler fh;
	public ArrayList<Task> taskList;
	private ArrayList<ArrayList<Task>> snapshot;
	private TaskRetriever retriever;
	private TaskSorter sorter;
	private DatabaseModifier modifier;

	public DataBase() {
		// taskList = null;
		fh = new FileHandler();
		retriever = new TaskRetriever();
		sorter = new TaskSorter();
		modifier = new DatabaseModifier();
		loadFromFile();
		snapshot = new ArrayList<ArrayList<Task>>();

	}

	private void writeToFile() {
		fh.write(taskList);
	}

	public void clear() {
		taskList = new ArrayList<Task>();
		writeToFile();
	}

	// helper method
	/*
	 * public Task convert_StringToTask(String taskStr) { String[] taskInfo =
	 * taskStr.split(" "); Name name = new Name(taskInfo[0]); LocalDateTime
	 * startTime = null; LocalDateTime endTime = null; Category category = null;
	 * Reminder reminder = null; Boolean isDone = null;
	 * 
	 * if (!taskInfo[1].equals(new String("mynull"))) { startTime =
	 * LocalDateTime.parse(taskInfo[1]); }
	 * 
	 * if (!taskInfo[2].equals(new String("mynull"))) { endTime =
	 * LocalDateTime.parse(taskInfo[2]); }
	 * 
	 * if (!taskInfo[3].equals(new String("mynull"))) { category = new
	 * Category(taskInfo[3]); }
	 * 
	 * if (!taskInfo[4].equals(new String("mynull"))) { isDone =
	 * Boolean.valueOf(taskInfo[4]); }
	 * 
	 * if (!taskInfo[5].equals(new String("mynull+mynull"))) { if
	 * (!taskInfo[4].split("+")[1].equals("mynull")) { reminder = new
	 * Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]), null); } else {
	 * reminder = new Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]),
	 * LocalDateTime.parse(taskInfo[4].split("+")[1])); } }
	 * 
	 * // Reminder reminder = new //
	 * Reminder(Boolean.valueOf(taskInfo[4].split("+")[0]), //
	 * LocalDateTime.parse(taskInfo[4].split("+")[1]));
	 * 
	 * return new Task(name, startTime, endTime, category, reminder, isDone); }
	 */
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
			task_str += currentTask.getCategory().getCategory() + " ";
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

		return task_str;
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
		try {
			taskList = modifier.addTask(taskList, task);
		} catch (IOException e) {
			return false;
		}
		ArrayList<Task> temp = fh.read();
		snapshot.add(temp);
		writeToFile();
		return true;
	}

	/**
	 * This method handles the updating of text file when the specified task is
	 * to be deleted. Returns true if the task is successfully deleted.
	 * 
	 * @param Task
	 *            the task to be deleted
	 * @return boolean true if the task is successfully deleted; false if the task to delete does not exist
	 */
	public boolean delete(Task taskToDelete) {
		assert (taskToDelete instanceof Task);
		loadFromFile();

		dataBase_Logger.log(Level.INFO, LOGGING_DELETING_TASK + taskToDelete.getName().getName());
		try {
			taskList = modifier.deleteTask(taskList, taskToDelete);
		} catch (IOException e) {
			return false;
		}

		dataBase_Logger.log(Level.INFO, LOGGING_TASK_DELETED + taskToDelete.getName().getName());
		ArrayList<Task> temp = fh.read();
		snapshot.add(temp);
		writeToFile();
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
		return taskList;
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
		assert (command instanceof SearchCommand);
		return retriever.retrieveHandler(taskList, command);
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
		boolean isSet = false;
		isSet = fh.setFile(newFilePath);

		if (!isSet) {
			return false;
		}
		this.loadFromFile();
		return isSet;
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
	 * This method returns the name of the local text file that stores all the
	 * tasks.
	 * 
	 * @return the file name in string representation
	 */
	public String getFileName() {
		return fh.getFileName();
	}

	/**
	 * Goes back to a number of steps ago according to the number of steps pass
	 * in.
	 * 
	 * @param steps
	 *            the number of steps to go back
	 */
	public Boolean retrieveHistory(int steps) {
		taskList = snapshot.get(steps);
		writeToFile();
		return true;
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
		taskList = sorter.sortHandler(taskList, fieldName, order);
		ArrayList<Task> temp = fh.read();
		snapshot.add(temp);
	}
}
