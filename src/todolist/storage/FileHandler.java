//@@author yuxin
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


//@@author A0131334W

/*
 * This class is to read and write from the file directly. It will be called by the database.
 * 
 * 
 *
 */
public class FileHandler {
	private static String PATH_UPDATEDDIRECTORY = "updatedDirectory.txt";
	private static String FILENAME = "taskStorage.txt";
	private static String FILEPATH = "taskStorage.txt";

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
		File path = new File(FILEPATH);

		if (isFileReady(path) && !isFileEmpty(path)) {
			try {
				FileReader fr = new FileReader(FILEPATH);
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
			FileWriter fw = new FileWriter(FILEPATH);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (Task eachTask : taskList) {
				bw.write(gson.toJson(eachTask) + "\n");
			}
			bw.close();

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
		//check whether the directory is valid
		if(!isPathCorrect(newFilePath)) {
			return false;
		}
		
		newFilePath = newFilePath + FILENAME;
		
		Path from = Paths.get(FILEPATH);
		Path to = Paths.get(newFilePath);
		CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES };

		try {
			Files.copy(from, to, options);
			
		} catch (IOException e1) {
			return false;
		}
		
        try {
			storeNewDirectory(newFilePath);
		} catch (IOException e) {
			return false;			
		}
        
        FILEPATH = newFilePath;
        
		return true;
	}

	private void storeNewDirectory(String newFilePath) throws IOException {

		FileWriter fw = new FileWriter(PATH_UPDATEDDIRECTORY);
		BufferedWriter bw = new BufferedWriter(fw);
		
		// store the new path in the local file
		bw.write(newFilePath + "\n");
		bw.close();
	}

	public boolean openFile(String newFilePath) {
		
		//check whether the directory exist		
		String tempFilePath = newFilePath + FILENAME;
		if(!isPathCorrect(tempFilePath)) {
			new File(newFilePath).mkdir();
			try {
				FileWriter fw = new FileWriter(tempFilePath);
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.close();
				
			} catch (Exception e) {
				return false;
			}
			
			openFile(newFilePath);
			return false;
		}
		
		//check whether the txt file contains the correct format (gson format)
		if(read() == null) {			
			return false;
		}
		
		FILEPATH = tempFilePath;
		return true;
	}

	public String getPath() {
		
		if(FILEPATH.equalsIgnoreCase("taskStorage.txt")) {
			
			Path currentRelativePath = Paths.get("");
			String currentDirectory = currentRelativePath.toAbsolutePath().toString();
			return currentDirectory + "/" + FILENAME;
			
		}else {			
			return FILEPATH;
		}
	}

	// helper methods
	private void checkForUpdatedDirectory() {
		File updatedDirectory = new File(PATH_UPDATEDDIRECTORY);

		try {
			if (isFileReady(updatedDirectory) && !isFileEmpty(updatedDirectory)) {
				FileReader fr = new FileReader(updatedDirectory);
				BufferedReader br = new BufferedReader(fr);
				FILEPATH = br.readLine();
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
