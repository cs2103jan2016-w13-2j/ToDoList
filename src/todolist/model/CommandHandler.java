package todolist.model;

public class CommandHandler {
    
    private UIHandler uiHandler;
    private DataBase dataBase;
    private FlexiCommandHandler flexiHandler;
    private NormalCommandHandler normalHandler;
    
    
    public CommandHandler(UIHandler uiHandler, DataBase dataBase) {
        this.uiHandler = uiHandler;
        this.dataBase = dataBase;
        normalHandler = new NormalCommandHandler(this.uiHandler, this.dataBase);
        flexiHandler = new FlexiCommandHandler(this.uiHandler, this.dataBase, this.normalHandler);
    }
    
    public void execute(Command Command) {
        
        if(Command.isSmart()) {
            flexiHandler.execute(new FlexiCommand(Command));
        } else {
            normalHandler.execute(new NormalCommand(Command));
        }
    }
  
}
