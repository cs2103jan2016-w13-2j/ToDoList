package todolist.model;

public class FlexiCommandHandler {
    
    public FlexiCommandHandler() {

    }
    
    public TokenizedCommand parse(String input) {
    	String action = null;
    	String args[] = null;
    	return new TokenizedCommand(action, args);
    }
}