package todolist.model;

public class FlexiCommand {
    
    private String rawInput = null;

    public FlexiCommand(Command command) {
        this.rawInput = command.getCommand();
    }
    
    public String getCommand() {
        return rawInput;
    }
}