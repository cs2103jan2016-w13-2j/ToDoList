package todolist.ui.controllers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import todolist.MainApp;
import todolist.common.UtilityLogger;

//@@author A0123994W
/**
 * HelpModalController controls the display of the modal list view
 * 
 * @author Huang Lie Jun (A0123994W)
 *
 */
public class HelpModalController {

    /**
     * EntryComparator compares 2 help table entries and sorts them accordingly.
     */
    public class EntryComparator implements Comparator<CommandPair> {
        /**
         * compare compares the two CommandPair provided
         * 
         * @param CommandPair
         *            pair1
         * @param CommandPair
         *            pair2
         * @return int compareValue
         */
        public int compare(CommandPair pair1, CommandPair pair2) {
            if (pair1.getCommand() == pair2.getCommand()) {
                return 0;
            }
            return pair1.getCommand().compareTo(pair2.getCommand());
        }
    }

    /**
     * CommandPair wraps a help table entry.
     */
    private class CommandPair {
        private SimpleStringProperty command = null;
        private SimpleStringProperty format = null;

        /**
         * CommandPair constructs a new command format pair.
         * 
         * @param command
         * @param format
         */
        public CommandPair(String command, String format) {
            this.command = new SimpleStringProperty(command);
            this.format = new SimpleStringProperty(format);
        }

        /**
         * getCommand returns the command field.
         * 
         * @return
         */
        public String getCommand() {
            return command.get();
        }

        /**
         * setCommand sets the command property as the given command
         * 
         * @param command
         */
        @SuppressWarnings("unused")
        public void setCommand(String command) {
            this.command.set(command);
        }

        /**
         * getCommandProperty returns the command property.
         * 
         * @return StringProperty
         */
        public StringProperty getCommandProperty() {
            return command;
        }

        /**
         * getCommandProperty returns the command property.
         * 
         * @return String format
         */
        @SuppressWarnings("unused")
        public String getFormat() {
            return format.get();
        }

        /**
         * setFormat sets the format using the given string format.
         * 
         * @param format
         */
        @SuppressWarnings("unused")
        public void setFormat(String format) {
            this.format.set(format);
        }

        /**
         * getFormatProperty returns the current format.
         * 
         * @return StringProperty format
         */
        public StringProperty getFormatProperty() {
            return format;
        }
    }

    // Messages and Defaults
    private static final String TITLE_POPUP_HELP = "Help";
    protected static final String MESSAGE_HIDE_MODAL = "Help modal popover hidden.";
    protected static final String MESSAGE_DISPLAY_MODAL = "Help modal popover displayed.";

    // Window properties
    private static final double OPACITY = 0.90;
    private static final int BORDER_RADIUS = 10;

    // Elements
    private BorderPane helpView = null;
    private UtilityLogger logger = null;
    private static PopOver modalPopup = null;

    // Initialised status
    private boolean initialized = false;

    // Action Format HELP table
    private HashMap<String, String> actionFormatTable = null;

    // HELP table elements
    @FXML
    private TableView<CommandPair> helpTable = null;
    @FXML
    private TableColumn<CommandPair, String> commandColumn = null;
    @FXML
    private TableColumn<CommandPair, String> formatColumn = null;

    @SuppressWarnings("unused")
    private MainApp mainApp;

    ObservableList<CommandPair> entries = null;
    boolean isTableBuilt = false;

    /**
     * Constructor sets up various elements such as logger and help table
     * entries.
     */
    public HelpModalController() {
        logger = new UtilityLogger();
        actionFormatTable = new HashMap<String, String>();
        entries = FXCollections.observableArrayList();
    }

