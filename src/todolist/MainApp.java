package todolist;

import java.io.IOException;
import java.util.ArrayList;

import todolist.common.UtilityLogger;
import todolist.common.UtilityLogger.Component;
import todolist.logic.Logic;
import todolist.logic.UIHandler;
import todolist.model.Task;
import todolist.ui.TaskWrapper;
import todolist.ui.controllers.ArchiveController;
import todolist.ui.controllers.MainViewController;
import todolist.ui.controllers.OverdueController;
import todolist.ui.controllers.SideBarController;
import todolist.ui.controllers.TodayController;
import todolist.ui.controllers.WeekController;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.controlsfx.control.NotificationPane;

//@@author huangliejun

/*
 * MainApp is the main running class for the application.
 * It provides the user with the graphical user interface to control the application.
 * 
 * @author Huang Lie Jun (A0123994W)
 * 
 */
public class MainApp extends Application {

    // Window constants
    private static final double MIN_HEIGHT = 600;
    private static final double MIN_WIDTH = 400;
    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 800;
    private static final String WINDOW_TITLE = "ToDoList by [w13-2j]";

    // Error messages for loading views
    private static final String MESSAGE_ERROR_LOAD_ROOT = "Error loading root view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_MAIN = "Error loading main view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_TITLEBAR = "Error loading title bar view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_SIDEBAR = "Error loading side bar view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_OVERDUE = "Error loading overdue view.";
    private static final String MESSAGE_ERROR_LOAD_TODAY = "Error loading today view.";
    private static final String MESSAGE_ERROR_LOAD_WEEK = "Error loading week view.";
    private static final String MESSAGE_ERROR_LOAD_ARCHIVE = "Error loading archive view.";
    private static final String MESSAGE_ERROR_LOAD_SETTINGS = "Error loading settings view.";
    private static final String MESSAGE_ERROR_LOAD_HELP = "Error loading help view.";
    private static final String MESSAGE_ERROR_PAGE_INDEX = "Page index is out of bounds @ #";

    // Action messages
    private static final String ACTION_NOTIFICATION_TRIGGERED = "Notification triggered";

    // Notification messages and delay constant
    private static final String NOTIFICATION_WELCOME = "Welcome to ToDoList! Let's get started...";
    private static final int DELAY_PERIOD = 5;

    // Root view directories
    private static final String DIRECTORY_ROOT = "ui/views/RootLayout.fxml";
    private static final String DIRECTORY_TITLEBAR = "ui/views/TitleBarView.fxml";
    private static final String DIRECTORY_SIDEBAR = "ui/views/SideBarView.fxml";
    public static final String DIRECTORY_TASKITEM = "ui/views/TaskNode.fxml";

    // Class styles
    private static final String STYLE_CLASS_ROOT = "root-layout";
    private static final String STYLE_CLASS_TITLEBAR = "title-bar";
    private static final String STYLE_CLASS_SIDEBAR = "side-bar";
    private static final String STYLE_NOTIFICATION = "-fx-font-size: 10px;";

    // Tab view directories
    private static final String DIRECTORY_MAIN = "ui/views/MainView.fxml";
    private static final String DIRECTORY_OVERDUE = "ui/views/OverdueView.fxml";
    private static final String DIRECTORY_TODAY = "ui/views/TodayView.fxml";
    private static final String DIRECTORY_WEEK = "ui/views/WeekView.fxml";
    private static final String DIRECTORY_ARCHIVE = "ui/views/ArchiveView.fxml";
    private static final String DIRECTORY_SETTINGS = "ui/views/SettingsView.fxml";
    private static final String DIRECTORY_HELP = "ui/views/HelpView.fxml";

    // Sound file directories
    private static final String DIRECTORY_NOTIFICATION_SOUND = "ui/views/assets/notification-sound-flyff.wav";
    private static final String DIRECTORY_WELCOME_SOUND = "ui/views/assets/notification-sound-twitch.mp3";

    // Views: Display and UI components
    private BorderPane rootView;
    private BorderPane mainView;
    private TextField commandField;
    private HBox titleBarView;
    private VBox sideBarView;
    private BorderPane overdueView;
    private BorderPane todayView;
    private BorderPane weekView;
    private BorderPane archiveView;
    private BorderPane settingsView;
    private BorderPane helpView;

