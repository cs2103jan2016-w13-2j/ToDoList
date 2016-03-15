package todolist.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import todolist.MainApp;

public class Logic {
	
    private MainApp mainApp;
    private DataBase dataBase;
    public UIHandler uiHandler;
    private CommandHandler commandHandler;
    
    private static Logger logic_Logger = Logger.getLogger("Logic logger");

    public Logic(MainApp mainApp) {
        this.mainApp = mainApp;
        this.dataBase = new DataBase();
        this.uiHandler = new UIHandler(dataBase, mainApp);
        this.commandHandler = new CommandHandler(uiHandler, dataBase, this);

    }
    
    
    public void process(Command input) throws Exception {
        commandHandler.execute(input);
    }
    
    public void writeLog(String log) {
    	logic_Logger.log(Level.INFO, log);
    }
}