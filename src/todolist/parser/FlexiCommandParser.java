package todolist.parser;

import java.util.Date;
import java.util.List;
//import java.util.Map;
import java.util.TimeZone;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import todolist.model.TokenizedCommand;

//@@author zhangjiyi
public class FlexiCommandParser {

    public FlexiCommandParser() {

    }

    public TokenizedCommand parse(String input) {

        Parser parser = new Parser(TimeZone.getDefault());
        List<DateGroup> groups = parser.parse(input);
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
//            int line = group.getLine();
//            int column = group.getPosition();
//            String matchingValue = group.getText();
//            String syntaxTree = group.getSyntaxTree().toStringTree();
//            Map parseMap = group.getParseLocations();
//            boolean isRecurreing = group.isRecurring();
//            Date recursUntil = group.getRecursUntil();
            System.out.println(dates);

        }

        String action = null;
        String args[] = null;
        return new TokenizedCommand(action, args);
    }
}