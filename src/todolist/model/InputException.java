//@@author A0130620B
package todolist.model;

import todolist.logic.ErrorBase;

public class InputException {
	String commandType = null;
	String errorType = null;
	Boolean error = true;
	
	public InputException() {
		this.error = true;
	}
	
	public InputException(String commandType, String errorType) {
		this.commandType = commandType;
		this.errorType = errorType;
		this.error = false;
	}
	
	public String getCommandType() {
		return commandType;
	}
	
	public String getErrorType() {
		return errorType;
	}
	
	public String getErrorMessage() {
		return new ErrorBase().getErrorMessage(commandType, errorType);
	}
	
	public Boolean getCorrectness() {
		return this.error;
	}
}
