package todolist.model;
/*
 * the following are the possible searchCommand
 * 1)"name"+name
 * 2)"byDate"+date+cat./view
 * 3)"betweenDate"+startDate+endDate+cat./view (inclusive)
 * 4)"viewAndCat"+view+cat.
 * 
 * cat.:existing category or all
 * view:overdue, archive
 * 
 */
public class SearchCommand {
    
    private String type = null;
    private String startDate = null;
    private String endDate = null;
    private String byDate = null;
    private String field = null; //cat. or view
    private String taskName = null;
    
    //constructor for type3
    public SearchCommand(String type_betweenDate, String startDate, String endDate, String field) {
    	this.type = type_betweenDate;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.field = field;
    }
    //constructor for type2,4
    public SearchCommand(String type,String date,String field) {
    	this.type = type;
    	this.byDate = date;
    	this.field = field;
    }
    
    //constructor for type1
    public SearchCommand(String type_name, String taskName) {
        this.type = type_name;
        this.taskName = taskName;
    }
    
    //getters
    public String getSearchCommandType() {
        return type;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public String getEndDate() {
    	return endDate;
    }
 
    public String getByDate() {
    	return byDate;
    }
    
    public String getField() {
    	return field;
    }
    
    public String getTaskName() {
    	return taskName;
    }

}
