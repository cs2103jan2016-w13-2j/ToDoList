package todolist.model;

public class NormalCommandHandler {

    public NormalCommandHandler() {

    }
    
    public TokenizedCommand parse(String input) {
    	String temp[] = input.split(" ");
    	String action = temp[0];
        String args[] = new String[temp.length - 1];
        for(int i=0; i<temp.length-1; i++) {
            args[i] = temp[i + 1];
        }
        return new TokenizedCommand(action, args);
    }
}
