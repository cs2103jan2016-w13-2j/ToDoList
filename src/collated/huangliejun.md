# huangliejun
###### src/todolist/common/tests/NormalCommandParserTest.java
``` java

public class NormalCommandParserTest {
    NormalCommandParser normalCommandParser = null;

    @Before
    public void initNormalCommandParser() {
        normalCommandParser = new NormalCommandParser();
    }

    @Test
    public void testParse() {

        final String testCase = " add event CS2103T-Tutorial 2016-03-23 13:00 1 hour";
//        final String expectedAction = "add";
        final int expectedArgsSize = 7;
        String[] expectedArgs = new String[expectedArgsSize];
        expectedArgs[0] = "add";
        expectedArgs[1] = "event";
        expectedArgs[2] = "CS2103T-Tutorial";
        expectedArgs[3] = "2016-03-23";
        expectedArgs[4] = "13:00";
        expectedArgs[5] = "1";
        expectedArgs[6] = "hour";

        TokenizedCommand output = normalCommandParser.parse(testCase);

        assertEquals(expectedArgsSize, output.getArgs().length);

        for (int i = 0; i < output.getArgs().length; ++i) {
            String arg = output.getArgs()[i];
            assertEquals(expectedArgs[i], arg);
        }

    }
}
```
###### src/todolist/common/tests/UIHandlerTest.java
``` java

public class UIHandlerTest {

}
```
###### src/todolist/common/UtilityLogger.java
``` java

/*
 * UtilityLogger is a common logger to log all ToDoList activities at runtime.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class UtilityLogger {

    // Logger constants
    private static final int MAX_LOG_SIZE = 1048576;
    private static final int NUMBER_OF_LOGS = 1;
    private static final boolean IS_APPEND = false;

    // Log variants
    private static final String ACTION = "ACTION >> ";
    private static final String ERROR = "ERROR >> ";
    private static final String EXCEPTION = "EXCEPTION >> ";
    private static final String COMPONENTCALL = "COMPONENT CALL >> ";

    // Log components
    private static final String UI = "UI";
    private static final String LOGIC = "LOGIC";
    private static final String PARSER = "PARSER";
    private static final String STORAGE = "STORAGE";
    private static final String UNKNOWN = "UNKNOWN";
    private static final String LOGGER_NAME = "ToDoList-Logger";
    private static final String ERROR_CREATE_LOG = "Error accessing log file.";
    private static final String ERROR_GET_DEFAULT_PATH = "Error obtaining default application directory for file path.";

    // Log file handler
    private static File logDirectory = null;
    private Logger logger = null;
    private static FileHandler fileHandler = null;

    // Component types
    public static enum Component {
        UI, Logic, Parser, Storage
    };

    /*** Constructors ***/

    /*
     * Constructor with filepath. This constructor takes in a file path, checks
     * its validity and sets the path as the destination path for the log file.
     * 
     * @param String logDir The directory at which the log file will be read or
     * written to.
     */
    public UtilityLogger(String logDir) {
        // Instantiate a singleton logger if not already created
        logger = Logger.getLogger(LOGGER_NAME);

        try {

            // Check validity of directory and set directory accordingly
            if (isValidPath(logDir)) {
                logDirectory = new File(logDir);
            } else {
                logDirectory = getDefaultPath();
            }

            fileHandler = new FileHandler(logDirectory.getPath(), MAX_LOG_SIZE, NUMBER_OF_LOGS, IS_APPEND);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException exception) {
            handleConstructionError(exception);
        }
    }

    /*
     * Constructor without filepath. This constructor uses the application
     * default root directory as the destination path for the log file.
     */
    public UtilityLogger() {
        // Instantiate a singleton logger if not already created
        logger = Logger.getLogger(LOGGER_NAME);

        // Set default directory as destination path for log file
        logDirectory = getDefaultPath();

        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler(logDirectory.getPath(), MAX_LOG_SIZE, NUMBER_OF_LOGS, IS_APPEND);
            }
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException exception) {
            handleConstructionError(exception);
        }
    }

    /*** Utility Functions ***/

    /*
     * getDefaultPath checks and returns the file path that the application is
     * currently running from.
     * 
     * @return String defaultPath
     */
    private File getDefaultPath() {
        File file = null;
        CodeSource codeSource = null;
        File applicationFile = null;

        try {
            codeSource = UtilityLogger.class.getProtectionDomain().getCodeSource();
            applicationFile = new File(codeSource.getLocation().toURI().getPath());
            file = new File(applicationFile.getParent() + ".log");
        } catch (URISyntaxException exception) {
            handleDefaultPathError(exception);
        }

        System.out.println(applicationFile.getParent());

        return file;
    }

    /*
     * isValidPath checks for validity of the file path in accordance to OS
     * rules.
     * 
     * @param String logDir is the candidate directory for the log file to be
     * read from or written to.
     */
    private boolean isValidPath(String logDir) {
        File file = new File(logDir);
        try {
            file.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /*** Access Functions ***/

    /*
     * getLogDirectory returns the current file path of the log file.
     * 
     * @return String logFilePath
     */
    public static String getLogDirectory() {
        return logDirectory.getPath();
    }

    /*
     * setLogDirectory sets the current file path of the log file.
     * 
     * @param String logDirectory is the candidate file path of the log file.
     */
    public static void setLogDirectory(String logDirectory) {
        UtilityLogger.logDirectory = new File(logDirectory);
    }

    /*
     * getLogger returns the current common utility loggger used in this
     * application.
     * 
     * @return Logger logger
     */
    public Logger getLogger() {
        return logger;
    }

    /*
     * setLogger sets the current common utility logger used in this
     * application.
     * 
     * @param Logger logger is the candidate logger to substitute the current
     * utility logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /*** Error Handling Functions ***/

    /*
     * handleConstructionError handles failure to construct this utility logger
     * by displaying error message and trace.
     * 
     * @param Exception exception is the exception caught
     */
    private void handleConstructionError(Exception exception) {
        System.err.println(ERROR_CREATE_LOG);
        exception.printStackTrace();
    }

    /*
     * handleDefaultPathError handles failure to obtain the file path of the
     * application by displaying error message and trace.
     * 
     * @param URISyntaxException exception is the exception caught
     */
    private void handleDefaultPathError(URISyntaxException exception) {
        System.err.println(ERROR_GET_DEFAULT_PATH);
        exception.printStackTrace();
    }

    /*** Logging API ***/

    /*
     * logAction logs an action performed by a function or by the user.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logAction(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + ACTION + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + ACTION + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + ACTION + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + ACTION + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + ACTION + message);
        }
    }

    /*
     * logError logs an error encountered by a function.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logError(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + ERROR + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + ERROR + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + ERROR + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + ERROR + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + ERROR + message);
        }
    }

    /*
     * logException logs an exception caught by a function.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logException(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + EXCEPTION + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + EXCEPTION + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + EXCEPTION + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + EXCEPTION + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + EXCEPTION + message);
        }
    }

    /*
     * logComponentCall logs an inter-component function call.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logComponentCall(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + COMPONENTCALL + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + COMPONENTCALL + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + COMPONENTCALL + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + COMPONENTCALL + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + COMPONENTCALL + message);
        }
    }

    /*
     * logInfo logs a message (pre-formatted) into the log file.
     * 
     * @param String message
     * 
     */
    private void logInfo(String message) {
        logger.info(message);
    }

    // public static void main(String[] args) {
    // UtilityLogger logger = new UtilityLogger();
    // logger.logAction(Component.UI, "HELLO WORLD");
    //
    // UtilityLogger logger2 = new UtilityLogger();
    // logger2.logAction(Component.UI, "HELLO WORLD");
    //
    // UtilityLogger logger3 = new UtilityLogger();
    // logger3.logAction(Component.UI, "HELLO WORLD");
    // }
}
```
###### src/todolist/MainApp.java
``` java

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
```
###### src/todolist/ui/controllers/ArchiveController.java
``` java

public class ArchiveController extends MainViewController {

    public ArchiveController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    @FXML
    public void initialize() {
        initTaskListView();
    }

    @Override
    public void setTasks(ArrayList<Task> tasks) {

        // List provided by logic must be valid
        assert (tasks != null);

        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            Task task = tasks.get(i);
            if (task.getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(task);
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
    }
}
```
###### src/todolist/ui/controllers/MainViewController.java
``` java

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
    private static final String MESSAGE_UPDATED_MAIN_TASKLIST = "Updated display task list [HOME].";
    private static final String MESSAGE_HIGHLIGHT_ITEM = "Item #%1$s in display task list [HOME] highlighted.";
    private static final String MESSAGE_HIGHLIGHT_ITEM_NOT_FOUND = "Item to be highlighted cannot be found in display task list [HOME].";

    // Model data
    protected ObservableList<TaskWrapper> tasksToDisplay = null;

    // Main application linkback
    private MainApp mainApplication = null;

    /*** Views ***/
    @FXML
    protected ListView<TaskWrapper> listView = null;

    // Logger
    UtilityLogger logger = null;

```
###### src/todolist/ui/controllers/MainViewController.java
``` java
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
    }

    /*
     * setMainApp takes a MainApp reference and stores it locally as the
     * mainApplication reference.
     * 
     * @param MainApp mainApp is the reference provided to the calling function
     * 
     */
    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
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
    public void setCommandLineCallback(TextField commandField) {
        // Set Callback for TextField
        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String commandString = commandField.getText();
                // Command command = new Command(commandString);
                // System.out.println(command.getCommand());

                // Pass command line input for processing
                try {

                    commandField.clear();
                    logger.logAction(Component.UI, MESSAGE_CLEAR_TEXTFIELD);

                    mainApplication.uiHandlerUnit.process(commandString);
                    logger.logComponentCall(Component.UI, MESSAGE_CALL_LOGIC_COMPONENT);

                } catch (Exception exception) {
                    logger.logError(Component.UI, ERROR_PROCESSING_USER_INPUT);
                    exception.printStackTrace();
                }
            }
        };

        commandField.setOnAction(commandHandler);
    }

```
###### src/todolist/ui/controllers/MainViewController.java
``` java
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
            String.format(MESSAGE_HIGHLIGHT_ITEM, Integer.toString(index));
            logger.logAction(Component.UI, MESSAGE_HIGHLIGHT_ITEM);
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

}
```
###### src/todolist/ui/controllers/OverdueController.java
``` java

public class OverdueController extends MainViewController {

    public OverdueController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    @FXML
    public void initialize() {
        initTaskListView();
    }

    @Override
    public void setTasks(ArrayList<Task> tasks) {

        // List provided by logic must be valid
        assert (tasks != null);

        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            Task task = tasks.get(i);
            if (task.getEndTime() != null && task.getEndTime().isBefore(LocalDateTime.now()) && !task.getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
    }
}
```
###### src/todolist/ui/controllers/SideBarController.java
``` java

public class SideBarController {

    /*** TAB STYLES ***/
    private static final String STYLE_TAB_NORMAL = "-fx-background-color: transparent;";
    private static final String STYLE_TAB_FOCUSED = "-fx-background-color: #95E1D3;";
    // private static final String STYLE_TAB_FOCUSED_DARK =
    // "-fx-background-color: #EB586F;";

    /*** VIEWS ***/

    // HOME TAB
    @FXML
    private Button home = null;
    @FXML
    private ImageView homeIcon = null;

    // EXPIRED TAB
    @FXML
    private Button expired = null;
    @FXML
    private ImageView expiredIcon = null;

    // TODAY TAB
    @FXML
    private StackPane todayStack = null;
    @FXML
    private Label todayLabel = null;
    @FXML
    private Button today = null;
    @FXML
    private ImageView todayIcon = null;
    private int todayDate = 0;

    // WEEK TAB
    @FXML
    private Button week = null;
    @FXML
    private ImageView weekIcon = null;

    // DONE TAB
    @FXML
    private Button done = null;
    @FXML
    private ImageView doneIcon = null;

    // OPTIONS TAB
    @FXML
    private Button options = null;
    @FXML
    private ImageView optionsIcon = null;

    // HELP TAB
    @FXML
    private Button help = null;
    @FXML
    private ImageView helpIcon = null;

    /*** SIDEBAR AND PAGE PROPERTIES ***/

    private int index = 1;
    private static int NUMBER_BUTTONS = 7;
    private Button[] buttonArray;

    // Main Application reference
    private MainApp mainApplication = null;

    /*** CORE FUNCTIONS ***/

    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
    }

    @FXML
    public void initialize() {
        setButtonArray();
        setTodayDate();
        colourTab();
    }

    private void setButtonArray() {
        buttonArray = new Button[NUMBER_BUTTONS];
        buttonArray[0] = home;
        buttonArray[1] = expired;
        buttonArray[2] = today;
        buttonArray[3] = week;
        buttonArray[4] = done;
        buttonArray[5] = options;
        buttonArray[6] = help;
    }

    private void setTodayDate() {
        todayDate = LocalDateTime.now().getDayOfMonth();
        todayLabel.setText(Integer.toString(todayDate));
    }

    public void setIndex(int index) {
        this.index = index;
        colourTab();
        mainApplication.setPageView(index);
    }

    private void colourTab() {
        for (int i = 0; i < NUMBER_BUTTONS; ++i) {
            Button currentButton = buttonArray[i];
            currentButton.setStyle(STYLE_TAB_NORMAL);

            // Highlight if focused
            if (i == index - 1) {
                currentButton.setStyle(STYLE_TAB_FOCUSED);
                // currentButton.setStyle(STYLE_TAB_FOCUSED_DARK);

            }

        }

    }

    public MainApp getMainApplication() {
        return mainApplication;
    }

    public int getIndex() {
        return index;
    }

}
```
###### src/todolist/ui/controllers/TaskListCell.java
``` java

/*
 * TaskListCell formats task views by assigning every task node to a controller.
 * 
 * @author Huang Lie Jun (A0123994W)
 * 
 */
public class TaskListCell extends ListCell<TaskWrapper> {

    /*
     * (non-Javadoc)
     * 
     * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
     */
    @Override
    public void updateItem(TaskWrapper task, boolean empty) throws IllegalArgumentException {
        // Only displays tasks if they are not empty
        if (task != null) {
            super.updateItem(task, empty);

            // Link task to graphic node
            try {
                TaskNodeController taskNode = new TaskNodeController(task, this.getIndex());
                setGraphic(taskNode.getNode());
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
                throw iae;
            }
        } else {
            nullifyItem();
        }
    }

    /*
     * nullifyItem hides empty task views.
     */
    private void nullifyItem() {
        setText(null);
        setGraphic(null);
    }
}
```
###### src/todolist/ui/controllers/TaskNodeController.java
``` java

public class TaskNodeController {

    /*** STATIC MESSAGES ***/

    // ERRORS
    private static final String ERROR_DISPLAY_ITEM_INDEX = "Task Display: Index number out of bounds.";
    private static final String ERROR_DISPLAY_ITEM_TITLE = "Task Display: Task title invalid.";

    // DEFAULTS
    private static final String NULL_DISPLAY_ITEM_CATEGORY = "Category: Not Available";
    private static final String DISPLAY_ITEM_UNARCHIVED = "ONGOING";
    private static final String DISPLAY_ITEM_ARCHIVED = "DONE";

    /*** STYLES ***/

    // TASK TYPE STYLE
    private static final String COLOR_FLOATING = "#3BB873";
    private static final String COLOR_DEADLINE = "#FF6464";
    private static final String COLOR_EVENT = "#F3825F";

    // TASK OVERDUE STYLE
    private static final String COLOR_OVERDUE = "#FF6464";
    private static final String COLOR_TODAY = "#FAAC64";
    private static final String COLOR_SPARE = "#5BAAEC";

    // TASK COMPLETION STYLE
    private static final String COLOR_COMPLETE = "#5BE7A9";
    private static final String COLOR_INCOMPLETE = "#FAAC64";

    // UNKNOWN STYLE
    private static final String COLOR_UNKNOWN = "#748B9C";

    /*** TASK ITEM COMPONENTS ***/

    private TaskWrapper task = null;
    private int index = -1;

    private static enum TASK_TYPE {
        FLOAT, DEADLINE, EVENT, OTHER
    };

    // Base
    @FXML
    private HBox root = null;

    // Priority
    @FXML
    private Rectangle priorityLabel = null;

    // Index
    @FXML
    private StackPane numberLabel = null;
    @FXML
    private Circle numLabelBase = null;
    @FXML
    private Label number = null;

    // Content
    @FXML
    private VBox details = null;

    // Content-TITLE+REMINDER
    @FXML
    private HBox titleBox = null;
    @FXML
    private Label title = null;
    @FXML
    private ImageView reminderIcon = null;

    // Content-DATE(S)
    @FXML
    private HBox dateRangeBox = null;
    @FXML
    private Circle overdueFlag = null;
    @FXML
    private Label dateRange = null;

    // Content-CATEGORY
    @FXML
    private HBox categoryBox = null;
    @FXML
    private Circle categorySprite = null;
    @FXML
    private Label category = null;

    // Content-ARCHIVE
    @FXML
    private StackPane completeStatus = null;
    @FXML
    private Rectangle statusBacking = null;
    @FXML
    private Label status = null;

    // Indicators
    @FXML
    private VBox indicatorsHolder = null;
    @FXML
    private ImageView recurringIndicator = null;
    @FXML
    private ImageView reminderIndicator = null;

    public TaskNodeController(TaskWrapper task, int index) throws IllegalArgumentException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(MainApp.DIRECTORY_TASKITEM));
        fxmlLoader.setController(this);

        this.task = task;
        this.index = index;

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Assignment TaskWrapper to HBox Layout
        try {
            validateAndFormatItem();
        } catch (IllegalArgumentException iae) {
            throw iae;
        }

    }

    public void validateAndFormatItem() throws IllegalArgumentException {
        int indexNumber = index + 1;

        String taskTitle = task.getTaskTitle();
        Category category = task.getCategory();
        String categoryName = null;
        Reminder reminder = task.getReminder();
        LocalDateTime startDateTime = task.getStartTime();
        LocalDateTime endDateTime = task.getEndTime();

        /** Validation **/

        // Ensure integrity of task object
        assert (task != null);

        // Index Number
        if (indexNumber <= 0) {
            Logger.logMsg(Logger.ERROR, ERROR_DISPLAY_ITEM_INDEX);
            throw new IllegalArgumentException(ERROR_DISPLAY_ITEM_INDEX);
        } else {
            number.setText(Integer.toString(indexNumber));
        }

        // Title
        if (taskTitle == null) {
            Logger.logMsg(Logger.ERROR, ERROR_DISPLAY_ITEM_TITLE);
            throw new IllegalArgumentException(ERROR_DISPLAY_ITEM_TITLE);
        } else {
            title.setText(taskTitle);
        }

        // Category
        if (category == null) {
            categoryName = new String(NULL_DISPLAY_ITEM_CATEGORY);
        } else {
            categoryName = category.getCategory();
        }

        this.category.setText(categoryName);
        categorySprite.setFill(Color.web(COLOR_UNKNOWN));

        // Dates
        try {
            String dateFieldOutput = formatDateField(task, startDateTime, endDateTime);
            dateRange.setText(dateFieldOutput);
        } catch (IllegalArgumentException iae) {
            throw iae;
        }

        // Archive Status
        if (task.getIsCompleted()) {
            statusBacking.setFill(Color.web(COLOR_COMPLETE));
            status.setText(DISPLAY_ITEM_ARCHIVED);
        } else {
            statusBacking.setFill(Color.web(COLOR_INCOMPLETE));
            status.setText(DISPLAY_ITEM_UNARCHIVED);
        }

        // Recurring Status
        recurringIndicator.setVisible(task.getIsRecurring());

        // Reminder Status
        if (reminder == null) {
            reminderIndicator.setVisible(false);
        } else {
            reminderIndicator.setVisible(reminder.getStatus());
        }
    }

    public HBox getNode() {
        return root;
    }

    public String formatDateField(TaskWrapper task, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TASK_TYPE taskType = checkTaskType(startDateTime, endDateTime);

        switch (taskType) {

        case FLOAT:
            styleFloatingTask();
            return "Anytime";

        case DEADLINE:
            styleDeadline(endDateTime);
            return formatDeadlineRange(endDateTime);

        case EVENT:
            styleEvent(startDateTime);

            // Smart formatting of range
            if (startDateTime.getDayOfYear() == endDateTime.getDayOfYear()
                    && startDateTime.getYear() == endDateTime.getYear()) {
                return formatEventRangeSameDay(startDateTime, endDateTime);
            } else {
                return formatEventRangeDiffDay(startDateTime, endDateTime);
            }

        default:
            styleUnknown();
            return "Not Available";
        }
    }

    private TASK_TYPE checkTaskType(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TASK_TYPE taskType;
        if (startDateTime != null && endDateTime != null) {
            taskType = TASK_TYPE.EVENT;
        } else if (startDateTime == null && endDateTime != null) {
            taskType = TASK_TYPE.DEADLINE;
        } else if (startDateTime == null && endDateTime == null) {
            taskType = TASK_TYPE.FLOAT;
        } else {
            taskType = TASK_TYPE.OTHER;
        }

        return taskType;
    }

    /** FORMATTING FUNCTIONS **/

    private String formatEventRangeDiffDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return "From " + startDateTime.getDayOfWeek() + ", "
                + startDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
                + " to " + endDateTime.getDayOfWeek() + ", "
                + endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }

    private String formatEventRangeSameDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ", from "
                + startDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) + " to "
                + endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }

    private String formatDeadlineRange(LocalDateTime endDateTime) {
        return "Due: " + endDateTime.getDayOfWeek() + ", "
                + endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }

    /** STYLING FUNCTIONS **/
    private void styleUnknown() {
        numLabelBase.setFill(Color.web(COLOR_UNKNOWN));
        overdueFlag.setFill(Color.web(COLOR_UNKNOWN));
    }

    private void styleEvent(LocalDateTime startDateTime) {
        numLabelBase.setFill(Color.web(COLOR_EVENT));

        if (startDateTime.isBefore(LocalDateTime.now())) {
            overdueFlag.setFill(Color.web(COLOR_OVERDUE));
        } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), startDateTime) <= 24) {
            overdueFlag.setFill(Color.web(COLOR_TODAY));
        } else {
            overdueFlag.setFill(Color.web(COLOR_SPARE));
        }
    }

    private void styleDeadline(LocalDateTime endDateTime) {
        numLabelBase.setFill(Color.web(COLOR_DEADLINE));

        if (endDateTime.isBefore(LocalDateTime.now())) {
            overdueFlag.setFill(Color.web(COLOR_OVERDUE));
        } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), endDateTime) <= 24) {
            overdueFlag.setFill(Color.web(COLOR_TODAY));
        } else {
            overdueFlag.setFill(Color.web(COLOR_SPARE));
        }
    }

    private void styleFloatingTask() {
        numLabelBase.setFill(Color.web(COLOR_FLOATING));
        overdueFlag.setFill(Color.web(COLOR_SPARE));
    }

}
```
###### src/todolist/ui/controllers/TitleBarController.java
``` java

public class TitleBarController {
    // Work In Progress ...
}
```
###### src/todolist/ui/controllers/TodayController.java
``` java

public class TodayController extends MainViewController {

    public TodayController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    @FXML
    public void initialize() {
        initTaskListView();
    }

    @Override
    public void setTasks(ArrayList<Task> tasks) {

        // List provided by logic must be valid
        assert (tasks != null);

        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            Task task = tasks.get(i);
            if (task.getEndTime() != null && task.getEndTime().getYear() == LocalDateTime.now().getYear()
                    && task.getEndTime().getDayOfYear() == LocalDateTime.now().getDayOfYear()
                    && !task.getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
    }
}
```
###### src/todolist/ui/controllers/WeekController.java
``` java

public class WeekController extends MainViewController {

    public WeekController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    @FXML
    public void initialize() {
        initTaskListView();
    }

    @Override
    public void setTasks(ArrayList<Task> tasks) {

        // List provided by logic must be valid
        assert (tasks != null);

        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            Task task = tasks.get(i);
            if (task.getEndTime() != null && LocalDateTime.now().until(task.getEndTime(), ChronoUnit.DAYS) <= 7
                    && !task.getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
    }
}
```
###### src/todolist/ui/TaskWrapper.java
``` java

/*
 * TaskWrapper is the wrapper class for Task class. It wraps each attribute into a property for display.
 * 
 * @author Huang Lie Jun (A0123994W)
 * 
 */
public class TaskWrapper {

    private Task task;
    private StringProperty taskTitle;
    private ObjectProperty<LocalDateTime> startTime;
    private ObjectProperty<LocalDateTime> endTime;
    private ObjectProperty<Category> category;
    private ObjectProperty<Reminder> reminder;
    private ObjectProperty<Boolean> isDone;
    private ObjectProperty<Boolean> isRecurring;
    private ObjectProperty<String> interval;

    /*
     * Constructor builds the TaskWrapper class with the given task.
     * 
     * @param Task task is the task being wrapped around
     * 
     */
    public TaskWrapper(Task task) {
        this.taskTitle = new SimpleStringProperty(task.getName().getName());
        this.startTime = new SimpleObjectProperty<LocalDateTime>(task.getStartTime());
        this.endTime = new SimpleObjectProperty<LocalDateTime>(task.getEndTime());
        this.category = new SimpleObjectProperty<Category>(task.getCategory());
        this.reminder = new SimpleObjectProperty<Reminder>(task.getReminder());
        this.isDone = new SimpleObjectProperty<Boolean>(task.getDoneStatus());
        this.isRecurring = new SimpleObjectProperty<Boolean>(task.getRecurringStatus());
        this.interval = new SimpleObjectProperty<String>(task.getInterval());
        this.task = task;
    }

    /*** GETTER-SETTER FUNCTIONS ***/

    /*
     * getTaskTitleProperty returns the string property of the title.
     * 
     * @return StringProperty taskTitle
     */
    public StringProperty getTaskTitleProperty() {
        return taskTitle;
    }

    /*
     * setTaskTitle takes in a string and sets it as the task title.
     * 
     * @param String taskTitle
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle.set(taskTitle);
    }

    /*
     * getTaskTitle returns the task title.
     * 
     * @return String taskTitle
     */
    public String getTaskTitle() {
        return taskTitle.get();
    }

    /*
     * getStartTimeProperty returns the property of the start time.
     * 
     * @return ObjectProperty<LocalDateTime> startTime
     */
    public ObjectProperty<LocalDateTime> getStartTimeProperty() {
        return startTime;
    }

    /*
     * setStartTime sets the given startTime as the task start time.
     * 
     * @param LocalDateTime startTime
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    /*
     * getStartTime returns the task start time.
     * 
     * @return LocalDateTime startTime
     * 
     */
    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    /*
     * getEndTimeProperty returns the property of the end time.
     * 
     * @return ObjectProperty<LocalDateTime> endTime
     */
    public ObjectProperty<LocalDateTime> getEndTimeProperty() {
        return endTime;
    }

    /*
     * setEndTime sets the given endTime as the task end time.
     * 
     * @param LocalDateTime endTime
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    /*
     * getEndTime returns the task end time.
     * 
     * @return LocalDateTime endTime
     * 
     */
    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    /*
     * getCategoryProperty returns the property of the category.
     * 
     * @return ObjectProperty<Category> category
     */
    public ObjectProperty<Category> getCategoryProperty() {
        return category;
    }

    /*
     * setCategory sets the task category as the given category.
     * 
     * @param Category category
     * 
     */
    public void setCategory(Category category) {
        this.category.set(category);
    }

    /*
     * getCategory returns the category of the task.
     * 
     * @return Category category
     * 
     */
    public Category getCategory() {
        return category.get();
    }

    /*
     * getReminderProperty returns the property of the reminder.
     * 
     * @return ObjectProperty<Reminder> reminder
     */
    public ObjectProperty<Reminder> getReminderProperty() {
        return reminder;
    }

    /*
     * setReminder sets the task reminder as the given reminder.
     * 
     * @param Reminder reminder
     * 
     */
    public void setReminder(Reminder reminder) {
        this.reminder.set(reminder);
    }

    /*
     * getReminder returns the task reminder.
     * 
     * @return Reminder reminder
     * 
     */
    public Reminder getReminder() {
        return reminder.get();
    }

    /*
     * getIsCompletedProperty returns the property of the completion status.
     * 
     * @return ObjectProperty<Boolean> isDone
     */
    public ObjectProperty<Boolean> getIsCompletedProperty() {
        return isDone;
    }

    /*
     * setIsCompleted sets the completion status of the task.
     * 
     * @param ObjectProperty<Boolean> isDone
     */
    public void setIsCompleted(ObjectProperty<Boolean> isDone) {
        this.isDone = isDone;
    }

    /*
     * getIsCompleted returns the completion status of the task.
     * 
     * @return Boolean isDone
     */
    public Boolean getIsCompleted() {
        return isDone.get();
    }

    /*
     * getRecurringStatusProperty returns the property of the recurrence status.
     * 
     * @return ObjectProperty<Boolean> isRecurring
     */
    public ObjectProperty<Boolean> getRecurringStatusProperty() {
        return isRecurring;
    }

    /*
     * setIsRecurring sets the recurrence status of the task.
     * 
     * @param ObjectProperty<Boolean> isRecurring
     * 
     */
    public void setIsRecurring(ObjectProperty<Boolean> isRecurring) {
        this.isRecurring = isRecurring;
    }

    /*
     * getIsRecurring returns the recurrence status of the task.
     * 
     * @return Boolean isRecurring
     */
    public Boolean getIsRecurring() {
        return isRecurring.get();
    }

    /*
     * getIntervalProperty returns the property of the recurrence interval.
     * 
     * @return ObjectProperty<String> interval
     */
    public ObjectProperty<String> getIntervalProperty() {
        return interval;
    }

    /*
     * setInterval sets the recurrence interval of the task.
     * 
     * @param ObjectProperty<String> interval
     * 
     */
    public void setInterval(ObjectProperty<String> interval) {
        this.interval = interval;
    }

    /*
     * getInterval returns the recurrence interval of the task.
     * 
     * @return String interval
     * 
     */
    public String getInterval() {
        return interval.get();
    }

    /*
     * getTaskObject returns the task object reference.
     * 
     * @return Task task
     * 
     */
    public Task getTaskObject() {
        return task;
    }
}
```
###### src/todolist/ui/views/ArchiveView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.ArchiveController">
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label style="-fx-font-weight: bold;" text="no completed tasks yet! get working now!" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0" opacity="100.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="completed tasks" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
###### src/todolist/ui/views/DefaultTheme.css
``` css

/* Color Template */

/* default-theme
 * --------------
 * background: #D8E9F0;
 * accent: #4AA0D5;
 * accent-two: #A9EEE6;
 * accent-three: #95E1D3;
 * content: #F8F3D4;
 * content-two: #FEFAEC;
 * focus: #EB586F;
 * focus-two: #F08A5D;
 * dark: #454553;
 * hint: #7A9EAF;
*/

/* dark-theme
 * --------------
 * background: #D8E9F0;
 * accent: #4AA0D5;
 * accent-two: #A9EEE6;
 * accent-three: #95E1D3;
 * content: #F8F3D4;
 * content-two: #303841;
 * focus: #EB586F;
 * focus-two: #F08A5D;
 * dark: #454553;
 * hint: #7A9EAF;
*/


/* Panel CSS Options */

.root-layout {
	-fx-background-color: #7A9EAF;
}

.main-view {
    -fx-padding: 6.0 6.0 6.0 6.0;
	-fx-border-color: transparent;
	-fx-background-color: transparent;
	-fx-box-border: transparent;
}

.split-pane:horizontal > .split-pane-divider {
    -fx-border-color: transparent;
    -fx-background-color: transparent;
}

.split-pane {
    -fx-border-color: transparent;
    -fx-background-color: transparent;
}

.titled-pane {
    -fx-padding: 0.0 0.0 6.0 0.0;
	-fx-text-fill: white;
    -fx-font-size: 12.0pt;
    -fx-font-family: "System Font";
}

.titled-pane:focused {
	-fx-text-fill: white;
}

#titled-pane-hbox-text {
	-fx-fill: white;
	-fx-font-size: 16.0pt;
}

.titled-pane > .title {
	-fx-background-color: #F08A5D;
	-fx-background-color: #EB586F;
    -fx-font-size: 20.0pt;
    -fx-text-fill: #FEFAEC;
    -fx-font-weight: bold;
}

.titled-pane:focused > .title {
	-fx-color: #4AA0D5;
    -fx-text-fill: #FEFAEC;
    -fx-font-weight: bold;
}

.titled-pane > .content {
	-fx-background-color: #454553;
	-fx-background-color: #FEFAEC;
	-fx-background-radius: 0.0 0.0 6.5 6.5;
}

.list-view .scroll-bar:vertical {
    -fx-opacity: 0.0;
    -fx-padding: -12.0;
}

.list-cell {
    -fx-background-color: transparent;
    -fx-padding: 10.0px;
    -fx-background-color: transparent;
    -fx-background-insets: 0.0px, 10.0px;
}

.list-cell:filled:selected:focused > #task-hbox-item {
    -fx-background-color: #EB586F;
    -fx-background-color: #95E1D3;
    -fx-text-fill: #FEFAEC;
	-fx-fill: #FEFAEC;
}

.list-cell:filled:selected:focused > #task-hbox-item .label {
    -fx-text-fill: #FEFAEC;
}

.list-cell:filled > #task-hbox-item {
    -fx-background-color: #506F86;
    -fx-background-color: #D8E9F0;
    -fx-text-fill: #FEFAEC;
	-fx-fill: #FEFAEC;
}

.list-cell:filled > #task-hbox-item .label {
    -fx-text-fill: #FEFAEC;
    -fx-text-fill: #454553;
}


.list-cell:filled:hover > #task-hbox-item {
    -fx-text-fill: #FEFAEC;
	-fx-fill: #FEFAEC;
}

#task-hbox-item {
    -fx-border-color: transparent;
    -fx-border-width: 0.0;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header-background .label {
    -fx-background-color: transparent;
    -fx-text-fill: #454553;
    -fx-font-size: 10.0pt;
    -fx-font-family: "System Font";
    -fx-alignment: center-left;
}

.table-view .column-header {
    -fx-background-color: transparent;
    -fx-text-fill: #454553;
    -fx-font-size: 10.0pt;
    -fx-font-family: "System Font";
}

.table-view .table-cell {
    -fx-font-size: 10.0pt;
    -fx-font-family: "System Font";
  	-fx-border-width: 0.0;
}

.table-row-cell {
    -fx-background-color: transparent;
  	-fx-border-color: transparent;
    -fx-text-fill: #454553;
}

.table-row-cell:odd {
    -fx-background-color: transparent;
    -fx-text-fill: #454553;
}

.table-row-cell:selected {
    -fx-background-color: #EB586F;
    -fx-text-fill: white;
}

.table-column-cell:empty { 
	-fx-background-color: transparent;
    -fx-border-style: solid;
    -fx-border-color: #454553;
}

.text-field {
	-fx-background-color: #454553;	
	-fx-background-color: #FEFAEC;
    -fx-font-size: 12.0pt;
    -fx-font-family: "System Font";
    -fx-text-fill: #454553;
    -fx-prompt-text-fill: #7A9EAF;
}

.title-bar {
	-fx-background-color: #3576A7;
}

.hbox-group {
	-fx-font-size: 20.0pt;
}

.image-view {
    -fx-padding: 6.0 6.0 6.0 10.0;
}

.side-bar {
	-fx-background-color: #454553;
}

.accent {
    -fx-background-color: #4AA0D5;
}

.button {
    -fx-background-color: transparent;
    -fx-background-insets: 0.0,1.0,2.0,3.0;
    -fx-background-radius: 3.0,2.0,2.0,2.0;
    -fx-text-fill: #FEFAEC;
    -fx-font-size: 8.0px;
    -fx-font-weight: bold;
}

#new-button {
    -fx-background-color: 
        #000000,
        linear-gradient(#7ebcea, #2f4b8f),
        linear-gradient(#426ab7, #263e75),
        linear-gradient(#395cab, #223768);
    -fx-background-insets: 0.0,1.0,2.0,3.0;
    -fx-background-radius: 3.0,2.0,2.0,2.0;
    -fx-text-fill: white;
    -fx-font-size: 12.0px;
}

#today-label {
    -fx-text-fill: #FEFAEC;
    -fx-font-size: 12.0px;
    -fx-font-weight: bold;
    -fx-text-alignment: center;
    -fx-padding: 0.0 0.0 8.0 0.0;
}
```
###### src/todolist/ui/views/EmptyView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.MainViewController">
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label text="THIS IS AN EMPTY VIEW" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0" opacity="0.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
###### src/todolist/ui/views/HelpView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.MainViewController">
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label style="-fx-font-weight: bold;" text="need help? we will be assisting you shortly!" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0" opacity="100.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="help" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
###### src/todolist/ui/views/MainView.fxml
``` fxml

<?import java.lang.String?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Font?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.MainViewController">
	<bottom>
		<TextField prefHeight="40.0" prefWidth="720.0"
			promptText="enter your command here ..." styleClass="text-field"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_LEFT">
			<font>
				<Font size="14.0" />
			</font>
			<BorderPane.margin>
				<Insets />
			</BorderPane.margin>
		</TextField>
	</bottom>
	<padding>
		<Insets left="6.0" top="6.0" />
	</padding>
	<styleClass>
		<String fx:value="main-view" />
		<String fx:value="split-pane" />
		<String fx:value="split-pane-divider" />
	</styleClass>
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label text="not sure how to use me? try 'tab help' !" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="tasks" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
###### src/todolist/ui/views/OverdueView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.OverdueController">
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label style="-fx-font-weight: bold;"
							text="you are right on schedule! no overdues! hooray!" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0" opacity="100.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="overdue" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
###### src/todolist/ui/views/RootLayout.fxml
``` fxml

