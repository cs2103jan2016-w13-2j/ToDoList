//@@author A0130620B
package todolist.logic;

import java.util.HashMap;
import java.util.Map;

public class ErrorBase {

	static Map<String, String> errorBase = new HashMap<String, String>();

	public ErrorBase() {
		errorBase.put(("ADD" + "INCOMPLETE"),
				"Your command was incomplete! To create a task, you will first need to specify the type of task that you wish to create.");
		errorBase.put(("ADD EVENT" + "INCOMPLETE"),
				"Your command was incomplete! To add an event, try: add event [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day].");
		errorBase.put(("ADD DEADLINE" + "INCOMPLETE"),
				"Your command was incomplete! To add a deadline, try: add deadline [title] [YYYY-MM-DD] [HH:MM]");
		errorBase.put(("ADD TASK" + "INCOMPLETE"),
				"Your command was incomplete! To add an un-dated task: add task [title]");
		errorBase.put(("ADD RECURRING EVENT" + "INCOMPLETE"),
				"Your command was incomplete! To add a recurring event, try: add recurring event [7-day] [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day]");
		errorBase.put(("ADD RECURRING DEADLINE" + "INCOMPLETE"),
				"Your command was incomplete! To add a recurring deadline, try: add recurring deadline [7-day] [title] [YYYY-MM-DD] [HH:MM]");
		errorBase.put(("EDIT" + "INCOMPLETE"),
				"Your command was incomplete! To edit a task, try: edit [title] [field-name] [new-value]");
		errorBase.put(("FILTER" + "INCOMPLETE"),
				"Your command was incomplete! To apply a filter, try: filter [category-name]");
		errorBase.put(("SET-RECURRING" + "INCOMPLETE"),
				"Your command was incomplete! To set a task to repeat, try: set-recurring [title] [interval]");
		errorBase.put(("LABEL" + "INCOMPLETE"),
				"Your command was incomplete! To categorise or label a task, try: label [title] [category-name]");
		errorBase.put(("REMOVE-RECURRING" + "INCOMPLETE"),
				"Your command was incomplete! To stop a task from repeating, try: remove-recurring [title]");
		errorBase.put(("POSTPONE" + "INCOMPLETE"),
				"Your command was incomplete! To postpone a task, try: postpone [title] [number] [hour | day]");
		errorBase.put(("FORWARD" + "INCOMPLETE"),
				"Your command was incomplete! To forward a task, try: forward [title] [number] [hour | day]");
		errorBase.put(("REMIND" + "INCOMPLETE"),
				"Your command was incomplete! To get ToDoList to remind you on a task, try: remind [title]");
		errorBase.put(("REMIND-BEF" + "INCOMPLETE"),
				"Your command was incomplete! To get ToDoList to remind you on a task sometime before it is due, try: remind-bef [title] [number] [hour | day]");
		errorBase.put(("DONE" + "INCOMPLETE"),
				"Your command was incomplete! To archive a completed task, try: done [title]");
		errorBase.put(("UNDONE" + "INCOMPLETE"),
				"Your command was incomplete! To un-archive an ongoing task, try: undone [title]");
		errorBase.put(("UNDO" + "INCOMPLETE"),
				"Your command was incomplete! To undo a few action(s), try: undo [number-of-actions]");
		errorBase.put(("REDO" + "INCOMPLETE"),
				"Your command was incomplete! To redo a few action(s), try: redo [number-of-actions]");
		errorBase.put(("TAB" + "INCOMPLETE"),
				"Your command was incomplete! To navigate to a certain page, try: tab [page-name] (as reflected on the tab bar)");
		errorBase.put(("REDO" + "INCOMPLETE"),
				"Your command was incomplete! To redo a few action(s), try: redo [number-of-actions]");
		errorBase.put(("ADD" + "REPEAT TITLE"), 
				"You have added a task with same name before! Try another name!");
		errorBase.put(("SORT" + "NO FIELD"), 
				"Please specify a sorting order! Try sort ['start' | 'end' | 'category' | 'name'], followed by [ascending | descending].");
		errorBase.put(("ADD" + "REPEAT TITLE"), 
				"Task exist liao change change lah");
		errorBase.put(("ADD EVENT" + "INVALID TIME UNIT"), 
				"add event unit not correct lah");
		errorBase.put(("ADD EVENT" + "INVALID QUANTITY"), 
				"add event event quantity");
		errorBase.put(("ADD EVENT" + "INVALID START TIME"), 
				"add event invalid start time");
		errorBase.put(("ADD EVENT" + "INVALID START DATE"), 
				"add event invalid start date");
		errorBase.put(("ADD DEADLINE" + "INVALID END TIME"), 
				"add deadline invalid end time");
		errorBase.put(("ADD DEADLINE" + "INVLAID END DATE"), 
				"invalid end date");
		errorBase.put(("ADD RECURRING EVENT" + "INVALID INTERVAL"), 
				"invalid interval");
		errorBase.put(("ADD RECURRING DEADLINE" + "INVALID INTERVAL"), 
				"invalid interval");
		errorBase.put(("REMIND BEF" + "INVALID TIME UNIT"), 
				"remind bef invalid time unit");
		errorBase.put(("REMIND BEF" + "INVALID QUANTITY"), 
				"remind bef invalid time quantity");
		errorBase.put(("REMIND BEF" + "TASK NOT EXIST"), 
				"remind bef task not exist");
		errorBase.put(("ADD REMIND" + "INVALID TYPE"), 
				"add remind invalid type");
		errorBase.put(("REMIND BEF" + "INVALID TIME UNIT"), 
				"remind bef invalid unit");
		errorBase.put(("REMIND BEF" + "INVALID QUANTITY"), 
				"remind bef invalid quantity");
		errorBase.put(("REMIND" + "TASK NOT EXIST"), 
				"remind task not exist");
		errorBase.put(("FORWARD" + "INVALID TIME UNIT"), 
				"forward invalid time unit");
		errorBase.put(("FORWARD" + "INVALID QUANTITY"), 
				"forward invalid quantity");
		errorBase.put(("FORWARD" + "NOT EXIST"), 
				"froward task not exist");
		errorBase.put(("POSTPONE" + "INVALID TIME UNIT"), 
				"postpone  invalid time unit");
		errorBase.put(("POSTPONE" + "INVALID QUANTITY"), 
				"postpone invalid quantity");
		errorBase.put(("POSTPONE" + "NOT EXIST"), 
				"postpone task not exit");
		errorBase.put(("REDO" + "NO ACTION TO REDO"), 
				"redo no more action to redo");
		errorBase.put(("REDO" + "STEP NOT POSITIVE"), 
				"step must be positive integer");
		errorBase.put(("REDO" + "AUGUMENT NOT INTEGER"), 
				"redo not integer");
		errorBase.put(("UNDO" + "NO ACTION TO UNDO"), 
				"undo no more action to undo");
		errorBase.put(("UNDO" + "STEP NOT POSITIVE"), 
				"undo step not positive");
		errorBase.put(("UNDO" + "AUGUMENT NOT INTEGER"), 
				"undo not integer");
		errorBase.put(("UNDONE" + "NOT EXIST"), 
				"undone task not exist");
		errorBase.put(("DONE" + "NOT EXIST"), 
				"done task not exist");
		errorBase.put(("TAB" + "WORDPLACE NOT EXIST"), 
				"tab work place not exist");
		errorBase.put(("SET RECURRING" + "INVALID INTERVAL"), 
				"set recurring invalid interval");
		errorBase.put(("SET RECURRING" + "TASK NOT EXIST"), 
				"set recurring task not exist");
		errorBase.put(("REMOVE RECURRING" + "INVALID INTERVAL"), 
				"set recurring invalid interval");
		errorBase.put(("REMOVE RECURRING" + "TASK NOT EXIST"), 
				"set recurring task not exist");
		errorBase.put(("LABEL" + "TASK NOT EXIST"), 
				"label task not exist");
		errorBase.put(("EDIT" + "FIELD NOT EXIST"), 
				"edit field not exist");
		errorBase.put(("EDIT" + "TASK NOT EXIST"), 
				"edit task not exist");
		errorBase.put(("DELETE" + "TASK NOT EXIST"), 
				"delete task not exist");
		errorBase.put(("ADD REMIND" + "INVALID TYPE"), 
				"add remind invalid type");
	}

	public String getErrorMessage(String commandType, String errorType) {
		for (Map.Entry<String,String> entry : errorBase.entrySet()) {
			  String key = entry.getKey();
			  String value = entry.getValue();
			  System.out.println(key + " " + value);
			  // do stuff
			}
		System.out.println(errorBase.containsKey((commandType + errorType)));
		System.out.println(commandType + errorType);
		return errorBase.get((commandType + errorType));
	}
}
