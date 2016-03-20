package todolist.model;

public class Parser {
	
	private FlexiCommandHandler flexiCommandHandler;
    private NormalCommandHandler normalCommandHandler;
 
    public Parser() {
    	this.flexiCommandHandler = new FlexiCommandHandler();
    	this.normalCommandHandler = new NormalCommandHandler();
    }
    
    /**
	 * This method takes in a string and parse it.
	 *
	 * 
	 * @return TokenizedCommand 
	 */
    public TokenizedCommand parse(String input) {
    	if(checkType(input)) {
    		return normalCommandHandler.parse(input);
    	} else {
    		return flexiCommandHandler.parse(input);
    	}
    }
    
    /**
	 * This method takes in a string and check whether it is a flexi command.
	 *
	 * 
	 * @return Boolean
	 */
    private Boolean checkType(String input) {
		return true;   
    }
}