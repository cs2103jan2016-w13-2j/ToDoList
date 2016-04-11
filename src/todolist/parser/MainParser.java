//@@author A0130620B
package todolist.parser;

import todolist.model.TokenizedCommand;

public class MainParser {

	private FlexiCommandParser flexiCommandParser;
	private NormalCommandParser normalCommandParser;

	public MainParser() {
		this.flexiCommandParser = new FlexiCommandParser();
		this.normalCommandParser = new NormalCommandParser();
	}

	/**
	 * This method takes in a string and parse it.
	 *
	 * @param input
	 *            raw user input
	 * 
	 * @return tokenized command
	 */
	public TokenizedCommand parse(String input) {
		if (checkType(input)) {
			return normalCommandParser.parse(input);
		} else {
			// return normalCommandParser.parse(input);
			return flexiCommandParser.parse(input);
		}
	}

	/**
	 * This method takes in a string and check whether it is a flexible command.
	 *
	 * @param input
	 *            raw user input
	 * 
	 * @return Boolean
	 */
	private Boolean checkType(String input) {
		String temp[] = input.split(" ");
		String head = temp[0];
		Boolean type = head.equals("add") || head.equals("edit") || head.equals("delete") || head.equals("search")
				|| head.equals("filter") || head.equals("sort") || head.equals("insert")
				|| head.equals("switchposition") || head.equals("label") || head.equals("postpone")
				|| head.equals("forward") || head.equals("add-remind") || head.equals("remind")
				|| head.equals("add-remind-bef") || head.equals("remind-bef") || head.equals("remove-remind")
				|| head.equals("done") || head.equals("undone") || head.equals("exit") || head.equals("undo")
				|| head.equals("redo") || head.equals("reset") || head.equals("tab") || head.equals("set-recurring")
				|| head.equals("remove-recurring") || head.equals("create") || head.equals("schedule")
				|| head.equals("cancel") || head.equals("remove") || head.equals("modify") || head.equals("change")
				|| head.equals("replace") || head.equals("archive") || head.equals("complete") || head.equals("finish")
				|| head.equals("shelf") || head.equals("unarchive") || head.equals("incomplete")
				|| head.equals("unfinish") || head.equals("unshelf") || head.equals("delay") || head.equals("advance")
				|| head.equals("categorize") || head.equals("tag") || head.equals("load") || head.equals("open")
				|| head.equals("save") || head.equals("help");
		return type;
	}
}