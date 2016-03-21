package todolist.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class DatabaseModifier {
	public static String EXCEPEPTION_REPEATED_TASK = "The task has already existed!";
	public static String EXCEPTION_TASKNOTEXIST = "The task to delete does not exist!";
	
    private ArrayList<Task> taskList;
    
    public DatabaseModifier() {
    	taskList = new ArrayList<Task>();
    }
    
    
    /**
     * add a task to the arraylist after checking whether it is already existing
     * @param tasks    list of tasks to which the task to add
     * @param task     the task to be added
     * @return         the resultant task list
     * @throws IOException   if the task is already existing
     */
    public ArrayList<Task> addTask(ArrayList<Task> tasks, Task task) throws IOException {
    	this.taskList = tasks;
    	if(isExistingTask(task)) {
        	//dataBase_Logger.log(Level.INFO, LOGGING_REPEATED_TASK + task.getName().getName());
        	throw new IOException(EXCEPEPTION_REPEATED_TASK) ;
        }
        
        //dataBase_Logger.log(Level.INFO, LOGGING_ADDING_TASK + task.getName().getName());
    	taskList.add(0, task);
    	return taskList;
    }
    
    //helper method for add function
    private boolean isExistingTask(Task task) {
    	return taskList.contains(task);    	
    }
    
    /**
     * delete the specific task from the list of tasks
     * @param tasks            list of tasks from where the task to be deleted
     * @param taskToDelete     the task to be deleted
     * @return                 the list of 
     * @throws IOException     when the task to delete not in the task list
     */
    public ArrayList<Task> deleteTask(ArrayList<Task> tasks, Task taskToDelete) throws IOException {
    	taskList = tasks;
    	if (taskList.size() == 0) {
            //dataBase_Logger.log(Level.INFO, LOGGING_TASK_NOTEXIST + taskToDelete.getName().getName());
            throw new IOException(EXCEPTION_TASKNOTEXIST);
        }

        Integer index = searchForIndexOfTask(taskToDelete);

        if (index == null) {
            //dataBase_Logger.log(Level.INFO, LOGGING_TASK_NOTEXIST + taskToDelete.getName().getName());
            System.out.println("why not throwing");
        	throw new IOException(EXCEPTION_TASKNOTEXIST);
        }
        taskList.remove(taskList.get(index));
        
        return taskList;
    }

    // helper methods
    private Integer searchForIndexOfTask(Task taskToDelete) {
        for (int i = 0; i < taskList.size(); i++) {
            Task currentTask = taskList.get(i);

            if (currentTask.getName().getName().equals(taskToDelete.getName().getName())) {
                System.out.println(i);
                return i;
            }
        }
        return null;
    }
}
