package todolist.model;

public class SearchCommand {
    
    String type = null;
    String content = null;
    
    public SearchCommand(String type, String content) {
        this.type = type;
        this.content = content;
    }
    
    public getSearchCommandType() {
        return type;
    }
    
    public getSearchCommand() {
        return content;
    }
}
