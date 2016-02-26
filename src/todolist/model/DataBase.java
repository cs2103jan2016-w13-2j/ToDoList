package todolist.model;

import java.util.ArrayList;

/*
 * This class is the storage class, handling the read and write of local file with relative commands.
 * It will be called by the logic.
 * 
 * @author yuxin
 */
public class DataBase {
	private FileHandler fh;
	private ArrayList<Task> taskList;
	
	public DataBase() {
		fh = new FileHandler(); 
		loadFromFile();
	}
	
	private void loadFromFile() {
		taskList = fh.read();
		if(taskList == null) {
			taskList = new ArrayList<Task>();
		}		
	}

	/**
	 * This method handles the writing into the text file with the add command.
	 * It returns true if the task is successfully written into the file.
	 * 
	 * @param    Task       the task to be added
	 * @return   boolean    whether the task is successfully added
	 */
	public boolean add(Task task) {
		insertIntoTaskList(task);
		fh.write(taskList);
		return true;		
	}
	
	/**
	 * This method handles the updating of text file when the specified task is to be deleted.
	 * Returns true if teh task is successfully deleted. 
	 * 
	 * @param    Task    the task to be deleted
	 * @return   boolean  true if the task is successfully deleted;
	 *                     false if the task cannot be found
	 */
	public boolean delete(Task taskToDelete) {
		if(taskList.size() == 0) {
			return false;
		}
		Integer index = searchForIndexOfTask(taskToDelete);
		if(index == null) {
			return false;
		}
		taskList.remove(index);
		fh.write(taskList);
		return true;		
	}
	
	/**
	 * This method returns whether a task is in the text file.
	 * 
	 * @param   Task      task to search
	 * @return  boolean   returns true if the task is found; false if not.
	 */
	public boolean checkExistence(Task taskToCheck) {
		Integer index = searchForIndexOfTask(taskToCheck);
		if(index == null) {
			return false;
		}
		return true;		
	}
	
	/**
	 * This method is to find from the storage and return an ArrayList of the queried list of tasks.
	 * 
	 * @param command  the search command
	 * @return         an ArrayList<Task> containing the tasks that fulfill the requirement
	 */
	public Task retrieve(SearchCommand command) {
		Task task = null;
		
		return task;
	}
	
	/**
	 * This method is to set new file for the storage of data. 
	 * 
	 * @param newFilePath   the string that contains the new path and file name
	 * @return              true if the directory exists and the new file is set.
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
	
	//helper methods
	private void insertIntoTaskList(Task taskToInsert) {
		for(int i = 0; i < taskList.size(); i++) {//Task needs to have a compareTo method based on date and time
	        Task currentTask=taskList.get(i);
			if(currentTask.compareTo(taskToInsert) >= 0) {
				taskList.add(i,taskToInsert);
				return;
			}
		}
		taskList.add(taskToInsert);
	}
	
	private Integer searchForIndexOfTask(Task taskToDelete) {
	    for(int i = 0; i < taskList.size(); i++) {
	    	Task currentTask=taskList.get(i);
	    	if(currentTask.equals(taskToDelete)) {
	    		return i;
	    	}
	    }
		return null;
	}
}
