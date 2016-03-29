package todolist;

import java.io.IOException;
import java.util.ArrayList;

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

import com.sun.media.jfxmedia.logging.Logger;

/*
 * MainApp is the GUI class for the application.
 * @author Huang Lie Jun (A0123994W)
 */
public class MainApp extends Application {

    // Window constants
    private static final double MIN_HEIGHT = 600;
    private static final double MIN_WIDTH = 400;
    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 800;
    private static final String WINDOW_TITLE = "ToDoList by [w13-2j]";

    // Error messages
    private static final String MESSAGE_ERROR_LOAD_ROOT = "Error loading root view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_MAIN = "Error loading main view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_TITLEBAR = "Error loading title bar view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_SIDEBAR = "Error loading side bar view. Exiting now ...";
//    private static final String MESSAGE_ERROR_LOAD_EMPTY = "Error loading empty view. Exiting now ...";

    // Notification messages and delay
    private static final String NOTIFICATION_WELCOME = "Welcome to ToDoList! Let's get started...";
    private static final int DELAY_PERIOD = 5;

    // Directories and labels
    private static final String DIRECTORY_ROOT = "ui/views/RootLayout.fxml";
    private static final String DIRECTORY_TITLEBAR = "ui/views/TitleBarView.fxml";
    private static final String DIRECTORY_SIDEBAR = "ui/views/SideBarView.fxml";
    public static final String DIRECTORY_TASKITEM = "ui/views/TaskNode.fxml";

    private static final String STYLE_CLASS_ROOT = "root-layout";
//    private static final String STYLE_CLASS_MAIN = "main-view";
    private static final String STYLE_CLASS_TITLEBAR = "title-bar";
    private static final String STYLE_CLASS_SIDEBAR = "side-bar";

    private static final String DIRECTORY_MAIN = "ui/views/MainView.fxml";
    private static final String DIRECTORY_OVERDUE = "ui/views/OverdueView.fxml";
    private static final String DIRECTORY_TODAY = "ui/views/TodayView.fxml";
    private static final String DIRECTORY_WEEK = "ui/views/WeekView.fxml";
    private static final String DIRECTORY_ARCHIVE = "ui/views/ArchiveView.fxml";
    private static final String DIRECTORY_SETTINGS = "ui/views/SettingsView.fxml";
    private static final String DIRECTORY_HELP = "ui/views/HelpView.fxml";
//    private static final String DIRECTORY_EMPTY = "ui/views/EmptyView.fxml";

    private static final String DIRECTORY_NOTIFICATION_SOUND = "ui/views/assets/notification-sound-flyff.wav";
    private static final String DIRECTORY_WELCOME_SOUND = "ui/views/assets/notification-sound-twitch.mp3";

    // Views: Display and UI components
    private BorderPane rootView;
    private BorderPane mainView;
    private TextField commandField;
    private HBox titleBarView;
    private VBox sideBarView;
//    private BorderPane emptyView;
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

    // Controllers
    private MainViewController mainController;
    private SideBarController sidebarController;
    private OverdueController overdueController;
    private TodayController todayController;
    private WeekController weekController;
    private ArchiveController archiveController;

    // Other Components
    public Logic logicUnit = null;
    public UIHandler uiHandlerUnit = null;

    // Notification System
    public NotificationPane rootWithNotification = null;
    public PauseTransition delay = null;
    private boolean isFirstNotif = true;

    /*** CORE FUNCTIONS ***/

    public MainApp() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Reference and link with Logic component
        logicUnit = new Logic(this);
        uiHandlerUnit = logicUnit.getUIHandler();

