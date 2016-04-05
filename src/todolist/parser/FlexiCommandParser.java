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
        for(int i = 0; i < temp.length; i++) {
        	if(temp[i].equals("add") || temp[i].equals("edit") || temp[i].equals("delete") || temp[i].equals("search")
            || temp[i].equals("filter") || temp[i].equals("sort") || temp[i].equals("insert")
            || temp[i].equals("switchposition") || temp[i].equals("label") || temp[i].equals("postpone")
            || temp[i].equals("forward") || temp[i].equals("add-remind") || temp[i].equals("remind")
            || temp[i].equals("add-remind-bef") || temp[i].equals("remind-bef") || temp[i].equals("done")
            || temp[i].equals("undone") || temp[i].equals("exit") || temp[i].equals("undo") || temp[i].equals("redo")
            || temp[i].equals("reset") || temp[i].equals("tab") || temp[i].equals("set-recurring")
            || temp[i].equals("remove-recurring")) {
        		return new TokenizedCommand("invalid", temp);
        	} else {
        		
        	}
        }
    	
    	
    	
    	
    	
    	

        Parser parser = new Parser(TimeZone.getDefault());
        List<DateGroup> groups = parser.parse(input);
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
//            int line = group.getLine();
//            int column = group.getPosition();
//            String matchingValue = group.getText();
//            String syntaxTree = group.getSyntaxTree().toStringTree();
//            Map parseMap = group.getParseLocations();
//            boolean isRecurreing = group.isRecurring();
//            Date recursUntil = group.getRecursUntil();
            System.out.println(dates);

        }

        String action = null;
        String args[] = null;
        return new TokenizedCommand(action, args);
    }
}