package todolist.model;

public class FlexiCommandHandler {
    
    private Database database;
    private UIhandler uiHandler;
    private NormalCommandHandler normalHandler;
    
    public FlexiCommandHandler(UIHandler uiHandler, Database database, NormalCommandHandler normalHandler) {
        this.database = database;
        this.uiHandler = uiHandler;
        this.normalHandler = normalHandler;
    }
    
    public void execute(FlexiCommand flexiCommand) {
        normalHandler.execute(Parser.translate(flexiCommand));
    }
}