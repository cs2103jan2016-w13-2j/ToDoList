package todolist.model;

public class FlexiCommandHandler {
    
    public static void execute(FlexiCommand flexiCommand) {
        NormalCommandHandler.execute(Parser.translate(flexiCommand));
    }
    
}