<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.FlowPane?>
<?import javafx.scene.layout.HBox?>

<BorderPane minHeight="300.0" minWidth="300.0" prefHeight="600.0"
	prefWidth="800.0" styleClass="root-layout" stylesheets="@DefaultTheme.css"
	xmlns="http://javafx.com/javafx/8.0.65" xmlns:fx="http://javafx.com/fxml/1">
	<left>
		<FlowPane maxWidth="-Infinity" minWidth="-Infinity"
			prefWidth="60.0" BorderPane.alignment="TOP_LEFT" />
	</left>
	<center>
		<BorderPane minHeight="200.0" minWidth="200.0" prefHeight="540.0"
			prefWidth="740.0" BorderPane.alignment="TOP_LEFT" />
	</center>
	<top>
		<HBox maxWidth="-Infinity" minHeight="-Infinity" prefHeight="40.0"
			BorderPane.alignment="TOP_LEFT" />
	</top>
</BorderPane>
```
###### src/todolist/ui/views/SettingsView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.MainViewController">
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label style="-fx-font-weight: bold;" text="no settings for now ... get back to work!" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0" opacity="100.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="settings" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
###### src/todolist/ui/views/SideBarView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.geometry.Rectangle2D?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.VBox?>

<VBox alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
	maxWidth="-Infinity" minWidth="-Infinity" prefHeight="540.0" prefWidth="60.0"
	spacing="20.0" styleClass="side-bar" stylesheets="@DefaultTheme.css"
	xmlns="http://javafx.com/javafx/8.0.65" xmlns:fx="http://javafx.com/fxml/1"
	fx:controller="todolist.ui.controllers.SideBarController">
	<children>
		<Button fx:id="home" contentDisplay="TOP" mnemonicParsing="false"
			prefHeight="50.0" prefWidth="50.0" text="HOME">
			<VBox.margin>
				<Insets top="20.0" />
			</VBox.margin>
			<graphic>
				<ImageView id="home-icon" fx:id="homeIcon" fitHeight="30.0"
					fitWidth="30.0" pickOnBounds="true" preserveRatio="true">
					<viewport>
						<Rectangle2D />
					</viewport>
					<image>
						<Image url="@assets/home.png" />
					</image>
				</ImageView>
			</graphic>
		</Button>
		<Button fx:id="expired" contentDisplay="TOP" mnemonicParsing="false"
			prefHeight="50.0" prefWidth="50.0" text="EXPIRED">
			<graphic>
				<ImageView id="button-graphics" fx:id="expiredIcon"
					fitHeight="30.0" fitWidth="30.0" pickOnBounds="true" preserveRatio="true">
					<viewport>
						<Rectangle2D />
					</viewport>
					<image>
						<Image url="@assets/overdue.png" />
					</image>
				</ImageView>
			</graphic>
		</Button>
		<StackPane fx:id="todayStack" prefHeight="50.0" prefWidth="50.0">
			<children>
				<Button fx:id="today" contentDisplay="TOP" mnemonicParsing="false"
					prefHeight="50.0" prefWidth="50.0" text="TODAY">
					<graphic>
						<ImageView id="button-graphics" fx:id="todayIcon"
							fitHeight="30.0" fitWidth="30.0" pickOnBounds="true"
							preserveRatio="true">
							<viewport>
								<Rectangle2D />
							</viewport>
							<image>
								<Image url="@assets/today.png" />
							</image>
						</ImageView>
					</graphic>
				</Button>
				<Label id="today-label" fx:id="todayLabel" contentDisplay="CENTER"
					stylesheets="@DefaultTheme.css" text="00" />
			</children>
		</StackPane>
		<Button fx:id="week" contentDisplay="TOP" mnemonicParsing="false"
			prefHeight="50.0" prefWidth="50.0" text="WEEK">
			<graphic>
				<ImageView id="button-graphics" fx:id="weekIcon"
					fitHeight="30.0" fitWidth="30.0" pickOnBounds="true" preserveRatio="true">
					<viewport>
						<Rectangle2D />
					</viewport>
					<image>
						<Image url="@assets/week.png" />
					</image>
				</ImageView>
			</graphic>
		</Button>
		<Button fx:id="done" contentDisplay="TOP" mnemonicParsing="false"
			prefHeight="50.0" prefWidth="50.0" text="DONE">
			<graphic>
				<ImageView id="button-graphics" fx:id="doneIcon"
					fitHeight="30.0" fitWidth="30.0" pickOnBounds="true" preserveRatio="true">
					<viewport>
						<Rectangle2D />
					</viewport>
					<image>
						<Image url="@assets/archived.png" />
					</image>
				</ImageView>
			</graphic>
		</Button>
		<Button fx:id="options" contentDisplay="TOP" mnemonicParsing="false"
			prefHeight="50.0" prefWidth="50.0" text="OPTIONS">
			<graphic>
				<ImageView id="button-graphics" fx:id="optionsIcon"
					fitHeight="30.0" fitWidth="30.0" pickOnBounds="true" preserveRatio="true">
					<viewport>
						<Rectangle2D />
					</viewport>
					<image>
						<Image url="@assets/settings.png" />
					</image>
				</ImageView>
			</graphic>
		</Button>
		<Button fx:id="help" contentDisplay="TOP" mnemonicParsing="false"
			prefHeight="50.0" prefWidth="50.0" text="HELP">
			<graphic>
				<ImageView id="button-graphics" fx:id="helpIcon"
					fitHeight="30.0" fitWidth="30.0" pickOnBounds="true" preserveRatio="true">
					<viewport>
						<Rectangle2D />
					</viewport>
					<image>
						<Image url="@assets/help.png" />
					</image>
				</ImageView>
			</graphic>
		</Button>
	</children>
</VBox>
```
###### src/todolist/ui/views/TaskNode.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.shape.Circle?>
<?import javafx.scene.shape.Rectangle?>
<?import javafx.scene.text.Font?>

