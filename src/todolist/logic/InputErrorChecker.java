//@@author A0130620B
package todolist.logic;

import todolist.model.InputException;
import todolist.model.TokenizedCommand;
import todolist.storage.DataBase;

public class InputErrorChecker {
	private Logic logic;
	private DataBase dataBase;
	private CommandChecker commandChecker;

	protected InputErrorChecker(Logic logic, DataBase dataBase) {
		this.logic = logic;
		this.dataBase = dataBase;
		commandChecker = new CommandChecker(this.logic, this.dataBase);
	}

	protected InputException validate(TokenizedCommand command) {
		String action = command.getAction();
		String arg[] = command.getArgs();

		switch (action) {
		case "add":
			return commandChecker.add(arg);
		case "edit":
			return commandChecker.edit(arg);
		case "delete":
			return commandChecker.delete(arg);
		case "search":
			return commandChecker.search(arg);
		case "filter":
			return commandChecker.filter(arg);
		case "sort":
			return commandChecker.sort(arg);
		case "label":
			return commandChecker.label(arg);
		case "set-recurring":
			return commandChecker.setRecurring(arg);
		case "remove-recurring":
			return commandChecker.removeRecurring(arg);
		case "postpone":
			return commandChecker.postpone(arg);
		case "forward":
			return commandChecker.forward(arg);
		case "add-remind":
			return commandChecker.addRemind(arg);
		case "remind":
			return commandChecker.remind(arg);
		case "add-remind-bef":
			return commandChecker.addRemindBef(arg);
		case "remind-bef":
			return commandChecker.remindBef(arg);
		case "remove-remind":
			return commandChecker.removeRemind(arg);
		case "done":
			return commandChecker.done(arg);
		case "undone":
			return commandChecker.undone(arg);
		case "exit":
			return commandChecker.exit(arg);
		case "undo":
			return commandChecker.undo(arg);
		case "redo":
			return commandChecker.redo(arg);
		case "reset":
			return commandChecker.reset(arg);
		case "save":
			return commandChecker.save(arg);
		case "open":
			return commandChecker.open(arg);
		case "tab":
			return commandChecker.tab(arg);
		case "invalid":
			return commandChecker.invalid(arg);
		case "help":
			return commandChecker.help(arg);
		case "clean":
			return commandChecker.clean(arg);
		default:
			return new InputException("UNKNOWN", "UNKNOWN");
		}
	}
}
