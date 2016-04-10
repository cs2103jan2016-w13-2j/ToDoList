package todolist;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.textfield.TextFields;

import com.sun.javafx.css.StyleManager;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import todolist.common.UtilityLogger;
import todolist.common.UtilityLogger.Component;
import todolist.logic.Logic;
import todolist.logic.UIHandler;
import todolist.model.Task;
import todolist.ui.TaskWrapper;
import todolist.ui.controllers.ArchiveController;
import todolist.ui.controllers.HelpModalController;
import todolist.ui.controllers.MainViewController;
import todolist.ui.controllers.OverdueController;
import todolist.ui.controllers.SettingsController;
import todolist.ui.controllers.SideBarController;
import todolist.ui.controllers.TodayController;
import todolist.ui.controllers.WeekController;

//@@author A0123994W

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
    private static final String MESSAGE_ERROR_PAGE_INDEX = "Page index is out of bounds @ #";
    private static final String MESSAGE_ERROR_LOAD_HELP = "Error loading help view.";

    // Action messages
    private static final String ACTION_NOTIFICATION_TRIGGERED = "Notification triggered";
    protected static final String FOCUS_COMMAND = "Command field is toggled into focus";
    protected static final String FOCUS_LIST = "Current list is toggled into focus";
    private static final String MESSAGE_CHANGED_PAGE = "Switched tab to %1$s";

    // Notification messages and delay constant
    private static final String NOTIFICATION_WELCOME = "Welcome to ToDoList! Let's get started...";
    private static final int DELAY_PERIOD = 5;

    // Root view directories
    private static final String DIRECTORY_ROOT = "ui/views/RootLayout.fxml";
    private static final String DIRECTORY_TITLEBAR = "ui/views/TitleBarView.fxml";
    private static final String DIRECTORY_SIDEBAR = "ui/views/SideBarView.fxml";
    public static final String DIRECTORY_TASKITEM = "ui/views/TaskNode.fxml";

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

    // Logo directory
    private static final String APPLICATION_ICON = "ui/views/assets/icon.png";

    // Stylesheets
    private static final String UI_VIEWS_DEFAULT_THEME_CSS = "ui/views/styles/DefaultTheme.css";
    private static final String UI_VIEWS_DARK_THEME_CSS = "ui/views/styles/DarkTheme.css";

    // Styles
    private String nightModeTheme = null;
    private String dayModeTheme = null;
    private static final String STYLE_CLASS_ROOT = "root-layout";
    private static final String STYLE_CLASS_TITLEBAR = "title-bar";
    private static final String STYLE_CLASS_SIDEBAR = "side-bar";
    private static final String STYLE_NOTIFICATION_DAY = "-fx-font-size: 1.0em; -fx-font-family:"
            + "\"System Font\"; -fx-text-fill: #454553;";

    // Views: Display and UI components
    private Stage primaryStage;
    private BorderPane rootView;
    private BorderPane mainView;
    private Node mainViewDisplay;
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
    private static final int DEFAULT_TAB = 3;
    private static final int SMALLEST_PAGE_INDEX = 1;
    private static final int LARGEST_PAGE_INDEX = 7;

    // Controllers
    private MainViewController mainController;
    private SideBarController sidebarController;
    private OverdueController overdueController;
    private TodayController todayController;
    private WeekController weekController;
    private ArchiveController archiveController;
    private SettingsController settingsController;
    private HelpModalController helpModal;

    // Other components
    private Logic logicUnit = null;
    private UIHandler uiHandlerUnit = null;

    // Notification system
    private static final int NOTIFICATION_PADDING = 50;
    private NotificationPane rootWithNotification = null;
    private PauseTransition delay = null;
    private boolean isFirstNotif = true;

    // Logger
    private UtilityLogger logger = null;

    // Command history
    private Stack<String> commandHistoryBackward = null;
    private Stack<String> commandHistoryForward = null;
    int commandHistoryPointer = -1;

    // Autocomplete dictionary
    private static final String[] suggestions = { "add", "edit", "delete", "mark", "done", "undone", "sort", "search",
            "filter", "redo", "undo", "reset", "forward", "postpone", "remind", "set-recurring", "open", "task",
            "event", "deadline" };

    ObservableList<String> keywords = null;

    /*** CORE FUNCTIONS ***/

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

        // Setting application icon
        com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(MainApp.class.getResource(getApplicationIcon()));
        application.setDockIconImage(image);

        // Initializing utilities
        logger = new UtilityLogger();
        commandHistoryBackward = new Stack<String>();
        commandHistoryForward = new Stack<String>();

        // Reference and link with Logic component
        logicUnit = new Logic(this);
        uiHandlerUnit = logicUnit.getUIHandler();

        // Load views
        loadRootView(primaryStage);
        loadMainView();
        loadTitleBar();
        loadSideBar();

        // Prepare for user input
        commandField.requestFocus();
    }

    /*
     * loadNotifBubbles updates the sidebar notification bubbles with the latest
     * respective task count
     */
    private void loadNotifBubbles() {
        MainViewController[] controllers = { mainController, overdueController, todayController, weekController,
                archiveController };

        sidebarController.linkBubbles(controllers);
    }

    /*
     * initializeTabs load and initialize the controllers for each tab or page
     */
    private void initializeTabs() {
        for (int i = EXPIRED_TAB; i <= OPTIONS_TAB; ++i) {
            loadPage(i);
        }

        loadPage(getDefaultTab());
        rootView.setCenter(mainView);
        uiHandlerUnit.refresh();
    }

    /*
     * addShortcuts sets key listeners for the pre-defined keyboard shortcuts
     * 
     * @param Scene scene
     * 
     */
    private void addShortcuts(Scene scene) {
        KeyCodeCombination focusOnCommand = new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN);
        KeyCodeCombination focusOnList = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (focusOnCommand.match(event)) {
                    commandField.requestFocus();
                    logger.logAction(UtilityLogger.Component.UI, FOCUS_COMMAND);
                }
            }
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (focusOnList.match(event)) {
                    int page = sidebarController.getIndex();
                    switch (page) {
                    case HOME_TAB:
                        mainController.getTaskListView().requestFocus();
                        logger.logAction(UtilityLogger.Component.UI, FOCUS_LIST);
                        break;
                    case EXPIRED_TAB:
                        overdueController.getTaskListView().requestFocus();
                        logger.logAction(UtilityLogger.Component.UI, FOCUS_LIST);
                        break;
                    case TODAY_TAB:
                        todayController.getTaskListView().requestFocus();
                        logger.logAction(UtilityLogger.Component.UI, FOCUS_LIST);
                        break;
                    case WEEK_TAB:
                        weekController.getTaskListView().requestFocus();
                        logger.logAction(UtilityLogger.Component.UI, FOCUS_LIST);
                        break;
                    case DONE_TAB:
                        archiveController.getTaskListView().requestFocus();
                        logger.logAction(UtilityLogger.Component.UI, FOCUS_LIST);
                        break;
                    default:
                        commandField.requestFocus();
                        logger.logAction(UtilityLogger.Component.UI, FOCUS_COMMAND);
                    }
                }
            }
        });
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

            this.setPrimaryStage(primaryStage);

            // Acquire FXML and CSS component for root layout
            rootView = (BorderPane) FXMLLoader.load(MainApp.class.getResource(DIRECTORY_ROOT));
            rootView.getStyleClass().add(STYLE_CLASS_ROOT);

            // Setup notification system
            setupNotificationPane();
            setWindowDimensions(primaryStage);

            // Display wrapper notification scene
            Scene scene = new Scene(rootWithNotification, DEFAULT_WIDTH, DEFAULT_HEIGHT);

            // Stylesheet Handling
            nightModeTheme = MainApp.class.getResource(UI_VIEWS_DARK_THEME_CSS).toExternalForm();
            dayModeTheme = MainApp.class.getResource(UI_VIEWS_DEFAULT_THEME_CSS).toExternalForm();
            Application.setUserAgentStylesheet(null);
            StyleManager.getInstance().addUserAgentStylesheet(dayModeTheme);
            scene.getStylesheets().add(dayModeTheme);

            // Shortcuts Handling
            addShortcuts(scene);

            // Display
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

        if (keywords == null) {
            keywords = FXCollections.observableArrayList();

            for (String str : suggestions) {
                keywords.add(str);
            }
        }

        if (commandField == null) {
            commandField = (TextField) mainView.getBottom();

            TextFields.bindAutoCompletion(commandField, suggestions);

            mainController.setCommandLineCallback(commandField, dayModeTheme, nightModeTheme);

            // Cycle through history of commands
            KeyCodeCombination scrollHistoryUp = new KeyCodeCombination(KeyCode.UP, KeyCombination.ALT_DOWN);
            KeyCodeCombination scrollHistoryDown = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.ALT_DOWN);

            ContextMenu menu = new ContextMenu();
            commandField.setContextMenu(menu);

            commandField.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {

                    if (scrollHistoryUp.match(event) && !commandHistoryBackward.isEmpty()) {

                        // Browse history backward
                        String history = commandHistoryBackward.pop();
                        commandField.setText(history);
                        commandHistoryForward.push(history);
                        commandField.selectAll();

                    } else if (scrollHistoryDown.match(event) && !commandHistoryForward.isEmpty()) {

                        // Browse history forward
                        String history = commandHistoryForward.pop();
                        commandField.setText(history);
                        commandHistoryBackward.push(history);
                        commandField.selectAll();

                    } else if (scrollHistoryUp.match(event) || scrollHistoryDown.match(event)) {
                        // do nothing ...

                    } else {

                        // Reset on other input
                        while (!commandHistoryForward.isEmpty()) {
                            commandHistoryBackward.push(commandHistoryForward.pop());
                        }
                    }

                }

            });
        }

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
            initializeTabs();
            loadNotifBubbles();

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
            if (mainView == null) {
                mainView = (BorderPane) getView(loader, DIRECTORY_MAIN);
                mainViewDisplay = mainView.getCenter();
            }

            if (mainController == null) {
                // Set up display logic for main view
                mainController = loader.getController();
                mainController.setMainApp(this, uiHandlerUnit);
                mainController.setPageIndex(HOME_TAB);
                mainController.setPlaceHolder("ui/views/MainViewPlaceHolder.fxml");
            }

            rootView.setCenter(mainView);
            mainView.setCenter(mainViewDisplay);
            if (commandField == null) {
                loadCommandLine();
            }
            // uiHandlerUnit.refresh();

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

            if (overdueView == null) {
                overdueView = (BorderPane) getView(loader, DIRECTORY_OVERDUE);
            }

            // loadMainView();
            mainView.setCenter(overdueView);

            if (overdueController == null) {
                // Set up display logic for main view
                overdueController = loader.getController();
                overdueController.setMainApp(this, uiHandlerUnit);
                overdueController.setPageIndex(EXPIRED_TAB);
                overdueController.setPlaceHolder("ui/views/OverduePlaceHolder.fxml");
            }

            // uiHandlerUnit.refresh();

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
            if (todayView == null) {
                todayView = (BorderPane) getView(loader, DIRECTORY_TODAY);
            }

            // loadMainView();
            mainView.setCenter(todayView);

            if (todayController == null) {
                // Set up display logic for main view
                todayController = loader.getController();
                todayController.setMainApp(this, uiHandlerUnit);
                todayController.setPageIndex(TODAY_TAB);
                todayController.setPlaceHolder("ui/views/TodayPlaceHolder.fxml");
            }

            // uiHandlerUnit.refresh();

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

            if (weekView == null) {
                weekView = (BorderPane) getView(loader, DIRECTORY_WEEK);
            }

            // loadMainView();
            mainView.setCenter(weekView);

            if (weekController == null) {
                // Set up display logic for main view
                weekController = loader.getController();
                weekController.setMainApp(this, uiHandlerUnit);
                weekController.setPageIndex(WEEK_TAB);
                weekController.setPlaceHolder("ui/views/WeekPlaceHolder.fxml");

            }

            // uiHandlerUnit.refresh();

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
            if (archiveView == null) {
                archiveView = (BorderPane) getView(loader, DIRECTORY_ARCHIVE);
            }

            // loadMainView();
            mainView.setCenter(archiveView);

            if (archiveController == null) {
                // Set up display logic for main view
                archiveController = loader.getController();
                archiveController.setMainApp(this, uiHandlerUnit);
                archiveController.setPageIndex(DONE_TAB);
                archiveController.setPlaceHolder("ui/views/ArchivePlaceHolder.fxml");

            }

            // uiHandlerUnit.refresh();
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
            if (settingsView == null) {
                settingsView = (BorderPane) getView(loader, DIRECTORY_SETTINGS);
            }

            // loadMainView();
            if (settingsController == null) {
                // Set up display logic for main view
                settingsController = loader.getController();
                settingsController.setMainApp(this, uiHandlerUnit);
                settingsController.setPageIndex(OPTIONS_TAB);
                settingsController.setPlaceHolder("ui/views/SettingsPlaceHolder.fxml");

            }

            mainView.setCenter(settingsView);

            // uiHandlerUnit.refresh();
        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_SETTINGS);
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
            int oldIndex = sidebarController.getIndex();
            sidebarController.setIndex(index);
            setPageView(index, oldIndex);

            commandField.requestFocus();
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
    public void setPageView(int index, int oldIndex) {

        int page = index;

        switch (page) {
        case HOME_TAB:
            if (index != oldIndex) {
                loadMainView();
            }
            break;
        case EXPIRED_TAB:
            if (index != oldIndex) {
                loadOverdueView();
            }
            break;
        case TODAY_TAB:
            if (index != oldIndex) {
                loadTodayView();
            }
            break;
        case WEEK_TAB:
            if (index != oldIndex) {
                loadWeekView();
            }
            break;
        case DONE_TAB:
            if (index != oldIndex) {
                loadArchiveView();
            }
            break;
        case OPTIONS_TAB:
            if (index != oldIndex) {
                loadSettingsView();
            }
            break;
        case HELP_TAB:
            loadHelpPopup();
            break;
        default:
            break;
        }

        logger.logAction(Component.UI, String.format(MESSAGE_CHANGED_PAGE, sidebarController.getTabName(index)));

    }

    /*
     * loadHelpPopup displays a help table popover for easy reference.
     */
    private void loadHelpPopup() {

        // Acquire FXML and CSS component for main view
        FXMLLoader loader = new FXMLLoader();

        try {
            if (helpView == null) {
                helpView = (BorderPane) getView(loader, DIRECTORY_HELP);
            }

            if (helpModal == null) {
                helpModal = loader.getController();
                helpModal.setMainApp(this, helpView);
            }
        } catch (IOException ioException) {
            logger.logError(UtilityLogger.Component.UI, MESSAGE_ERROR_LOAD_HELP);
            ioException.printStackTrace();
        }

        if (helpModal.getModalPopup() == null) {
            helpModal.initializeHelpModal();
        }
        if (!helpModal.getModalPopup().isShowing()) {
            helpModal.displayPopup(sidebarController.help);
        }
        rootView.setCenter(mainView);
    }

    /*
     * getPage returns the current page index.
     * 
     * @return int page
     * 
     */
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
        label.setPadding(new Insets(NOTIFICATION_PADDING));

        BorderPane borderPane = new BorderPane(label);
        rootWithNotification = new NotificationPane(borderPane);

        rootWithNotification.setStyle(STYLE_NOTIFICATION_DAY);
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

        MainViewController[] controllers = { mainController, overdueController, todayController, weekController,
                archiveController };

        // Update all controllers on new list to display
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

        if (sidebarController != null) {
            sidebarController.linkBubbles(controllers);
        }

        mainController.refreshReminders();
