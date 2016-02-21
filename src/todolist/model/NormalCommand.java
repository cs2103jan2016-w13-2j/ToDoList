package todolist.model;

public class NormalCommand {

  String rawInput = null;
  String action = null;
  String args[];

  public NormalCommand(Command command) {
      this.rawInput = command.getCommand();
      
      String temp[] = rawInput.split(" ");
      this.action = temp[0];
      
      args = new String[temp.length -1];
      
      for(int i=0; i<temp.length-1; i++) {
          args[i] = temp[i+1];
      }
  }
  
  public String getCommand() {
      return rawInput;
  }
  
  public String getAction() {
      return action;
  }
  
  public String[] getArgs() {
      return args;
  }
}
