package todolist.model;

public class DataBase {
	
	public DataBase(String filename) {
		new FileHandler(filename); 
	}
	
	public boolean add(Task task) {
		return false;
		
	}
	
	public boolean delete(Task task) {
		return false;
		
	}
	
	public boolean modify(Task task) {
		
		delete(task);
		add(task);
		
		return false;
		
	}
	
	public boolean checkExistence(Task task) {
		return false;
		
	}
	
	public Task retrieve(SearchCommand command) {
		Task task = null;
		
		return task;
	}
	
}
