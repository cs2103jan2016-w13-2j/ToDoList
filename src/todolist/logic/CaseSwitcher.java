//@@author A0130620B
package todolist.logic;

import todolist.model.InputException;
import todolist.model.Task;
import todolist.model.TokenizedCommand;
import todolist.storage.DataBase;

public class CaseSwitcher {

	private Logic logic;
	private DataBase dataBase;
	private InputErrorChecker inputErrorChecker;
	private CaseExecuter caseExecuter;

	public CaseSwitcher(Logic logic, DataBase dataBase) {
		this.logic = logic;
		this.dataBase = dataBase;
		this.inputErrorChecker = new InputErrorChecker(logic, dataBase);
		this.caseExecuter = new CaseExecuter(logic);
	}

	public void execute(TokenizedCommand command) {
		InputException inputException = inputErrorChecker.validate(command);
		if (inputException.getCorrectness()) {
			forceExecute(command);
		} else {
			logic.getUIHandler().sendMessage(inputException.getErrorMessage(), true);
		}
	}

	public void forceExecute(TokenizedCommand command) {

		String action = command.getAction();
		String arg[] = command.getArgs();

		switch (action) {

		case "add":
			caseExecuter.add(arg);
			break;
		case "edit":
			caseExecuter.edit(arg);
			break;
		case "delete":
			caseExecuter.delete(arg);
			break;
		case "search":
			caseExecuter.search(arg);
			break;
		case "filter":
			caseExecuter.filter(arg);
			break;
		case "sort":
			caseExecuter.sort(arg);
			break;
		case "label":
			caseExecuter.label(arg);
			break;
		case "set-recurring":
			caseExecuter.setRecurring(arg);
			break;
		case "remove-recurring":
			caseExecuter.removeRecurring(arg);
			break;
		case "postpone":
			caseExecuter.postpone(arg);
			break;
		case "forward":
			caseExecuter.forward(arg);
			break;
		case "add-remind":
			caseExecuter.addRemind(arg);
			break;
		case "remind":
			caseExecuter.remind(arg);
			break;
		case "remind-bef":
			caseExecuter.addRemindBef(arg);
			break;
		case "add-remind-bef":
			caseExecuter.addRemindBef(arg);
			break;
		case "done":
			caseExecuter.done(arg);
			break;
		case "undone":
			caseExecuter.undone(arg);
			break;
		case "exit":
			caseExecuter.exit(arg);
			break;
		case "undo":
			caseExecuter.undo(arg);
			break;
		case "redo":
			caseExecuter.redo(arg);
			break;
		case "reset":
			caseExecuter.reset(arg);
			break;
		case "save":
			caseExecuter.save(arg);
			break;
		case "open":
			caseExecuter.open(arg);
			break;
		case "tab":
			caseExecuter.tab(arg);
			break;
		case "invalid":
			caseExecuter.invalid(arg);
			break;
		case "clean":
		caseExecuter.clean(arg);
		default:

		}

		if (!action.equals("undo") && !action.equals("redo")) {
			logic.stepForward();
		}
	}
}
