package todolist.ui.controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableNumberValue;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

//@@author A0123994W

/**
 * TaskNodeController controls and manipulates data for display in task view(s).
 * 
 * @author Huang Lie Jun (A0123994W)
 * 
 */
public class TaskNodeController {

    // Defaults and Tags
    private static final int HOURS_PER_DAY = 24;
    private static final Font FONT_RELATIVE_DESCRIPTOR = Font.font("Calibri", FontPosture.ITALIC, 14);
    private static final String TAG_DEADLINE_DUE = "deadline due ";
    private static final String TAG_REPLACE_FROM_NOW = "from now";
    private static final String TAG_ENDED = "ended ";
    private static final String TAG_ENDS = "ends in %1$s";
    private static final String TAG_STARTED = "event started ";
    private static final String TAG_LONG = "%1$slong";
    private static final String TAG_EVENT_STARTS = "event starts ";
    private static final String TAG_EMPTY = "";
    private static final String TAG_AD_HOC = "ad-hoc task";
    private static final String TAG_ANYTIME = "Anytime";
    private static final String TAG_NOT_AVAILABLE = "Not Available";

    /*** STATIC MESSAGES ***/

    // ERRORS
    private static final String ERROR_DISPLAY_ITEM_INDEX = "Task Display: Index number out of bounds.";
    private static final String ERROR_DISPLAY_ITEM_TITLE = "Task Display: Task title invalid.";

    // DEFAULTS
    private static final String NULL_DISPLAY_ITEM_CATEGORY = "uncategorised";
    private static final String DISPLAY_ITEM_UNARCHIVED = "ONGOING";
    private static final String DISPLAY_ITEM_ARCHIVED = "DONE";
    private static final String DISPLAY_ITEM_OVERDUE = "OVERDUE";
    private static final String DISPLAY_ITEM_HEADER_CATEGORY = TAG_EMPTY;
    private static final String COMPLETED_RELATIVE = "completed!";

    /*** STYLES ***/

    // MONTHS
    private static final String[] months = { TAG_EMPTY, "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec" };

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

    // Task model
    private TaskWrapper task = null;

    // Task index number
    private int index = -1;

    // Task types
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
    private Text title = null;
    @FXML
    private TextFlow titleFlow = null;

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
    @FXML
    private Label recurInterval = null;

    // Date-Time Field Live Update Interval (in seconds)
    private static final int UPDATE_INTERVAL = 5;

    // Time Zone Offset (from UTC)
    private static final int ZONE_OFFSET = 8;

    /**
     * Constructor intializes task and index of task.
     * 
     * @param task
     * @param index
     * @throws IllegalArgumentException
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

    /**
     * validateAndFormatItem checks the validity of the display content and
     * formats it for display.
     * 
     * @throws IllegalArgumentException
     */
    public void validateAndFormatItem() throws IllegalArgumentException {
        int indexNumber = index + 1;

        String taskTitle = task.getTaskTitle();
        Category category = task.getCategory();
        Reminder reminder = task.getReminder();
        LocalDateTime startDateTime = task.getStartTime();
        LocalDateTime endDateTime = task.getEndTime();

        /** Preparation **/

        // Prepare to scale label height relative to text wrapping
        bindPriorityLabelHeight();

        /** Validate and theme **/

        // Ensure integrity of task object
        assert (task != null);

        // Index Number
        validateAndFormatIndexNumber(indexNumber);

        // Title
        validateAndFormatTitle(taskTitle);

        // Category
        validateAndFormatCategory(category);

        // Actively update date-time related fields and colors
        try {

            // Set up callback for date-time field updates
            KeyFrame updateFrame = new KeyFrame(javafx.util.Duration.ZERO, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    formatDateField(task, startDateTime, endDateTime);
                    // Archive Status
                    if (task.getIsCompleted()) {
                        setTaskStatusCompleted();
                    } else if (task.getIsExpired()) {
                        setTaskStatusExpired();
                    } else {
                        setTaskStatusOngoing();
                    }
                }

                private void setTaskStatusOngoing() {
                    statusBacking.setFill(Color.web(COLOR_INCOMPLETE));
                    status.setText(DISPLAY_ITEM_UNARCHIVED);
                }

                private void setTaskStatusExpired() {
                    statusBacking.setFill(Color.web(COLOR_OVERDUE));
                    status.setText(DISPLAY_ITEM_OVERDUE);
                }

                private void setTaskStatusCompleted() {
                    statusBacking.setFill(Color.web(COLOR_COMPLETE));
                    status.setText(DISPLAY_ITEM_ARCHIVED);
                }
            });

