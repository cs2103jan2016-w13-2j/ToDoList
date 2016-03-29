package todolist.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import todolist.MainApp;
import todolist.model.Task;
import todolist.ui.TaskWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

//@@author Huang Lie Jun

/* 
 * MainViewController
 */
public class MainViewController {

	/*** MODEL DATA ***/
	protected ObservableList<TaskWrapper> tasksToDisplay = null;

	/*** MAIN APP ***/
	private MainApp mainApplication = null;

	/*** VIEWS ***/
	@FXML
	protected ListView<TaskWrapper> listView = null;

	/*** CORE CONTROLLER FUNCTIONS ***/

	public MainViewController() {
		// Initialise models
		tasksToDisplay = FXCollections.observableArrayList();
		listView = new ListView<TaskWrapper>();
	}

	public void setMainApp(MainApp mainApp) {
		mainApplication = mainApp;
	}

	@FXML
	public void initialize() {
		initTaskListView();
	}

	public void initTaskListView() {
		listView.setCellFactory(new Callback<ListView<TaskWrapper>, javafx.scene.control.ListCell<TaskWrapper>>() {
			@Override
			public ListCell<TaskWrapper> call(ListView<TaskWrapper> listView) {
				return new TaskListCell();
			}
		});

		VBox.setVgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(listView, Priority.ALWAYS);

	}

	public void setCommandLineCallback(TextField commandField) {
		// Set Callback for TextField
		EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String commandString = commandField.getText();
				// Command command = new Command(commandString);
				// System.out.println(command.getCommand());

				// Pass Command Line input for processing
				try {
					commandField.clear();
					mainApplication.uiHandlerUnit.process(commandString);
				} catch (Exception e) {

				}

			}
		};

		commandField.setOnAction(commandHandler);
	}
	
	
	//temp code for demo and testing purpose
	
	//@@author zhangjiyi
	public String path = "demo.txt";
	
	//@@author zhangjiyi
	public int demoCounter = 0;
	
	//@@author zhangjiyi
	public ArrayList<String> demoFileHandler(String path) {
		ArrayList<String> myList = new ArrayList<String>();
		try {

			File file = new File(path);
			Scanner scr = new Scanner(file);
			while (scr.hasNextLine()) {
				String temp = scr.nextLine();
				myList.add(temp);
				System.out.println(temp);
			}
			scr.close();
		} catch (Exception e) {

		}
		return myList;
	}

	//@@author zhangjiyi
	public void setCommandLineCallbackDemo(TextField commandField) {
		// Set Callback for TextField
		EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ArrayList<String> demoList = demoFileHandler(path);
				String commandString = demoList.get(demoCounter);
				demoCounter++;
				// Command command = new Command(commandString);
				// System.out.println(command.getCommand());

				// Pass Command Line input for processing
				try {
					commandField.clear();
					commandField.setText(commandString);
					mainApplication.uiHandlerUnit.process(commandString);
				} catch (Exception e) {

				}
			}
		};

		commandField.setOnAction(commandHandler);
	}

	/*** VIEW GETTERS-SETTERS-LOADERS ***/

	public ListView<TaskWrapper> getTaskListView() {
		return listView;
	}

	/*** MODEL GETTERS-SETTERS-RELOADERS ***/

	public ObservableList<TaskWrapper> getTasks() {
		return tasksToDisplay;
	}

	public void setTasks(ArrayList<Task> tasks) {

		// List provided by logic must be valid
		assert (tasks != null);

		ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
		listView.getItems().clear();

		// Convert Task to TaskWrapper for display handling
		for (int i = 0; i < tasks.size(); ++i) {
			if (!tasks.get(i).getDoneStatus()) {
				TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
				arrayOfWrappers.add(wrappedTask);
			}
		}

		listView.getItems().addAll(arrayOfWrappers);
	}

	public void highLight(Task task) {
		// TODO Auto-generated method stub
		int index = searchInList(task);

		if (index != -1) {
			listView.getSelectionModel().select(index);
			listView.getFocusModel().focus(index);
			listView.scrollTo(index);
		}
	}

	private int searchInList(Task task) {

		for (int i = 0; i < listView.getItems().size(); ++i) {
			if (listView.getItems().get(i).getTaskObject().equals(task)) {
				return i;
			}
		}
		return -1;
	}

}
