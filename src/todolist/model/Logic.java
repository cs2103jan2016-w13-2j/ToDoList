package todolist.model;

public class Logic {
	
    private MainApp mainApp;
    private DataBase dataBase;
    private UIHandler uiHandler;
    private CommandHandler commandHandler;

    public Logic(MainApp mainApp) {
        this.mainApp = mainApp;
        this.dataBase = new Database("myDataBase.txt");
        this.uiHandler = new UIHandler(dataBase, mainApp);
        this.commandHandler = new CommandHandler(uiHandler, database);

    }
    
    
    public process(String input) {
        commandHandler(input);
    }
}