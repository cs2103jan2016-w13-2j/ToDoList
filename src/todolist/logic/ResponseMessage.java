//@@author A0130620B
package todolist.logic;

public final class ResponseMessage {
	protected static final String MESSAGE_FAILURE_OPEN_DIR = "I encountered a problem importing your new schedule. Sorry!";
    protected static final String MESSAGE_SUCCESS_OPEN_DIR = "A new schedule has been imported.";
    protected static final String MESSAGE_FAILURE_MOVE_DIR = "I encountered a problem migrating your schedule. Sorry!";
    protected static final String MESSAGE_SUCCESS_MOVE_DIR = "Schedule has been saved to %1$s";
    protected static final String MESSAGE_SUCCESS_REFRESH = "View refreshed. All searches and filters are cleared!";
    protected static final String MESSAGE_SUCCESS_REDO = "You have reverted the last %d called-back action(s).";
    protected static final String MESSAGE_SUCCESS_UNDO = "You have called back the last %d action(s).";
    protected static final String MESSAGE_SUCCESS_UNARCHIVE = "[%1$s] has been unarchived! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_ARCHIVE = "[%1$s] has been archived! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_REMIND = "[%1$s] is set to trigger a reminder on %2$s. (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_FORWARD = "[%1$s] has been brought forward! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_POSTPONE = "[%1$s] has been postponed! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_NON_RECURRING = "[%1$s] is now a one-time-off task. (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_RECURRING = "[%1$s] is now a recurring task. (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_LABEL = "You have tagged [%1$s] as '%2$s'! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_SORT = "Your tasks are now sorted by '%1$s'. (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_FILTER = "Here are the related tasks under: '%1$s'. (to clear a filter, type 'reset')";
    protected static final String MESSAGE_SUCCESS_SEARCH = "Here are your search results for: '%1$s'. (to clear a search, type 'reset')";
    protected static final String MESSAGE_SUCCESS_DELETE = "[%1$s] has been deleted! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_EDIT = "[%1$s] has been edited! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_ADD_DEADLINE = "A new deadline [%1$s] has been created! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_ADD_EVENT = "A new event [%1$s] has been created! (not what you want? try 'undo 1')";
    protected static final String MESSAGE_SUCCESS_ADD_TASK = "A new task [%1$s] has been created! (not what you want? try 'undo 1')";
    
    protected static String MESSAGE_ADDING_FLOATING_TASK = "tring to add floating task: ";
    protected static String MESSAGE_ADDING_EVENT = "tring to add event: ";
    protected static String MESSAGE_ADDING_DEADLINE = "tring to add deadline: ";
    protected static String MESSAGE_ADDING_RECURRING_EVENT = "tring to add recurring event: ";
    protected static String MESSAGE_ADDING_RECURRING_DEADLINE = "tring to add reucrring deadline: ";
    protected static String MESSAGE_EDITING_TASK = "tring to edit task: ";
    protected static String MESSAGE_SEARCHING_TASK = "tring to search task: ";
    protected static String MESSAGE_SORTING_TASK = "tring to sort task: ";
    protected static String MESSAGE_DELETING_TASK = "tring to delete task: ";
}
