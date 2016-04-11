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
				logic.edit(name[i], arg[1], arg[2]);
			}
		} else {
			logic.edit(arg[0], arg[1], arg[2]);
		}
	}

	protected void delete(String[] arg) {
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
				logic.delete(name[i]);
			}
		} else {
			logic.delete(arg[0]);
		}
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
				logic.label(name[i], arg[1]);
			}
		} else {
			logic.label(arg[0], arg[1]);
		}
	}

	protected void setRecurring(String[] arg) {
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
				logic.setRecurring(name[i], true, arg[1]);
			}
		} else {
			logic.setRecurring(arg[0], true, arg[1]);
		}
	}

	protected void removeRecurring(String[] arg) {
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
				logic.setRecurring(name[i], false, null);
			}
		} else {
			logic.setRecurring(arg[0], false, null);
		}
	}

	protected void postpone(String[] arg) {
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
				logic.postpone(name[i], arg[1], arg[2]);
			}
		} else {
			logic.postpone(arg[0], arg[1], arg[2]);
		}
	}

	protected void forward(String[] arg) {
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
				logic.forward(name[i], arg[1], arg[2]);
			}
		} else {
			logic.forward(arg[0], arg[1], arg[2]);
		}
	}

	protected void addRemind(String[] arg) {
		logic.addRemind(arg);
	}

	protected void remind(String[] arg) {
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
				logic.remind(name[i]);
			}
		} else {
			logic.remind(arg[0]);
		}
	}

	protected void addRemindBef(String[] arg) {
		String[] restOfArgs = new String[arg.length - 2];
		for (int i = 0; i < arg.length; i++) {
			restOfArgs[i] = arg[i + 2];
		}
		logic.addRemindBef(arg[0], arg[1], restOfArgs);
	}

	protected void remindBef(String[] arg) {
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
				logic.remindBef(name[i], arg[1], arg[2]);
			}
		} else {
			logic.remindBef(arg[0], arg[1], arg[2]);
		}
	}

	protected void done(String[] arg) {
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
				logic.done(name[i]);
			}
		} else {
			logic.done(arg[0]);
		}
	}

	protected void undone(String[] arg) {
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
				logic.undone(name[i]);
			}
		} else {
			logic.undone(arg[0]);
		}
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
