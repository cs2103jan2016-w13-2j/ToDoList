package todolist.ui.controllers;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import todolist.MainApp;
import todolist.common.UtilityLogger;
import todolist.model.Category;
import todolist.model.Reminder;
import todolist.ui.TaskWrapper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

//@@author huangliejun

/*
 * TaskNodeController controls and manipulates data for display in task view(s).
 * 
 * @author Huang Lie Jun (A0123994W)
 * 
 */
public class TaskNodeController {

    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOURS = 3600;
    private static final String DISPLAY_ITEM_HEADER_DUE = "Due: ";
    /*** STATIC MESSAGES ***/

    // ERRORS
    private static final String ERROR_DISPLAY_ITEM_INDEX = "Task Display: Index number out of bounds.";
    private static final String ERROR_DISPLAY_ITEM_TITLE = "Task Display: Task title invalid.";

    // DEFAULTS
    private static final String NULL_DISPLAY_ITEM_CATEGORY = "uncategorised";
    private static final String DISPLAY_ITEM_UNARCHIVED = "ONGOING";
    private static final String DISPLAY_ITEM_ARCHIVED = "DONE";
    private static final String DISPLAY_ITEM_OVERDUE = "OVERDUE";
    private static final String DISPLAY_ITEM_HEADER_CATEGORY = "";

    /*** STYLES ***/

    // COLORS
    private static final String[] colorsHex = { "#27E1CE", "#D6EC78", "#FF98DA", "#FA7E0A", "#7A8CF0", "#A45FE6",
            "#F4722B", "#B3A78C", "#AA3763", "#6078EA", "#FA5555", "#F7FB76", "#F98B60", "#FFC057", "#B64926",
            "#468966", "#7BC74D", "#A97555", "#6639A6", "#F6C90E", "#BEEB9F", "#FCC29A", "#C83B3B", "#537780",
            "#88304E", "#7AA5D2", "#A75265", "#57385C", "#F66095", "#D6C8FF", "#C79ECF", "#7E6BC4", "#ABD4C1",
            "#7E858B" };

    // TASK TYPE STYLE
    private static final String COLOR_FLOATING = "#3BB873";
    private static final String COLOR_DEADLINE = "#FF6464";
    private static final String COLOR_EVENT = "#F3825F";

    // TASK OVERDUE STYLE
    private static final String COLOR_OVERDUE = "#FF6464";
    private static final String COLOR_TODAY = "#FAAC64";
    private static final String COLOR_SPARE = "#A4D792";

    // TASK COMPLETION STYLE
    private static final String COLOR_COMPLETE = "#5BE7A9";
    private static final String COLOR_INCOMPLETE = "#FAAC64";

    // UNKNOWN STYLE
    private static final String COLOR_UNKNOWN = "#748B9C";

    // Logger and Logger messages
    UtilityLogger logger = null;

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

    // Content-RELATIVE TIME
    @FXML
    private HBox relativeRangeBox = null;
    @FXML
    private Label relativeRange = null;

    // Content-DATE(S)
    @FXML
    private HBox dateRangeBox = null;
    @FXML
    private Circle dateRangeSprite = null;
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

    // Date-Time Field Live Update Interval (in seconds)
    private static final int UPDATE_INTERVAL = 1;
    private static final int ZONE_OFFSET = 8;

    /*
     * Constructor intializes task and index of task.
     */
    public TaskNodeController(TaskWrapper task, int index) throws IllegalArgumentException {
        logger = new UtilityLogger();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(MainApp.DIRECTORY_TASKITEM));
        fxmlLoader.setController(this);

        this.task = task;
        this.index = index;

        try {
            fxmlLoader.load();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        // Assignment TaskWrapper to HBox Layout
        try {
            validateAndFormatItem();
        } catch (IllegalArgumentException iae) {
            throw iae;
        }

    }

