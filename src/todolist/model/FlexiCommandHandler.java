package todolist.model;

public class FlexiCommandHandler {
    
    private NormalCommandHandler normalHandler;
    
    public FlexiCommandHandler(NormalCommandHandler normalHandler) {
        this.normalHandler = normalHandler;
    }
    
    public void execute(FlexiCommand flexiCommand) throws Exception {
        normalHandler.execute(Parser.translate(flexiCommand));
    }
}