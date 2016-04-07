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
public class HelpModalController {

    public class EntryComparator implements Comparator<CommandPair> {
        public int compare(CommandPair obj1, CommandPair obj2) {
            if (obj1.getCommand() == obj2.getCommand()) {
                return 0;
            }
            return obj1.getCommand().compareTo(obj2.getCommand());
        }
    }

    private class CommandPair {
        private SimpleStringProperty command = null;
        private SimpleStringProperty format = null;

        public CommandPair(String command, String format) {
            this.command = new SimpleStringProperty(command);
            this.format = new SimpleStringProperty(format);
        }

        public String getCommand() {
            return command.get();
        }

        @SuppressWarnings("unused")
        public void setCommand(String command) {
            this.command.set(command);
        }

        public StringProperty getCommandProperty() {
            return command;
        }

        @SuppressWarnings("unused")
        public String getFormat() {
            return format.get();
        }

        @SuppressWarnings("unused")
        public void setFormat(String format) {
            this.format.set(format);
        }

        public StringProperty getFormatProperty() {
            return format;
        }
    }

    private static final String TITLE_POPUP_HELP = "Help";

    protected static final String MESSAGE_HIDE_MODAL = "Help modal popover hidden.";
    protected static final String MESSAGE_DISPLAY_MODAL = "Help modal popover displayed.";

    private static final double OPACITY = 0.90;
    private static final int BORDER_RADIUS = 10;

    private BorderPane helpView = null;
    private UtilityLogger logger = null;
    private PopOver modalPopup = null;
    private boolean initialized = false;

    private HashMap<String, String> actionFormatTable = null;

    @FXML
    private TableView<CommandPair> helpTable = null;
    @FXML
    private TableColumn<CommandPair, String> commandColumn = null;
    @FXML
    private TableColumn<CommandPair, String> formatColumn = null;

    @SuppressWarnings("unused")
    private MainApp mainApp;

    ObservableList<CommandPair> entries = null;

    public HelpModalController() {
        logger = new UtilityLogger();
        actionFormatTable = new HashMap<String, String>();
        entries = FXCollections.observableArrayList();
    }

    public boolean initializeHelpModal() {

        modalPopup = new PopOver();
        modalPopup.setContentNode(helpView);
        modalPopup.setCornerRadius(BORDER_RADIUS);
        modalPopup.setArrowLocation(ArrowLocation.LEFT_BOTTOM);
        modalPopup.hideOnEscapeProperty().setValue(true);
        modalPopup.setTitle(TITLE_POPUP_HELP);
        modalPopup.setOpacity(OPACITY);

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

        initialized = true;
        return initialized;

    }

    public void displayPopup(Node pointSource) {
        if (initialized) {
            populateCheatsheet();
            modalPopup.show(pointSource);
            modalPopup.getContentNode().requestFocus();
            logger.logAction(UtilityLogger.Component.UI, MESSAGE_DISPLAY_MODAL);
        }
    }

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
     */
    public void setMainApp(MainApp mainApp, BorderPane helpView) {
        this.mainApp = mainApp;
        this.helpView = helpView;

        // Add observable list data to the table
        helpTable.setItems(entries);
    }

    private void populateCheatsheet() {

        actionFormatTable.put("create a new task", "[add|create|schedule] <your-input>");
        actionFormatTable.put("deleting an existing task", "[delete|cancel|remove] <title|number>");
        actionFormatTable.put("editing a field of an existing task",
                "[edit|modify|change|replace] [title|start-time|end-time] <title|number> <new-value|date-time>");
        actionFormatTable.put("marking an existing task as completed",
                "[archive|done|complete|finish|shelf] <title|number>");
        actionFormatTable.put("marking an existing task as incomplete",
                "[unarchive|undone|incomplete|unfinish|unshelf] <title|number>");
        actionFormatTable.put("postponing an existing event or deadline", "[postpone|delay] <title|number> <duration>");
        actionFormatTable.put("advancing an existing event or deadline", "[forward|advance] <title|number> <duration>");
        actionFormatTable.put("search by keyword or letter", "[search] <your-input>");
        actionFormatTable.put("filter by category or date-time range", "[filter] <category-name|date-time>");
        actionFormatTable.put("sort the list in a certain order",
                "[sort] [title|start-time|end-time] [ascend|descend]");
        actionFormatTable.put("set the category of an existing task",
                "[categorise|label|tag] <title|number> <category-name>");
        actionFormatTable.put("set new file source", "[open|load] <file-source|relative-file-source>");
        actionFormatTable.put("create a new task with reminder", "[remind me to] <your-input>");
        actionFormatTable.put("set reminder for existing task", "[remind me for|remind] <title|number>");
        actionFormatTable.put("set a task as recurring",
                "[set-recurring] <title|number> <quantity> <minute|hour|day|week|month|year>");
        actionFormatTable.put("undo your previous step", "[undo]");
        actionFormatTable.put("redo your previously reverted step", "[redo]");
        actionFormatTable.put("set your default tab", "[set-home] <name-of-page>");
        actionFormatTable.put("set alarm ringtone", "[set-ringtone] <file-path>");
        actionFormatTable.put("mute or unmute", "[toggle-sound] [on|off]");
        actionFormatTable.put("reload a page", "[refresh|reload]");
        actionFormatTable.put("clear existing search and filters", "[reset|clear|clean]");
        actionFormatTable.put("navigate between pages", "[page-name]");
        actionFormatTable.put("day and night mode theming", "[day-mode|night-mode]");
        actionFormatTable.put("exiting the program", "[exit | quit | close | terminate]");
        actionFormatTable.put("[keyboard shortcut] browsing the task list", "[UP-ARROW | DOWN-ARROW]");
        actionFormatTable.put("[keyboard shortcut] browsing command history", "[SHIFT + UP | SHIFT + DOWN]");
        actionFormatTable.put("[keyboard shortcut] closing popups for help and reminders", "[ESC]");
        actionFormatTable.put("[keyboard shortcut] browsing tabs or pages", "[TAB] | [F1 | F2 | ... | F7]");
        actionFormatTable.put("[keyboard shortcut] change context between command field and task list",
                "[SHIFT + K] for command and [SHIFT + L] for list");

        for (Entry<String, String> entry : actionFormatTable.entrySet()) {
            entries.add(new CommandPair(entry.getKey(), entry.getValue()));
        }

        entries.sort(new EntryComparator());
    }

}