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
	 * 
	 * @return TokenizedCommand 
	 */
    public TokenizedCommand parse(String input) {
    	if(checkType(input)) {
    		return normalCommandParser.parse(input);
    	} else {
    		return flexiCommandParser.parse(input);
    	}
    }
    
    /**
	 * This method takes in a string and check whether it is a flexi command.
	 *
	 * 
	 * @return Boolean
	 */
    private Boolean checkType(String input) {
    	String temp[] = input.split(" ");
    	String head = temp[0];
    	Boolean type = head.equals("add")||
    			head.equals("edit")||
    			head.equals("delete")||
    			head.equals("search")||
    			head.equals("filter")||
    			head.equals("sort")||
    			head.equals("insert")||
    			head.equals("switchposition")||
    			head.equals("label")||
    			head.equals("postpone")||
    			head.equals("forward")||
    			head.equals("add-remind")||
    			head.equals("remind")||
    			head.equals("add-remind-bef")||
    			head.equals("remind-bef")||
    			head.equals("done")||
    			head.equals("exit")||
    			head.equals("undo")||
    			head.equals("redo")||
    			head.equals("reset")||
    			head.equals("set-recurring")||
    			head.equals("remove-recurring");
		return type;   
    }
}