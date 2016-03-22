package todolist.model;

public class FlexiCommandParser {
    
    public FlexiCommandParser() {

    }
    
    public TokenizedCommand parse(String input) {
    	String action = null;
    	String args[] = null;
    	return new TokenizedCommand(action, args);
    }
}