package todolist.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import todolist.MainApp;

public class Logic {
	
    private MainApp mainApp;
    private DataBase dataBase;
    public UIHandler uiHandler;
    private CommandHandler commandHandler;
    
    public Logger logger = Logger.getLogger("Logic Logger");

    public Logic(MainApp mainApp) {
        this.mainApp = mainApp;
        this.dataBase = new DataBase();
        this.uiHandler = new UIHandler(dataBase, mainApp);
        this.commandHandler = new CommandHandler(uiHandler, dataBase, this);

    }
    
    
    public void process(Command input) throws Exception {
        commandHandler.execute(input);
    }
}