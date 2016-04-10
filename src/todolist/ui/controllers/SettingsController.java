package todolist.ui.controllers;

import java.time.LocalDate;
import java.time.LocalTime;

import org.joda.time.LocalDateTime;

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

    private final static String monday = "Mon";
    private final static String tuesday = "Tue";
    private final static String wednesday = "Wed";
    private final static String thursday = "Thu";
    private final static String friday = "Fri";
    private final static String saturday = "Sat";
    private final static String sunday = "Sun";

    // private XYChart.Series<Number, String> series1 = new XYChart.Series<>();
    // private XYChart.Series<Number, String> series2 = new XYChart.Series<>();

    // private NumberAxis xAxis = new NumberAxis();
    // private CategoryAxis yAxis = new CategoryAxis();

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

    public void plotGraph() {

        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        StackedBarChart<Number, String> timeTable = new StackedBarChart<Number, String>(xAxis, yAxis);
        yAxis.setCategories(
                FXCollections.observableArrayList(monday, tuesday, wednesday, thursday, friday, saturday, sunday));

        XYChart.Series<Number, String> series1 = new XYChart.Series<Number, String>();
        series1.getData().add(new XYChart.Data<Number, String>(25, monday));
        series1.getData().add(new XYChart.Data<Number, String>(89, monday));
        series1.getData().add(new XYChart.Data<Number, String>(20, tuesday));
        series1.getData().add(new XYChart.Data<Number, String>(10, wednesday));
        series1.getData().add(new XYChart.Data<Number, String>(3, thursday));
        series1.getData().add(new XYChart.Data<Number, String>(12, friday));

        XYChart.Series<Number, String> series2 = new XYChart.Series<Number, String>();
        series2.getData().add(new XYChart.Data<Number, String>(57, monday));
        series2.getData().add(new XYChart.Data<Number, String>(41, tuesday));
        series2.getData().add(new XYChart.Data<Number, String>(45, wednesday));
        series2.getData().add(new XYChart.Data<Number, String>(11, thursday));
        series2.getData().add(new XYChart.Data<Number, String>(14, friday));

        timeTable.getData().add(series1);
        timeTable.getData().add(series2);

        box.getChildren().set(1, timeTable);

    }
}
