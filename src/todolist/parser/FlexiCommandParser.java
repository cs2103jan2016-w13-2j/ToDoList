package todolist.parser;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import todolist.model.TokenizedCommand;

public class FlexiCommandParser {
    
    public FlexiCommandParser() {

    }
    
    public TokenizedCommand parse(String input) {
        
    	Parser parser = new Parser();
    	List<DateGroup> groups = parser.parse(input);
    	for(DateGroup group:groups) {
    	  List<Date> dates = group.getDates();
    	  int line = group.getLine();
    	  int column = group.getPosition();
    	  String matchingValue = group.getText();
    	  String syntaxTree = group.getSyntaxTree().toStringTree();
    	  Map parseMap = group.getParseLocations();
    	  boolean isRecurreing = group.isRecurring();
    	  Date recursUntil = group.getRecursUntil();
    	
    	}
    	
    	String action = null;
    	String args[] = null;
    	return new TokenizedCommand(action, args);
    }
}