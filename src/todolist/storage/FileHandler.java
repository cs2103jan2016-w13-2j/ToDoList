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
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
	private String fileName = "/taskStorage.txt";
	private String filePath = "taskStorage.txt";

	private Gson gson = new Gson();

	public FileHandler() {
		checkForUpdatedDirectory();
	}

	/**
	 * This method reads from the local file. It returns a ArrayList containing
	 * all the Strings in the file.
	 * 
	 * @return taskList the list of tasks stored in file if no such file or the
	 *         file is empty, return an empty arraylist.
	 */
	public ArrayList<Task> read() {
		ArrayList<Task> taskList = new ArrayList<Task>();
		File path = new File(filePath);
		System.out.println(filePath);
		if (isFileReady(path) && !isFileEmpty(path)) {
			try {
				// System.out.println("kjkkkkkk" + filePath);
				FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr);
				String input = null;
				input = br.readLine();

				while (input != null && !input.equals("null")) {
					taskList.add(gson.fromJson(input, Task.class));
					input = br.readLine();
				}
				br.close();
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return taskList;
	}

	/**
	 * This method write directly the list of tasks to the local file.
	 * 
	 * @param taskList
	 *            the list of tasks to be written
	 * @return true if it is successfully written; false if the target file
	 *         cannot be found
	 */
	public boolean write(ArrayList<Task> taskList) {
		try {
			FileWriter fw = new FileWriter(filePath);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Task eachTask : taskList) {
				System.out.println("filehandler writing into file: " + gson.toJson(eachTask));
				bw.write(gson.toJson(eachTask) + "\n");
			}
			bw.close();
			System.out.println("filehandler writing into file: successfully ");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * The method is to set a new direction to store the tasks in the defined
	 * path.
	 * 
	 * @param newFilePath
	 *            the path of the new file (the path + the file name); it is not
	 *            null.
	 * @return true if the file is set; false if the path is not a correct path
	 */
	public boolean setFile(String newFilePath) {
		if(!isPathCorrect(newFilePath)) {
			return false;
		}
		
		newFilePath = newFilePath + fileName;
		Path from = Paths.get(filePath);
		Path to = Paths.get(newFilePath);
		CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES };

		try {
			Files.copy(from, to, options);
			
		} catch (IOException e1) {
			return false;
		}

		filePath = newFilePath;
        
		//write the new file path into local txt file
		try {
			FileWriter fw = new FileWriter(PATH_UPDATEDDIRECTORY);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(filePath + "\n");// store the new path in the local file
			bw.close();
		} catch (Exception e) {
			return false;
		}

		/*
		 * ArrayList<Task> existingTaskList = this.read(); File file = new
		 * File(newFilePath + ".txt"); //try { newFilePath =
		 * file.getAbsolutePath(); System.out.println("jsfdsfjlsdf" +
		 * newFilePath); // } catch (IOException e) { // return false; // }
		 * //newFilePath = file.getPath(); //System.out.println(newFilePath);
		 * //set path String newPath = getPathOfNewFile(newFilePath.trim()+"/");
		 * String newFileName = getNewFileName(newFilePath.trim()); file = new
		 * File(newPath); System.out.println("storageeeeeeee : " +
		 * file.isDirectory()); if(true) { this.filePath = newFilePath;
		 * this.path = newPath; this.fileName=newFileName + ".txt";
		 * if(existingTaskList.size() != 0) { this.write(existingTaskList); }
		 * 
		 * return true; } System.out.println("not directory"); return false;
		 */
		return true;
	}

	public boolean openFile(String newFilePath) {
		//check whether the directory exist		
		String tempFilePath = newFilePath + fileName;
		if(!isPathCorrect(tempFilePath)) {
			return false;
		}
		//check whether the txt file contains the correct format (gson format)
		if(read() == null) {			
			return false;
		}
		
		filePath = tempFilePath;
		return true;
	}

	public String getPath() {
		return filePath;
	}

	// helper methods
	private void checkForUpdatedDirectory() {
		File updatedDirectory = new File(PATH_UPDATEDDIRECTORY);

		try {
			if (!isFileEmpty(updatedDirectory)) {
				FileReader fr = new FileReader(updatedDirectory);
				BufferedReader br = new BufferedReader(fr);
				filePath = br.readLine();
				br.close();
			}
		} catch (Exception e) {
			return;
		}
	}

	private boolean isFileReady(File filePath) {
		return filePath.exists();
	}

	private boolean isFileEmpty(File filePath) {
		if (isFileReady(filePath)) {
			return filePath.length() == 0;
		}
		return true;
	}

    private boolean isPathCorrect(String pathName) {
		if (pathName.length() == 0) {

			return true;
		}
		File pathToCheck = new File(pathName);
		if (pathToCheck.isDirectory() || pathToCheck.isFile()) {
			return true;
		}
		return false;
	}




}
