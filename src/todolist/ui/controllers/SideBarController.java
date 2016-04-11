package todolist.ui.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import todolist.MainApp;
import todolist.common.UtilityLogger;
import todolist.ui.TaskWrapper;

//@@author A0123994W

/*
 * SideBarController controls the interface for the sidebar or tabs
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class SideBarController {

    /*** TAB STYLES ***/
    private static final String STYLE_TAB_NORMAL = "-fx-background-color: transparent;";
    private static final String STYLE_TAB_FOCUSED = "-fx-background-color: #069A8E;";
    // private static final String STYLE_TAB_FOCUSED_DARK =
    // "-fx-background-color: #EB586F;";

    /*** VIEWS ***/

    // ALL TAB
    @FXML
    private Button all = null;
    @FXML
    private ImageView allIcon = null;
    @FXML
    private Label allBubble = null;

    // EXPIRED TAB
    @FXML
    private Button expired = null;
    @FXML
    private ImageView expiredIcon = null;
    @FXML
    private Label expiredBubble = null;

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
    @FXML
    private Label todayBubble = null;

    // WEEK TAB
    @FXML
    private Button week = null;
    @FXML
    private ImageView weekIcon = null;
    @FXML
    private Label weekBubble = null;

    // DONE TAB
    @FXML
    private Button done = null;
    @FXML
    private ImageView doneIcon = null;
    @FXML
    private Label doneBubble = null;

    // OPTIONS TAB
    @FXML
    private Button options = null;
    @FXML
    private ImageView optionsIcon = null;

    // HELP TAB
    @FXML
    public Button help = null;
    @FXML
    private ImageView helpIcon = null;

    /*** SIDEBAR AND PAGE PROPERTIES ***/

    private int index = -1;
    private static int NUMBER_BUTTONS = 7;
    private Button[] buttonArray = null;
    private HashMap<Button, Integer> buttonHash = null;

    // Main Application reference
    private MainApp mainApplication = null;

    // Logger and Logger messages
    UtilityLogger logger = null;

    /*** CORE FUNCTIONS ***/

    /*
     * setMainApp sets the reference to link back to main application.
     * 
     * @param MainApp mainApp
     * 
     */
    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
    }

    @FXML
    public void initialize() {
        index = MainApp.getDefaultTab();
        logger = new UtilityLogger();
        setButtonArray();
        setButtonHash();
        setClickTabLogic();
        setTodayDate();
        colourTab();
    }

    /*
     * setButtonArray initializes the array of buttons
     */
    private void setButtonArray() {
        buttonArray = new Button[NUMBER_BUTTONS];
        buttonArray[0] = all;
        buttonArray[1] = expired;
        buttonArray[2] = today;
        buttonArray[3] = week;
        buttonArray[4] = done;
        buttonArray[5] = options;
        buttonArray[6] = help;
    }

    /*
     * setClickTabLogic sets the on-click event for tabs
     */
    private void setClickTabLogic() {
        for (int i = 0; i < buttonArray.length; ++i) {
            Button button = buttonArray[i];
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int index = getButtonIndex(button);
                    mainApplication.loadPage(index);
                }
            });
        }

        todayLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = getButtonIndex(today);
                mainApplication.loadPage(index);
            }
        });
    }

    /*
     * setButtonHash maps each button to its index or sequence on the sidebar
     */
    private void setButtonHash() {
        buttonHash = new HashMap<Button, Integer>();
        buttonHash.put(all, 1);
        buttonHash.put(expired, 2);
        buttonHash.put(today, 3);
        buttonHash.put(week, 4);
        buttonHash.put(done, 5);
        buttonHash.put(options, 6);
        buttonHash.put(help, 7);
    }

    /*
     * setTodayDate sets the date display label on the Today tab button
     */
    private void setTodayDate() {
        todayDate = LocalDateTime.now().getDayOfMonth();
        todayLabel.setText(Integer.toString(todayDate));
    }

    /*
     * setIndex takes in a page / tab number and navigates to it if valid.
     * 
     * @param int index
     * 
     */
    public void setIndex(int index) {
        
        if (index != MainApp.getHelpTab()) {
            this.index = index;
            colourTab();
        }

    }

    /*
     * colourTab sets the highlight logic for the tab buttons, based on the
     * current tab in focus
     */
    private void colourTab() {
        for (int i = 0; i < NUMBER_BUTTONS; ++i) {
            Button currentButton = buttonArray[i];
            currentButton.setStyle(STYLE_TAB_NORMAL);

            // Highlight if focused
            if (i == index - 1) {
                currentButton.setStyle(STYLE_TAB_FOCUSED);
            }
        }
    }

    /*
     * getMainApplication is an access function for the reference to main
     * application
     */
    public MainApp getMainApplication() {
        return mainApplication;
    }

    /*
     * getIndex returns the current tab number
     * 
     * @return int tabNumber
     * 
     */
    public int getIndex() {
        return index;
    }

    /*
     * getButtonIndex takes in a button and returns its position / page /
     * sequence number in the sidebar.
     * 
     * @param Button button
     * 
     * @return int index
     * 
     */
    private int getButtonIndex(Button button) {
        if (buttonHash.get(button) != null) {
            return buttonHash.get(button);
        } else {
            return -1;
        }
    }

    /*
     * getTabName returns the tab name given its sequence or index number
     * 
     * @param int indexNumber
     * 
     * @return String tabName
     * 
     */
    public String getTabName(int indexNumber) {
        switch (indexNumber) {
        case 1:
            return "ALL";
        case 2:
            return "EXPIRED";
        case 3:
            return "TODAY";
        case 4:
            return "WEEK";
        case 5:
            return "DONE";
        case 6:
            return "OPTIONS";
        case 7:
            return "HELP";
        default:
            return "UNKNOWN";
        }
    }

    public void linkBubbles(MainViewController[] controllers) {
        // Bubble Array
        Label[] bubbles = { allBubble, expiredBubble, todayBubble, weekBubble, doneBubble };

        for (int i = 0; i < controllers.length; ++i) {
            MainViewController controller = controllers[i];

            if (controller != null) {
                FilteredList<TaskWrapper> incomplete = new FilteredList<TaskWrapper>(
                        controller.getTaskListView().getItems(), task -> !task.getIsCompleted());
                bubbles[i].textProperty().bind(Bindings.format("%d", Bindings.size(incomplete)));
                
            }
            
            if (Integer.parseInt(bubbles[i].getText()) == 0) {
                bubbles[i].setVisible(false);
            } else {
                bubbles[i].setVisible(true);
            }
        }
    }
}
