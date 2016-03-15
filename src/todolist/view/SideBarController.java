package todolist.view;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import todolist.MainApp;

public class SideBarController {

    /*** VIEWS ***/
    @FXML
    private Button home = null;
    @FXML
    private ImageView homeIcon = null;

    @FXML
    private Button expired = null;
    @FXML
    private ImageView expiredIcon = null;

    @FXML
    private StackPane todayStack = null;
    @FXML
    private Label todayLabel = null;
    @FXML
    private Button today = null;
    @FXML
    private ImageView todayIcon = null;

    @FXML
    private Button week = null;
    @FXML
    private ImageView weekIcon = null;

    @FXML
    private Button done = null;
    @FXML
    private ImageView doneIcon = null;

    @FXML
    private Button options = null;
    @FXML
    private ImageView optionsIcon = null;

    @FXML
    private Button help = null;
    @FXML
    private ImageView helpIcon = null;

    /*** MAIN APP ***/
    private MainApp mainApplication = null;

    private int todayDate = 0;

    private int index = 0;

    private static int NUMBER_BUTTONS = 7;

    private Button[] buttonArray;

    public SideBarController() {

    }

    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
    }

    @FXML
    public void initialize() {
        buttonArray = new Button[NUMBER_BUTTONS];
        buttonArray[0] = home;
        buttonArray[1] = expired;
        buttonArray[2] = today;
        buttonArray[3] = week;
        buttonArray[4] = done;
        buttonArray[5] = options;
        buttonArray[6] = help;

        setTodayDate();
        setInitialIndex();
    }

    private void setInitialIndex() {
        this.index = 1;
        colourTab();        
    }

    public void setIndex(int index) {
        this.index = index;
        mainApplication.loadPage(index);
        colourTab();
    }

    private void colourTab() {
        for (int i = 0; i < NUMBER_BUTTONS; ++i) {
            Button currentButton = buttonArray[i];
            currentButton.setStyle("-fx-background-color: transparent;");

            if (i == index - 1) {
                currentButton.setStyle("-fx-background-color: #95E1D3;");
            }
        }
    }

    private void setTodayDate() {
        todayDate = LocalDateTime.now().getDayOfMonth();

        if (todayLabel != null) {
            todayLabel.setText(Integer.toString(todayDate));
        }

    }

    public MainApp getMainApplication() {
        return mainApplication;
    }

}
