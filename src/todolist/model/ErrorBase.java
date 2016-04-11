//@@author A0130620B
package todolist.model;

import java.util.HashMap;
import java.util.Map;

public class ErrorBase {

	protected static Map<String, String> errorBase = new HashMap<String, String>();

	public ErrorBase() {
		errorBase.put(("ADD" + "INCOMPLETE"), "Your command to ADD was incomplete! Please specify a TASK TYPE.");
		errorBase.put(("ADD EVENT" + "INCOMPLETE"),
				"Your command to ADD EVENT was incomplete! Try FLEXICOMMAND or check HELP for formats.");
		errorBase.put(("ADD DEADLINE" + "INCOMPLETE"),
				"Your command to ADD DEADLINE was incomplete! Try FLEXICOMMAND or check HELP for formats.");
		errorBase.put(("ADD TASK" + "INCOMPLETE"),
				"Your command to ADD TASK was incomplete! Try FLEXICOMMAND or check HELP for formats.");
		errorBase.put(("ADD RECURRING EVENT" + "INCOMPLETE"),
				"Your command to ADD RECURRING EVENT was incomplete! Try FLEXICOMMAND or check HELP for formats.");
		errorBase.put(("ADD RECURRING DEADLINE" + "INCOMPLETE"),
				"Your command to ADD RECURRING DEADLINE was incomplete! Try FLEXICOMMAND or check HELP for formats.");
		errorBase.put(("EDIT" + "INCOMPLETE"),
				"Your command to EDIT TASK was incomplete! Please check HELP for formats.");
		errorBase.put(("FILTER" + "INCOMPLETE"),
				"Your command to FILTER (CATEGORY) was incomplete! Please check HELP for formats.");
		errorBase.put(("SET-RECURRING" + "INCOMPLETE"),
				"Your command to SET RECURRENCE was incomplete! Please check HELP for formats.");
		errorBase.put(("LABEL" + "INCOMPLETE"),
				"Your command to LABEL CATEGORY was incomplete! Please check HELP for formats.");
		errorBase.put(("REMOVE-RECURRING" + "INCOMPLETE"),
				"Your command to REMOVE RECURRENCE was incomplete! Please check HELP for formats.");
		errorBase.put(("POSTPONE" + "INCOMPLETE"),
				"Your command to POSTPONE TASK was incomplete! Please check HELP for formats.");
		errorBase.put(("FORWARD" + "INCOMPLETE"),
				"Your command to FORWARD TASK was incomplete! Please check HELP for formats.");
		errorBase.put(("REMIND" + "INCOMPLETE"),
				"Your command to SET REMINDER was incomplete! Please check HELP for formats.");
		errorBase.put(("REMIND-BEF" + "INCOMPLETE"),
				"Your command to SET ADVANCE REMINDER was incomplete! Please check HELP for formats.");
		errorBase.put(("DONE" + "INCOMPLETE"),
				"Your command to ARCHIVE was incomplete! Please check HELP for formats.");
		errorBase.put(("UNDONE" + "INCOMPLETE"),
				"Your command to UN-ARCHIVE was incomplete! Please check HELP for formats.");
		errorBase.put(("UNDO" + "INCOMPLETE"),
				"Your command to UNDO ACTION was incomplete! Please check HELP for formats.");
		errorBase.put(("REDO" + "INCOMPLETE"),
				"Your command to REVERT ACTION was incomplete! Please check HELP for formats.");
		errorBase.put(("TAB" + "INCOMPLETE"),
				"Your command to CHANGE TAB was incomplete! Please check HELP for formats.");
		errorBase.put(("ADD" + "REPEAT TITLE"), "You might have added this task before! Try a different TASK TITLE.");
		errorBase.put(("SORT" + "INCOMPLETE"), "Your command to SORT was incomplete! Please check HELP for formats.");
		errorBase.put(("ADD EVENT" + "INVALID TIME UNIT"),
				"You have specified a TIME UNIT that I don't understand! Try {minute, hour, day, week, month, year}.");
		errorBase.put(("ADD EVENT" + "INVALID QUANTITY"),
				"You have specified a TIME QUANTITY that I don't understand! Try using whole numbers instead.");
		errorBase.put(("ADD EVENT" + "INVALID START TIME"),
				"You have specified a START TIME that I don't understand! Please check HELP for formats.");
		errorBase.put(("ADD EVENT" + "INVALID START DATE"),
				"You have specified a START DATE that I don't understand! Please check HELP for formats.");
		errorBase.put(("ADD DEADLINE" + "INVALID END TIME"),
				"You have specified an invalid END TIME! ToDoList does not support time-travelling yet ...");
		errorBase.put(("ADD DEADLINE" + "INVLAID END DATE"),
				"You have specified an invalid END DATE! ToDoList does not support time-travelling yet ...");
		errorBase.put(("ADD RECURRING EVENT" + "INVALID INTERVAL"),
				"You have specified an invalid EVENT RECURRENCE INTERVAL! Please check HELP for formats.");
		errorBase.put(("ADD RECURRING DEADLINE" + "INVALID INTERVAL"),
				"You have specified an invalid DEADLINE RECURRENCE INTERVAL! Please check HELP for formats.");
		errorBase.put(("REMIND BEF" + "INVALID TIME UNIT"),
				"You have specified a TIME UNIT that I don't understand! Try {minute, hour, day, week, month, year}.");
		errorBase.put(("REMIND BEF" + "INVALID QUANTITY"),
				"You have specified a TIME QUANTITY that I don't understand! Try using whole numbers instead.");
		errorBase.put(("REMIND BEF" + "TASK NOT EXIST"),
				"The task that you want to set ADVANCE REMINDER for does not exist! Try another?");
		errorBase.put(("ADD REMIND" + "INVALID TYPE"),
				"You may only SET REMINDER for an EVENT or DEADLINE! Try creating an EVENT or DEADLINE instead!");
		errorBase.put(("REMIND" + "TASK NOT EXIST"),
				"The task that you want to set REMINDER for does not exist! Try another?");
		errorBase.put(("FORWARD" + "INVALID TIME UNIT"),
				"You have specified a TIME UNIT that I don't understand! Try {minute, hour, day, week, month, year}.");
		errorBase.put(("FORWARD" + "INVALID QUANTITY"),
				"You have specified a TIME QUANTITY that I don't understand! Try using whole numbers instead.");
		errorBase.put(("FORWARD" + "NOT EXIST"), "The task that you want to set FORWARD does not exist! Try another?");
		errorBase.put(("POSTPONE" + "INVALID TIME UNIT"),
				"You have specified a TIME UNIT that I don't understand! Try {minute, hour, day, week, month, year}.");
		errorBase.put(("POSTPONE" + "INVALID QUANTITY"),
				"You have specified a TIME QUANTITY that I don't understand! Try using whole numbers instead.");
		errorBase.put(("POSTPONE" + "NOT EXIST"),
				"The task that you want to set POSTPONE for does not exist! Try another?");
		errorBase.put(("REDO" + "NO ACTION TO REDO"), "There are no actions to REVERT!");
		errorBase.put(("REDO" + "STEP NOT POSITIVE"),
				"Uh-oh! I encountered some problems reverting your action. I will scold my makers for you.");
		errorBase.put(("REDO" + "AUGUMENT NOT INTEGER"),
				"You may only specify a whole number following the REDO command! Please refer to HELP for formats.");
		errorBase.put(("UNDO" + "NO ACTION TO UNDO"), "There are no actions to UNDO!");
		errorBase.put(("UNDO" + "STEP NOT POSITIVE"),
				"Uh-oh! I encountered some problems undoing your action. I will scold my developers for you.");
		errorBase.put(("UNDO" + "AUGUMENT NOT INTEGER"),
				"You may only specify a whole number following the UNDO command! Please refer to HELP for formats.");
		errorBase.put(("UNDONE" + "NOT EXIST"), "The task that you want to UNARCHIVE does not exist! Try another?");
		errorBase.put(("DONE" + "NOT EXIST"), "The task that you want to ARCHIVE does not exist! Try another?");
		errorBase.put(("TAB" + "WORDPLACE NOT EXIST"), "The TAB you are looking for does not exist! Try another?");
		errorBase.put(("SET RECURRING" + "INVALID INTERVAL"),
				"You have specified a TIME INTERVAL that I don't understand! Try [WHOLE NUMBER]-[TIME UNIT]. ");
		errorBase.put(("SET RECURRING" + "TASK NOT EXIST"),
				"The task that you want to SET RECURRENCE for does not exist! Try another?");
		errorBase.put(("REMOVE RECURRING" + "INVALID INTERVAL"),
				"You have specified a TIME INTERVAL that I don't understand! Try [WHOLE NUMBER]-[TIME UNIT]. ");
		errorBase.put(("REMOVE RECURRING" + "TASK NOT EXIST"),
				"The task that you want to REMOVE RECURRENCE from does not exist! Try another?");
		errorBase.put(("LABEL" + "TASK NOT EXIST"),
				"The task that you want to LABEL CATEGORY does not exist! Try another?");
		errorBase.put(("EDIT" + "FIELD NOT EXIST"),
				"The FIELD that you want to EDIT for this task does not exist! Please refer to HELP for formats.");
		errorBase.put(("EDIT" + "TASK NOT EXIST"), "The TASK that you want to EDIT does not exist! Try another?");
		errorBase.put(("DELETE" + "TASK NOT EXIST"), "The TASK that you want to DELETE does not exist! Try another?");
		errorBase.put(("ADD REMIND" + "INVALID TYPE"),
				"You may only SET REMINDER for an EVENT or DEADLINE! Try creating an EVENT or DEADLINE instead!");
		errorBase.put(("ADD REMIND BEF" + "INVALID TYPE"),
				"You may only SET ADVANCE REMINDER for an EVENT or DEADLINE! Try creating an EVENT or DEADLINE instead!");
		errorBase.put(("ADD REMIND BEF" + "INVALID TIME UNIT"),
				"You have specified a TIME UNIT that I don't understand! Try {minute, hour, day, week, month, year}.");
		errorBase.put(("ADD REMIND BEF" + "INVALID QUANTITY"),
				"You have specified a TIME QUANTITY that I don't understand! Try using whole numbers instead.");
		errorBase.put(("REMIND" + "FLOATING TASK"),
				"You may only SET REMINDER for an EVENT or DEADLINE! Try picking an EVENT or DEADLINE instead!");
		errorBase.put(("REMOVE REMIND" + "NOT EXIST"),
				"The task that you want to remove reminder does not exist! Try another?");
		errorBase.put(("FORWARD" + "FLOATING TASK"),
				"You may only FORWARD an EVENT or DEADLINE! Try picking an EVENT or DEADLINE instead!");
		errorBase.put(("POSTPONE" + "FLOATING TASK"),
				"You may only POSTPONE an EVENT or DEADLINE! Try picking an EVENT or DEADLINE instead!");
		errorBase.put(("REMIND BEF" + "FLOATING TASK"),
				"You may only SET REMINDER for an EVENT or DEADLINE! Try picking an EVENT or DEADLINE instead!");
		errorBase.put(("SET RECURRING" + "FLOATING TASK"),
				"You may only SET RECURRENCE for an EVENT or DEADLINE! Try picking an EVENT or DEADLINE instead!");
		errorBase.put(("PARSER" + "INVALID INPUT"),
				"Sorry! I don't understand this command! Please refer to HELP for formats.");
	}

	protected String getErrorMessage(String commandType, String errorType) {
		return errorBase.get((commandType + errorType));
	}
}
