package todolist.common;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// @@author Huang Lie Jun

/*
 * UtilityLogger is a common logger to log all ToDoList activities at runtime.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class UtilityLogger {

    // Logger constants
    private static final int MAX_LOG_SIZE = 1048576;
    private static final int NUMBER_OF_LOGS = 1;
    private static final boolean IS_APPEND = false;

    // Log variants
    private static final String ACTION = "ACTION >> ";
    private static final String ERROR = "ERROR >> ";
    private static final String EXCEPTION = "EXCEPTION >> ";
    private static final String COMPONENTCALL = "COMPONENT CALL >> ";

    // Log components
    private static final String UI = "UI";
    private static final String LOGIC = "LOGIC";
    private static final String PARSER = "PARSER";
    private static final String STORAGE = "STORAGE";
    private static final String UNKNOWN = "UNKNOWN";
    private static final String LOGGER_NAME = "ToDoList-Logger";
    private static final String ERROR_CREATE_LOG = "Error accessing log file.";
    private static final String ERROR_GET_DEFAULT_PATH = "Error obtaining default application directory for file path.";

    // Log file handler
    private static File logDirectory = null;
    private Logger logger = null;
    private static FileHandler fileHandler = null;

    // Component types
    public static enum Component {
        UI, Logic, Parser, Storage
    };

    /*** Constructors ***/

    /*
     * Constructor with filepath. This constructor takes in a file path, checks
     * its validity and sets the path as the destination path for the log file.
     * 
     * @param String logDir The directory at which the log file will be read or
     * written to.
     */
    public UtilityLogger(String logDir) {
        // Instantiate a singleton logger if not already created
        logger = Logger.getLogger(LOGGER_NAME);

        try {

            // Check validity of directory and set directory accordingly
            if (isValidPath(logDir)) {
                logDirectory = new File(logDir);
            } else {
                logDirectory = getDefaultPath();
            }

            fileHandler = new FileHandler(logDirectory.getPath(), MAX_LOG_SIZE, NUMBER_OF_LOGS, IS_APPEND);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException exception) {
            handleConstructionError(exception);
        }
    }

    /*
     * Constructor without filepath. This constructor uses the application
     * default root directory as the destination path for the log file.
     */
    public UtilityLogger() {
        // Instantiate a singleton logger if not already created
        logger = Logger.getLogger(LOGGER_NAME);

        // Set default directory as destination path for log file
        logDirectory = getDefaultPath();

        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler(logDirectory.getPath(), MAX_LOG_SIZE, NUMBER_OF_LOGS, IS_APPEND);
            }
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException exception) {
            handleConstructionError(exception);
        }
    }

    /*** Utility Functions ***/

    /*
     * getDefaultPath checks and returns the file path that the application is
     * currently running from.
     * 
     * @return String defaultPath
     */
    private File getDefaultPath() {
        File file = null;
        CodeSource codeSource = null;
        File applicationFile = null;

        try {
            codeSource = UtilityLogger.class.getProtectionDomain().getCodeSource();
            applicationFile = new File(codeSource.getLocation().toURI().getPath());
            file = new File(applicationFile.getParent() + ".log");
        } catch (URISyntaxException exception) {
            handleDefaultPathError(exception);
        }

        System.out.println(applicationFile.getParent());

        return file;
    }

    /*
     * isValidPath checks for validity of the file path in accordance to OS
     * rules.
     * 
     * @param String logDir is the candidate directory for the log file to be
     * read from or written to.
     */
    private boolean isValidPath(String logDir) {
        File file = new File(logDir);
        try {
            file.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /*** Access Functions ***/

    /*
     * getLogDirectory returns the current file path of the log file.
     * 
     * @return String logFilePath
     */
    public static String getLogDirectory() {
        return logDirectory.getPath();
    }

    /*
     * setLogDirectory sets the current file path of the log file.
     * 
     * @param String logDirectory is the candidate file path of the log file.
     */
    public static void setLogDirectory(String logDirectory) {
        UtilityLogger.logDirectory = new File(logDirectory);
    }

    /*
     * getLogger returns the current common utility loggger used in this
     * application.
     * 
     * @return Logger logger
     */
    public Logger getLogger() {
        return logger;
    }

    /*
     * setLogger sets the current common utility logger used in this
     * application.
     * 
     * @param Logger logger is the candidate logger to substitute the current
     * utility logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /*** Error Handling Functions ***/

    /*
     * handleConstructionError handles failure to construct this utility logger
     * by displaying error message and trace.
     * 
     * @param Exception exception is the exception caught
     */
    private void handleConstructionError(Exception exception) {
        System.err.println(ERROR_CREATE_LOG);
        exception.printStackTrace();
    }

    /*
     * handleDefaultPathError handles failure to obtain the file path of the
     * application by displaying error message and trace.
     * 
     * @param URISyntaxException exception is the exception caught
     */
    private void handleDefaultPathError(URISyntaxException exception) {
        System.err.println(ERROR_GET_DEFAULT_PATH);
        exception.printStackTrace();
    }

    /*** Logging API ***/

    /*
     * logAction logs an action performed by a function or by the user.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logAction(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + ACTION + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + ACTION + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + ACTION + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + ACTION + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + ACTION + message);
        }
    }

    /*
     * logError logs an error encountered by a function.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logError(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + ERROR + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + ERROR + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + ERROR + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + ERROR + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + ERROR + message);
        }
    }

    /*
     * logException logs an exception caught by a function.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logException(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + EXCEPTION + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + EXCEPTION + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + EXCEPTION + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + EXCEPTION + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + EXCEPTION + message);
        }
    }

    /*
     * logComponentCall logs an inter-component function call.
     * 
     * @param Component {UI, Logic, Parser, Storage}, String message
     * 
     */
    public void logComponentCall(Component level, String message) {
        switch (level) {
        case UI:
            logInfo(UI + "-" + COMPONENTCALL + message);
            break;
        case Logic:
            logInfo(LOGIC + "-" + COMPONENTCALL + message);
            break;
        case Parser:
            logInfo(PARSER + "-" + COMPONENTCALL + message);
            break;
        case Storage:
            logInfo(STORAGE + "-" + COMPONENTCALL + message);
            break;
        default:
            logInfo(UNKNOWN + "-" + COMPONENTCALL + message);
        }
    }

    /*
     * logInfo logs a message (pre-formatted) into the log file.
     * 
     * @param String message
     * 
     */
    private void logInfo(String message) {
        logger.info(message);
    }

    // public static void main(String[] args) {
    // UtilityLogger logger = new UtilityLogger();
    // logger.logAction(Component.UI, "HELLO WORLD");
    //
    // UtilityLogger logger2 = new UtilityLogger();
    // logger2.logAction(Component.UI, "HELLO WORLD");
    //
    // UtilityLogger logger3 = new UtilityLogger();
    // logger3.logAction(Component.UI, "HELLO WORLD");
    // }
}
