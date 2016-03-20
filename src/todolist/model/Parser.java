package todolist.model;

public class Parser {
	
	private FlexiCommandHandler flexiCommandHandler;
    private NormalCommandHandler normalCommandHandler;
 
    public Parser() {
    	this.flexiCommandHandler = new FlexiCommandHandler();
    	this.normalCommandHandler = new NormalCommandHandler();
    }
    
    public TokenizedCommand parse(String input) {
    	if(checkType(input)) {
    		return normalCommandHandler.parse(input);
    	} else {
    		return flexiCommandHandler.parse(input);
    	}
    }
    
    private Boolean checkType(String input) {
		return true;   
    }
}