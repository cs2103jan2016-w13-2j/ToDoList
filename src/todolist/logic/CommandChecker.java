package todolist.logic;

import todolist.model.InputException;
import todolist.model.Task;

public class CommandChecker {
	private Logic logic;
	private FunctionChecker functionChecker;
	
	
	public CommandChecker(Logic logic) {
		this.logic = logic;
		this.functionChecker = new FunctionChecker(this.logic);
	}
	
	public InputException add(String[] arg) {
		String type = "null";
		if (arg.length == 0) {
			return new InputException("ADD", "INCOMPLETE");
		} else {
			type = arg[0];
		}

		switch (type) {
		case "event":
			return addEvent(arg);
		case "deadline":
			return addDeadline(arg);
		case "task":
			return addTask(arg);
		case "recurring":
			return addRecurring(arg);
		default:
			return new InputException("ADD", "INCOMPLETE");
		}
	}

	public InputException addEvent(String[] arg) {
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

	public InputException addDeadline(String[] arg) {
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

	public InputException addTask(String[] arg) {
		if (arg.length != 2) {
			return new InputException("ADD TASK", "INCOMPLETE");
		} else {
			return functionChecker.addTaskChecker(arg[1]);
		}
	}

	public InputException addRecurring(String[] arg) {
		switch (arg[1]) {
		case "event":
			return addRecurringEvent(arg);
		case "deadline":
			return addRecurringDeadline(arg);
		default:
			return new InputException("ADD RECURRING", "INCOMPLETE");
		}
	}

	public InputException addRecurringEvent(String[] arg) {
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

	public InputException addRecurringDeadline(String[] arg) {
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

	public InputException edit(String[] arg) {
		if (arg.length != 3) {
			return new InputException("EDIT", "INCOMPLETE");

		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("EDIT", "NOT EXIST");
				} else {
					return functionChecker.editChecker(task.getName().getName(), arg[1], arg[2]);
				}
			} else {
				return functionChecker.editChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	public InputException delete(String[] arg) {
		if (arg.length != 1) {
			return new InputException("DELETE", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("DELETE", "NOT EXIST");
				} else {
					return functionChecker.deleteChecker(task.getName().getName());
				}
			} else {
				return functionChecker.deleteChecker(arg[0]);
			}
		}
	}

	public InputException search(String[] arg) {
		if (arg.length != 1) {
			return new InputException("SEARCH", "INCOMPLETE");
		} else {
			return functionChecker.searchChecker(arg);
		}
	}

	public InputException filter(String[] arg) {
		if (arg.length != 1) {
			return new InputException("FILTER", "INCOMPLETE");
		} else {
			return functionChecker.filterChecker(arg);
		}
	}

	public InputException sort(String[] arg) {
		if (arg.length != 2) {
			return new InputException("SORT", "INCOMPLETE");
		} else {
			return functionChecker.sortChecker(arg);
		}
	}

	public InputException label(String[] arg) {
		if (arg.length != 2) {
			return new InputException("LABEL", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("LABEL", "NOT EXIST");
				} else {
					return functionChecker.labelChecker(task.getName().getName(), arg[1]);
				}
			} else {
				return functionChecker.labelChecker(arg[0], arg[1]);
			}
		}
	}

	public InputException setRecurring(String[] arg) {
		if (arg.length != 2) {
			return new InputException("SET-RECURRING", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("SET-RECURRING", "NOT EXIST");
				} else {
					return functionChecker.setRecurringChecker(task.getName().getName(), true, arg[1]);
				}
			} else {
				return functionChecker.setRecurringChecker(arg[0], true, arg[1]);
			}
		}
	}

	public InputException removeRecurring(String[] arg) {
		if (arg.length != 2) {
			return new InputException("SET-RECURRING", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("REMOVE-RECURRING", "NOT EXIST");
				} else {
					return functionChecker.removeRecurringChecker(task.getName().getName(), true, arg[1]);
				}
			} else {
				return functionChecker.removeRecurringChecker(arg[0], true, arg[1]);
			}
		}
	}

	public InputException postpone(String[] arg) {
		if (arg.length != 3) {
			return new InputException("POSTPONE", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("POSTPONE", "NOT EXIST");
				} else {
					return functionChecker.postponeChecker(task.getName().getName(), arg[1], arg[2]);
				}
			} else {
				return functionChecker.postponeChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	public InputException forward(String[] arg) {
		if (arg.length != 3) {
			return new InputException("FORWARD", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("FORWARD", "NOT EXIST");
				} else {
					return functionChecker.forwardChecker(task.getName().getName(), arg[1], arg[2]);
				}
			} else {
				return functionChecker.forwardChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	public InputException addRemind(String[] arg) {
		return functionChecker.addRemindChecker(arg);
	}

	public InputException remind(String[] arg) {
		if (arg.length != 1) {
			return new InputException("REMIND", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("REMIND", "NOT EXIST");
				} else {
					return functionChecker.remindChecker(task.getName().getName());
				}
			} else {
				return functionChecker.remindChecker(arg[0]);
			}
		}
	}

	public InputException addRemindBef(String[] arg) {
		String[] restOfArgs = new String[arg.length - 2];
		for (int i = 0; i < arg.length; i++) {
			restOfArgs[i] = arg[i + 2];
		}
		return functionChecker.addRemindBefChecker(arg[0], arg[1], restOfArgs);
	}

	public InputException remindBef(String[] arg) {
		if (arg.length != 3) {
			return new InputException("REMIND-BEF", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("REMIND-BEF", "NOT EXIST");
				} else {
					return functionChecker.remindBefChecker(task.getName().getName(), arg[1], arg[2]);
				}
			} else {
				return functionChecker.remindBefChecker(arg[0], arg[1], arg[2]);
			}
		}
	}

	public InputException done(String[] arg) {
		if (arg.length != 1) {
			return new InputException("DONE", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("DONE", "NOT EXIST");
				} else {
					return functionChecker.doneChecker(task.getName().getName());
				}
			} else {
				return functionChecker.doneChecker(arg[0]);
			}
		}
	}

	public InputException undone(String[] arg) {
		if (arg.length != 1) {
			return new InputException("UNDONE", "INCOMPLETE");
		} else {
			if (isInteger(arg[0])) {
				int index = Integer.parseInt(arg[0]);
				Task task = logic.getMainApp().getTaskAt(index);
				if (task == null) {
					return new InputException("UNDONE", "NOT EXIST");
				} else {
					return functionChecker.undoneChecker(task.getName().getName());
				}
			} else {
				return functionChecker.undoneChecker(arg[0]);
			}
		}
	}
	
	public InputException exit(String[] arg) {
		return functionChecker.exitChecker(arg);
	}
	
	public InputException undo(String[] arg) {
		if (arg.length != 1) {
			return new InputException("UNDO", "INCOMPLETE");
		} else {
			return functionChecker.undoChecker(arg);
		}
	}
	
	public InputException redo(String[] arg) {
		if (arg.length != 1) {
			return new InputException("REDO", "INCOMPLETE");
		} else {
			return functionChecker.redoChecker(arg);
		}
	}
	
	public InputException reset(String[] arg) {
		return functionChecker.resetChecker(arg);
	}
	
	public InputException tab(String[] arg) {
		if (arg.length != 1) {
			return new InputException("TAB", "INCOMPLETE");
		} else {
			return functionChecker.tabChecker(arg);
		}
	}
	
	public InputException open(String[] arg) {
		return functionChecker.openChecker(arg);
	}
	
	public InputException save(String[] arg) {
		return functionChecker.saveChecker(arg);
	}
	
	public InputException invalid(String[] arg) {
		return functionChecker.invalidChecker(arg);
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
