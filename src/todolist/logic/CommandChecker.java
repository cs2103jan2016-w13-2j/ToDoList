//@@author A0130620B
package todolist.logic;

import todolist.model.InputException;
import todolist.model.Task;
import todolist.storage.DataBase;

public class CommandChecker {
	private Logic logic;
	private DataBase dataBase;
	private FunctionChecker functionChecker;

	protected CommandChecker(Logic logic, DataBase dataBase) {
		this.logic = logic;
		this.dataBase = dataBase;
		this.functionChecker = new FunctionChecker(this.logic, this.dataBase);
	}

	protected InputException add(String[] arg) {
		String type = "null";
		if (arg.length == 0) {
			return new InputException("ADD", "INCOMPLETE");
		} else {
			type = arg[0];
		}

		switch (type) {
		case "task":
			return addTask(arg);
		case "event":
			return addEvent(arg);
		case "deadline":
			return addDeadline(arg);
		case "recurring":
			return addRecurring(arg);
		default:
			return new InputException("ADD", "INCOMPLETE");
		}
	}

	protected InputException addTask(String[] arg) {
		if (arg.length != 2) {
			return new InputException("ADD TASK", "INCOMPLETE");
		} else {
			return functionChecker.addTaskChecker(arg[1]);
		}
	}

	protected InputException addEvent(String[] arg) {
		if (arg.length != 6 && arg.length != 5) {
			return new InputException("ADD EVENT", "INCOMPLETE");
		} else {
			if (arg.length == 6) {
				return functionChecker.addEventChecker(arg[1], arg[2], arg[3], arg[4], arg[5]);
			} else {
				return functionChecker.addEventLessChecker(arg[1], arg[2], arg[3], arg[4]);
			}
		}
	}

	protected InputException addDeadline(String[] arg) {
		if (arg.length != 4 && arg.length != 3) {
			return new InputException("ADD DEADLINE", "INCOMPLETE");
		} else {
			if (arg.length == 4) {
				return functionChecker.addDeadlineChecker(arg[1], arg[2], arg[3]);
			} else {
				return functionChecker.addDeadlineLessChecker(arg[1], arg[2]);
			}
		}
	}

	protected InputException addRecurring(String[] arg) {
		switch (arg[1]) {
		case "event":
			return addRecurringEvent(arg);
		case "deadline":
			return addRecurringDeadline(arg);
		default:
			return new InputException("ADD RECURRING", "INCOMPLETE");
		}
	}

	protected InputException addRecurringEvent(String[] arg) {
		if (arg.length != 8 && arg.length != 7) {
			return new InputException("ADD RECURRING EVENT", "INCOMPLETE");
		} else {
			if (arg.length == 8) {
				return functionChecker.addRecurringEventChecker(arg[2], arg[3], arg[4], arg[5], arg[6], arg[7]);
			} else {
				return functionChecker.addRecurringEventLessChecker(arg[2], arg[3], arg[4], arg[5], arg[6]);
			}
		}
	}

	protected InputException addRecurringDeadline(String[] arg) {
		if (arg.length != 6 && arg.length != 5) {
			return new InputException("ADD RECURRING DEADLINE", "INCOMPLETE");
		} else {
			if (arg.length == 6) {
				return functionChecker.addRecurringDeadlineChecker(arg[2], arg[3], arg[4], arg[5]);
			} else {
				return functionChecker.addRecurringDeadlineLessChecker(arg[2], arg[3], arg[4]);
			}
		}
	}

