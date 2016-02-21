package todolist.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
	private String fileName;

	public FileHandler(String fileName) {
		this.fileName = fileName;
	}

	public String read() throws IOException {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		String content = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		bufferedReader.close();
		return content;
	}

	public boolean write(String content) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(content);
		bufferedWriter.newLine();
		bufferedWriter.close();
		return false;
	}
}
