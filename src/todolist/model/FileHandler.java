package todolist.model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/*
 * This class is to read and write from the file directly. It will be called by the database.
 * 
 * 
 * @author Xyx
 *
 */
public class FileHandler {
	private String fileName = "taskStorage.xml";
    private String path = "";

	public FileHandler() {
	}

	/**
	 * This method reads from the local file. It returns a ArrayList containing all the objects in the file.
	 * @return taskList     the list of task objects stored in file
	 *                      if no objects in the file, return null.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Task> read() {
		ArrayList<Task> taskList;
		try{ 
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(fileName)));
			taskList = (ArrayList<Task>) decoder.readObject();
			decoder.close();
		} catch(Exception e) {
			return null;
		}
		return taskList;		
	}
    
	/**
	 * This method write directly the task list of the user to the local file.
	 * 
	 * @param taskList    the list of task objects to be written
	 * @return            true if it is successfully written; false if the target file cannot be found
	 */
	public boolean write(ArrayList<Task> taskList) {
		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
			encoder.writeObject(taskList);
			encoder.close();
		} catch(Exception e) {
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
	public boolean setPath(String path) {
		if(path.equals("")) {
			return true;
		}else if(isPathCorrect(path+"/")) {
			this.path=path+"/";
		}		
		return true;
	}
	
	/**
	 * The method is to set a new file to store the tasks in the defined path.
	 * 
	 * @param newFilePath   the path of the new file (the path + the file name); it is not null.
	 * @return              true if the file is set; false if the path is not a correct path
	 */
	public boolean setFile(String newFilePath) {
		ArrayList<Task> existingTaskList = this.read();
		//set path
		String newPath = getPathOfNewFile(newFilePath);
		if(isPathCorrect(newFilePath)) {
			this.path = newPath;
		}
		
		//set fileName
		String newFileName = getNewFileName(newFilePath);
		this.fileName=newFileName;
		
		if(!existingTaskList.equals(null)) {
			this.write(existingTaskList);			
		}		  	
		return true;
	}
	
	public String getPath() {
		File pathOfFile = new File(path);
		return pathOfFile.getAbsolutePath();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	//helper methods
	private boolean isPathCorrect(String pathName) {
		if(pathName.equals("")) {
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
		for(int i = 0; i < splitedPath.length-1; i++) {
			newPath = newPath + splitedPath[i] + "/";
		}
		return newPath;
	}
	
	private String getNewFileName(String newFilePath) {
		String[] splitedPath = getSplitedPath(newFilePath);
		if(splitedPath.length == 1) {
			return newFilePath;
		}
		return splitedPath[splitedPath.length-1] + ".xml";
	}
	
	private String[] getSplitedPath(String newFilePath) {
		return newFilePath.split("/");
	}
}
