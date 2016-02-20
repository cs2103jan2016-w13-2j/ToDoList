package todolist;

public class FlexiCommandHandler() {
    
    public void static execute(FlexiCommand flexiCommand) {
        NormalCommandHandler.execute(Parser.translate(flexiCommand));
    }
    
}