<HBox id="task-hbox-item" fx:id="root" alignment="CENTER_LEFT"
	maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	prefHeight="60.0" prefWidth="200.0" stylesheets="@DefaultTheme.css"
	xmlns="http://javafx.com/javafx/8.0.65" xmlns:fx="http://javafx.com/fxml/1">
	<children>
		<Rectangle fx:id="priorityLabel" fill="DODGERBLUE" height="70.0"
			stroke="BLACK" strokeType="INSIDE" strokeWidth="0.0" width="5.0" />
		<StackPane fx:id="numberLabel" prefWidth="50.0">
			<children>
				<Circle fx:id="numLabelBase" fill="DODGERBLUE" radius="25.0"
					stroke="BLACK" strokeType="INSIDE" strokeWidth="0.0">
					<StackPane.margin>
						<Insets left="10.0" right="10.0" />
					</StackPane.margin>
				</Circle>
				<Label fx:id="number" text="1" textFill="WHITE">
					<font>
						<Font name="System Bold" size="14.0" />
					</font>
				</Label>
			</children>
		</StackPane>
		<VBox fx:id="details" maxHeight="1.7976931348623157E308"
			maxWidth="1.7976931348623157E308" prefHeight="60.0" prefWidth="100.0"
			HBox.hgrow="ALWAYS">
			<children>
				<HBox fx:id="titleBox" alignment="CENTER_LEFT" maxHeight="1.7976931348623157E308"
					prefHeight="100.0">
					<children>
						<Label fx:id="title" maxHeight="1.7976931348623157E308"
							text="Title" textFill="WHITE" wrapText="true">
							<font>
								<Font name="System Bold" size="16.0" />
							</font>
						</Label>
						<ImageView fx:id="reminderIcon" fitHeight="15.0"
							fitWidth="15.0" pickOnBounds="true" preserveRatio="true">
							<HBox.margin>
								<Insets left="5.0" right="5.0" />
							</HBox.margin>
						</ImageView>
					</children>
					<VBox.margin>
						<Insets />
					</VBox.margin>
					<padding>
						<Insets top="10.0" />
					</padding>
				</HBox>
				<HBox fx:id="dateRangeBox" alignment="CENTER_LEFT" prefHeight="100.0"
					prefWidth="200.0">
					<children>
						<Circle fx:id="overdueFlag" fill="DODGERBLUE" radius="3.0"
							stroke="BLACK" strokeType="INSIDE" strokeWidth="0.0">
							<HBox.margin>
								<Insets right="2.0" />
							</HBox.margin>
						</Circle>
						<Label fx:id="dateRange" text="Date Range" textFill="WHITE">
							<font>
								<Font size="10.0" />
							</font>
						</Label>
					</children>
				</HBox>
				<HBox fx:id="categoryBox" alignment="CENTER_LEFT" prefHeight="100.0"
					prefWidth="200.0">
					<children>
						<Circle fx:id="categorySprite" fill="DODGERBLUE" radius="3.0"
							stroke="BLACK" strokeType="INSIDE" strokeWidth="0.0">
							<HBox.margin>
								<Insets right="2.0" />
							</HBox.margin>
						</Circle>
						<Label fx:id="category" text="Category" textFill="WHITE">
							<font>
								<Font size="10.0" />
							</font>
						</Label>
					</children>
					<padding>
						<Insets bottom="10.0" />
					</padding>
				</HBox>
			</children>
		</VBox>
		<StackPane fx:id="completeStatus" stylesheets="@DefaultTheme.css">
			<children>
				<Rectangle fx:id="statusBacking" arcHeight="10.0"
					arcWidth="10.0" fill="#ffeb1f" height="25.0" stroke="BLACK"
					strokeType="INSIDE" strokeWidth="0.0" width="55.0" />
				<Label fx:id="status" text="Ongoing">
					<font>
						<Font name="System Bold" size="10.0" />
					</font>
				</Label>
			</children>
			<HBox.margin>
				<Insets left="15.0" right="15.0" />
			</HBox.margin>
		</StackPane>
		<VBox fx:id="indicatorsHolder" maxHeight="1.7976931348623157E308"
			maxWidth="-Infinity" prefWidth="20.0" spacing="7.0">
			<children>
				<ImageView fx:id="recurringIndicator" fitHeight="10.0"
					fitWidth="10.0" pickOnBounds="true" preserveRatio="true">
					<image>
						<Image url="@assets/recurring-indicator.png" />
					</image>
					<VBox.margin>
						<Insets right="10.0" top="10.0" />
					</VBox.margin>
				</ImageView>
				<ImageView fx:id="reminderIndicator" fitHeight="10.0"
					fitWidth="10.0" layoutX="10.0" layoutY="20.0" pickOnBounds="true"
					preserveRatio="true">
					<image>
						<Image url="@assets/reminder-indicator.png" />
					</image>
				</ImageView>
			</children>
		</VBox>
	</children>
	<opaqueInsets>
		<Insets />
	</opaqueInsets>
