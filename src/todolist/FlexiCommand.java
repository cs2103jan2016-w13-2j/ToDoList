Package todolist;

public class FlexiCommand() {
    
    String rawInput = null;

    public FlexiCommand(Command command) {
        this.rawInput = command.getCommand();
    }
    
    public String getCommand() {
        return rawInput;
    }
}