package todolist;

public class CommandHandler() {


  public CommandHandler(Command rawCommand) {

    Type commandType = rawCommand.getCommandType();


    if(Parser.checkType(rawCommand)) {
      NormalCommandHandler(rawCommand);
    } else {
      FlexiCommandHander(rawCommand);
    }



  }


}
