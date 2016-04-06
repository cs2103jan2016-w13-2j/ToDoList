package todolist.model;

//@@author A0130620B
public class TokenizedCommand {
	
  private String action = null;
  private String args[];

  public TokenizedCommand(String action, String[] args) {
	  this.action = action;
	  this.args = args;
  }
    
  public String getAction() {
      return action;
  }
  
  public String[] getArgs() {
      return args;
  }
}
