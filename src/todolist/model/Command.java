package todolist.model;

public class Command {
    private String rawCommand = null;
    private Boolean smart;
    
    public Command(String input) {
        this.rawCommand = input;
        smart = Parser.checkType(this);
    }
    
    public String getCommand() {
        return rawCommand;
    }
    
    public Boolean isSmart() {
        return smart;
    }
}
