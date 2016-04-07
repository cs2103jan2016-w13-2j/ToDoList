package todolist.model;

public class InputException {
	String commandType = null;
	String errorType = null;
	Boolean error = true;
	
	public InputException() {
		this.error = true;
	}
	
	public InputException(String commandType, String errorType) {
		this.commandType = null;
		this.errorType = null;
		this.error = false;
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
	
	public Boolean getCorrectness() {
		return this.error;
	}
}
