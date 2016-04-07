package todolist.model;

public class InputException {
	String commandType = null;
	String errorType = null;
	
	public InputException(String commandType, String errorType) {
		this.commandType = null;
		this.errorType = null;
	}
	
	public String getCommandType() {
		return commandType;
	}
	
	public String getErrorType() {
		return errorType;
	}
	
	public String getErrorMessage() {
		return null;
	}
}
