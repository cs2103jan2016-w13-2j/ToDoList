//@@author A0130620B
package todolist.logic;

import java.util.ArrayList;

import todolist.MainApp;
import todolist.model.Task;
import todolist.storage.DataBase;

public class UIHandler {

	private DataBase dataBase;
	private MainApp mainApp;
	private Logic logic;

	public UIHandler(DataBase dataBase, MainApp mainApp, Logic logic) {
		this.dataBase = dataBase;
		this.mainApp = mainApp;
		this.logic = logic;
	}

	public void process(String input) {
		logic.process(input);
	}

	public String getPath() {
		return dataBase.getPath();
	}

	public void refresh() {
		mainApp.setDisplayTasks(dataBase.retrieveAll());
	}
	
	public ArrayList<Task> getAllTask() {
		return dataBase.retrieveAll();
	}

	protected void sendMessage(String message, boolean autohide) {
		mainApp.notifyWithText(message, autohide);
	}

	protected void highLight(Task task) {
		mainApp.highlightItem(task);
	}

	protected void display(ArrayList<Task> taskList) {
		mainApp.setDisplayTasks(taskList);
	}

	protected void tab(int index) {
		mainApp.loadPage(index);
	}
}