//        settingsController.loadCalendar(mainController.getTaskListView().getItems());
        settingsController.plotGraph(mainController.getTaskListView().getItems());
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

    /*** GETTERS AND SETTERS ***/

    public Task getTaskAt(int pos) {
        switch (getPage()) {
        case 1:
            if (mainController != null) {
                return mainController.getTaskAt(pos);
            }
            // Fallthrough
        case 2:
            if (overdueController != null) {
                return overdueController.getTaskAt(pos);
            }
            // Fallthrough
        case 3:
            if (todayController != null) {
                return todayController.getTaskAt(pos);
            }
            // Fallthrough
        case 4:
            if (weekController != null) {
                return weekController.getTaskAt(pos);
            }
            // Fallthrough
        case 5:
            if (archiveController != null) {
                return archiveController.getTaskAt(pos);
            }
            // Fallthrough
        default:
            return null;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public HelpModalController getHelpModal() {
        return helpModal;
    }

    public void setHelpModal(HelpModalController helpModal) {
        this.helpModal = helpModal;
    }

    public SideBarController getSideBarController() {
        return sidebarController;
    }

    public Stack<String> getCommandHistoryBackward() {
        return commandHistoryBackward;
    }

    public Stack<String> getCommandHistoryForward() {
        return commandHistoryForward;
    }

    public static int getHelpTab() {
        return HELP_TAB;
    }

    public static String[] getSuggestions() {
        return suggestions;
    }

    public static int getDefaultTab() {
        return DEFAULT_TAB;
    }

    public static String getApplicationIcon() {
        return APPLICATION_ICON;
    }

}
