package todolist.model;

import java.time.LocalDate;

/*
 * the following are the possible searchCommand
 * 1)"name"+name
 * 2)"byDate"+date+cate
 * 3)"byDate"+date+view
 * 4)"betweenDate"+startDate+endDate+cate
 * 5)"betweenDate"+startDate+endDate+view (inclusive; for tasks in one specific day, startDate=endDate)
 * 6)"viewAndCate"+view+cat.
 * 
 * cat.:existing category or all
 * view:overdue, archive
 * 
 */
public class SearchCommand {
    
    private String type = null;
    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private LocalDate byDate = null;
    private String taskName = null;
    private Category cate = null;
    private String view = null;
    
  //constructor for type1: "name"+name
    public SearchCommand(String type_name, String taskName) {
        this.type = type_name;
        this.taskName = taskName;
    }
    
  //constructor for type2: "byDate"+date+cate
    public SearchCommand(String type_byDate,LocalDate date,Category cate) {
    	this.type = type_byDate;
    	this.byDate = date;
    	this.cate = cate;
    }
  //constructor for type3: "byDate"+date+view
    public SearchCommand(String type_byDate,LocalDate date,String view) {
    	this.type = type_byDate;
    	this.byDate = date;
    	this.view = view;
    }
    
    //constructor for type4: "betweenDate"+startDate+endDate+cate
    public SearchCommand(String type_betweenDate, LocalDate startDate, LocalDate endDate, Category cate) {
    	this.type = type_betweenDate;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.cate = cate;
    }

    //constructor for type5: "betweenDate"+startDate+endDate+view
    public SearchCommand(String type_betweenDate, LocalDate startDate, LocalDate endDate, String view) {
    	this.type = type_betweenDate;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.view = view;
    }
    
    //constructor for type6: "viewAndCate"+view+cate
    public SearchCommand(String type_viewAndCate, String view, Category cate) {
    	this.type = type_viewAndCate;
    	this.view = view;
    	this.cate = cate;
    }
    
    
    //getters
    public String getSearchCommandType() {
        return type;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getEndDate() {
    	return endDate;
    }
 
    public LocalDate getByDate() {
    	return byDate;
    }
    
    public Category getCate() {
    	return cate;
    }
    
    public String getView() {
    	return view;
    }
    
    public String getTaskName() {
    	return taskName;
    }

}
