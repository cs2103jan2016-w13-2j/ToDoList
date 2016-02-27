
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
		if (taskList == null) {
			taskList = new ArrayList<Task>();
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
		fh.write(taskList);
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
		fh.write(taskList);
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
    
	public ArrayList<Task> retrieveAll() {
		return taskList;
	}
	
	public ArrayList<Task> retrieve(SearchCommand command) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		switch (command.getType()) {
			case "category" :
				resultList = searchByCategory(command.getContent());
				break;
			case "name" :
				resultList = searchByName(command.getContent());
				break;
		}
		return resultList;
	}
	
	private ArrayList<Task> searchByName(String content) {
		ArrayList<Task> result = new ArrayList<Task>();
		for(Task eachTask: taskList) {
			if(eachTask.getName().compareTo(new Name(content)) == 0) {
				result.add(eachTask);
			}
		}
		return result;
	}

	private ArrayList<Task> searchByCategory(String content) {
		ArrayList<Task> result = new ArrayList<Task>();
		for(Task eachTask: taskList) {
			if(eachTask.getCategory().compareTo(new Category(content)) == 0) {
				result.add(eachTask);
			}
		}
		return result;
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

}