package todolist.ui.controllers;

import java.util.HashMap;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import todolist.MainApp;
import todolist.logic.UIHandler;
import todolist.ui.TaskWrapper;

//@@author A0123994W

/* 
 * SettingsController controls and manipulates data for display on the main display area.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class SettingsController extends MainViewController {

    private static final int POSITION_CHART_IN_VBOX = 1;
    private static String monday = "%1$s";
    private static String tuesday = "%1$s";
    private static String wednesday = "%1$s";
    private static String thursday = "%1$s";
    private static String friday = "%1$s";
    private static String saturday = "%1$s";
    private static String sunday = "%1$s";
    private static String undated = "Any";

    @FXML
    private StackedBarChart<Number, String> timeTable = null;

    @FXML
    private VBox box = null;

    @FXML
    private Text directoryDisplay = null;
    
    @FXML
    private Text soundStatus = null;

    // Logger messages
    private static final String MESSAGE_UPDATED_SETTINGS = "Updated display [OPTIONS].";

    /*
     * Constructor overrides super constructor and intializes the display task
     * list and list view.
     * 
     */
    public SettingsController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
    }

    /*
     * setMainApp takes a MainApp reference and stores it locally as the
     * mainApplication reference.
     * 
     * @param MainApp mainApp is the reference provided to the calling function
     * 
     */
    public void setMainApp(MainApp mainApp, UIHandler uiHandlerUnit) {
        setMainApplication(mainApp);
        setUiHandler(uiHandlerUnit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see todolist.ui.controllers.MainViewController#initialize()
     */
    @FXML
    public void initialize() {
    }

    /*
     * getMessageUpdatedSettings returns the standard output message for display when the settings view gets updated.
     * 
     * @return String MESSAGE_UPDATED_SETTINGS
     * 
     */
    public static String getMessageUpdatedSettings() {
        return MESSAGE_UPDATED_SETTINGS;
    }
    
    /*
     * Deprecated. We drop the use of CalendarFX due to the requirement for license.
     */
    public void loadCalendar(ObservableList<TaskWrapper> observableList) {

    }

    /*
     * setupPage takes in an observable list of tasks (wrapped) and filters for a neat weekly summary. 
     * The options area will also be dynamically linked to the relevant listeners for changes and updates.
     * 
     * @param ObservableList<TaskWrapper> observableList
     * 
     */
    public void setupPage(ObservableList<TaskWrapper> observableList) {
        
        // Update directory
        updateDirectory();
        
        // Create chart
        StackedBarChart<Number, String> timeTable = buildChart(observableList);
        timeTable.backgroundProperty().set(Background.EMPTY);
        box.getChildren().set(POSITION_CHART_IN_VBOX, timeTable);
    }

    private StackedBarChart<Number, String> buildChart(ObservableList<TaskWrapper> observableList) {
        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        StackedBarChart<Number, String> timeTable = new StackedBarChart<Number, String>(xAxis, yAxis);
        org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern("d-MMM");

        formatChartLabelsWithDate(format);

        yAxis.setCategories(FXCollections.observableArrayList(sunday, saturday, friday, thursday, wednesday, tuesday,
                monday, undated));
        HashMap<String, int[]> reference = new HashMap<String, int[]>();

        // ... filter list if necessary
        LocalDateTime startOfWeek = LocalDateTime.now().withDayOfWeek(1).withTime(0, 0, 0, 0);
        LocalDateTime endOfWeek = LocalDateTime.now().withDayOfWeek(7).withTime(23, 59, 59, 0);                
        
        for (TaskWrapper task : observableList) {
            
            // Skip deadlines and events that are out of the week zone
            if (isOutOfWeekRange(startOfWeek, endOfWeek, task)) {
                continue;
            }
            
            // Get category name
            String catName = "uncategorised";
            if (task.getCategory() != null) {
                catName = task.getCategory().getCategory();
            }
            
            int[] sameCatTasks = reference.get(catName);

            // New category encountered
            if (sameCatTasks == null) {
                sameCatTasks = new int[8];
            }

            // Keep count for types of tasks under category
            if (task.getStartTime() == null && task.getEndTime() == null) {
                sameCatTasks[0] += 1;
            } else if (task.getStartTime() == null && task.getEndTime() != null) {
                sameCatTasks[task.getEndTime().getDayOfWeek().getValue()] += 1;
            } else if (task.getStartTime() != null && task.getEndTime() != null) {
                sameCatTasks[task.getStartTime().getDayOfWeek().getValue()] += 1;
            }

            // Update reference hashtable
            reference.put(catName, sameCatTasks);

        }

        // Create series for each category
        for (java.util.Map.Entry<String, int[]> entry : reference.entrySet()) {
            int[] sameCatTasks = entry.getValue();
            XYChart.Series<Number, String> series = new XYChart.Series<Number, String>();
            setEntryInSeries(entry, sameCatTasks, series);
            timeTable.getData().add(series);
        }
        return timeTable;
    }

    private void updateDirectory() {
        String path = getUiHandler().getPath();
        directoryDisplay.setText(path);
    }

    private void setEntryInSeries(java.util.Map.Entry<String, int[]> entry, int[] sameCatTasks,
            XYChart.Series<Number, String> series) {
        series.setName(entry.getKey());
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[0], undated));
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[1], monday));
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[2], tuesday));
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[3], wednesday));
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[4], thursday));
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[5], friday));
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[6], saturday));
        series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[7], sunday));
    }

    private boolean isOutOfWeekRange(LocalDateTime startOfWeek, LocalDateTime endOfWeek, TaskWrapper task) {
        return task.getEndTime() != null && !isWithinWeek(startOfWeek, endOfWeek, task.getEndTime());
    }

    private boolean isWithinWeek(LocalDateTime startOfWeek, LocalDateTime endOfWeek, java.time.LocalDateTime endTime) {

        int millis = 0;
        int seconds = endTime.getSecond();
        int minutes = endTime.getMinute();
        int hours = endTime.getHour();
        int day = endTime.getDayOfMonth();
        int month = endTime.getMonthValue();
        int year = endTime.getYear();
        
        LocalDateTime endTimeFormatted = new LocalDateTime();
        endTimeFormatted = endTimeFormatted.withDate(year, month, day);
        endTimeFormatted = endTimeFormatted.withTime(hours, minutes, seconds, millis);
        
        return endTimeFormatted.isAfter(startOfWeek) && endTimeFormatted.isBefore(endOfWeek);    
    }

    private void formatChartLabelsWithDate(org.joda.time.format.DateTimeFormatter format) {
        monday = String.format(monday, LocalDateTime.now().withDayOfWeek(1).toString(format));
        tuesday = String.format(tuesday, LocalDateTime.now().withDayOfWeek(2).toString(format));
        wednesday = String.format(wednesday, LocalDateTime.now().withDayOfWeek(3).toString(format));
        thursday = String.format(thursday, LocalDateTime.now().withDayOfWeek(4).toString(format));
        friday = String.format(friday, LocalDateTime.now().withDayOfWeek(5).toString(format));
        saturday = String.format(saturday, LocalDateTime.now().withDayOfWeek(6).toString(format));
        sunday = String.format(sunday, LocalDateTime.now().withDayOfWeek(7).toString(format));
    }
    
    /*
     * setSoundStatus takes in a status string descriptor and sets the sound status display text accordingly.
     * 
     * @param String status
     * 
     */
    public void setSoundStatus(String status) {
        soundStatus.setText(status); 
    }
}
