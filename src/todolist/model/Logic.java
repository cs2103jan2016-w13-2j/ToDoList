package todolist.model;

import todolist.MainApp;

public class Logic {
	
    private MainApp mainApp;
    private DataBase dataBase;
    private UIHandler uiHandler;
    private CommandHandler commandHandler;

    public Logic(MainApp mainApp) {
        this.mainApp = mainApp;
        this.dataBase = new DataBase("myDataBase.txt");
        this.uiHandler = new UIHandler(dataBase, mainApp);
        this.commandHandler = new CommandHandler(uiHandler, dataBase);
    }
    
    
    public process(String input) {
        commandHandler.execute(input);
    }
}

