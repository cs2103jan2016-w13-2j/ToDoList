package todolist.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import todolist.model.SearchCommand;
import todolist.model.Task;
//@@author yuxin
public class TaskRetriever {

    private static enum FilterType {
        VIEW, CATEGORY, NAME, END_DATE, START_DATE;
    }

    private static enum ViewType {
        ARCHIVE, OVERDUE, TODAY;
    }

    private ArrayList<Task> taskList;

    public TaskRetriever() {
        taskList = new ArrayList<Task>();
    }

    public ArrayList<Task> retrieveHandler(ArrayList<Task> tasks, SearchCommand command) {
        // System.out.println("is retrieving");
        assert (command instanceof SearchCommand);
        // System.out.println("not even here");
        taskList = tasks;

        ArrayList<Task> resultList = new ArrayList<Task>();
        FilterType type = getFilterType(command);
        // dataBase_Logger.log(Level.INFO, LOGGING_RETRIEVE_TASK + type);
        switch (type) {
        case CATEGORY:
            // System.out.println("is retrieving 2222");
            resultList = retrieve_Category(command);
            break;
        case NAME:
            System.out.println("name");
            resultList = retrieve_Name(command);
            break;
        case VIEW:
            resultList = retrieve_View(command);
            break;
        default:
            // System.out.println("is retrieving default");
            return resultList;
        }

        // System.out.println(Arrays.toString(resultList.toArray()));

        return resultList;
    }

    private FilterType getFilterType(SearchCommand command) {
        String type = command.getType();
        if (isCategory(type)) {
            return FilterType.CATEGORY;
        }
        if (isView(type)) {
            return FilterType.VIEW;
        }
        if (isName(type)) {
            return FilterType.NAME;
        }
        return null;
    }

    private boolean isName(String type) {
        return type.equalsIgnoreCase("name");
    }

    private boolean isView(String type) {
        return type.equalsIgnoreCase("view");
    }

    private boolean isCategory(String type) {
        return type.equalsIgnoreCase("category");
    }

    private ArrayList<Task> retrieve_View(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        ViewType viewToFilter = determineViewType(command.getContent());
        switch (viewToFilter) {
        case OVERDUE:
            resultList = retrieve_ViewOverDue();
            break;
        case ARCHIVE:
            resultList = retrieve_ViewArchive();
            break;
        default:
            return resultList;
        }
        return resultList;
    }

    // helper method for retrieve_View
    private ArrayList<Task> retrieve_ViewArchive() {
        ArrayList<Task> resultList = new ArrayList<Task>();
        for (Task eachTask : taskList) {
            if (eachTask.getDoneStatus()) {
                resultList.add(eachTask);
            }
        }
        return resultList;
    }

    // helper method for retrieve_View
    private ArrayList<Task> retrieve_ViewOverDue() {
        ArrayList<Task> resultList = new ArrayList<Task>();
        for (Task eachTask : taskList) {
            if (isTaskOverdue(eachTask.getEndTime())) {
                resultList.add(eachTask);
            }
        }
        return null;
    }

    private boolean isTaskOverdue(LocalDateTime endTime) {
        if (endTime == null) {
            return false;
        }
        return endTime.isBefore(LocalDateTime.now());
    }

    private ViewType determineViewType(String content) {
        if (isOverdue(content)) {
            return ViewType.OVERDUE;
        }
        if (isArchive(content)) {
            return ViewType.ARCHIVE;
        }
        return null;
    }

    private boolean isArchive(String content) {
        return content.equalsIgnoreCase("archive");
    }

    private boolean isOverdue(String content) {
        return content.equalsIgnoreCase("overdue");
    }

    private ArrayList<Task> retrieve_Name(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        String requiredName = command.getContent();
        for (Task eachTask : taskList) {
            if (isSubstring(eachTask.getName().getName(), requiredName)) {
                resultList.add(eachTask);
            }
        }
        System.out.println(Arrays.toString(resultList.toArray()));
        return resultList;
    }

    private boolean isSame(String str1, String str2) {
        return str1.equalsIgnoreCase(str2);
    }

    private boolean isSubstring(String str1, String str2) {
        return str1.equalsIgnoreCase(str2);
    }

    private ArrayList<Task> retrieve_Category(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        String requiredCategory = command.getContent();

        System.out.println(requiredCategory);

        for (Task eachTask : taskList) {
            if (eachTask.getCategory() != null && isSame(eachTask.getCategory().getCategory(), requiredCategory)) {
                System.out.println(eachTask.getName().getName());
                resultList.add(eachTask);
            }
        }
        return resultList;
    }

    /**
     * check whether the required task exist in the local storage file
     * 
     */
    public boolean isTaskExisting(ArrayList<Task> taskList, Task taskToCheck) {
        return taskList.contains(taskToCheck);
    }

}
