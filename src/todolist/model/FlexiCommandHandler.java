package todolist.model;

public class FlexiCommandHandler {
    
    private DataBase dataBase;
    private UIHandler uiHandler;
    private NormalCommandHandler normalHandler;
    
    public FlexiCommandHandler(UIHandler uiHandler, DataBase dataBase, NormalCommandHandler normalHandler) {
        this.setDataBase(dataBase);
        this.setUiHandler(uiHandler);
        this.normalHandler = normalHandler;
    }
    
    public void execute(FlexiCommand flexiCommand) {
        normalHandler.execute(Parser.translate(flexiCommand));
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public UIHandler getUiHandler() {
        return uiHandler;
    }

    public void setUiHandler(UIHandler uiHandler) {
        this.uiHandler = uiHandler;
    }
}