# Huang Lie Jun
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

    /*
     * setWindowDimensions initializes the window properties for application display.
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
###### src/todolist/ui/controllers/MainViewController.java
``` java

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
	
	public String path = "demo.txt";
	
	public int demoCounter = 0;

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
```
###### src/todolist/common/UtilityLogger.java
``` java

/*
 * UtilityLogger is a common logger to log all ToDoList activities at runtime.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class UtilityLogger {

    private static final int MAX_LOG_SIZE = 1048576;
    private static final int NUMBER_OF_LOGS = 1;
    private static final boolean IS_APPEND = false;

    private static final String ACTION = "ACTION >> ";
    private static final String ERROR = "ERROR >> ";
    private static final String EXCEPTION = "EXCEPTION >> ";
    private static final String COMPONENTCALL = "COMPONENT CALL >> ";

    private static final String UI = "UI";
    private static final String LOGIC = "LOGIC";
    private static final String PARSER = "PARSER";
    private static final String STORAGE = "STORAGE";
    private static final String UNKNOWN = "UNKNOWN";
    private static final String LOGGER_NAME = "ToDoList-Logger";
    private static final String ERROR_CREATE_LOG = "Error accessing log file.";
    private static final String ERROR_GET_DEFAULT_PATH = "Error obtaining default application directory for file path.";

    private static File logDirectory = null;
    private Logger logger = null;
    private static FileHandler fileHandler = null;

    private static enum Component {
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
