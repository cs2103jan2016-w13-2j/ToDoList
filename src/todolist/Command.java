Package todolist;

public class Command() {
    String rawCommand = null;
    Boolean smart;
    
    public Command(String input) {
        this.rawCommand = input;
        smart = Parser.CheckType(this);
    }
    
    public String getCommand() {
        return rawCommand;
    }
    
    public Boolean isSmart() {
        return smart;
    }
}
