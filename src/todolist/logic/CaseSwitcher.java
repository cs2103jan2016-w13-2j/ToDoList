//@@author A0130620B
package todolist.logic;

import todolist.model.InputException;
import todolist.model.Task;
import todolist.model.TokenizedCommand;

public class CaseSwitcher {

	private Logic logic;
	private InputErrorChecker inputErrorChecker;

	public CaseSwitcher(Logic logic) {
		this.logic = logic;
		this.inputErrorChecker = new InputErrorChecker(logic);
	}

	private boolean isInteger(String s) {
		try {
			@SuppressWarnings("unused")
            int i = Integer.parseInt(s);
			return true;
		} catch (NumberFormatException er) {
			return false;
		}
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
			String type = arg[0];
			switch (type) {
			case "event":
				if (arg.length == 6) {
					logic.addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
				} else {
					logic.addEventLess(arg[1], arg[2], arg[3], arg[4]);
				}
				break;
			case "deadline":
				if (arg.length == 4) {
					logic.addDeadline(arg[1], arg[2], arg[3]);
				} else {
					logic.addDeadlineLess(arg[1], arg[2]);
				}
				break;
			case "task":
				logic.addTask(arg[1]);
				break;
			case "recurring":
				switch (arg[1]) {
				case "event":
					if (arg.length == 8) {
						logic.addRecurringEvent(arg[2], arg[3], arg[4], arg[5], arg[6], arg[7]);
					} else {
						logic.addRecurringEventLess(arg[2], arg[3], arg[4], arg[5], arg[6]);
					}
					break;
				case "deadline":
					if (arg.length == 6) {
						logic.addRecurringDeadline(arg[2], arg[3], arg[4], arg[5]);
					} else {
						logic.addRecurringDeadlineLess(arg[2], arg[3], arg[4]);
					}
					break;
				default:
				}
				break;
			default:
			}
			break;
		case "edit":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.edit(task.getName().getName(), arg[1], arg[2]);
			} else {
				logic.edit(arg[0], arg[1], arg[2]);
			}
			break;
		case "delete":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.delete(task.getName().getName());
			} else {
				logic.delete(arg[0]);
			}
			break;
		case "search":
			logic.search(arg);
			break;
		case "filter":
			logic.filter(arg[0]);
			break;
		case "sort":
			logic.sort(arg[0], arg[1]);
			break;
		case "label":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.label(task.getName().getName(), arg[1]);
			} else {
				logic.label(arg[0], arg[1]);
			}
			break;
		case "set-recurring":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.setRecurring(task.getName().getName(), true, arg[1]);
			} else {
				logic.setRecurring(arg[0], true, arg[1]);
			}
			break;
		case "remove-recurring":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.setRecurring(task.getName().getName(), false, null);
			} else {
				logic.setRecurring(arg[0], false, null);
			}
			break;
		case "postpone":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.postpone(task.getName().getName(), arg[1], arg[2]);
			} else {
				logic.postpone(arg[0], arg[1], arg[2]);
			}
			break;
		case "forward":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.forward(task.getName().getName(), arg[1], arg[2]);
			} else {
				logic.forward(arg[0], arg[1], arg[2]);
			}
			break;
		case "add-remind":
			logic.addRemind(arg);
			break;
		case "remind":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.remind(task.getName().getName());
			} else {
				logic.remind(arg[0]);
			}
			break;
		case "add-remind-bef":
			String[] restOfArgs = new String[arg.length - 2];
			for (int i = 0; i < arg.length; i++) {
				restOfArgs[i] = arg[i + 2];
			}
			logic.addRemindBef(arg[0], arg[1], restOfArgs);
			break;
		case "remind-bef":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.remindBef(task.getName().getName(), arg[1], arg[2]);
			} else {
				logic.remindBef(arg[0], arg[1], arg[2]);
			}
			break;
		case "done":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.done(task.getName().getName());
			} else {
				logic.done(arg[0]);
			}
			break;
		case "undone":
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				logic.undone(task.getName().getName());
			} else {
				logic.undone(arg[0]);
			}
			break;
		case "exit":
			logic.exit();
			break;
		case "undo":
			logic.undo(Integer.parseInt(arg[0]));
			break;
		case "redo":
			logic.redo(Integer.parseInt(arg[0]));
			break;
		case "reset":
			logic.reset();
			break;
		case "save":
			logic.setNewFile(arg[0]);
			break;
		case "open":
			logic.openNewFile(arg[0]);
			break;
		case "tab":
			logic.tab(arg[0]);
			break;
		case "invalid":
			logic.invalid(arg[0]);
			break;
		default:
			
		}
		
		if(!action.equals("undo") && !action.equals("redo")) {
			logic.stepForward();
		}
	}
}
