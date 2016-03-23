package todolist.common.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import todolist.model.TokenizedCommand;
import todolist.parser.NormalCommandParser;

public class NormalCommandParserTest {
    NormalCommandParser normalCommandParser = null;

    @Before
    public void initNormalCommandParser() {
        normalCommandParser = new NormalCommandParser();
    }

    @Test
    public void testParse() {

        final String testCase = " add event CS2103T-Tutorial 2016-03-23 13:00 1 hour";
        final String expectedAction = "add";
        final int expectedArgsSize = 7;
        String[] expectedArgs = new String[expectedArgsSize];
        expectedArgs[0] = "add";
        expectedArgs[1] = "event";
        expectedArgs[2] = "CS2103T-Tutorial";
        expectedArgs[3] = "2016-03-23";
        expectedArgs[4] = "13:00";
        expectedArgs[5] = "1";
        expectedArgs[6] = "hour";

        TokenizedCommand output = normalCommandParser.parse(testCase);

//        assertEquals(expectedAction, output.getAction());
        assertEquals(expectedArgsSize, output.getArgs().length);

        for (int i = 0; i < output.getArgs().length; ++i) {
            String arg = output.getArgs()[i];
            assertEquals(expectedArgs[i], arg);
        }

    }
}
