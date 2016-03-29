package todolist.logic;

import todolist.model.TokenizedCommand;

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
            String type = "null";
            if (arg.length == 0) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To create a task, you will first need to specify the type of task that you wish to create.");
            } else {
                type = arg[0];
            }

            switch (type) {
            case "event":
                if (arg.length != 6 && arg.length != 5) {
                    logic.getUIHandler().sendMessage(
                            "Your command was incomplete! To add an event, try: add event [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day].");
                } else {
                	if(arg.length == 6) {
                		logic.addEvent(arg[1], arg[2], arg[3], arg[4], arg[5]);
                        logic.stepForward(1);
                	} else {
                		logic.addEventLess(arg[1], arg[2], arg[3], arg[4]);
                        logic.stepForward(1);
                	}
                }
                break;
            case "deadline":
                if (arg.length != 4 && arg.length != 3) {
                    logic.getUIHandler().sendMessage(
                            "Your command was incomplete! To add a deadline, try: add deadline [title] [YYYY-MM-DD] [HH:MM]");
                } else {
                	if(arg.length == 4) {
                        logic.addDeadline(arg[1], arg[2], arg[3]);
                        logic.stepForward(1);
                	} else {
                        logic.addDeadlineLess(arg[1], arg[2]);
                        logic.stepForward(1);
                	}
                }
                break;
            case "task":
                if (arg.length != 2) {
                    logic.getUIHandler()
                            .sendMessage("Your command was incomplete! To add an un-dated task: add task [title]");
                } else {
                    logic.addTask(arg[1]);
                    logic.stepForward(1);
                }
                break;
            case "recurring":
                switch (arg[1]) {
                case "event":
                    if (arg.length != 8 && arg.length != 7 ) {
                        logic.getUIHandler().sendMessage(
                                "Your command was incomplete! To add a recurring event, try: add recurring event [7-day] [title] [YYYY-MM-DD] [HH:MM] [number] [hour | day]");
                    } else {
                    	if(arg.length == 8) {
                    		logic.addRecurringEvent(arg[2], arg[3], arg[4], arg[5], arg[6], arg[7]);
                            logic.stepForward(3);
                    	} else {
                    		logic.addRecurringEventLess(arg[2], arg[3], arg[4], arg[5], arg[6]);
                            logic.stepForward(3);
                    	}
                    }
                    break;
                case "deadline":
                    if (arg.length != 6 && arg.length != 5) {
                        logic.getUIHandler().sendMessage(
                                "Your command was incomplete! To add a recurring deadline, try: add recurring deadline [7-day] [title] [YYYY-MM-DD] [HH:MM]");
                    } else {
                    	if(arg.length == 6) {
                            logic.addRecurringDeadline(arg[2], arg[3], arg[4], arg[5]);
                            logic.stepForward(3);
                    	} else {
                            logic.addRecurringDeadlineLess(arg[2], arg[3], arg[4]);
                            logic.stepForward(3);
                    	}
                    }
                    break;
                default:
                    logic.getUIHandler().sendMessage("Opps! I don't understand this command! Please try again.");

                }
                break;
            default:
                logic.getUIHandler().sendMessage("Opps! I don't understand this command! Please try again.");
            }
            break;
        case "edit":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To edit a task, try: edit [title] [field-name] [new-value]");
            } else {
                logic.edit(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "delete":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage("Your command was incomplete! To delete a task, try: delete [title]");
            } else {
                logic.delete(arg[0]);
                logic.stepForward(1);
            }
            break;
        case "search":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To search for something, try: search [search-term] (You are searching task names!)");
            } else {
                logic.search(arg[0]);
            }
            break;
        case "filter":
            if (arg.length != 1) {
                logic.getUIHandler()
                        .sendMessage("Your command was incomplete! To apply a filter, try: filter [category-name]");
            } else {
                logic.filter(arg[0]);
            }
            break;
        case "sort":
            if (arg.length != 2) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To sort, try: sort ['start' | 'end' | 'category' | 'name'] [ascending | descending]");
            } else {
                logic.sort(arg[0], arg[1]);
                logic.stepForward(1);
            }
            break;
        case "insert":
            // logic.insert(arg[0], arg[1], arg[2]);
            break;
        case "switchPosition":
            // logic.switchPosition(arg[0], arg[1]);
            break;
        case "label":
            if (arg.length != 2) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To categorise or label a task, try: label [title] [category-name]");
            } else {
                logic.label(arg[0], arg[1]);
                logic.stepForward(2);
            }
            break;
        case "set-recurring":
            if (arg.length != 2) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To set a task to repeat, try: set-recurring [title] [interval]");
            } else {
                logic.setRecurring(arg[0], true, arg[1]);
                logic.stepForward(2);
            }
            break;
        case "remove-recurring":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To stop a task from repeating, try: remove-recurring [title]");
            } else {
                logic.setRecurring(arg[0], false, null);
                logic.stepForward(2);
            }
            break;
        case "postpone":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To postpone a task, try: postpone [title] [number] [hour | day]");
            } else {
                logic.postpone(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "forward":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To forward a task, try: forward [title] [number] [hour | day]");
            } else {
                logic.forward(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "add-remind":
            try {
                // need to handle exceptions here
                logic.addRemind(arg);
                logic.stepForward(3);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            break;
        case "remind":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To get ToDoList to remind you on a task, try: remind [title]");
            } else {
                logic.remind(arg[0]);
                logic.stepForward(2);
            }
            break;
        case "add-remind-bef":
            // need to handle exceptions here
            String[] restOfArgs = new String[arg.length - 2];
            for (int i = 0; i < arg.length; i++) {
                restOfArgs[i] = arg[i + 2];
            }
            try {
                logic.addRemindBef(arg[0], arg[1], restOfArgs);
                logic.stepForward(3);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case "remind-bef":
            if (arg.length != 3) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To get ToDoList to remind you on a task sometime before it is due, try: remind-bef [title] [number] [hour | day]");
            } else {
                logic.remindBef(arg[0], arg[1], arg[2]);
                logic.stepForward(2);
            }
            break;
        case "done":
            if (arg.length != 1) {
                logic.getUIHandler()
                        .sendMessage("Your command was incomplete! To archive a completed task, try: done [title]");
            } else {
                logic.done(arg[0]);
                logic.stepForward(2);
            }
            break;
        case "undone":
            if (arg.length != 1) {
                logic.getUIHandler()
                        .sendMessage("Your command was incomplete! To un-archive an ongoing task, try: undone [title]");
            } else {
                logic.undone(arg[0]);
                logic.stepForward(2);
            }
            break;
        case "exit":
            logic.exit();
            break;
        case "undo":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To undo a few action(s), try: undo [number-of-actions]");
            } else {
                logic.undo(Integer.parseInt(arg[0]));
            }
            break;
        case "redo":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To redo a few action(s), try: redo [number-of-actions]");
            } else {
                logic.redo(Integer.parseInt(arg[0]));
            }
            break;
        case "reset":
            logic.reset();
            break;
        case "save":
        	logic.setNewFile(arg[0]);
        	break;
        case "tab":
            if (arg.length != 1) {
                logic.getUIHandler().sendMessage(
                        "Your command was incomplete! To navigate to a certain page, try: tab [page-name] (as reflected on the tab bar)");
            } else {
                logic.tab(arg[0]);
            }
            break;
        default:
            logic.getUIHandler().sendMessage("Opps! I don't understand this command! Please try again.");
        }
    }
}
