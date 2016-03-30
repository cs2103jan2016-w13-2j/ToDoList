package todolist.storage;


import java.io.BufferedReader;
import com.google.gson.Gson;

import todolist.model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//@@author yuxin
/*
 * This class is to read and write from the file directly. It will be called by the database.
 * 
 * 
 * @author Xyx
 *
 */
public class FileHandler {
	private static String PATH_UPDATEDDIRECTORY = "updatedDirectory.txt";
	private String fileName = "taskStorage.txt";
    private String path = "";
    private String filePath = "taskStorage.txt";
    
    private Gson gson = new Gson();
    
	public FileHandler() {
		checkForUpdatedDirectory();
	}
    
	/**
	 * This method reads from the local file. It returns a ArrayList containing all the Strings in the file.
	 * @return taskList    the list of tasks stored in file
	 *                      if no such file or the file is empty, return an empty arraylist.
	 */
	public ArrayList<Task> read() {
		ArrayList<Task> taskList = new ArrayList<Task>();
		File path = new File(filePath);
		
		if(isFileReady(path) && !isFileEmpty(path)) {
			try {
				System.out.println("kjkkkkkk" + filePath);
				FileReader fr=new FileReader(filePath);
				BufferedReader br=new BufferedReader(fr);
				String input=null;
				input=br.readLine();
				
				while(input != null && !input.equals("null")){
					taskList.add(gson.fromJson(input, Task.class));
					input=br.readLine();
				}
				br.close();
			}catch (FileNotFoundException e) {
				return null;
			}catch (IOException e) {
				return null;
			}
		}	
		return taskList;		
	}
    
	/**
	 * This method write directly the list of tasks to the local file.
	 * 
	 * @param taskList    the list of tasks to be written
	 * @return            true if it is successfully written; false if the target file cannot be found
	 */
	public boolean write(ArrayList<Task> taskList) {
		    try{
		    	System.out.println("whyyy" + filePath);
				FileWriter fw=new FileWriter(filePath);
				BufferedWriter bw=new BufferedWriter(fw);
				for(Task eachTask: taskList) {	
					System.out.println("filehandler writing into file: " +  gson.toJson(eachTask));
					bw.write(gson.toJson(eachTask) + "\n");
				}
				bw.close();
				System.out.println("filehandler writing into file: successfully ");
			}catch (Exception e) {
				return false;
			}			
		return true;	
	}
	
	/**
	 * The method is to set the path to store the storage file.
	 * 
	 * @param path  the path that is going to store the file.
	 * @return      true if the path exists and is able to store the file
	 */
	/*
	public boolean setPath(String path) {
		if(path.equals("")) {
			return true;
		}else if(isPathCorrect(path+"/")) {
			this.path=path+"/";
		}		
		return true;
	}
	*/
	/**
	 * The method is to set a new direction to store the tasks in the defined path.
	 * 
	 * @param newFilePath   the path of the new file (the path + the file name); it is not null.
	 * @return              true if the file is set; false if the path is not a correct path  
	 */
	public boolean setFile(String newFilePath) {
		ArrayList<Task> existingTaskList = this.read();
		File file = new File(newFilePath);
		  try {
	            newFilePath = file.getCanonicalPath();
	           System.out.println(newFilePath);
	        } catch (IOException e) {
	            return false;
	        }
		
		//set path 
		String newPath = getPathOfNewFile(newFilePath.trim());
		String newFileName = getNewFileName(newFilePath.trim());
		System.out.println("newPath:  " + newPath);
		System.out.println("newFileName: " + newFileName);
		if(true) {
			this.filePath = newFilePath + ".txt";
			this.path = newPath;
			this.fileName=newFileName + ".txt";
			if(existingTaskList.size() != 0) {
				this.write(existingTaskList);			
			}				
			try{
				FileWriter fw=new FileWriter(PATH_UPDATEDDIRECTORY);
				BufferedWriter bw=new BufferedWriter(fw);
				bw.write(path + "\n");
				bw.write(fileName + "\n");
				bw.close();
			}catch (Exception e) {
				return false;
			}
		    return true;
		}
		
	    return false;
		  	
		
	}
	
	public String getPath() {
		File pathOfFile = new File(path);
		return pathOfFile.getAbsolutePath();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	//helper methods
	private void checkForUpdatedDirectory() {
		File updatedDirectory = new File(PATH_UPDATEDDIRECTORY);	
		
		try {
			if(!isFileEmpty(updatedDirectory)) {
			    FileReader fr = new FileReader(updatedDirectory);
			    BufferedReader br = new BufferedReader(fr);		
			    path = br.readLine();
			    fileName = br.readLine();
			    br.close();
			}
		}catch (Exception e) {
			return;
		}
	}
	
	private boolean isFileReady(File filePath) {
		return filePath.exists();
	}
	
	private boolean isFileEmpty(File filePath) {
		if(isFileReady(filePath)) {
			return filePath.length() == 0;
		}
		return true;
	}
	
	private boolean isPathCorrect(String pathName) {
		if(pathName.length() == 0) {
			
			return true;
		}
		File pathToCheck = new File(pathName);
		if(pathToCheck.isDirectory()) {
			return true;
		}
		return false;
	}
	
	private String getPathOfNewFile(String newFilePath) {
		String[] splitedPath = getSplitedPath(newFilePath);
		String newPath = "";
		
		if(splitedPath.length == 1) {
			return newPath;
		}
		
		for(int i = 0; i < splitedPath.length-1; i++) {
			newPath = newPath + splitedPath[i] + "/";
		}
		return newPath.substring(0, newPath.length()-1);
	}
	
	private String getNewFileName(String newFilePath) {
		String[] splitedPath = getSplitedPath(newFilePath);
		if(splitedPath.length == 1) {
			return newFilePath;
		}
		return splitedPath[splitedPath.length-1];
	}
	
	private String[] getSplitedPath(String newFilePath) {
		return newFilePath.split("/");
	}
}
