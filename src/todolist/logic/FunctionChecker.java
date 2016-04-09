package todolist.logic;

import todolist.model.InputException;

public class FunctionChecker {
	private Logic logic;
	
	public FunctionChecker(Logic logic) {
		this.logic = logic;
	}
	
	public InputException openChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException saveChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException invalidChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException tabChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException redoChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException undoChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException undoneChecker(String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException doneChecker(String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException remindBefChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRemindBefChecker(String string, String string2, String[] restOfArgs) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException remindChecker(String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException forwardChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException postponeChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException setRecurringChecker(String string, boolean b, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException labelChecker(String name, String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException sortChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException filterChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException searchChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException editChecker(String name, String string, String string2) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	
	public InputException resetChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException exitChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRemindChecker(String[] arg) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException removeRecurringChecker(String name, boolean b, String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRecurringDeadlineLessChecker(String string, String string2, String string3) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRecurringDeadlineChecker(String string, String string2, String string3, String string4) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRecurringEventLessChecker(String string, String string2, String string3, String string4,
			String string5) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addRecurringEventChecker(String string, String string2, String string3, String string4,
			String string5, String string6) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addTaskChecker(String string) {
		// TODO Auto-generated method stub
		return new InputException();
	}

	public InputException addDeadlineLessChecker(String string, String string2) {
		return new InputException();
		// TODO Auto-generated method stub

	}

	public InputException addDeadlineChecker(String string, String string2, String string3) {
		return new InputException();
		// TODO Auto-generated method stub

	}

	public InputException addEventLessChecker(String string, String string2, String string3, String string4) {
		return new InputException();
		// TODO Auto-generated method stub

	}

	public InputException addEventChecker(String string, String string2, String string3, String string4,
			String string5) {
		return new InputException();
		// TODO Auto-generated method stub

	}

	public InputException deleteChecker(String name) {
		// TODO Auto-generated method stub
		return new InputException();
	}
}