    // Page view index
    private static final int HOME_TAB = 1;
    private static final int EXPIRED_TAB = 2;
    private static final int TODAY_TAB = 3;
    private static final int WEEK_TAB = 4;
    private static final int DONE_TAB = 5;
    private static final int OPTIONS_TAB = 6;
    private static final int HELP_TAB = 7;
    private static final int SMALLEST_PAGE_INDEX = 1;
    private static final int LARGEST_PAGE_INDEX = 7;

    // Controllers
    private MainViewController mainController;
    private SideBarController sidebarController;
    private OverdueController overdueController;
    private TodayController todayController;
    private WeekController weekController;
    private ArchiveController archiveController;

    // Other components
    public Logic logicUnit = null;
    public UIHandler uiHandlerUnit = null;

    // Notification system
    public NotificationPane rootWithNotification = null;
    public PauseTransition delay = null;
    private boolean isFirstNotif = true;

    // Logger
    private UtilityLogger logger = null;

    /*** CORE FUNCTIONS ***/

    /*
     * Empty constructor.
     */
    public MainApp() {

    }

    /*
     * Starts the application with launch() command.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) {

        logger = new UtilityLogger();

        // Reference and link with Logic component
        logicUnit = new Logic(this);
        uiHandlerUnit = logicUnit.getUIHandler();

        // Load Views
        loadRootView(primaryStage);
        loadMainView();
        loadTitleBar();
        loadSideBar();

    }

    /*
     * setWindowDimensions initializes the window properties for application
     * display.
     * 
     * @param Stage primaryStage
     * 
     */
    private void setWindowDimensions(Stage primaryStage) {
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /*** VIEW LOADERS ***/

    /*
     * loadRootView wraps root view with notification pane and displays it
     * within a preset window.
     * 
     * @param Stage primaryStage is the display window for mounting the root
     * view
     */
    private void loadRootView(Stage primaryStage) {
        try {

            // Acquire FXML and CSS component for root layout
            rootView = (BorderPane) FXMLLoader.load(MainApp.class.getResource(DIRECTORY_ROOT));
            rootView.getStyleClass().add(STYLE_CLASS_ROOT);

            // Setup notification system
            setupNotificationPane();
            setWindowDimensions(primaryStage);

            // Display wrapper notification scene
            Scene scene = new Scene(rootWithNotification, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Show Welcome Text
            notifyWithText(NOTIFICATION_WELCOME, true);

        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_ROOT);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * loadCommandLine embeds the command line in place in the root view and
     * sets the callback function for text input.
     * 
     */
    private void loadCommandLine() {
        commandField = (TextField) mainView.getBottom();
        mainController.setCommandLineCallback(commandField);
        // mainController.setCommandLineCallbackDemo(commandField);
    }

    /*
     * loadTitleBar embeds the title bar in place in the root view
     */
    private void loadTitleBar() {
        try {

            // Acquire FXML and CSS component for title bar
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(DIRECTORY_TITLEBAR));
            titleBarView = (HBox) loader.load();
            titleBarView.getStyleClass().add(STYLE_CLASS_TITLEBAR);

            rootView.setTop(titleBarView);

        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_TITLEBAR);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * loadSideBar embeds the side bar in place in the root view and initializes
     * the controller (logic) for the side bar
     */
    private void loadSideBar() {
        try {

            // Acquire FXML and CSS component for side bar
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(DIRECTORY_SIDEBAR));
            sideBarView = (VBox) loader.load();
            sideBarView.getStyleClass().add(STYLE_CLASS_SIDEBAR);

            rootView.setLeft(sideBarView);

            // Set up display logic for side bar
            sidebarController = loader.getController();
            sidebarController.setMainApp(this);

        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_SIDEBAR);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * getView takes in a FXML loader and a FXML directory, loads and returns
     * the JavaFX component object from the FXML file.
     * 
     * @param FXMLLoader loader, String directory
     * 
     * @return Node abstractView is the view initialized from the FXML file
     */
    private Node getView(FXMLLoader loader, String directory) throws IOException {
        loader.setLocation(MainApp.class.getResource(directory));
        Node abstractView = loader.load();
        rootView.setCenter(abstractView);
        return abstractView;
    }

    /*
     * loadMainView loads the main page into the main display area
     */
    private void loadMainView() {
        try {

            // Acquire FXML and CSS component for main view
            FXMLLoader loader = new FXMLLoader();
            mainView = (BorderPane) getView(loader, DIRECTORY_MAIN);

            // Set up display logic for main view
            mainController = loader.getController();
            mainController.setMainApp(this);

            loadCommandLine();
            uiHandlerUnit.refresh();

        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_MAIN);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * loadOverdueView loads the overdue page into the main display area
     */
    private void loadOverdueView() {

        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            overdueView = (BorderPane) getView(loader, DIRECTORY_OVERDUE);
            loadMainView();
            mainView.setCenter(overdueView);

            // Set up display logic for main view
            overdueController = loader.getController();
            overdueController.setMainApp(this);

            uiHandlerUnit.refresh();

        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_OVERDUE);
            ioException.printStackTrace();
        }
    }

    /*
     * loadTodayView loads the today page into the main display area
     */
    private void loadTodayView() {
        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            todayView = (BorderPane) getView(loader, DIRECTORY_TODAY);
            loadMainView();
            mainView.setCenter(todayView);

            // Set up display logic for main view
            todayController = loader.getController();
            todayController.setMainApp(this);

            uiHandlerUnit.refresh();

        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_TODAY);
            ioException.printStackTrace();
        }
    }

