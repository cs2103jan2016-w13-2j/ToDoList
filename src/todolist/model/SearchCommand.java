package todolist.model;

public class SearchCommand {
    
    private String type = null;
    private String content = null;
    
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
