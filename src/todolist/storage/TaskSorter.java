package todolist.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import todolist.model.Task;
//@@author yuxin

public class TaskSorter {
	
	private ArrayList<Task> taskList;
	
    public TaskSorter() {
    	taskList = new ArrayList<Task>();
    }
    
    
	public ArrayList<Task> sortHandler(ArrayList<Task> tasks, String fieldName, String order) {
		
        taskList = tasks;
        
		switch (fieldName) {
		case "start":
			sort_StartDate(taskList);
			break;
		case "end":
			sort_EndDate(taskList);
			break;
		case "category":
			sort_Category(taskList);
			break;
		case "name":
			sort_Name(taskList);
			break;
		default:
			
		}
		
		if(order.equals("descending")) {
			Collections.reverse(taskList);
		}
		
		return taskList;
	}
    // sorting
    // 1.startDate
    private ArrayList<Task> sort_StartDate(ArrayList<Task> currentList) {
        Collections.sort(currentList, new StartDateComparator());
        return currentList;
    }

    private class StartDateComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            LocalDateTime firstDate = ((todolist.model.Task) t1).getStartTime();
            LocalDateTime secondDate = ((todolist.model.Task) t2).getStartTime();
            if (firstDate == null && secondDate == null) {
                String t1_Name = ((todolist.model.Task) t1).getName().getName();
                String t2_Name = ((todolist.model.Task) t2).getName().getName();
                return t1_Name.compareToIgnoreCase(t2_Name);
            } else if (firstDate == null) {
                return -1;
            } else if (secondDate == null) {
                return 1;
            } else {
                return firstDate.compareTo(secondDate);
            }
        }
    }

    // 2.endDate
    private ArrayList<Task> sort_EndDate(ArrayList<Task> currentList) {
        Collections.sort(currentList, new EndDateComparator());
        return currentList;
    }

    private class EndDateComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            LocalDateTime firstDate = ((todolist.model.Task) t1).getEndTime();
            LocalDateTime secondDate = ((todolist.model.Task) t2).getEndTime();
            if (firstDate == null && secondDate == null) {
                String t1_Name = ((todolist.model.Task) t1).getName().getName();
                String t2_Name = ((todolist.model.Task) t2).getName().getName();
                return t1_Name.compareToIgnoreCase(t2_Name);
            } else if (firstDate == null) {
                return -1;
            } else if (secondDate == null) {
                return 1;
            } else {
                return firstDate.compareTo(secondDate);
            }
        }
    }

    // 3.category
    private ArrayList<Task> sort_Category(ArrayList<Task> currentList) {
        Collections.sort(currentList, new CategoryComparator());
        return currentList;
    }

    private class CategoryComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            String firstCategory = ((todolist.model.Task) t1).getCategory().getCategory();
            String secondCategory = ((todolist.model.Task) t2).getCategory().getCategory();
            return firstCategory.compareToIgnoreCase(secondCategory);
        }
    }

    // 4.name
    private ArrayList<Task> sort_Name(ArrayList<Task> currentList) {
        Collections.sort(currentList, new NameComparator());
        return currentList;
    }

    private class NameComparator implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            String firstName = ((todolist.model.Task) t1).getName().getName();
            String secondName = ((todolist.model.Task) t2).getName().getName();
            return firstName.compareToIgnoreCase(secondName);
        }
    }
}
