package todolist.ui.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Scanner;

import org.controlsfx.control.Notifications;

import todolist.MainApp;
import todolist.common.UtilityLogger;
import todolist.common.UtilityLogger.Component;
import todolist.logic.UIHandler;
import todolist.model.Reminder;
import todolist.model.Task;
import todolist.ui.TaskListCell;
import todolist.ui.TaskWrapper;
import todolist.ui.TaskWrapper.TaskType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Callback;

//@@author A0123994W

/* 
 * MainViewController controls and manipulates data for display on the main display area, for the main tab.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class MainViewController {

    // Logger Messages
    private static final String MESSAGE_CALL_LOGIC_COMPONENT = "User input detected at UI component --calling--> Logic component for processing.";
    private static final String MESSAGE_CLEAR_TEXTFIELD = "Text field cleared for next input.";
    private static final String ERROR_PROCESSING_USER_INPUT = "Error processing user input.";
    private static final String MESSAGE_UPDATED_MAIN_TASKLIST = "Updated display task list [ALL].";
    private static final String MESSAGE_HIGHLIGHT_ITEM = "Item #%1$s in display task list highlighted.";
    private static final String MESSAGE_HIGHLIGHT_ITEM_NOT_FOUND = "Item to be highlighted cannot be found in display task list.";
    private static final String NIGHT_MODE = "night-mode";
    private static final String DAY_MODE = "day-mode";

    // Reminders
    protected static final String REMINDER_EVENT = "Remember to attend your event [%1$s] ! All the best!";
    protected static final String REMINDER_DEADLINE = "[%1$s] will be due soon! You can do it!";
    protected static final String REMINDER_OTHER = "Please be reminded to [%1$s] !";
    protected static final String REMINDER_HEADER = "ToDoList Reminder: ";
    private static final String DIRECTORY_REMINDER_SOUND = "ui/views/assets/notification-sound-ting.mp3";
    private static ArrayList<Timeline> reminders = null;

    // Model data
    protected ObservableList<TaskWrapper> tasksToDisplay = null;

    // Main application linkback
    protected MainApp mainApplication = null;
    private UIHandler uiHandler = null;

    /*** Views ***/
    @FXML
    protected ListView<TaskWrapper> listView = null;

    // Logger
    UtilityLogger logger = null;

    protected int index = 0;

    // @@author A0130620B
    // Temporary attributes for testing
    public String path = "demo.txt";
    public int demoCounter = 0;

    // @@author A0123994W
    /*** Controller Functions ***/

    /*
     * Constructor initializes to contain the display data structure.
     *
     */
    public MainViewController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
        logger = new UtilityLogger();
        reminders = new ArrayList<Timeline>();
    }

    /*
     * setMainApp takes a MainApp reference and stores it locally as the
     * mainApplication reference.
     * 
     * @param MainApp mainApp is the reference provided to the calling function
     * 
     */
    public void setMainApp(MainApp mainApp, UIHandler uiHandlerUnit) {
        setMainApplication(mainApp);
        setUiHandler(uiHandlerUnit);
    }

    /*
     * initialize prepares the task list.
     * 
     */
    @FXML
    public void initialize() {
        initTaskListView();
    }

    /*
     * initTaskListView initializes the task list by setting the list properties
     * and default format.
     * 
     */
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

    /*
     * setCommandLineCallback takes in a textfield and sets a function as the
     * action callback function.
     * 
     * @param TextField commandField is the command line that reads the user
     * input
     * 
     */
    public void setCommandLineCallback(TextField commandField, String dayMode, String nightMode) {
        // Set Callback for TextField
        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String commandString = commandField.getText();

                // Pass command line input for processing
                try {

                    commandField.clear();
                    logger.logAction(Component.UI, MESSAGE_CLEAR_TEXTFIELD);

                    getMainApplication().getCommandHistoryBackward().push(commandString);

                    if (commandString.equals(NIGHT_MODE)) {
                        Scene scene = getMainApplication().getPrimaryStage().getScene();
                        scene.getStylesheets().remove(dayMode);
                        scene.getStylesheets().add(nightMode);
                        getMainApplication().getPrimaryStage().setScene(scene);
                        getMainApplication().getPrimaryStage().show();

                    } else if (commandString.equals(DAY_MODE)) {
                        Scene scene = getMainApplication().getPrimaryStage().getScene();
                        scene.getStylesheets().remove(nightMode);
                        scene.getStylesheets().add(dayMode);
                        getMainApplication().getPrimaryStage().setScene(scene);
                        getMainApplication().getPrimaryStage().show();

                    } else {
                        getUiHandler().process(commandString);
                        logger.logComponentCall(Component.UI, MESSAGE_CALL_LOGIC_COMPONENT);
                    }

                } catch (Exception exception) {
                    logger.logError(Component.UI, ERROR_PROCESSING_USER_INPUT);
                    exception.printStackTrace();
                }
            }
        };

        commandField.setOnAction(commandHandler);
    }

    // @@author A0130620B
    /*** Temporary Functions for Testing ***/

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

    Boolean isDemoing = false;

    public void setCommandLineCallbackDemo(TextField commandField) {
        // Set Callback for TextField

        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!isDemoing) {
                    String commandString = commandField.getText();
                    // Command command = new Command(commandString);
                    // System.out.println(command.getCommand());

                    // Pass command line input for processing
                    try {

                        commandField.clear();
                        logger.logAction(Component.UI, MESSAGE_CLEAR_TEXTFIELD);
                        if (commandString.equals("Start demo")) {
                            isDemoing = true;
                        } else {
                            getUiHandler().process(commandString);
                            logger.logComponentCall(Component.UI, MESSAGE_CALL_LOGIC_COMPONENT);
                        }
                    } catch (Exception exception) {
                        logger.logError(Component.UI, ERROR_PROCESSING_USER_INPUT);
                        exception.printStackTrace();
                    }
                } else {
                    synchronized (this) {
                        ArrayList<String> demoList = demoFileHandler(path);
                        String commandString = demoList.get(demoCounter);
                        demoCounter++;
                        if (commandString.equals("exit")) {
                            isDemoing = false;
                        } else {
                            // System.out.println(event.getEventType());

                            // Pass command line input for processing
                            commandField.clear();

                            final Animation animation = new Transition() {
                                {
                                    setCycleDuration(new Duration(commandString.length() * 50));
                                }

                                protected void interpolate(double frac) {
                                    final int length = commandString.length();
                                    final int n = Math.round(length * (float) frac);
                                    commandField.setText(commandString.substring(0, n));
                                }

                            };

                            animation.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    getUiHandler().process(commandString);
                                }
                            });

                            animation.play();
                        }
                    }
                }
            }
        };

        commandField.setOnAction(commandHandler);
    }

    // @@author A0123994W
    /*** View Access Functions ***/

    /*
     * getTaskListView returns the current list view of tasks.
     * 
     * @return ListView<TaskWrapper> listView
     * 
     */
    public ListView<TaskWrapper> getTaskListView() {
        return listView;
    }

    /*** Model Access Functions ***/

    /*
     * getTasks returns the list of tasks stored.
     * 
     * @return ObservableList<TaskWrapper> tasks
     * 
     */
    public ObservableList<TaskWrapper> getTasks() {
        return tasksToDisplay;
    }

    /*
     * setTasks takes a list of tasks and sets it as the list of tasks to
     * display by associating with the list view. This overrides all the tasks
     * in the current list of display tasks.
     * 
     * @param ArrayList<Task> tasks is the list of tasks to replace the current
     * display list
     * 
     */
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

        logger.logAction(Component.UI, MESSAGE_UPDATED_MAIN_TASKLIST);
    }

    /*** Utility Functions ***/

    /*
     * highlight gets the index of the task to highlight and put that index item
     * on focus.
     * 
     * @param Task task is the task to be highlighted.
     */
    public void highlight(Task task) {
        int index = searchInList(task);

        // Only highlights if the task is within range and can be found in the
        // list
        if (index != -1) {
            listView.getSelectionModel().select(index);
            listView.getFocusModel().focus(index);
            listView.scrollTo(index);
            logger.logAction(Component.UI, String.format(MESSAGE_HIGHLIGHT_ITEM, Integer.toString(index)));
        }
    }

    /*
     * searchInList takes a task and searches for its position in the current
     * display list.
     * 
     * @param Task task is the task to locate
     * 
     * @return int searchIndex is the position of the task in the list of tasks
     * 
     */
    private int searchInList(Task task) {

        // Return the index if the task can be found
        for (int i = 0; i < listView.getItems().size(); ++i) {
            if (listView.getItems().get(i).getTaskObject().equals(task)) {
                return i;
            }
        }

        // Otherwise, denote as cannot be found by -1
        logger.logAction(Component.UI, MESSAGE_HIGHLIGHT_ITEM_NOT_FOUND);
        return -1;
    }

    /*
     * isCompleted checks if a given task has already been completed.
     * 
     * @param Task task is the given task to check for completion
     * 
     * @return boolean isCompleted
     * 
     */
    protected Boolean isCompleted(Task task) {
        return task.getDoneStatus();
    }

    /*
     * getTaskAt returns the task at the position specified in the list view, or
     * null if it is not found.
     * 
     * @param int pos
     * 
     * @return Task task
     * 
     */
    public Task getTaskAt(int pos) {
        ObservableList<TaskWrapper> itemList = listView.getItems();
        if (pos >= 1 && pos <= itemList.size()) {
            return itemList.get(pos - 1).getTaskObject();
        } else {
            return null;
        }
    }

    /*
     * refreshReminders updates all the reminders to be triggered
     */
    public void refreshReminders() {
        // Identify tasks with reminders switched on
        FilteredList<TaskWrapper> tasksToRemind = new FilteredList<TaskWrapper>(getTaskListView().getItems(),
                task -> task.getReminder() != null && !task.getIsCompleted() && task.getReminder().getStatus());

        // Stop all current reminders
        for (Timeline reminder : reminders) {
            reminder.stop();
        }

        // Clear current reminders
        reminders.clear();

        // Set current reminders
        for (TaskWrapper task : tasksToRemind) {
            Reminder taskReminder = task.getReminder();
            LocalDateTime remindTime = taskReminder.getTime();

            // Event without pre-defined reminder time
            if (remindTime == null) {
                remindTime = task.getStartTime();
            }
            // Deadline without pre-defined reminder time
            if (remindTime == null) {
                remindTime = task.getEndTime();
            }

            // Schedule reminder
            scheduleReminder(task, remindTime);
        }
    }

    /*
     * scheduleReminder sets the task reminder to trigger at the given timing.
     * 
     * @param TaskWrapper task, LocalDateTime remindTime
     *
     */
    static public void scheduleReminder(TaskWrapper task, LocalDateTime remindTime) {
        if (remindTime != null && remindTime.isAfter(LocalDateTime.now())) {
            long difference = LocalDateTime.now().until(remindTime, ChronoUnit.SECONDS);
            Timeline timer = new Timeline(new KeyFrame(Duration.seconds(difference), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    setReminderNotifPop(task);
                }

                private void setReminderNotifPop(TaskWrapper task) {

                    String output = null;
                    Notifications notification = Notifications.create();

                    // FORMAT HERE //
                    notification.title(REMINDER_HEADER + task.getTaskTitle());
                    if (task.getTaskType() == TaskType.EVENT) {
                        output = String.format(REMINDER_EVENT, task.getTaskTitle());
                    } else if (task.getTaskType() == TaskType.DEADLINE) {
                        output = String.format(REMINDER_DEADLINE, task.getTaskTitle());
                    } else {
                        output = String.format(REMINDER_OTHER, task.getTaskTitle());
                    }
                    notification.text(output);
                    notification.hideAfter(Duration.minutes(5));

                    AudioClip notificationSound = new AudioClip(
                            MainApp.class.getResource(DIRECTORY_REMINDER_SOUND).toExternalForm());
                    notificationSound.play();
                    notification.show();
                }
            }));
            timer.setCycleCount(1);
            timer.play();
            reminders.add(timer);
        }
    }

    public int getCompletedCount() {
        int count = 0;
        for (TaskWrapper task : listView.getItems()) {
            if (task.getIsCompleted()) {
                count += 1;
            }
        }

        return count;
    }

    public void setPageIndex(int tab) {
        index = tab;
    }

    public int getPageIndex() {
        return index;
    }

    public void setPlaceHolder(String glyphSource) {

        VBox box = null;
        try {
            box = (VBox) FXMLLoader.load(MainApp.class.getResource(glyphSource));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        box.visibleProperty().bind(Bindings.isEmpty(listView.getItems()));
        listView.setPlaceholder(box);

    }

    public MainApp getMainApplication() {
        return mainApplication;
    }

    public void setMainApplication(MainApp mainApplication) {
        this.mainApplication = mainApplication;
    }

    public UIHandler getUiHandler() {
        return uiHandler;
    }

    public void setUiHandler(UIHandler uiHandler) {
        this.uiHandler = uiHandler;
    }
}
