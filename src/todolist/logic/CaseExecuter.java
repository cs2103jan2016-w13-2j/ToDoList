//@@author A0130620B
package todolist.logic;

public class CaseExecuter {
	private Logic logic;

	protected CaseExecuter(Logic logic) {
		this.logic = logic;
	}

	protected void add(String[] arg) {
		String type = arg[0];
		switch (type) {
		case "task":
			addTask(arg);
			break;
		case "event":
			addEvent(arg);
			break;
		case "deadline":
			addDeadline(arg);
			break;
		case "recurring":
			addRecurring(arg);
			break;
		default:
		}
	}

	protected void addTask(String[] arg) {
		logic.addTask(arg[1]);
	}

	protected void addEvent(String[] arg) {
		if (arg.length == 6) {
			logic.addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
		} else {
			logic.addEventLess(arg[1], arg[2], arg[3], arg[4]);
		}
	}

	protected void addDeadline(String[] arg) {
		if (arg.length == 4) {
			logic.addDeadline(arg[1], arg[2], arg[3]);
		} else {
			logic.addDeadlineLess(arg[1], arg[2]);
		}
	}

	protected void addRecurring(String[] arg) {
		switch (arg[1]) {
		case "event":
			addRecurringEvent(arg);
			break;
		case "deadline":
			addRecurringDeadline(arg);
			break;
		default:
		}
	}

	protected void addRecurringEvent(String[] arg) {
		if (arg.length == 8) {
			logic.addRecurringEvent(arg[2], arg[3], arg[4], arg[5], arg[6], arg[7]);
		} else {
			logic.addRecurringEventLess(arg[2], arg[3], arg[4], arg[5], arg[6]);
		}
	}

	protected void addRecurringDeadline(String[] arg) {
		if (arg.length == 6) {
			logic.addRecurringDeadline(arg[2], arg[3], arg[4], arg[5]);
		} else {
			logic.addRecurringDeadlineLess(arg[2], arg[3], arg[4]);
		}
	}

	protected void edit(String[] arg) {
		actionOnIndex("EDIT", arg);
	}

	protected void delete(String[] arg) {
		actionOnIndex("DELETE", arg);
	}

	protected void search(String[] arg) {
		logic.search(arg);
	}

	protected void filter(String[] arg) {
		logic.filter(arg[0]);
	}

	protected void sort(String[] arg) {
		logic.sort(arg[0], arg[1]);
	}

	protected void label(String[] arg) {
		actionOnIndex("LABEL", arg);
	}

	protected void setRecurring(String[] arg) {
		actionOnIndex("SET-RECURRING", arg);
	}

	protected void removeRecurring(String[] arg) {
		actionOnIndex("REMOVE-RECURRING", arg);
	}

	protected void postpone(String[] arg) {
		actionOnIndex("POSTPONE", arg);
	}

	protected void forward(String[] arg) {
		actionOnIndex("FORWARD", arg);
	}

	protected void addRemind(String[] arg) {
		logic.addRemind(arg);
	}

	protected void remind(String[] arg) {
		actionOnIndex("REMIND", arg);
	}

	protected void addRemindBef(String[] arg) {
		String[] restOfArgs = new String[arg.length - 2];
		for (int i = 0; i < arg.length; i++) {
			restOfArgs[i] = arg[i + 2];
		}
		logic.addRemindBef(arg[0], arg[1], restOfArgs);
	}

	protected void remindBef(String[] arg) {
		actionOnIndex("REMIND-BEF", arg);
	}

	protected void removeRemind(String[] arg) {
		actionOnIndex("REMOVE-REMIND", arg);
	}

	protected void done(String[] arg) {
		actionOnIndex("DONE", arg);
	}

	protected void undone(String[] arg) {
		actionOnIndex("UNDONE", arg);
	}

	protected void exit(String[] arg) {
		logic.exit();
	}

	protected void undo(String[] arg) {
		logic.undo(Integer.parseInt(arg[0]));
	}

	protected void redo(String[] arg) {
		logic.redo(Integer.parseInt(arg[0]));
	}

	protected void reset(String[] arg) {
		logic.reset();
	}

	protected void save(String[] arg) {
		logic.setNewFile(arg[0]);
	}

	protected void open(String[] arg) {
		logic.openNewFile(arg[0]);
	}

	protected void tab(String[] arg) {
		logic.tab(arg[0]);
	}

	protected void help(String[] arg) {
		logic.tab("help");
	}

	protected void invalid(String[] arg) {
		logic.invalid(arg[0]);
	}

	protected void clean(String[] arg) {
		logic.clean();
	}

	private void actionOnIndex(String type, String[] arg) {
		String temp[] = arg[0].split(",");
		int index = -1;
		String name[] = new String[temp.length];
		Boolean flag = true;
		for (int i = 0; i < temp.length; i++) {
			if (isInteger(temp[i])) {
				index = Integer.parseInt(temp[i]);
				name[i] = logic.getMainApp().getTaskAt(index).getName().getName();
			} else {
				flag = false;
			}
		}

		if (flag) {
			for (int i = 0; i < name.length; i++) {
				typeCaseSwitcher(type, name[i], arg);
			}
		} else {
			typeCaseSwitcher(type, arg[0], arg);
		}
	}

	private void typeCaseSwitcher(String type, String taskname, String[] arg) {
		switch (type) {
		case "EDIT":
			logic.edit(taskname, arg[1], arg[2]);
			break;
		case "DELETE":
			logic.delete(taskname);
			break;
		case "LABEL":
			logic.label(taskname, arg[1]);
			break;
		case "SET-RECURRING":
			logic.setRecurring(taskname, true, arg[1]);
			break;
		case "REMOVE-RECURRING":
			logic.setRecurring(taskname, false, null);
			break;
		case "POSTPONE":
			logic.postpone(taskname, arg[1], arg[2]);
			break;
		case "FORWARD":
			logic.forward(arg[0], arg[1], arg[2]);
			break;
		case "REMIND":
			logic.remind(taskname);
			break;
		case "REMIND-BEF":
			logic.remindBef(taskname, arg[1], arg[2]);
			break;
		case "REMOVE-REMIND":
			logic.removeRemind(taskname);
			break;
		case "DONE":
			logic.done(taskname);
			break;
		case "UNDONE":
			logic.undone(taskname);
			break;
		default:

		}
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
}