    /*
     * validateAndFormatItem checks the validity of the display content and
     * formats it for display.
     * 
     */
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
            logger.logError(UtilityLogger.Component.UI, ERROR_DISPLAY_ITEM_INDEX);
            throw new IllegalArgumentException(ERROR_DISPLAY_ITEM_INDEX);
        } else {
            number.setText(Integer.toString(indexNumber));
        }

        // Title
        if (taskTitle == null) {
            logger.logError(UtilityLogger.Component.UI, ERROR_DISPLAY_ITEM_TITLE);
            throw new IllegalArgumentException(ERROR_DISPLAY_ITEM_TITLE);
        } else {
            title.setText(taskTitle);
        }

        // Category
        if (category == null) {
            categoryName = new String(NULL_DISPLAY_ITEM_CATEGORY);
            categorySprite.setFill(Color.web(COLOR_UNKNOWN));
            // numLabelBase.setFill(Color.web(COLOR_UNKNOWN));
        } else {
            categoryName = DISPLAY_ITEM_HEADER_CATEGORY + category.getCategory();
            Color catColor = Color.web(colorsHex[Math.abs(categoryName.hashCode()) % colorsHex.length]);
            categorySprite.setFill(catColor);
            // numLabelBase.setFill(catColor);

        }

        this.category.setText(categoryName);

        // Dates
        try {
            final Timeline timeline = new Timeline(
                    new KeyFrame(javafx.util.Duration.ZERO, new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            formatDateField(task, startDateTime, endDateTime);
                        }
                    }), new KeyFrame(javafx.util.Duration.seconds(UPDATE_INTERVAL)));

            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        } catch (IllegalArgumentException iae) {
            throw iae;
        }

        // Archive Status
        if (task.getIsCompleted()) {
            statusBacking.setFill(Color.web(COLOR_COMPLETE));
            status.setText(DISPLAY_ITEM_ARCHIVED);
        } else if (task.getIsExpired()) {
            statusBacking.setFill(Color.web(COLOR_OVERDUE));
            status.setText(DISPLAY_ITEM_OVERDUE);
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

    /*
     * getNode returns the formatted task view for display.
     * 
     * @return HBox getNode
     */
    public HBox getNode() {
        return root;
    }

    /*
     * formatDateField takes in a task, start date-time, end date-time to format
     * date range field for display.
     * 
     * @param TaskWrapper task, LocalDateTime startDateTime, LocalDateTime
     * endDateTime
     * 
     * @return String dateTimeField
     */
    public void formatDateField(TaskWrapper task, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TASK_TYPE taskType = checkTaskType(startDateTime, endDateTime);
        String outputDate = "";
        String relativeText = "ad-hoc task";

        switch (taskType) {

        case FLOAT:
            outputDate = "Anytime";
            break;
        case DEADLINE:
            outputDate = formatDeadlineRange(endDateTime);
            relativeText = formatDeadlineRelativeText(endDateTime);
            break;
        case EVENT:
            // Smart formatting of range
            if (startDateTime.getDayOfYear() == endDateTime.getDayOfYear()
                    && startDateTime.getYear() == endDateTime.getYear()) {
                outputDate = formatEventRangeSameDay(startDateTime, endDateTime);
            } else {
                outputDate = formatEventRangeDiffDay(startDateTime, endDateTime);
            }
            relativeText = formatEventRelativeText(startDateTime, endDateTime);
            break;
        default:
            outputDate = "Not Available";
        }

        setStyle(startDateTime, endDateTime, task.getIsCompleted());

        this.relativeRange.setText(relativeText);
        dateRange.setText(outputDate);
        this.relativeRange.setFont(Font.font("Calibri", FontPosture.ITALIC, 14));
    }

    /*
     * checkTaskType takes in the start date-time and end date-time to determine
     * the task type.
     * 
     * @param LocalDateTime startDateTime, LocalDateTime endDateTime
     * 
     * @return TASK_TYPE
     * 
     */
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

    /*
     * formatEventRange
     * 
     * @param LocalDateTime startDateTime, LocalDateTime endDateTime
     * 
     * @return String eventRange
     */
    private String formatEventRelativeText(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        // Output will consists of two parts: [start time reference] and
        // remaining time left
        String startOutput = null;
        String durationOutput = null;

        LocalDate startDate = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        LocalDate endDate = endDateTime.toLocalDate();
        LocalTime endTime = endDateTime.toLocalTime();
        LocalDate nowDate = LocalDateTime.now().toLocalDate();
        LocalTime nowTime = LocalDateTime.now().toLocalTime();

        Period dateDifference = null;
        Duration timeDifference = null;
        String yearDiff = "";
        String monthDiff = "";
        String dayDiff = "";
        String hourDiff = "";
        String minDiff = "";

        PrettyTime prettyParser = new PrettyTime();
        Instant startInstant = startDateTime.toInstant(ZoneOffset.ofHours(ZONE_OFFSET));
        Date start = Date.from(startInstant);
        Instant endInstant = endDateTime.toInstant(ZoneOffset.ofHours(ZONE_OFFSET));
        Date end = Date.from(endInstant);

        // Set output for the start of event
        startOutput = prettyParser.format(start);

        String relativeStart = "event starts ";
        String relativeEnd = "%1$slong";

        // Start reference date varies, depending if event has already begun
        if (hasStarted(startDateTime)) {
            dateDifference = Period.between(nowDate, endDate);
            timeDifference = Duration.between(nowTime, endTime);
            relativeStart = "event started ";
            relativeEnd = "ends in %1$s";

        } else {
            dateDifference = Period.between(startDate, endDate);
            timeDifference = Duration.between(startTime, endTime);
        }

        // Check years left
        if (dateDifference.getYears() > 1) {
            yearDiff = Integer.toString(dateDifference.getYears()) + " yrs ";
        } else if (dateDifference.getYears() == 1) {
            yearDiff = "1 yr ";

        }

        // Check months left
        if (dateDifference.getMonths() > 1) {
            monthDiff = Integer.toString(dateDifference.getMonths()) + " mths ";
        } else if (dateDifference.getMonths() == 1) {
            monthDiff = "1 mth ";

        }

        // Check days left
        if (dateDifference.getDays() > 1) {
            dayDiff = Integer.toString(dateDifference.getDays()) + " days ";
        } else if (dateDifference.getDays() == 1) {
            dayDiff = "1 day ";
        }

        // Check hours left
        if (timeDifference.getSeconds() >= 2 * SECONDS_IN_HOURS) {
            hourDiff = Long.toString(timeDifference.getSeconds() / SECONDS_IN_HOURS) + " hrs ";
        } else if (timeDifference.getSeconds() >= SECONDS_IN_HOURS) {
            hourDiff = "1 hr ";
        }

        // Check minutes left
        if ((timeDifference.getSeconds() % SECONDS_IN_HOURS) / SECONDS_IN_MINUTE > 1) {
            minDiff = Long.toString((timeDifference.getSeconds() % SECONDS_IN_HOURS) / SECONDS_IN_MINUTE) + " mins ";
        } else if ((timeDifference.getSeconds() % SECONDS_IN_HOURS) / SECONDS_IN_MINUTE == 1) {
            minDiff = "1 min ";
        }

        if (endDateTime.isBefore(LocalDateTime.now())) {
            relativeEnd = "ended " + prettyParser.format(end);
        }

        durationOutput = yearDiff + monthDiff + dayDiff + hourDiff + minDiff;

        return relativeStart + startOutput + ", " + String.format(relativeEnd, durationOutput);

        // if (startDate.equals(endDate)) {
        // return
        // startDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        // + " "
        // +
        // startDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        // + " to "
        // +
        // endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        // } else {
        // return
        // startDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        // + " "
        // +
        // startDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        // + " to "
        // +
        // endDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        // + " "
        // +
        // endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        // }
    }

    /*
     * formatDeadlineRange
     * 
     * @param LocalDateTime endDateTime
     * 
     * @return String deadlineRange
     */
    private String formatDeadlineRelativeText(LocalDateTime endDateTime) {

        PrettyTime prettyParser = new PrettyTime();

        Instant endInstant = endDateTime.toInstant(ZoneOffset.ofHours(ZONE_OFFSET));
        Date end = Date.from(endInstant);

        return "deadline due " + prettyParser.format(end);
    }

    /*
     * hasStarted checks if an event has already begun.
     * 
     * @param LocalDateTime startDateTime
     * 
     * @return boolean hasStarted
     */
    private boolean hasStarted(LocalDateTime startDateTime) {
        return LocalDateTime.now().isAfter(startDateTime);
    }

    /*
     * formatEventRangeDiffDay formats the date-time range for events with start
     * and end date-times on different days
     * 
     * @param LocalDateTime startDateTime, LocalDateTime endDateTime
     * 
     * @return String dateTimeRange
     */
    private String formatEventRangeDiffDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return "From " + startDateTime.getDayOfWeek() + ", "
                + startDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
                + " to " + endDateTime.getDayOfWeek() + ", "
                + endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }

    /*
     * formatEventRangeSameDay formats the date-time range for events with start
     * and end date-times on the same day
     * 
     * @param LocalDateTime startDateTime, LocalDateTime endDateTime
     * 
     * @return String dateTimeRange
     */
    private String formatEventRangeSameDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + " from "
                + startDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) + " to "
                + endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }

    /*
     * formatDeadlineRange formats the date-time range for deadlines
     * 
     * @param LocalDateTime endDateTime
     * 
     * @return String dateTimeRange
     */
    private String formatDeadlineRange(LocalDateTime endDateTime) {
        return DISPLAY_ITEM_HEADER_DUE + endDateTime.getDayOfWeek() + ", "
                + endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }

    /** STYLING FUNCTIONS **/

    /*
     * Apply styles on unknown task type
     * 
     */
    @SuppressWarnings("unused")
    private void styleUnknown() {
        numLabelBase.setFill(Color.web(COLOR_UNKNOWN));
    }

    /*
     * Apply styles on event task type
     * 
     * @param LocalDateTime startDateTime
     * 
     */
    @SuppressWarnings("unused")
    private void styleEvent(LocalDateTime startDateTime) {
        numLabelBase.setFill(Color.web(COLOR_EVENT));

        if (startDateTime.isBefore(LocalDateTime.now())) {
            priorityLabel.setFill(Color.web(COLOR_OVERDUE));
        } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), startDateTime) <= 24) {
            priorityLabel.setFill(Color.web(COLOR_TODAY));
        } else {
            priorityLabel.setFill(Color.web(COLOR_SPARE));
        }
    }

    /*
     * Apply styles on deadline task type
     * 
     * @param LocalDateTime endDateTime
     */
    @SuppressWarnings("unused")
    private void styleDeadline(LocalDateTime endDateTime) {
        numLabelBase.setFill(Color.web(COLOR_DEADLINE));

        if (endDateTime.isBefore(LocalDateTime.now())) {
            priorityLabel.setFill(Color.web(COLOR_OVERDUE));
        } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), endDateTime) <= 24) {
            priorityLabel.setFill(Color.web(COLOR_TODAY));
        } else {
            priorityLabel.setFill(Color.web(COLOR_SPARE));
        }
    }

    /*
     * Apply styles on floating task type
     */
    @SuppressWarnings("unused")
    private void styleFloatingTask() {
        numLabelBase.setFill(Color.web(COLOR_FLOATING));
        priorityLabel.setFill(Color.web(COLOR_SPARE));
    }

    /*
     * Apply styles on task
     * 
     * @param LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isCompleted
     * 
     */
    private void setStyle(LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isCompleted) {

        if (endDateTime != null && endDateTime.isBefore(LocalDateTime.now()) && !isCompleted) {
            numLabelBase.setFill(Color.web(COLOR_OVERDUE));
            priorityLabel.setFill(Color.web(COLOR_OVERDUE));
            // dateRangeSprite.setFill(Color.web(COLOR_OVERDUE));
        } else if (endDateTime != null && ChronoUnit.HOURS.between(LocalDateTime.now(), endDateTime) <= 24 && !isCompleted) {
            numLabelBase.setFill(Color.web(COLOR_TODAY));
            priorityLabel.setFill(Color.web(COLOR_TODAY));
            // dateRangeSprite.setFill(Color.web(COLOR_TODAY));
        } else {
            numLabelBase.setFill(Color.web(COLOR_SPARE));
            priorityLabel.setFill(Color.web(COLOR_SPARE));
            // dateRangeSprite.setFill(Color.web(COLOR_SPARE));
        }

    }
}
