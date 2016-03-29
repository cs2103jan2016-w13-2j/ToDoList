package todolist.model;

//@@author zhangjiyi
public class SearchCommand {
	private String type = null;
	private String content = null;
	
	public SearchCommand(String type, String content) {
		this.type = type;
		this.content = content;
	}

    public String getType() {
    	return type;
    }
    
    public String getContent() {
    	return content;
    }

}
