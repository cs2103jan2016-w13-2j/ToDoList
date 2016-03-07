package todolist.model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
    
	public FileHandler() {
		checkForUpdatedDirectory();
	}
    public static void main(String[] args){
    	FileHandler fh=new FileHandler();
    	System.out.println(fh.read());
    }
    
	/**
	 * This method reads from the local file. It returns a ArrayList containing all the Strings in the file.
	 * @return taskList    the list of string representation of tasks stored in file
	 *                      if no such file or the file is empty, return an empty arraylist.
	 */
	public ArrayList<String> read() {
		ArrayList<String> taskList = new ArrayList<String>();
		File filePath = new File(path + fileName);
		if(isFileReady(filePath) && !isFileEmpty(filePath)) {
			try {
				taskList = new ArrayList<String>();
				FileReader fr=new FileReader(path + fileName);
				BufferedReader br=new BufferedReader(fr);
				String input=null;
				input=br.readLine();
				
				while(!input.equals("null")){
					taskList.add(input);
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
	 * This method write directly the string representation of the task to the local file.
	 * 
	 * @param taskList    the list of string of tasks to be written
	 * @return            true if it is successfully written; false if the target file cannot be found
	 */
	public boolean write(ArrayList<String> taskList) {
		    try{
				FileWriter fw=new FileWriter(path + fileName);
				BufferedWriter bw=new BufferedWriter(fw);
				for(String eachTask: taskList) {
					bw.write(eachTask);
				}
				bw.close();
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
		ArrayList<String> existingTaskList = this.read();
		//set path
		String newPath = getPathOfNewFile(newFilePath);
		String newFileName = getNewFileName(newFilePath);
		
		if(isPathCorrect(newFilePath)) {
			this.path = newPath;
			this.fileName=newFileName;
			if(existingTaskList!=null) {
				this.write(existingTaskList);			
			}	
			
			try{
				FileWriter fw=new FileWriter(path + fileName);
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
