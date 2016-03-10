package todolist.view;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.IllegalFormatException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import todolist.MainApp;
import todolist.model.TaskWrapper;

public class TaskNodeController {

//    private static enum PriorityLevel {
//        URGENT, NORMAL, CASUAL
//    }

    private static final String COLOR_FLOATING = "#3BB873";
    private static final String COLOR_DEADLINE = "#FF6464";
    private static final String COLOR_EVENT = "#F3825F";

    private static final String COLOR_OVERDUE = "#FF6464";
    private static final String COLOR_TODAY = "#FAAC64";
    private static final String COLOR_SPARE = "#5BAAEC";

//    private static final String COLOR_IMMEDIATE = "#FF6464";
//    private static final String COLOR_URGENT = "#FF6464";
//    private static final String COLOR_NEUTRAL = "#FF6464";

    private static final String COLOR_COMPLETE = "#5BE7A9";
    private static final String COLOR_INCOMPLETE = "#FAAC64";

    private static final String COLOR_UNKNOWN = "#748B9C";

    private TaskWrapper task = null;
    private int index = -1;

    @FXML
    private HBox root = null;

    @FXML
    private Rectangle priorityLabel = null;

    @FXML
    private StackPane numberLabel = null;
    @FXML
    private Circle numLabelBase = null;
    @FXML
    private Label number = null;

    @FXML
    private VBox details = null;

    @FXML
    private HBox titleBox = null;
    @FXML
    private Label title = null;
    @FXML
    private ImageView reminderIcon = null;

    @FXML
    private HBox dateRangeBox = null;
    @FXML
    private Circle overdueFlag = null;
    @FXML
    private Label dateRange = null;

    @FXML
    private HBox categoryBox = null;
    @FXML
    private Circle categorySprite = null;
    @FXML
    private Label category = null;

    @FXML
    private StackPane completeStatus = null;
    @FXML
    private Rectangle statusBacking = null;
    @FXML
    private Label status = null;

    public TaskNodeController(TaskWrapper task, int index) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainApp.DIRECTORY_TASKITEM));
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
            mapTaskToNode();
        } catch (IllegalArgumentException iae) {
            // call feedback modal
        }

    }

    public void mapTaskToNode() throws IllegalFormatException {
        int indexNumber = index + 1;
        String taskTitle = task.getTaskTitle();
        String category = null;

        if (indexNumber <= 0) {
            throw new IllegalArgumentException("Task Display: Index number out of bounds.");
        } else {
            number.setText(Integer.toString(indexNumber));
        }

        if (taskTitle == null) {
            throw new IllegalArgumentException("Task Display: Task title invalid.");
        } else {
            title.setText(task.getTaskTitle());
        }

        if (task.getCategory() == null) {
            category = new String("Category: Not Available");
        } else {
            category = task.getCategory().getCategory();
        }

        try {
            dateRange.setText(parseDateTimeRange(task));
        } catch (IllegalArgumentException iae) {
            throw iae;
        }

        this.category.setText(category);

        categorySprite.setFill(Color.web(COLOR_UNKNOWN));

        if (task.getIsDone()) {
            statusBacking.setFill(Color.web(COLOR_COMPLETE));
            status.setText("DONE");
        } else {
            statusBacking.setFill(Color.web(COLOR_INCOMPLETE));
            status.setText("ONGOING");
        }

        // reminderIcon.setImage(new Image("reminderIcon.png"));
        //
        // if (task.getReminder() == null || !task.getReminder().getStatus()) {
        // reminderIcon.setEffect(new ColorAdjust(1, 1, 1, 1));
        // }
    }

    public HBox getNode() {
        return root;
    }

    public String parseDateTimeRange(TaskWrapper task) throws IllegalArgumentException {
        LocalDateTime startDateTime = task.getStartTime();
        LocalDateTime endDateTime = task.getEndTime();

        // Handle Floating Task
        if (startDateTime == null && endDateTime == null) {
            numLabelBase.setFill(Color.web(COLOR_FLOATING));
            overdueFlag.setFill(Color.web(COLOR_SPARE));
            return "Anytime";
            // Handle Deadline
        } else if (startDateTime == null && endDateTime != null) {
            numLabelBase.setFill(Color.web(COLOR_DEADLINE));

            if (endDateTime.isBefore(LocalDateTime.now())) {
                overdueFlag.setFill(Color.web(COLOR_OVERDUE));
            } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), endDateTime) <= 24) {
                overdueFlag.setFill(Color.web(COLOR_TODAY));
            } else {
                overdueFlag.setFill(Color.web(COLOR_SPARE));
            }

            return "Due: " + endDateTime.getDayOfWeek() + ", "
                    + endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
            // Handle Event
        } else if (startDateTime != null && endDateTime != null) {
            numLabelBase.setFill(Color.web(COLOR_EVENT));

            if (startDateTime.isBefore(LocalDateTime.now())) {
                overdueFlag.setFill(Color.web(COLOR_OVERDUE));
            } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), startDateTime) <= 24) {
                overdueFlag.setFill(Color.web(COLOR_TODAY));
            } else {
                overdueFlag.setFill(Color.web(COLOR_SPARE));
            }

            if (startDateTime.getDayOfYear() == endDateTime.getDayOfYear()
                    && startDateTime.getYear() == endDateTime.getYear()) {
                return startDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ", from "
                        + startDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) + " to "
                        + endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
                // Handle Exceptions
            } else {
                return "From " + startDateTime.getDayOfWeek() + ", "
                        + startDateTime
                                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
                        + " to " + endDateTime.getDayOfWeek() + ", " + endDateTime
                                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
            }
            // Handle Exceptions
        } else {
            numLabelBase.setFill(Color.web(COLOR_UNKNOWN));
            overdueFlag.setFill(Color.web(COLOR_UNKNOWN));

            return "Not Available";
        }
    }

}
