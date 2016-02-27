package todolist.model;

public class FlexiCommandHandler {
    
    private DataBase dataBase;
    private UIhandler uiHandler;
    private NormalCommandHandler normalHandler;
    
    public FlexiCommandHandler(UIHandler uiHandler, Database dataBase, NormalCommandHandler normalHandler) {
        this.dataBase = dataBase;
        this.uiHandler = uiHandler;
        this.normalHandler = normalHandler;
    }
    
    public void execute(FlexiCommand flexiCommand) {
        normalHandler.execute(Parser.translate(flexiCommand));
    }
}