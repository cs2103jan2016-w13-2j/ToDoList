package todolist.model;

public class Logic {

    public Logic() {
        private DataBase dataBase = new Database("myDataBase.txt");
        private UIHandler uiHandler = new UIHandler(dataBase);
        private CommandHandler commandHandler = new CommandHandler(uiHandler, database);
    }
    
    public void run() {
        while(true) {
            if(uiHandler.enter()) {
                commandHandler(uiHandler.retrieve());
            }
        }
    }
}