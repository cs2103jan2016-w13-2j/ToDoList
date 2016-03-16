package todolist;

import java.io.IOException;
import java.util.ArrayList;

import todolist.model.Logic;
import todolist.model.Task;
import todolist.model.TaskWrapper;
import todolist.view.MainViewController;
import todolist.view.SideBarController;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private static final String WINDOW_TITLE = "todolist by [w13-2j]";

    // Error messages
    private static final String MESSAGE_ERROR_LOAD_ROOT = "Error loading root view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_MAIN = "Error loading main view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_TITLEBAR = "Error loading title bar view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_SIDEBAR = "Error loading side bar view. Exiting now ...";
    private static final String MESSAGE_ERROR_LOAD_EMPTY = "Error loading empty view. Exiting now ...";

    // Notification messages
    private static final String NOTIFICATION_WELCOME = "Welcome to todolist! Let's get started...";

    // Directories and labels
    private static final String DIRECTORY_ROOT = "view/RootLayout.fxml";
    private static final String STYLE_CLASS_ROOT = "root-layout";
    private static final String DIRECTORY_MAIN = "view/MainView.fxml";
    private static final String STYLE_CLASS_MAIN = "main-view";
    private static final String DIRECTORY_TITLEBAR = "view/TitleBarView.fxml";
    private static final String STYLE_CLASS_TITLEBAR = "title-bar";
    private static final String DIRECTORY_SIDEBAR = "view/SideBarView.fxml";
    private static final String STYLE_CLASS_SIDEBAR = "side-bar";
    public static final String DIRECTORY_TASKITEM = "TaskNode.fxml";
    private static final String DIRECTORY_EMPTY = "view/EmptyView.fxml";

    // Views: Display and UI components
    private BorderPane rootView;
    private BorderPane mainView;
    private TextField commandField;
    private HBox titleBarView;
    private VBox sideBarView;
    private BorderPane emptyView;

    // Controllers
    private MainViewController mainController;
    private SideBarController sidebarController;

    // Other Components
    public Logic logicUnit = null;

    // Notification System
    public NotificationPane rootWithNotification = null;
    public PauseTransition delay = null;

    
    /*** CORE FUNCTIONS ***/
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Reference and link with Logic component
        logicUnit = new Logic(this);

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

    private void refreshTaskList() {
        mainController.populateTaskListView();
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

    private void loadMainView() {
        try {

            // Acquire FXML and CSS component for main view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(DIRECTORY_MAIN));
            mainView = (BorderPane) loader.load();
            mainView.getStyleClass().add(STYLE_CLASS_MAIN);

            rootView.setCenter(mainView);

            // Set up display logic for main view
            mainController = loader.getController();
            mainController.setMainApp(this);

            loadCommandLine();
            reloadDisplay();

        } catch (IOException ioException) {
            Logger.logMsg(Logger.ERROR, MESSAGE_ERROR_LOAD_MAIN);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    private void loadCommandLine() {
        commandField = (TextField) mainView.getBottom();
        mainController.setCommandLineCallback(commandField);
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

    private void loadDefaultEmptyView() {
        try {

            // Acquire FXML and CSS component for empty view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(DIRECTORY_EMPTY));
            emptyView = (BorderPane) loader.load();

            rootView.setCenter(emptyView);
        } catch (IOException ioException) {
            Logger.logMsg(Logger.ERROR, MESSAGE_ERROR_LOAD_EMPTY);
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    public void reloadDisplay() {
        refreshTaskList();
    }

   
    /*** NOTIFICATION FUNCTIONS ***/
    
    private void setupNotificationPane() {
        Label label = new Label();
        label.setPadding(new Insets(50));

        BorderPane borderPane = new BorderPane(label);
        rootWithNotification = new NotificationPane(borderPane);

        rootWithNotification.setShowFromTop(true);
        rootWithNotification.setContent(rootView);
    }
    
    
    
    public void notifyWithText(String text) {

        rootWithNotification.setText(text);
        rootWithNotification.show();

        // Delay factor
        delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> rootWithNotification.hide());
        delay.play();
    }
    
    
    /*** ACCESS FUNCTIONS FOR MODELS ***/

    public void setDisplayTasks(ArrayList<Task> listOfTasks) {
        mainController.setTasks(listOfTasks);
    }

    public ObservableList<TaskWrapper> getDisplayTasks() {
        return mainController.getTasks();
    }

    public void loadPage(int index) {
        switch (index) {
        case 1:
            loadMainView();
        default:
            loadDefaultEmptyView();
        }
    }

}