            // Set up pause interval
            KeyFrame intervalFrame = new KeyFrame(javafx.util.Duration.seconds(UPDATE_INTERVAL));

            // Run update on separate thread(s)
            updateDateTimeThreaded(updateFrame, intervalFrame);

        } catch (IllegalArgumentException iae) {
            throw iae;
        }

        // Recurring Status
        setRecurringStatus();

        // Reminder Status
        setReminderStatus(reminder);
    }

    /**
     * updateDateTimeThreaded is a busy update function that will animate the
     * two given frames infinitely. The first frame contains a callback function
     * that will update the date-time related fields accordingly. (Not the most
     * efficient)
     * 
     * @param updateFrame
     * @param intervalFrame
     */
    private void updateDateTimeThreaded(KeyFrame updateFrame, KeyFrame intervalFrame) {
        Timeline timeline = new Timeline(updateFrame, intervalFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * ValidateAndFormatCategory will take in a category and format the category
     * field accordingly.
     * 
     * @param Category
     *            category
     * 
     */
    private void validateAndFormatCategory(Category category) {
        String categoryName;
        if (category == null) {
            categoryName = new String(NULL_DISPLAY_ITEM_CATEGORY);
            categorySprite.setFill(Color.web(COLOR_UNKNOWN));
        } else {
            categoryName = DISPLAY_ITEM_HEADER_CATEGORY + category.getCategory();
            Color categoryHexColor = getColorForCategory(categoryName);
            categorySprite.setFill(categoryHexColor);
        }
        this.category.setText(categoryName);
    }

    /**
     * validateAndFormatTitle will take in a title and format the title field
     * accordingly.
     * 
     * @param String
     *            taskTitle
     * 
     */
    private void validateAndFormatTitle(String taskTitle) {

        // Synchronize title color with index number color
        title.fillProperty().bind(number.textFillProperty());

        if (taskTitle == null) {
            logger.logError(UtilityLogger.Component.UI, ERROR_DISPLAY_ITEM_TITLE);
            throw new IllegalArgumentException(ERROR_DISPLAY_ITEM_TITLE);
        } else {
            title.setText(taskTitle);
        }
    }

    /**
     * validateAndFormatIndexNumber will take in an index and format the index
     * field accordingly.
     * 
     * @param int
     *            indexNumber
     * 
     */
    private void validateAndFormatIndexNumber(int indexNumber) {
        if (indexNumber <= 0) {
            logger.logError(UtilityLogger.Component.UI, ERROR_DISPLAY_ITEM_INDEX);
            throw new IllegalArgumentException(ERROR_DISPLAY_ITEM_INDEX);
        } else {
            number.setText(Integer.toString(indexNumber));
        }
    }

    /**
     * setReminderStatus will take in a reminder and format the reminder flag
     * indicator accordingly.
     * 
     * @param Reminder
     *            reminder
     * 
     */
    private void setReminderStatus(Reminder reminder) {
        if (reminder == null) {
            reminderIndicator.setVisible(false);
        } else {
            reminderIndicator.setVisible(reminder.getStatus());
        }
    }

    /**
     * setRecurringStatus will check if a task is recurring and toggle the
     * recurrence flag indicator accordingly.
     * 
     */
    private void setRecurringStatus() {
        recurringIndicator.setVisible(task.getIsRecurring());
        recurInterval.setText(task.getInterval());
    }

    /**
     * getColorForCategory will calculate a color index and return a color that
     * expresses the category name.
     * 
     * @param String
     *            categoryName
     * 
     * @return Color categoryColor
     * 
     */
    private Color getColorForCategory(String categoryName) {
        int hashModColorIndex = Math.abs(categoryName.hashCode()) % colorsHex.length;
        String colorDescriptor = colorsHex[hashModColorIndex];
        Color categoryHexColor = Color.web(colorDescriptor);
        return categoryHexColor;
    }

    /**
     * bindPriorityLabelHeight binds the priority label to the node height
     * loosely. (Still experimental)
     * 
     */
    private void bindPriorityLabelHeight() {
        DoubleBinding categoryWithOffsetHeight = categoryBox.heightProperty().add(16);
        DoubleBinding categoryAndDateWithOffsetHeight = dateRangeBox.heightProperty().add(categoryWithOffsetHeight);
        ObservableNumberValue categoryAndDateAndTitleWithOffset = relativeRangeBox.heightProperty()
                .add(categoryAndDateWithOffsetHeight);
        DoubleBinding totalInfoFieldHeight = titleFlow.heightProperty().add(categoryAndDateAndTitleWithOffset);

        // Binding
        priorityLabel.heightProperty().bind(totalInfoFieldHeight);
    }

    /**
     * getNode returns the formatted task view for display.
     * 
     * @return HBox getNode
     */
    public HBox getNode() {
        return root;
    }

    /**
     * formatDateField takes in a task, start date-time, end date-time to format
     * date range field for display.
     * 
     * @param task
     * @param startDateTime
     * @param endDateTime
     */
    public void formatDateField(TaskWrapper task, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TASK_TYPE taskType = checkTaskType(startDateTime, endDateTime);
        String outputDate = TAG_EMPTY;
        String relativeText = TAG_AD_HOC;

        switch (taskType) {

        case FLOAT:
            outputDate = TAG_ANYTIME;
            break;
        case DEADLINE:
            // Formatting of deadline date
            outputDate = formatDeadlineRange(endDateTime);
            // Set relative descriptor
            relativeText = formatDeadlineRelativeText(endDateTime);
            break;
        case EVENT:
            // Smart formatting of range for same-day events
            if (isSameDay(startDateTime, endDateTime)) {
                outputDate = formatEventRangeSameDay(startDateTime, endDateTime);
                // Normal formatting of range for multi-day events
            } else {
                outputDate = formatEventRangeDiffDay(startDateTime, endDateTime);
            }
            // Set relative descriptor
            relativeText = formatEventRelativeText(startDateTime, endDateTime);

            break;
        default:
            // Formatting of date-time range for unknown types
            outputDate = TAG_NOT_AVAILABLE;
        }

        // Set styles
        setDateTimeRelStyle(startDateTime, endDateTime, task.getIsCompleted());

        if (task.getIsCompleted()) {
            relativeText = COMPLETED_RELATIVE;
        }
        setDateTimeRelativeFields(outputDate, relativeText);
    }

    /**
     * setDateTimeRelativeFields takes in an outputDate and relative text and
     * sets the flag accordingly.
     * 
     * @param outputDate
     * @param relativeText
     */
    private void setDateTimeRelativeFields(String outputDate, String relativeText) {
        this.relativeRange.setText(relativeText);
        dateRange.setText(outputDate);
        this.relativeRange.setFont(FONT_RELATIVE_DESCRIPTOR);
    }

    /**
     * isSameDay takes in a start and end date-time and checks if the start and
     * end date-time falls on the same day.
     * 
     * @param startDateTime
     * @param endDateTime
     * @return boolean isSameDay
     */
    private boolean isSameDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.getDayOfYear() == endDateTime.getDayOfYear()
                && startDateTime.getYear() == endDateTime.getYear();
    }

    /**
     * checkTaskType takes in the start date-time and end date-time to determine
     * the task type.
     * 
     * @param startDateTime
     * @param endDateTime
     * @return TASK_TYPE
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

    /**
     * formatEventRange
     * 
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    private String formatEventRelativeText(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        // Output will consists of two parts: [start time reference] and
        // remaining time left
        String startOutput = null;
        String durationOutput = null;

        ZoneId defaultZoneId = ZoneId.of(ZoneOffset.systemDefault().getId());
        ZonedDateTime zoneDateTime = LocalDateTime.now().atZone(defaultZoneId);
        ZoneOffset zoneOffset = zoneDateTime.getOffset();

        // Type conversions for handling
        PrettyTime prettyParser = new PrettyTime();
        Instant startInstant = startDateTime.toInstant(zoneOffset);
        Date start = Date.from(startInstant);
        Instant endInstant = endDateTime.toInstant(zoneOffset);
        Date end = Date.from(endInstant);
        Instant nowInstant = LocalDateTime.now().toInstant(zoneOffset);
        Date now = Date.from(nowInstant);

        // Set output for the start of event
        startOutput = prettyParser.format(start);

        // String formatting
        String relativeStart = TAG_EVENT_STARTS;
        String relativeEnd = TAG_LONG;

        // Start reference date varies, depending if event has already begun
        if (hasStarted(startDateTime)) {
            prettyParser.setReference(now);
            relativeStart = TAG_STARTED;
            relativeEnd = TAG_ENDS;
            // Change point of reference if event has yet to start
        } else {
            prettyParser.setReference(start);
        }

        // End modifiers
        if (endDateTime.isBefore(LocalDateTime.now())) {
            relativeEnd = TAG_ENDED + prettyParser.format(end);
        }

        // Format PrettyTime output for display
        durationOutput = prettyParser.format(end).replace(TAG_REPLACE_FROM_NOW, TAG_EMPTY);

        // Format and display relative descriptor
        return relativeStart + startOutput + ", " + String.format(relativeEnd, durationOutput);
    }

    /**
     * formatDeadlineRange
     * 
     * @param LocalDateTime
     *            endDateTime
     * 
     * @return String deadlineRange
     */
    private String formatDeadlineRelativeText(LocalDateTime endDateTime) {

        PrettyTime prettyParser = new PrettyTime();

        Instant endInstant = endDateTime.toInstant(ZoneOffset.ofHours(ZONE_OFFSET));
        Date end = Date.from(endInstant);

        return TAG_DEADLINE_DUE + prettyParser.format(end);
    }

    /**
     * hasStarted checks if an event has already begun.
     * 
     * @param LocalDateTime
     *            startDateTime
     * 
     * @return boolean hasStarted
     */
    private boolean hasStarted(LocalDateTime startDateTime) {
        return LocalDateTime.now().isAfter(startDateTime);
    }

    /**
     * formatEventRangeDiffDay formats the date-time range for events with start
     * and end date-times on different days
     * 
     * @param LocalDateTime
     *            startDateTime
     * @param LocalDateTime
     *            endDateTime
     * 
     * @return String dateTimeRange
     */
    private String formatEventRangeDiffDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.getDayOfMonth() + "-" + months[startDateTime.getMonthValue()] + "-"
                + startDateTime.getYear() + ", "
                + startDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) + " to "
                + endDateTime.getDayOfMonth() + "-" + months[endDateTime.getMonthValue()] + "-" + endDateTime.getYear()
                + ", " + endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }

    /**
     * formatEventRangeSameDay formats the date-time range for events with start
     * and end date-times on the same day
     * 
     * @param LocalDateTime
     *            startDateTime
     * @param LocalDateTime
     *            endDateTime
     * 
     * @return String dateTimeRange
     */
    private String formatEventRangeSameDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.getDayOfMonth() + "-" + months[startDateTime.getMonthValue()] + "-"
                + startDateTime.getYear() + ", "
                + startDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) + " to "
                + endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }

    /**
     * formatDeadlineRange formats the date-time range for deadlines
     * 
     * @param LocalDateTime
     *            endDateTime
     * 
     * @return String dateTimeRange
     */
    private String formatDeadlineRange(LocalDateTime endDateTime) {
        return endDateTime.getDayOfMonth() + "-" + months[endDateTime.getMonthValue()] + "-" + endDateTime.getYear()
                + ", " + endDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }

    /** STYLING FUNCTIONS **/

    /**
     * Apply styles on unknown task type
     * 
     */
    @SuppressWarnings("unused")
    private void styleUnknown() {
        numLabelBase.setFill(Color.web(COLOR_UNKNOWN));
    }

    /**
     * Apply styles on event task type
     * 
     * @param LocalDateTime
     *            startDateTime
     * 
     */
    @SuppressWarnings("unused")
    private void styleEvent(LocalDateTime startDateTime) {
        numLabelBase.setFill(Color.web(COLOR_EVENT));

        if (startDateTime.isBefore(LocalDateTime.now())) {
            priorityLabel.setFill(Color.web(COLOR_OVERDUE));
        } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), startDateTime) <= HOURS_PER_DAY) {
            priorityLabel.setFill(Color.web(COLOR_TODAY));
        } else {
            priorityLabel.setFill(Color.web(COLOR_SPARE));
        }
    }

    /**
     * Apply styles on deadline task type
     * 
     * @param LocalDateTime
     *            endDateTime
     */
    @SuppressWarnings("unused")
    private void styleDeadline(LocalDateTime endDateTime) {
        numLabelBase.setFill(Color.web(COLOR_DEADLINE));

        if (endDateTime.isBefore(LocalDateTime.now())) {
            priorityLabel.setFill(Color.web(COLOR_OVERDUE));
        } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), endDateTime) <= HOURS_PER_DAY) {
            priorityLabel.setFill(Color.web(COLOR_TODAY));
        } else {
            priorityLabel.setFill(Color.web(COLOR_SPARE));
        }
    }

    /**
     * Apply styles on floating task type
     * 
     */
    @SuppressWarnings("unused")
    private void styleFloatingTask() {
        numLabelBase.setFill(Color.web(COLOR_FLOATING));
        priorityLabel.setFill(Color.web(COLOR_SPARE));
    }

    /**
     * Apply styles on task
     * 
     * @param LocalDateTime
     *            startDateTime
     * @param LocalDateTime
     *            endDateTime
     * @param boolean
     *            isCompleted
     * 
     */
    private void setDateTimeRelStyle(LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isCompleted) {

        // Overdue Priority
        if (isOverdue(endDateTime, isCompleted)) {
            numLabelBase.setFill(Color.web(COLOR_OVERDUE));
            priorityLabel.setFill(Color.web(COLOR_OVERDUE));
            // Today Priority
        } else if (isToday(endDateTime, isCompleted)) {
            numLabelBase.setFill(Color.web(COLOR_TODAY));
            priorityLabel.setFill(Color.web(COLOR_TODAY));
            // Normal Priority
        } else {
            numLabelBase.setFill(Color.web(COLOR_SPARE));
            priorityLabel.setFill(Color.web(COLOR_SPARE));
        }

    }

    /**
     * isToday checks if the current task is an ongoing task due today
     * 
     * @param LocalDateTime
     *            endDateTime
     * @param boolean
     *            isCompleted
     * 
     * @return boolean isToday
     * 
     */
    private boolean isToday(LocalDateTime endDateTime, boolean isCompleted) {
        return endDateTime != null && ChronoUnit.HOURS.between(LocalDateTime.now(), endDateTime) <= 24 && !isCompleted;
    }

    /**
     * isOverdue checks if the current task is an expired task due before today
     * 
     * @param LocalDateTime
     *            endDateTime
     * @param boolean
     *            isCompleted
     * 
     * @return boolean isOverdue
     */
    private boolean isOverdue(LocalDateTime endDateTime, boolean isCompleted) {
        return endDateTime != null && endDateTime.isBefore(LocalDateTime.now()) && !isCompleted;
    }
}
