package todolist.logic;

import java.util.ArrayList;

import todolist.MainApp;
import todolist.model.SearchCommand;
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

    public void refresh() {
        mainApp.setDisplayTasks(dataBase.retrieveAll());
    }

    public void sendMessage(String message) {
        mainApp.notifyWithText(message);
    }

    public void highLight(Task task) {
        // mainApp.highLight(task);
    }
    
    public void display(ArrayList<Task> taskList) {
    	mainApp.setDisplayTasks(taskList);
    }
}