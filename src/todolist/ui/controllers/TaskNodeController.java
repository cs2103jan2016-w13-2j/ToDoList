package todolist.ui.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

import com.sun.media.jfxmedia.logging.Logger;

import todolist.MainApp;
import todolist.model.Category;
import todolist.ui.TaskWrapper;
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
        if (task.getIsDone()) {
            statusBacking.setFill(Color.web(COLOR_COMPLETE));
            status.setText(DISPLAY_ITEM_ARCHIVED);
        } else {
            statusBacking.setFill(Color.web(COLOR_INCOMPLETE));
            status.setText(DISPLAY_ITEM_UNARCHIVED);
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
