package todolist.model;

import java.util.ArrayList;

import todolist.MainApp;

public class UIHandler {

	private DataBase dataBase;

	// private Boolean isSorted = false;
	// private Boolean isFiltered = false;

	// private String fieldName = null;
	// private String category = null;
	// private String order = null;

	private MainApp mainApp;

	public UIHandler(DataBase dataBase, MainApp mainApp) {
		this.dataBase = dataBase;
		this.mainApp = mainApp;

	}

	public void refresh() {
		// if (isSorted && !isFiltered) {
		//// this.sort(fieldName, order);
		// }
		//
		// if (!isSorted && isFiltered) {
		//// this.filter(category);
		// }
		//
		// if (isFiltered && isSorted) {
		//
		// ArrayList<Task> tempTaskList = dataBase.retrieve(new
		// SearchCommand("Category", category));
		// // mainApp.setDisplayTasks(Sorter.sort(tempTaskList));
		// mainApp.setDisplayTasks(tempTaskList);
		// }
		//
		// if (!isSorted && !isFiltered) {
		// mainApp.setDisplayTasks(dataBase.retrieveAll());
		//
		// }
		mainApp.setDisplayTasks(dataBase.retrieveAll());
	}

	public void sendMessage(String message) {
		// mainApp.messageBox(message);
	}

	public void highLight(Task task) {
		// mainApp.highLight(task);
	}

	public void search(String title) {

		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("Name", title));
		mainApp.setDisplayTasks(tempTaskList);
		// mainApp.highLight(tempTaskList);

	}

	// public void sort(String fieldName, String order) {
	// if (isFiltered) {
	// this.sort = sort;
	//
	// ArrayList<Task> tempTaskList = dataBase.retrieve(new
	// SearchCommand("Category", category));
	// // mainApp.setDisplayTasks(Sorter.sort(tempTaskList));
	// mainApp.setDisplayTasks(tempTaskList);
	//
	// isSorted = true;
	// } else {
	// this.sort = sort;
	// mainApp.setDisplayTasks(Sorter.sort(dataBase.retrieveAll()));
	//
	// isSorted = true;
	// }
	// }
	//
	// public void filter(String category) {
	// if(isSorted) {
	// this.category = category;
	//
	// ArrayList<Task> tempTaskList = new Task(dataBase.retreive(new
	// SearchCommand("Category", category));
	// mainApp.setDisplayTasks(Sorter.sort(tempTaskList));
	// isFiltered = true;
	// } else {
	// this.category = category;
	// ArrayList<Task> tempTaskList = new Task(dataBase.retreive(new
	// SearchCommand("Category", category));
	// mainApp.setDisplayTasks(tempTaskList);
	// isFiltered = true;
	// }
	// }

	public void exit() {
		System.exit(0);
	}
}