	protected InputException edit(String[] arg) {
		if (arg.length != 3) {
			return new InputException("EDIT", "INCOMPLETE");

		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("EDIT", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.editChecker(taskname,arg[1], arg[2]);
			} else {
				return functionChecker.editChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	protected InputException delete(String[] arg) {
		if (arg.length != 1) {
			return new InputException("DELETE", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("DELETE", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.deleteChecker(taskname);
			} else {
				return functionChecker.deleteChecker(arg[0]);
			}
		}
	}

	protected InputException search(String[] arg) {
		if (arg.length != 1) {
			return new InputException("SEARCH", "INCOMPLETE");
		} else {
			return functionChecker.searchChecker(arg);
		}
	}

	protected InputException filter(String[] arg) {
		if (arg.length != 1) {
			return new InputException("FILTER", "INCOMPLETE");
		} else {
			return functionChecker.filterChecker(arg);
		}
	}

	protected InputException sort(String[] arg) {
		if (arg.length != 2) {
			return new InputException("SORT", "INCOMPLETE");
		} else {
			return functionChecker.sortChecker(arg[0], arg[1]);
		}
	}

	protected InputException label(String[] arg) {
		if (arg.length != 2) {
			return new InputException("LABEL", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("LABEL", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.labelChecker(taskname, arg[1]);
			} else {
				return functionChecker.labelChecker(arg[0], arg[1]);
			}
		}
	}

	protected InputException setRecurring(String[] arg) {
		if (arg.length != 2) {
			return new InputException("SET-RECURRING", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("SET-RECURRING", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.setRecurringChecker(taskname, true, arg[1]);
			} else {
			    System.out.println(arg[0] + "     " + arg[1]);
				return functionChecker.setRecurringChecker(arg[0], true, arg[1]);
			}
		}
	}

	protected InputException removeRecurring(String[] arg) {
		if (arg.length != 1) {
			return new InputException("REMOVE-RECURRING", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("REMOVE-RECURRING", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.setRecurringChecker(taskname, false, null);
			} else {
				return functionChecker.setRecurringChecker(arg[0], false, null);
			}
		}
	}

	protected InputException postpone(String[] arg) {
		if (arg.length != 3) {
			return new InputException("POSTPONE", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("POSTPONE", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.postponeChecker(taskname, arg[1], arg[2]);
			} else {
				return functionChecker.postponeChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	protected InputException forward(String[] arg) {
		if (arg.length != 3) {
			return new InputException("FORWARD", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("FORWARD", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.forwardChecker(taskname, arg[1], arg[2]);
			} else {
				return functionChecker.forwardChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	protected InputException addRemind(String[] arg) {
		return functionChecker.addRemindChecker(arg);
	}

	protected InputException remind(String[] arg) {
		if (arg.length != 1) {
			return new InputException("REMIND", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("REMIND", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.remindChecker(taskname);
			} else {
				return functionChecker.remindChecker(arg[0]);
			}
		}
	}

	protected InputException addRemindBef(String[] arg) {
		String[] restOfArgs = new String[arg.length - 2];
		for (int i = 0; i < arg.length; i++) {
			restOfArgs[i] = arg[i + 2];
		}
		return functionChecker.addRemindBefChecker(arg[0], arg[1], restOfArgs);
	}

	protected InputException remindBef(String[] arg) {
		if (arg.length != 3) {
			return new InputException("REMIND-BEF", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("REMIND-BEF", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.remindBefChecker(taskname,arg[1], arg[2]);
			} else {
				return functionChecker.remindBefChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	protected InputException done(String[] arg) {
		if (arg.length != 1) {
			return new InputException("DONE", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("DONE", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.doneChecker(taskname);
			} else {
				return functionChecker.doneChecker(arg[0]);
			}
		}
	}

	protected InputException undone(String[] arg) {
		if (arg.length != 1) {
			return new InputException("UNDONE", "INCOMPLETE");
		} else {
			String temp[] = arg[0].split(",");
			int[] index = new int[temp.length];
			Boolean flag = true;
			for (int i = 0; i < temp.length; i++) {
				if (isInteger(temp[i])) {
					index[i] = Integer.parseInt(temp[i]);
				} else {
					flag = false;
				}
			}

			if (flag) {
				for (int i = 0; i < index.length; i++) {
					Task task = logic.getMainApp().getTaskAt(index[i]);
					if (task == null) {
						return new InputException("DONE", "TASK NOT EXIST");
					}
				}
				String taskname = logic.getMainApp().getTaskAt(index.length).getName().getName();
				return functionChecker.undoneChecker(taskname);
			} else {
				return functionChecker.undoneChecker(arg[0]);
			}
		}
	}

	protected InputException exit(String[] arg) {
		return functionChecker.exitChecker(arg);
	}

	protected InputException undo(String[] arg) {
		if (arg.length != 1) {
			return new InputException("UNDO", "INCOMPLETE");
		} else {
			return functionChecker.undoChecker(arg[0]);
		}
	}

	protected InputException redo(String[] arg) {
		if (arg.length != 1) {
			return new InputException("REDO", "INCOMPLETE");
		} else {
			return functionChecker.redoChecker(arg[0]);
		}
	}

	protected InputException reset(String[] arg) {
		if (arg.length != 0) {
			return new InputException("RESET", "INVALID ARGUMENT");
		}
		return functionChecker.resetChecker();
	}

	protected InputException tab(String[] arg) {
		if (arg.length != 1) {
			return new InputException("TAB", "INCOMPLETE");
		} else {
			return functionChecker.tabChecker(arg[0]);
		}
	}

	protected InputException open(String[] arg) {
		return functionChecker.openChecker(arg[0]);
	}

	protected InputException save(String[] arg) {
		return functionChecker.saveChecker(arg[0]);
	}

	protected InputException invalid(String[] arg) {
		return functionChecker.invalidChecker(arg[0]);
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

	protected InputException help(String[] arg) {
		return new InputException();
	}
}