</HBox>
```
###### src/todolist/ui/views/TitleBarView.fxml
``` fxml

<?import java.lang.String?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.HBox?>

<HBox alignment="CENTER" prefHeight="40.0" prefWidth="800.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1">
	<children>
		<ImageView fitHeight="28.0" fitWidth="1000.0" pickOnBounds="true"
			preserveRatio="true">
			<HBox.margin>
				<Insets />
			</HBox.margin>
			<image>
				<Image url="@assets/logo.png" />
			</image>
		</ImageView>
	</children>
	<styleClass>
		<String fx:value="title-bar" />
		<String fx:value="image-view" />
		<String fx:value="hbox-group" />
	</styleClass>
</HBox>
```
###### src/todolist/ui/views/TodayView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.TodayController">
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label style="-fx-font-weight: bold;" text="woohoo! you've got nothing else on for today!" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0" opacity="100.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="today" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
###### src/todolist/ui/views/WeekView.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TitledPane?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.text.Text?>

<BorderPane maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
	minHeight="300.0" minWidth="300.0" prefHeight="540.0" prefWidth="740.0"
	stylesheets="@DefaultTheme.css" xmlns="http://javafx.com/javafx/8.0.65"
	xmlns:fx="http://javafx.com/fxml/1" fx:controller="todolist.ui.controllers.WeekController">
	<center>
		<TitledPane animated="false" collapsible="false"
			maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308"
			stylesheets="@DefaultTheme.css" BorderPane.alignment="TOP_CENTER">
			<content>
				<ListView fx:id="listView" prefHeight="200.0" prefWidth="200.0"
					stylesheets="@DefaultTheme.css">
					<placeholder>
						<Label style="-fx-font-weight: bold;" text="wow! your week is free! congratulations!" />
					</placeholder>
					<opaqueInsets>
						<Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
					</opaqueInsets>
					<padding>
						<Insets bottom="5.0" />
					</padding>
				</ListView>
			</content>
			<graphic>
				<HBox id="titled-pane-hbox" alignment="TOP_CENTER" maxHeight="1.7976931348623157E308"
					maxWidth="1.7976931348623157E308" spacing="10.0" stylesheets="@DefaultTheme.css">
					<children>
						<ImageView fitHeight="20.0" fitWidth="20.0" opacity="100.0"
							pickOnBounds="true" preserveRatio="true">
							<image>
								<Image url="@assets/pen.png" />
							</image>
							<HBox.margin>
								<Insets />
							</HBox.margin>
						</ImageView>
						<Text id="titled-pane-hbox-text" fontSmoothingType="LCD"
							strokeType="OUTSIDE" strokeWidth="0.0" style="-fx-font-weight: bold;"
							text="week" textAlignment="CENTER" />
					</children>
				</HBox>
			</graphic>
		</TitledPane>
	</center>
</BorderPane>
```
