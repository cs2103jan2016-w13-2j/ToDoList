package todolist.ui.controllers;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import todolist.MainApp;

//@@author Huang Lie Jun

public class SideBarController {

    /*** TAB STYLES ***/
    private static final String STYLE_TAB_NORMAL = "-fx-background-color: transparent;";
    private static final String STYLE_TAB_FOCUSED = "-fx-background-color: #95E1D3;";
//    private static final String STYLE_TAB_FOCUSED_DARK = "-fx-background-color: #EB586F;";
    
    
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
//                currentButton.setStyle(STYLE_TAB_FOCUSED_DARK);

            }
            
        }
        
    }

    public MainApp getMainApplication() {
        return mainApplication;
    }

}
