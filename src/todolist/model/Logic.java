package todolist.model;

public class Logic {
	
	private DataBase database;
    private UIHandler uiHandler;
    private CommandHandler commandHandler;

    public Logic() {
        database = new DataBase();
        uiHandler = new UIHandler(database);
        commandHandler = new CommandHandler(uiHandler, database);
    }
    
    public void run() {
        while(true) {
            if(uiHandler.enter()) {
                commandHandler(uiHandler.retrieve());
            }
        }
    }
}