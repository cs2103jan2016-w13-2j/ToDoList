package todolist.model;

public class CommandHandler {
    
    private UIHandler uiHandler;
    private DataBase dataBase;
    private FlexiCommandHandler flexiHandler;
    private NormalCommandHandler normalHandler;
    private Logic logic;
    
    
    public CommandHandler(UIHandler uiHandler, DataBase dataBase, Logic logic) {
        this.uiHandler = uiHandler;
        this.dataBase = dataBase;
        this.logic = logic;
        normalHandler = new NormalCommandHandler(this.dataBase, this.uiHandler, this.logic);
        flexiHandler = new FlexiCommandHandler(this.normalHandler);
    }
    
    public void execute(Command Command) throws Exception {
        
        if(false/*Command.isSmart()*/) {
            flexiHandler.execute(new FlexiCommand(Command));
        } else {
            normalHandler.execute(new NormalCommand(Command));
        }
    }
  
}
