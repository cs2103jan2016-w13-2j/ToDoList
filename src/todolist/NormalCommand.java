package todolist;

public class NormalCommand() {

  String rawInput = null;
  String action = null;
  String args[];

  public Command(String UserInput) {
      this.rawInput = Userinput;
      
      String temp[] = rawInput.split(" ");
      this.action = temp[0];
      
      args[] = new String[temp.size() -1];
      
      for(int i=0; i<temp.size()-1; i++) {
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
