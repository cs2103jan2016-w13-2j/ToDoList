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
        assert (command instanceof SearchCommand);

        taskList = tasks;

        ArrayList<Task> resultList = new ArrayList<Task>();
        FilterType type = getFilterType(command);
        
        switch (type) {
        case CATEGORY:
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
            return resultList;
        }

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

    private ArrayList<Task> retrieve_ViewArchive() {
        ArrayList<Task> resultList = new ArrayList<Task>();
        
        for (Task eachTask : taskList) {
        	
            if (eachTask.getDoneStatus()) {
                resultList.add(eachTask);
            }
        }
        
        return resultList;
    }

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
        
        return resultList;
    }
    
    protected ArrayList<Task> smartRetrieve(ArrayList<Task> taskList, String[] keywords) {
    	System.out.println("counterrrrrrrrrpppp: " );
    	ArrayList<Task> resultList = new ArrayList<Task>();
    	
    	if(taskList.isEmpty()) {
    		return resultList;
    	}
    	 		
    	resultList = retrieveByTokenizedName(taskList, keywords);

        return resultList;
    }

    private ArrayList<Task> retrieveByTokenizedName(ArrayList<Task> taskList2, String[] keywords) {
    	System.out.println("counterrrrrrrrr: " );
		ArrayList<Task> resultList = new ArrayList<Task>();
		int[] numMatch = new int[taskList2.size()];
		
		if(keywords.length == 1) {
			resultList = retrieveByInitial(taskList2, keywords[0]);
		}
		
		for (int i = 0; i < taskList2.size(); i++) {
			
        	Task eachTask = taskList2.get(i);        	
        	String eachName = eachTask.getName().getName();  
        	
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
		String[] splitedName = eachName.trim().split(" ");
		
		for(int i = 0; i < keywords.length; i++) {
			for(int j = 0; j < splitedName.length; j++) {
				String eachNameWord = splitedName[j].toLowerCase();
				String eachKeyword = keywords[i].toLowerCase();
				
				if(eachNameWord.equalsIgnoreCase(eachKeyword)) {
					counter++;
					break;
			    }			
			}
		}
		System.out.println("counterrrrrrrrr: " + counter);
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

	private boolean isSameCategory(String cat1, String cat2) {
		boolean isSameCat = false;
		
		if(cat1 == null || cat2 == null) {
			return false;
		}else {
			isSameCat = cat1.equalsIgnoreCase(cat2);					
		}
		
        return isSameCat;
    }


    private ArrayList<Task> retrieve_Category(SearchCommand command) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        String requiredCategory = command.getContent();

        for (Task eachTask : taskList) {
         
            if (eachTask.getCategory() != null && isSameCategory(eachTask.getCategory().getCategory(), requiredCategory)) {
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
