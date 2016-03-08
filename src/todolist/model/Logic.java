package todolist.model;

import todolist.MainApp;

public class Logic {
	
    private MainApp mainApp;
    private DataBase dataBase;
    public UIHandler uiHandler;
    private CommandHandler commandHandler;

    public Logic(MainApp mainApp) {
        this.mainApp = mainApp;
        this.dataBase = new DataBase();
        this.uiHandler = new UIHandler(dataBase, mainApp);
        this.commandHandler = new CommandHandler(uiHandler, dataBase);

    }
    
    
    public void process(Command input) {
        commandHandler.execute(input);
    }
}