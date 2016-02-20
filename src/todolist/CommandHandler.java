package todolist;

public class CommandHandler() {


  public CommandHandler(Command rawCommand) {

    if(Parser.checkType(rawCommand)) {
      NormalCommandHandler(rawCommand);
    } else {
      FlexiCommandHander(rawCommand);
    }



  }


}
