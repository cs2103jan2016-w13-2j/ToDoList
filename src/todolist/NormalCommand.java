package todolist;

public class NormalCommand() {

  String rawInput = null;
  String action = null;
  String args[] = new String[5];

  public Command(String UserInput) {
      this.rawInput = Userinput;
      
      String temp[] = rawInput.split(" ");
      this.action = temp[0];
  }
  
  public getCommand() {
      return rawInput;
  }
  
  public getAction() {
      
  }
}