    /*
     * loadWeekView loads the week page into the main display area
     */
    private void loadWeekView() {
        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            weekView = (BorderPane) getView(loader, DIRECTORY_WEEK);
            loadMainView();
            mainView.setCenter(weekView);

            // Set up display logic for main view
            weekController = loader.getController();
            weekController.setMainApp(this);

            uiHandlerUnit.refresh();

        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_WEEK);
            ioException.printStackTrace();
        }
    }

    /*
     * loadArchiveView loads the archive page into the main display area
     */
    private void loadArchiveView() {
        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            archiveView = (BorderPane) getView(loader, DIRECTORY_ARCHIVE);
            loadMainView();
            mainView.setCenter(archiveView);

            // Set up display logic for main view
            archiveController = loader.getController();
            archiveController.setMainApp(this);

            uiHandlerUnit.refresh();
        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_ARCHIVE);
            ioException.printStackTrace();
        }
    }

    /*
     * loadSettingsView loads the settings page into the main display area
     */
    private void loadSettingsView() {
        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            settingsView = (BorderPane) getView(loader, DIRECTORY_SETTINGS);
            loadMainView();
            mainView.setCenter(settingsView);
        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_SETTINGS);
            ioException.printStackTrace();
        }
    }

    /*
     * loadHelpView loads the help page into the main display area
     */
    private void loadHelpView() {
        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            helpView = (BorderPane) getView(loader, DIRECTORY_HELP);
            loadMainView();
            mainView.setCenter(helpView);
        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_HELP);
            ioException.printStackTrace();
        }
    }

    /*
     * loadPage sets the current page index to the given index
     * 
     * @param index is the given candidate index to navigate to
     * 
     */
    public void loadPage(int index) {
        if (index >= SMALLEST_PAGE_INDEX && index <= LARGEST_PAGE_INDEX) {
            sidebarController.setIndex(index);
        } else {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_PAGE_INDEX + index);
        }
    }

    /*
     * setPageView loads the corresponding page into the main display area
     * 
     * @param index is the page number to load
     * 
     */
    public void setPageView(int index) {
        switch (index) {
        case HOME_TAB:
            loadMainView();
            break;
        case EXPIRED_TAB:
            loadOverdueView();
            break;
        case TODAY_TAB:
            loadTodayView();
            break;
        case WEEK_TAB:
            loadWeekView();
            break;
        case DONE_TAB:
            loadArchiveView();
            break;
        case OPTIONS_TAB:
            loadSettingsView();
            break;
        case HELP_TAB:
            loadHelpView();
            break;
        default:
            loadMainView();
        }
    }

    public int getPage() {
        if (sidebarController != null) {
            return sidebarController.getIndex();
        } else {
            return 1;
        }
    }

    /*** NOTIFICATION FUNCTIONS ***/

    /*
     * setupNotificationPane intializes the notification system and wraps it
     * around the root display view
     */

    private void setupNotificationPane() {
        Label label = new Label();
        label.setPadding(new Insets(50));

        BorderPane borderPane = new BorderPane(label);
        rootWithNotification = new NotificationPane(borderPane);

        rootWithNotification.setStyle(STYLE_NOTIFICATION);

        rootWithNotification.setShowFromTop(true);
        rootWithNotification.setContent(rootView);
    }

    /*
     * notifyWithText triggers a notification with or without autohide.
     * 
     * @param String text is the notification text, boolean isAutohide is the
     * switch for autohiding notification after fixed delay.
     */
    public void notifyWithText(String text, boolean isAutohide) {

        // Trigger and display notification
        rootWithNotification.setText(text);
        rootWithNotification.show();
        logger.logAction(Component.UI, ACTION_NOTIFICATION_TRIGGERED + " = " + text);

        // Play notification sound(s) accordingly
        if (!isFirstNotif) {
            AudioClip notificationSound = new AudioClip(
                    this.getClass().getResource(DIRECTORY_NOTIFICATION_SOUND).toExternalForm());
            notificationSound.play();
        } else {
            AudioClip notificationSound = new AudioClip(
                    this.getClass().getResource(DIRECTORY_WELCOME_SOUND).toExternalForm());
            notificationSound.play();
            isFirstNotif = !isFirstNotif;
        }

        // Set autohide with delay factor
        if (isAutohide) {
            delay = new PauseTransition(Duration.seconds(DELAY_PERIOD));
            delay.setOnFinished(e -> rootWithNotification.hide());
            delay.play();
        }
    }

    /*** ACCESS FUNCTIONS FOR MODELS ***/

    /*
     * setDisplayTasks replaces and overwrites the current list of tasks to
     * display
     * 
     * @param ArrayList<Task> listOfTasks is the candidate list of tasks
     */
    public void setDisplayTasks(ArrayList<Task> listOfTasks) {
        switch (getPage()) {
        case 1:
            if (mainController != null) {
                mainController.setTasks(listOfTasks);
            }
            break;
        case 2:
            if (overdueController != null) {
                overdueController.setTasks(listOfTasks);
            }
            break;
        case 3:
            if (todayController != null) {
                todayController.setTasks(listOfTasks);
            }
            break;
        case 4:
            if (weekController != null) {
                weekController.setTasks(listOfTasks);
            }
            break;
        case 5:
            if (archiveController != null) {
                archiveController.setTasks(listOfTasks);
            }
            break;
        default:
            if (mainController != null) {
                mainController.setTasks(listOfTasks);
            }
        }

    }

    /*
     * getDisplayTask returns the current displayed list of wrapped task
     * 
     * @return ObservableList<TaskWrapper> listOfDisplayedTasks
     */
    public ObservableList<TaskWrapper> getDisplayTasks() {
        switch (getPage()) {
        case 1:
            return mainController.getTasks();
        case 2:
            return overdueController.getTasks();
        case 3:
            return todayController.getTasks();
        case 4:
            return weekController.getTasks();
        case 5:
            return archiveController.getTasks();
        default:
            return mainController.getTasks();
        }
    }

    /*** HIGHLIGHTER ***/

    /*
     * highlightItem sets the corresponding task item on focus.
     * 
     * @param Task task is the task to be highlighted
     * 
     */
    public void highlightItem(Task task) {
        switch (getPage()) {
        case 1:
            if (mainController != null) {
                mainController.highlight(task);
            }
            break;
        case 2:
            if (overdueController != null) {
                overdueController.highlight(task);
            }
            break;
        case 3:
            if (todayController != null) {
                todayController.highlight(task);
            }
            break;
        case 4:
            if (weekController != null) {
                weekController.highlight(task);
            }
            break;
        case 5:
            if (archiveController != null) {
                archiveController.highlight(task);
            }
            break;
        default:
            if (mainController != null) {
                mainController.highlight(task);
            }
            break;
        }

    }
}
