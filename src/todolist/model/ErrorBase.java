package todolist.model;

import java.util.HashMap;

import javafx.util.Pair;

public class ErrorBase {

	static HashMap<Pair<String, String>, String> errorBase = new HashMap<Pair<String, String>, String>();

	public ErrorBase() {
		errorBase.put(new Pair<String, String>("ADD", "INCOMPLETE"),
				"Your command was incomplete! To create a task, you will first need to specify the type of task that you wish to create.");
		errorBase.put(new Pair<String, String>("ADD EVENT", "INCOMPLETE"),
				"Your command was incomplete! To add an event, try: add event [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day].");
		errorBase.put(new Pair<String, String>("ADD DEADLINE", "INCOMPLETE"),
				"Your command was incomplete! To add a deadline, try: add deadline [title] [YYYY-MM-DD] [HH:MM]");
		errorBase.put(new Pair<String, String>("ADD TASK", "INCOMPLETE"),
				"Your command was incomplete! To add an un-dated task: add task [title]");
		errorBase.put(new Pair<String, String>("ADD RECURRING EVENT", "INCOMPLETE"),
				"Your command was incomplete! To add a recurring event, try: add recurring event [7-day] [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day]");
		errorBase.put(new Pair<String, String>("ADD RECURRING DEADLINE", "INCOMPLETE"),
				"Your command was incomplete! To add a recurring deadline, try: add recurring deadline [7-day] [title] [YYYY-MM-DD] [HH:MM]");
		errorBase.put(new Pair<String, String>("EDIT", "INCOMPLETE"),
				"Your command was incomplete! To edit a task, try: edit [title] [field-name] [new-value]");
		errorBase.put(new Pair<String, String>("FILTER", "INCOMPLETE"),
				"Your command was incomplete! To apply a filter, try: filter [category-name]");
		errorBase.put(new Pair<String, String>("SET-RECURRING", "INCOMPLETE"),
				"Your command was incomplete! To set a task to repeat, try: set-recurring [title] [interval]");
		errorBase.put(new Pair<String, String>("LABEL", "INCOMPLETE"),
				"Your command was incomplete! To categorise or label a task, try: label [title] [category-name]");
		errorBase.put(new Pair<String, String>("REMOVE-RECURRING", "INCOMPLETE"),
				"Your command was incomplete! To stop a task from repeating, try: remove-recurring [title]");
		errorBase.put(new Pair<String, String>("POSTPONE", "INCOMPLETE"),
				"Your command was incomplete! To postpone a task, try: postpone [title] [number] [hour | day]");
		errorBase.put(new Pair<String, String>("FORWARD", "INCOMPLETE"),
				"Your command was incomplete! To forward a task, try: forward [title] [number] [hour | day]");
		errorBase.put(new Pair<String, String>("LABEL", "INCOMPLETE"),
				"Your command was incomplete! To categorise or label a task, try: label [title] [category-name]");
		errorBase.put(new Pair<String, String>("LABEL", "INCOMPLETE"),
				"Your command was incomplete! To categorise or label a task, try: label [title] [category-name]");
	}

	public static String getErrorMessage(String commandType, String errorType) {
		return errorBase.get(new Pair<String, String>(commandType, errorType));
	}
}
