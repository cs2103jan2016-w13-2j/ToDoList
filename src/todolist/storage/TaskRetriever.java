//@@author A0131334W
package todolist.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import todolist.model.SearchCommand;
import todolist.model.Task;

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
            if (eachTask.getName().getName().equalsIgnoreCase(requiredName)) {
                resultList.add(eachTask);
            }
        }
        System.out.println(Arrays.toString(resultList.toArray()));
        return resultList;
    }
    
    protected ArrayList<Task> smartRetrieve(ArrayList<Task> taskList, String[] keywords) {
    	ArrayList<Task> resultList = new ArrayList<Task>();   
    	if(taskList.isEmpty()) {
    		return resultList;
    	}
    	
    	if(keywords.length == 1) {
    		resultList = retrieveByInitial(taskList, keywords[0]);   		
    	}else {    		
    		resultList = retrieveByTokenizedName(taskList, keywords);
    	}
      
        System.out.println(Arrays.toString(resultList.toArray()));
        return resultList;
    }

    private ArrayList<Task> retrieveByTokenizedName(ArrayList<Task> taskList2, String[] keywords) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		int[] numMatch = new int[taskList.size()];
		for (int i = 0; i < taskList.size(); i++) {
        	Task eachTask = taskList.get(i);
        	String eachName = eachTask.getName().getName();
        	//String[] splitedName = eachName.trim().split(" ");      	            	
        	numMatch[i] = findNumMatch(keywords, eachName);       	
        }
		
		//add those with more than 0 matches into resultlist in descending order
		for(int i = 1; i <= keywords.length; i++) {
			for(int j = 0; j < numMatch.length; j++) {
				if(numMatch[j] == i) {
					resultList.add(0, taskList2.get(j));
				}
			}			
		}		
		return resultList;
	}

	private int findNumMatch(String[] keywords, String eachName) {
		int counter = 0;
		
		for(int i = 0; i < keywords.length; i++) {
			eachName = eachName.toLowerCase();
			String eachKeyword = keywords[i].toLowerCase();
			if(eachName.contains(eachKeyword)) {
				counter++;
			}
		}
		return counter;
	}

	private ArrayList<Task> retrieveByInitial(ArrayList<Task> taskList, String keyword) {
		ArrayList<Task> resultList = new ArrayList<Task>();
		boolean isMatching = false;
		
		for(int i = 0; i < taskList.size(); i++) {
			String eachName = taskList.get(i).getName().getName();
			int nameLength = eachName.trim().split(" ")[0].length();
			int keywordLength = keyword.length();
			if(nameLength < keywordLength)  {
				isMatching = false;
			} else {
				String initialPart = eachName.substring(0, keywordLength);
				isMatching = initialPart.equalsIgnoreCase(keyword);
			}
			if(isMatching) {
				resultList.add(taskList.get(i));
			}
		}
		
		
		return resultList;
	}

	private boolean isSame(String str1, String str2) {
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
