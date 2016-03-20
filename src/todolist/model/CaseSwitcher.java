package todolist.model;

public class CaseSwitcher {
	
	private Logic logic;
	
	public CaseSwitcher(Logic logic) {
		this.logic = logic;
	}
	
	public void execute(TokenizedCommand command) {
		String action = command.getAction();
		String arg[] = command.getArgs();
		switch (action) {
		case "add":
			String type = arg[0];
			switch (type) {
			case "event":
				try {
					logic.addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "deadline":
				try {
					logic.addDeadline(arg[1], arg[2], arg[3]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "task":
				logic.addTask(arg[1]);
				break;
			default:
				logic.uiHandler.sendMessage("Opps! I don't understand this command! Please try again.");
			}
			logic.stepForward(1);
			break;
		case "edit":
			logic.edit(arg[0], arg[1], arg[2]);
			logic.stepForward(2);
			break;
		case "delete":
			logic.delete(arg[0]);
			logic.stepForward(1);
			break;
		case "search":
			logic.search(arg[0]);
			break;
		case "filter":
			logic.filter(arg[0]);
			break;
		case "sort":
			logic.sort(arg[0], arg[1]);
			break;
		case "insert":
			// logic.insert(arg[0], arg[1], arg[2]);
			break;
		case "switchPosition":
			// logic.switchPosition(arg[0], arg[1]);
			break;
		case "label":
			logic.label(arg[0], arg[1]);
			break;
		case "postpone":
			logic.postpone(arg[0], arg[1], arg[2]);
			break;
		case "forward":
			logic.forward(arg[0], arg[1], arg[2]);
			logic.stepForward(2);
			break;
		case "add-remind":
			try {
				logic.addRemind(arg);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logic.stepForward(2);
			break;
		case "remind":
			logic.remind(arg[0]);
			logic.stepForward(2);
			break;
		case "add-remind-bef":
			String[] restOfArgs = new String[arg.length - 2];
			for (int i = 0; i < arg.length; i++) {
				restOfArgs[i] = arg[i + 2];
			}
			try {
				logic.addRemindBef(arg[0], arg[1], restOfArgs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logic.stepForward(2);
			break;
		case "remind-bef":
			logic.remindBef(arg[0], arg[1], arg[2]);
			logic.stepForward(2);
			break;
		case "done":
			logic.done(arg[0]);
			logic.stepForward(2);
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
		default:
			logic.uiHandler.sendMessage("Opps! I don't understand this command! Please try again.");
		}
	}
}