    /**
     * initializeHelpModal initialises the PopOver for displaying the help
     * table.
     * 
     * @return boolean isInitialized
     */
    public boolean initializeHelpModal() {

        // Sets up modal
        modalPopup = new PopOver();
        modalPopup.setContentNode(helpView);
        modalPopup.setCornerRadius(BORDER_RADIUS);
        modalPopup.setArrowLocation(ArrowLocation.LEFT_BOTTOM);
        modalPopup.hideOnEscapeProperty().setValue(true);
        modalPopup.setTitle(TITLE_POPUP_HELP);
        modalPopup.setOpacity(OPACITY);
        modalPopup.setArrowSize(0);

        // Allow ESC to close popup
        for (Node component : helpView.getChildren()) {
            component.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent key) {
                    if (key.getCode() == KeyCode.ESCAPE) {
                        modalPopup.hide();
                        logger.logAction(UtilityLogger.Component.UI, MESSAGE_HIDE_MODAL);
                    }
                }
            });
        }

        // Sets initialised flag after PopOver is created
        initialized = true;
        return initialized;

    }

    /**
     * displayPopup shows the help table, given a source node to display as the
     * table.
     * 
     * @param Node
     *            pointSource
     * 
     */
    public void displayPopup(Node pointSource) {
        if (initialized) {
            if (!isTableBuilt) {
                populateCheatsheet();
            }
            if (!modalPopup.isShowing()) {
                modalPopup.show(pointSource);
                modalPopup.getContentNode().requestFocus();
                logger.logAction(UtilityLogger.Component.UI, MESSAGE_DISPLAY_MODAL);
            }
        }
    }

    /**
     * initialize sets up the table values and factory settings for displaying
     * every entry.
     * 
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        commandColumn.setCellValueFactory(cellData -> cellData.getValue().getCommandProperty());
        formatColumn.setCellValueFactory(cellData -> cellData.getValue().getFormatProperty());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     * @param helpView
     */
    public void setMainApp(MainApp mainApp, BorderPane helpView) {
        this.mainApp = mainApp;
        this.helpView = helpView;

        // Add observable list data to the table
        helpTable.setItems(entries);
    }

    /**
     * populateCheatsheet sets up the cheatsheet database.
     */
    private void populateCheatsheet() {

        actionFormatTable.put("add|schedule|create event <TITLE> <START-DATE> <START-TIME> <QUANTITY> <TIME-UNIT>",
                "creates a new event");
        actionFormatTable.put("add|schedule|create deadline <TITLE> <END-DATE> <END-TIME>", "creates a new deadline");
        actionFormatTable.put("add|schedule|create task <TITLE>", "creates a new floating task");
        actionFormatTable.put(
                "add|schedule|create recurring event <INTERVAL> <TITLE> <START-DATE> <START-TIME> <QUANTITY> <TIME-UNIT>",
                "create a new recurring event");
        actionFormatTable.put("add|schedule|create recurring deadline <INTERVAL> <TITLE> <START-DATE> <START-TIME>",
                "create a new recurring deadline");
        actionFormatTable.put("edit|modify|change|replace <TITLE> <FIELD-NAME> <NEW-VALUE>",
                "overwrites the specified field of an item with the new value");
        actionFormatTable.put("delete|cancel|remove <TITLE|INDEX>",
                "deletes the item with the specified title from your agenda");
        actionFormatTable.put("done|archive|complete|shelf|finish <TITLE|INDEX>",
                "marks the item with the specified title as completed");
        actionFormatTable.put("undone|unarchive|incomplete|unshelf|unfinish <TITLE|INDEX>",
                "marks the item with the specified title as incompleted");
        actionFormatTable.put("reset", "clears existing search and filters");
        actionFormatTable.put("save <FILE-PATH>", "saves the current schedule to a file path");
        actionFormatTable.put("open|load <FILE-PATH>", "sets new file source");
        actionFormatTable.put("search <TITLE>", "highlights the item with the specified title");
        actionFormatTable.put("filter <CATEGORY>",
                "filters the display for items belonging to a specific view / category");
        actionFormatTable.put("sort <FIELD-NAME> <ASCENDING|DESCENDING>",
                "orders the items in the current view list by field(s) in the specified order, first by the earliest field specified");
        actionFormatTable.put("label|categorize|tab <TITLE|INDEX> <CATEGORY>",
                "categorises a specified item with a category or label");
        actionFormatTable.put("undo <#STEPS>", "reverts the last few changes");
        actionFormatTable.put("redo <#STEPS>", "recovers the last few changes you have undone");
        actionFormatTable.put("postpone|delay <TITLE> <QUANTITY> <TIME-UNIT>",
                "postpones a certain event or deadline with the specified title for a certain duration");
        actionFormatTable.put("forward|advance <TITLE> <QUANTITY> <TIME-UNIT>",
                "brings forward a certain event or deadline with the specified title for a certain duration");
        actionFormatTable.put("remind <TITLE|INDEX>",
                "sets reminder for the event or deadline with the specified title to trigger upon <START-TIME> or <END-TIME> of event and deadline");
        actionFormatTable.put("remove-remind <TITLE|INDEX>", "removes reminder for the event or deadline");
        actionFormatTable.put("set-recurring <TITLE> <QUANTITY>-<TIME-UNIT>", "sets a task as a recurring task");
        actionFormatTable.put("remove-recurring <TITLE>", "sets a recurring task as a normal task");
        actionFormatTable.put("add-remind...",
                "functions as add feature but with reminders set to trigger upon <START-TIME> or <END-TIME> of event and deadline respectively");
        actionFormatTable.put("add-remind-bef <QUANTITY> <TIME-UNIT>...",
                "functions as add feature but with reminders set to trigger <DURATION> before event <START-TIME> or deadline <END-TIME>");
        actionFormatTable.put("remind-bef <TITLE> <QUANTITY> <TIME-UNIT>",
                "sets reminder for event or deadline with specified title to trigger <DURATION> before the event <START-TIME> or the deadline <END-TIME>. <DURATION> is expressed as <QUANTITY-TIMEUNIT>");
        actionFormatTable.put("tab <VIEW>", "navigates to the specified view");
        actionFormatTable.put("help", "display the help sheet");
        actionFormatTable.put("clean", "removes all the tasks in the schedule");
        actionFormatTable.put("exit", "quits and closes the application");

        actionFormatTable.put("[UP-ARROW | DOWN-ARROW]", "[keyboard shortcut] browsing the task list");
        actionFormatTable.put("[ALT + UP | ALT + DOWN]", "[keyboard shortcut] browsing command history");
        actionFormatTable.put("[ESC]", "[keyboard shortcut] closing popups for help and reminders");
        actionFormatTable.put("[CRTL + K] for command and [CRTL + L] for list",
                "[keyboard shortcut] change context between command field and task list");
        actionFormatTable.put("[CRTL + M]", "[keyboard shortcut] mute/unmute");

        for (Entry<String, String> entry : actionFormatTable.entrySet()) {
            entries.add(new CommandPair(entry.getValue(), entry.getKey()));
        }

        entries.sort(new EntryComparator());

        isTableBuilt = true;
    }

    /**
     * getModalPopup returns a PopOver to access the help table popup.
     * 
     * @return PopOver helpTable
     */
    public PopOver getModalPopup() {
        return modalPopup;
    }

}