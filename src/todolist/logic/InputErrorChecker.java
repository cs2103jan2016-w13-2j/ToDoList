package todolist.logic;

import todolist.model.InputException;
import todolist.model.Task;
import todolist.model.TokenizedCommand;

//@@author A0130620B
public class InputErrorChecker {
	Logic logic;
	
	public InputErrorChecker (Logic logic) {
		this.logic = logic;
	}
	
	
	public InputException validate(TokenizedCommand command) {
		String action = command.getAction();
		String arg[] = command.getArgs();
		
		switch(action) {
		case "add":
			String type = "null";
			if (arg.length == 0) {
				return new InputException("ADD", "INCOMPLETE");
			} else {
				type = arg[0];
			}

			switch (type) {
			case "event":
				if (arg.length != 6 && arg.length != 5) {
					return new InputException("ADD EVENT", "INCOMPLETE");
				} else {
					
				}
				break;
			case "deadline":
				if (arg.length != 4 && arg.length != 3) {
					return new InputException("ADD DEADLINE", "INCOMPLETE");
				} else {
					
				}
				break;
			case "task":
				if (arg.length != 2) {
					return new InputException("ADD TASK", "INCOMPLETE");
				} else {
					
				}
				break;
			case "recurring":
				switch (arg[1]) {
				case "event":
					if (arg.length != 8 && arg.length != 7) {
						return new InputException("ADD RECURRING EVENT", "INCOMPLETE");
					} else {
						
					}
					break;
				case "deadline":
					if (arg.length != 6 && arg.length != 5) {
						return new InputException("ADD RECURRING DEADLINE", "INCOMPLETE");
					} else {
						
					}
					break;
				default:

				}
				break;
			default:
				
			}
			break;
		case "edit":
			if (arg.length != 3) {
				return new InputException("EDIT", "INCOMPLETE");

			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("EDIT", "NOT EXIST");
					} else {
						
					}
				} catch (NumberFormatException nfe) {

				}
			}
			break;
		case "delete":
			if (arg.length != 1) {
				return new InputException("DELETE", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("DELETE", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}
			}
			break;
		case "search":
			if (arg.length != 1) {
				return new InputException("SEARCH", "INCOMPLETE");
			} else {

			}
			break;
		case "filter":
			if (arg.length != 1) {
				return new InputException("FILTER", "INCOMPLETE");
			} else {
			}
			break;
		case "sort":
			if (arg.length != 2) {
				return new InputException("SORT", "INCOMPLETE");
			} else {

			}
			break;
		case "label":
			if (arg.length != 2) {
				return new InputException("LABEL", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("LABEL", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}

			}
			break;
		case "set-recurring":
			if (arg.length != 2) {
				return new InputException("SET-RECURRING", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("SET-RECURRING", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				} catch (RuntimeException e) {

				}
			}
			break;
		case "remove-recurring":
			if (arg.length != 1) {
				return new InputException("REMOVE-RECURRING", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("REMOVE-RECURRING", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}
			}
			break;
		case "postpone":
			if (arg.length != 3) {
				return new InputException("POSTPONE", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("POSTPONE", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}

			}
			break;
		case "forward":
			if (arg.length != 3) {
				return new InputException("FORWARD", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("FORWARD", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}

			}
			break;
		case "add-remind":
			try {

			} catch (Exception e1) {

			}
			break;
		case "remind":
			if (arg.length != 1) {
				return new InputException("REMIND", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("REMIND", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}
			}
			break;
		case "add-remind-bef":

			break;
		case "remind-bef":
			if (arg.length != 3) {
				return new InputException("REMIND-BEF", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("REMIND", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}
			}
			break;
		case "done":
			if (arg.length != 1) {
				return new InputException("DONE", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("DONE", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}

			}
			break;
		case "undone":
			if (arg.length != 1) {
				return new InputException("UNDONE", "INCOMPLETE");
			} else {
				try {
					int index = Integer.parseInt(arg[0]);
					Task task = logic.getMainApp().getTaskAt(index);
					if (task == null) {
						return new InputException("UNDONE", "NOT EXIST");
					} else {

					}
				} catch (NumberFormatException nfe) {

				}
			}
			break;
		case "exit":

			break;
		case "undo":
			if (arg.length != 1) {
				return new InputException("UNDO", "INCOMPLETE");
			} else {
				
			}
			break;
		case "redo":
			if (arg.length != 1) {
				return new InputException("REDO", "INCOMPLETE");
			} else {

			}
			break;
		case "reset":

			break;
		case "save":

			break;
		case "open":

			break;
		case "tab":
			if (arg.length != 1) {
				return new InputException("TAB", "INCOMPLETE");
			} else {

			}
			break;
		case "invalid":

			break;
		default:
			
		}
		return new InputException();
	}
}
