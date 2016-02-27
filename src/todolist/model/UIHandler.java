package todolist.model;

public static class UIHandler {
    
    private DataBase dataBase;
    
    private Boolean isSorted = false;
    private Boolean isFiltered = false;
    
    private String fieldName = null;
    private String category = null;
    private String order = null;
    
    public UIHandler(Database dataBase) {
        this.dataBase = database;
    }
    
    public void refresh() {
        if(isSorted&&!isFiltered) {
            this.sort(fieldName, order);
        } 
        
        if(!isSorted&&isFiltered) {
            this.filter(category);
        }
        
        if(isFiltered&&isSorted) {
            ArrayList<Task> tempTaskList = new Task(dataBase.retreive(new SearchCommand("Category", category));
            UI.display(Sorter.sort(tempTaskList));
        }
        
        if(!isSorted&&!isFiltered) {
            UI.display(dataBase.retrieveAll());
        }
    }
    
    public void sendMessage(String message) {
        UI.messageBox(message);
    }
    
    public void highLight(Task task) {
        UI.highLight(task);
    }
    
    public void search(String title) {
        ArrayList<Task> tempTaskList = new Task(dataBase.retreive(new SearchCommand("Name", title));
        UI.highLight(tempTaskList);
    }
    
    public void sort(String fieldName, String order) {
        if(isFiltered) {
            this.sort = sort;
            ArrayList<Task> tempTaskList = new Task(dataBase.retreive(new SearchCommand("Category", category));
            UI.display(Sorter.sort(tempTaskList));
            isSorted = true;
        } else {
            this.sort = sort;
            UI.display(Sorter.sort(dataBase.retrieveAll()));
            isSorted = true;
        }
    }
    
    public void filter(String category) {
        if(isSorted) {
            this.category = category;
            ArrayList<Task> tempTaskList = new Task(dataBase.retreive(new SearchCommand("Category", category));
            UI.display(Sorter.sort(tempTaskList));
            isFiltered = true;
        } else {
            this.category = category;
            ArrayList<Task> tempTaskList = new Task(dataBase.retreive(new SearchCommand("Category", category));
            UI.display(tempTaskList);
            isFiltered = true;
        }
    }
    
    public void exit() {
        System.exit(0);
    }
    
    public String retrieve() {
        String userInput = null;
        return userInput;
    }
    
    public String enter() {
        return false;;
    }
}