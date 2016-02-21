package todolist.model;

public class CommandHandler {
    
  public static void execute(Command Command) {

    if(Command.isSmart()) {
        FlexiCommandHandler.execute(new FlexiCommand(Command));
    } else {
        NormalCommandHandler.execute(new NormalCommand(Command));
    }
  }
  
}
