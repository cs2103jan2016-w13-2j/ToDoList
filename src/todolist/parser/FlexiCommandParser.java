package todolist.parser;

import java.util.Date;
import java.util.List;
//import java.util.Map;
import java.util.TimeZone;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import todolist.model.TokenizedCommand;

//@@author zhangjiyi
public class FlexiCommandParser {

	public FlexiCommandParser() {

	}

	public TokenizedCommand parse(String input) {

		String temp[] = input.split(" ");
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equals("add") || temp[i].equals("edit") || temp[i].equals("delete") || temp[i].equals("search")
					|| temp[i].equals("filter") || temp[i].equals("sort") || temp[i].equals("insert")
					|| temp[i].equals("switchposition") || temp[i].equals("label") || temp[i].equals("postpone")
					|| temp[i].equals("forward") || temp[i].equals("add-remind") || temp[i].equals("remind")
					|| temp[i].equals("add-remind-bef") || temp[i].equals("remind-bef") || temp[i].equals("done")
					|| temp[i].equals("undone") || temp[i].equals("exit") || temp[i].equals("undo") || temp[i].equals("redo")
					|| temp[i].equals("reset") || temp[i].equals("tab") || temp[i].equals("set-recurring")
					|| temp[i].equals("remove-recurring") || temp[i].equals("create") || temp[i].equals("schedule")
					|| temp[i].equals("cancel") || temp[i].equals("remove") || temp[i].equals("modify") || temp[i].equals("change")
					|| temp[i].equals("replace") || temp[i].equals("archive") || temp[i].equals("complete") || temp[i].equals("finish")
					|| temp[i].equals("shelf") || temp[i].equals("unarchive") || temp[i].equals("incomplete")
					|| temp[i].equals("unfinish") || temp[i].equals("unshelf") || temp[i].equals("delay") || temp[i].equals("advance")
					|| temp[i].equals("categorize") || temp[i].equals("tag") || temp[i].equals("load")) {

				return new TokenizedCommand("invalid", temp);
			}
		}

		Parser parser = new Parser(TimeZone.getDefault());
		List<DateGroup> groups = parser.parse(input);
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			// int line = group.getLine();
			// int column = group.getPosition();
			// String matchingValue = group.getText();
			// String syntaxTree = group.getSyntaxTree().toStringTree();
			// Map parseMap = group.getParseLocations();
			// boolean isRecurreing = group.isRecurring();
			// Date recursUntil = group.getRecursUntil();
			System.out.println(dates);

		}

		String action = null;
		String args[] = null;
		return new TokenizedCommand("haha", temp);

	}
}