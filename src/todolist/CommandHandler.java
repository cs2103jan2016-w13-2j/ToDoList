package todolist;

public class CommandHandler() {
    
  public void static execute(Command Command) {

    if(command.isSmart) {
        FlexiCommandHander.execute(new FlexiCommand(command));
    } else {
        NormalCommandHandler.execute(new NormalCommand(command));
    }
  }
  
}
