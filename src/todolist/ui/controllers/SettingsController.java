package todolist.ui.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.WeekView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import todolist.ui.TaskWrapper;

//@@author A0123994W

/* 
 * SettingsController controls and manipulates data for display on the main display area, for the week tab.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class SettingsController extends MainViewController {

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

    // Logger messages
    private static final String MESSAGE_UPDATED_SETTINGS_TASKLIST = "Updated display [OPTIONS].";

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
     * (non-Javadoc)
     * 
     * @see todolist.ui.controllers.MainViewController#initialize()
     */
    @FXML
    public void initialize() {
    }

    public static String getMessageUpdatedSettingsTasklist() {
        return MESSAGE_UPDATED_SETTINGS_TASKLIST;
    }

    public void loadCalendar(ObservableList<TaskWrapper> observableList) {

        WeekView weekView = new WeekView();

        Calendar schedule = new Calendar("Schedule");

        CalendarSource myCalendarSource = new CalendarSource("My Calendar");
        myCalendarSource.getCalendars().addAll(schedule);

        weekView.getCalendarSources().addAll(myCalendarSource);

        weekView.setRequestedTime(LocalTime.now());

        // ... Get weekly events here

        FilteredList<TaskWrapper> weekTasks = new FilteredList<TaskWrapper>(observableList,
                task -> task.getEndTime() != null
                        && task.getEndTime().getDayOfYear() / 7 == LocalDateTime.now().getDayOfYear() / 7);

        for (TaskWrapper task : weekTasks) {
            @SuppressWarnings("rawtypes")
            Entry<?> entry = new Entry();
            if (task.getStartTime() == null) {
                entry.setTitle(task.getTaskTitle());
                entry.setEndDate(task.getEndTime().toLocalDate());
                entry.setEndTime(task.getEndTime().toLocalTime());
                entry.setStartDate(task.getEndTime().toLocalDate());
                entry.setStartTime(task.getEndTime().toLocalTime().minusHours(1));
            } else {
                entry.setStartDate(task.getStartTime().toLocalDate());
                entry.setStartTime(task.getStartTime().toLocalTime());
                entry.setEndDate(task.getEndTime().toLocalDate());
                entry.setEndTime(task.getEndTime().toLocalTime());
                entry.setTitle(task.getTaskTitle());
            }
            entry.setCalendar(schedule);
            schedule.addEntry(entry);
        }

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        weekView.setToday(LocalDate.now());
                        weekView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        box.getChildren().set(1, weekView);
        VBox.setVgrow(weekView, Priority.ALWAYS);

    }

    public void plotGraph(ObservableList<TaskWrapper> observableList) {

        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        StackedBarChart<Number, String> timeTable = new StackedBarChart<Number, String>(xAxis, yAxis);
        org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern("d-MMM");

        monday = String.format(monday, LocalDateTime.now().withDayOfWeek(1).toString(format));
        tuesday = String.format(tuesday, LocalDateTime.now().withDayOfWeek(2).toString(format));
        wednesday = String.format(wednesday, LocalDateTime.now().withDayOfWeek(3).toString(format));
        thursday = String.format(thursday, LocalDateTime.now().withDayOfWeek(4).toString(format));
        friday = String.format(friday, LocalDateTime.now().withDayOfWeek(5).toString(format));
        saturday = String.format(saturday, LocalDateTime.now().withDayOfWeek(6).toString(format));
        sunday = String.format(sunday, LocalDateTime.now().withDayOfWeek(7).toString(format));
        
        
        yAxis.setCategories(FXCollections.observableArrayList(sunday, saturday, friday, thursday, wednesday, tuesday,
                monday, undated));
        HashMap<String, int[]> reference = new HashMap<String, int[]>();
        
        // ... filter list if necessary
        for (TaskWrapper task : observableList) {
            String catName = "uncategorised";
            if (task.getCategory() != null) {
                catName = task.getCategory().getCategory();
            }
            int[] sameCatTasks = reference.get(catName);
            
            // New category encountered
            if (sameCatTasks == null) {
                sameCatTasks = new int[8];
            }

            if (task.getStartTime() == null && task.getEndTime() == null) {
                sameCatTasks[0] += 1;
            } else if (task.getStartTime() == null && task.getEndTime() != null) {
                sameCatTasks[task.getEndTime().getDayOfWeek().getValue()] += 1;
            } else if (task.getStartTime() != null && task.getEndTime() != null) {
                sameCatTasks[task.getStartTime().getDayOfWeek().getValue()] += 1;
            }

            reference.put(catName, sameCatTasks);

        }

        // Create series for each category
        for (java.util.Map.Entry<String, int[]> entry : reference.entrySet()) {
            int[] sameCatTasks = entry.getValue();
            XYChart.Series<Number, String> series = new XYChart.Series<Number, String>();
            series.setName(entry.getKey());       
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[0], undated));
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[1], monday));
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[2], tuesday));
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[3], wednesday));
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[4], thursday));
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[5], friday));
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[6], saturday));
            series.getData().add(new XYChart.Data<Number, String>(sameCatTasks[7], sunday));

            timeTable.getData().add(series);
        }
                
        timeTable.backgroundProperty().set(Background.EMPTY);

        box.getChildren().set(1, timeTable);

    }
}
