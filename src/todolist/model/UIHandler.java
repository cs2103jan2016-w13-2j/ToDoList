package todolist.model;

import java.util.ArrayList;

import todolist.MainApp;

public class UIHandler {

    private DataBase dataBase;
    private MainApp mainApp;

    public UIHandler(DataBase dataBase, MainApp mainApp) {
        this.dataBase = dataBase;
        this.mainApp = mainApp;

    }

    public void refresh() {
        mainApp.setDisplayTasks(dataBase.retrieveAll());
    }

    public void sendMessage(String message) {
        // mainApp.messageBox(message);
    }

    public void highLight(Task task) {
        // mainApp.highLight(task);
    }

    public void search(String title) {

        ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("NAME", title));
        mainApp.setDisplayTasks(tempTaskList);
        // mainApp.highLight(tempTaskList);

    }

	public void filter(String category) {
		ArrayList<Task> tempTaskList = dataBase.retrieve(new SearchCommand("Category", category));
		mainApp.setDisplayTasks(tempTaskList);
	}

    public void exit() {
        System.exit(0);
    }
}