        // Load Views
        loadRootView(primaryStage);
        loadMainView();
        loadTitleBar();
        loadSideBar();

    }

    private void setWindowDimensions(Stage primaryStage) {
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /*** VIEW LOADERS ***/

    private void loadRootView(Stage primaryStage) {
        try {

            // Acquire FXML and CSS component for root layout
            rootView = (BorderPane) FXMLLoader.load(MainApp.class.getResource(DIRECTORY_ROOT));
            rootView.getStyleClass().add(STYLE_CLASS_ROOT);

            setupNotificationPane();
            setWindowDimensions(primaryStage);

            Scene scene = new Scene(rootWithNotification, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();

            notifyWithText(NOTIFICATION_WELCOME);

        } catch (IOException ioException) {
            Logger.logMsg(Logger.ERROR, MESSAGE_ERROR_LOAD_ROOT);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    private void loadCommandLine() {
        commandField = (TextField) mainView.getBottom();
        //mainController.setCommandLineCallback(commandField);
        mainController.setCommandLineCallbackDemo(commandField);
    }

    private void loadTitleBar() {
        try {

            // Acquire FXML and CSS component for title bar
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(DIRECTORY_TITLEBAR));
            titleBarView = (HBox) loader.load();
            titleBarView.getStyleClass().add(STYLE_CLASS_TITLEBAR);

            rootView.setTop(titleBarView);

        } catch (IOException ioException) {
            Logger.logMsg(Logger.ERROR, MESSAGE_ERROR_LOAD_TITLEBAR);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

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
            Logger.logMsg(Logger.ERROR, MESSAGE_ERROR_LOAD_SIDEBAR);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    private Node getView(FXMLLoader loader, String directory) throws IOException {
        loader.setLocation(MainApp.class.getResource(directory));
        Node abstractView = loader.load();
        rootView.setCenter(abstractView);
        return abstractView;
    }

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
            Logger.logMsg(Logger.ERROR, MESSAGE_ERROR_LOAD_MAIN);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

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

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadSettingsView() {
        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            settingsView = (BorderPane) getView(loader, DIRECTORY_SETTINGS);
            loadMainView();
            mainView.setCenter(settingsView);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadHelpView() {
        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();
        try {
            helpView = (BorderPane) getView(loader, DIRECTORY_HELP);
            loadMainView();
            mainView.setCenter(helpView);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadPage(int index) {
        sidebarController.setIndex(index);
    }

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

    /*** NOTIFICATION FUNCTIONS ***/

    private void setupNotificationPane() {
        Label label = new Label();
        label.setPadding(new Insets(50));

        BorderPane borderPane = new BorderPane(label);
        rootWithNotification = new NotificationPane(borderPane);

        rootWithNotification.setStyle("-fx-font-size: 10px;");

        rootWithNotification.setShowFromTop(true);
        rootWithNotification.setContent(rootView);
    }

    public void notifyWithText(String text) {

        rootWithNotification.setText(text);
        rootWithNotification.show();

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

        // Delay factor
        delay = new PauseTransition(Duration.seconds(DELAY_PERIOD));
        delay.setOnFinished(e -> rootWithNotification.hide());
        delay.play();
    }

    /*** ACCESS FUNCTIONS FOR MODELS ***/

    public void setDisplayTasks(ArrayList<Task> listOfTasks) {
        if (mainController != null) {
            mainController.setTasks(listOfTasks);
        }

        if (overdueController != null) {
            overdueController.setTasks(listOfTasks);
        }

        if (todayController != null) {
            todayController.setTasks(listOfTasks);
        }

        if (weekController != null) {
            weekController.setTasks(listOfTasks);
        }

        if (archiveController != null) {
            archiveController.setTasks(listOfTasks);
        }
    }

    public ObservableList<TaskWrapper> getDisplayTasks() {
        return mainController.getTasks();
    }

    /*** HIGHLIGHTER ***/

    public void highLight(Task task) {
        if (mainController != null) {
            mainController.highLight(task);
        }

        if (overdueController != null) {
            overdueController.highLight(task);
        }

        if (todayController != null) {
            todayController.highLight(task);
        }

        if (weekController != null) {
            weekController.highLight(task);
        }

        if (archiveController != null) {
            archiveController.highLight(task);
        }
    }
}
