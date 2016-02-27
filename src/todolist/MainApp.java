package todolist;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import todolist.model.Category;
import todolist.model.Priority;
import todolist.model.Reminder;
import todolist.model.Task;
import todolist.view.MainViewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*
 * MainApp is the GUI class for the application.
 *  
 * @author Huang Lie Jun (A0123994W)
 */
public class MainApp extends Application {

    // Window constants
    private static final double MIN_HEIGHT = 424;
    private static final double MIN_WIDTH = 616;
    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 800;
    private static final String WINDOW_TITLE = "todolist by [w13-2j]";

    // Error messages
    private static final String MESSAGE_ERROR_LOAD_ROOT = "Error loading root view.";
    private static final String MESSAGE_ERROR_LOAD_MAIN = "Error loading main view.";
    private static final String MESSAGE_ERROR_LOAD_TITLEBAR = "Error loading title bar view.";
    private static final String MESSAGE_ERROR_LOAD_SIDEBAR = "Error loading side bar view.";

    // Directories and labels
    private static final String DIRECTORY_ROOT = "view/RootLayout.fxml";
    private static final String STYLE_CLASS_ROOT = "root-layout";
    private static final String DIRECTORY_MAIN = "view/MainView.fxml";
    private static final String STYLE_CLASS_MAIN = "main-view";
    private static final String DIRECTORY_TITLEBAR = "view/TitleBarView.fxml";
    private static final String STYLE_CLASS_TITLEBAR = "title-bar";
    private static final String DIRECTORY_SIDEBAR = "view/SideBarView.fxml";
    private static final String STYLE_CLASS_SIDEBAR = "side-bar";

    // Views: Display and UI components
    private BorderPane rootView;
    private BorderPane mainView;
    private TextField commandField;
    private HBox titleBarView;
    private FlowPane sideBarView;

    // Models: Lists to display, provided by backend components
    private ObservableList<Task> tasksToDisplay = FXCollections.observableArrayList();
    private ObservableList<Category> categoriesToDisplay = FXCollections.observableArrayList();
    
    // Controllers
    private MainViewController mainController;
//  private TitleBarController titlebarController;
//  private SideBarController sidebarController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // SAMPLE DATASET for testing
        tasksToDisplay.add(new Task("Do UI Handler", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                new Category("CS2103T Project"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));
        tasksToDisplay.add(new Task("Setup Trello", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                new Category("CS2103T Project"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));
        tasksToDisplay.add(new Task("Prepare CV", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                new Category("Personal"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));
        tasksToDisplay.add(new Task("Buy leather shoes", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                new Category("Personal"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));
        tasksToDisplay.add(new Task("Send emails", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                new Category("18th MC"), new Priority(1), new Reminder(LocalDateTime.now().plusHours(3))));

        // Load Model Lists from Storage
        // ... Load Categories into categoriesToDisplay
        // ... Load Tasks into tasksToDisplay

        // Load Views
        loadRootView(primaryStage);
        loadMainView();
        loadTitleBar();
        loadSideBar();

    }

    private void loadRootView(Stage primaryStage) {
        try {
            rootView = (BorderPane) FXMLLoader.load(MainApp.class.getResource(DIRECTORY_ROOT));
            rootView.getStyleClass().add(STYLE_CLASS_ROOT);

            Scene scene = new Scene(rootView, DEFAULT_WIDTH, DEFAULT_HEIGHT);

            setWindowDimensions(primaryStage);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ioException) {
            System.out.println(MESSAGE_ERROR_LOAD_ROOT);
            System.exit(1);
        }
    }

    private void setWindowDimensions(Stage primaryStage) {
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /*** MODEL GETTERS-SETTERS ***/
    
    public ObservableList<Task> getDisplayTasks() {
        return tasksToDisplay;
    }

    public void setDisplayTasks(ArrayList<Task> tasks) {
        tasksToDisplay.clear();
        for (int i = 0; i < tasks.size(); ++i) {
            tasksToDisplay.add(tasks.get(i));
        }
    }
    
    public ObservableList<Category> getDisplayCategories() {
        return categoriesToDisplay;
    }

    public void setDisplayCategories(ArrayList<Category> categories) {
        categoriesToDisplay.removeAll();
        for (int i = 0; i < categories.size(); ++i) {
            categoriesToDisplay.add(categories.get(i));
        }
    }

    /*** VIEW LOADERS ***/
    
    private void loadMainView() {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource(DIRECTORY_MAIN));
            mainView = (BorderPane) loader.load();
            mainView.getStyleClass().add(STYLE_CLASS_MAIN);
            rootView.setCenter(mainView);

            mainController = loader.getController();
            mainController.setMainApp(this);

            loadCommandLine();

        } catch (IOException e) {
            System.out.println(MESSAGE_ERROR_LOAD_MAIN);
            System.exit(1);
        }
    }

    private void loadCommandLine() {
        commandField = (TextField) mainView.getBottom();
        mainController.setCommandLineCallback(commandField);
    }

    private void loadTitleBar() {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource(DIRECTORY_TITLEBAR));
            titleBarView = (HBox) loader.load();
            titleBarView.getStyleClass().add(STYLE_CLASS_TITLEBAR);
            rootView.setTop(titleBarView);

            // titlebarController = loader.getController();
            // controller.setMainApp(this);

        } catch (IOException e) {
            System.out.println(MESSAGE_ERROR_LOAD_TITLEBAR);
            System.exit(1);
        }
    }

    private void loadSideBar() {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource(DIRECTORY_SIDEBAR));
            sideBarView = (FlowPane) loader.load();
            sideBarView.getStyleClass().add(STYLE_CLASS_SIDEBAR);
            rootView.setLeft(sideBarView);

            // sidebarController = loader.getController();
            // controller.setMainApp(this);

        } catch (IOException e) {
            System.out.println(MESSAGE_ERROR_LOAD_SIDEBAR);
            System.exit(1);
        }
    }

    // Must add listeners to models ...
    
    // Might need to cache states and load on start ...
}
