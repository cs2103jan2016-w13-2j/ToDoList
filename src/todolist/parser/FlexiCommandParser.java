package todolist.parser;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import todolist.model.TokenizedCommand;

//@@author zhangjiyi
public class FlexiCommandParser {

	public FlexiCommandParser() {

	}

	public TokenizedCommand parse(String input) {

		String temp[] = input.split(" ");
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equals("add") || temp[i].equals("edit") || temp[i].equals("delete") || temp[i].equals("search")
					|| temp[i].equals("filter") || temp[i].equals("sort") || temp[i].equals("insert")
					|| temp[i].equals("switchposition") || temp[i].equals("label") || temp[i].equals("postpone")
					|| temp[i].equals("forward") || temp[i].equals("add-remind") || temp[i].equals("remind")
					|| temp[i].equals("add-remind-bef") || temp[i].equals("remind-bef") || temp[i].equals("done")
					|| temp[i].equals("undone") || temp[i].equals("exit") || temp[i].equals("undo") || temp[i].equals("redo")
					|| temp[i].equals("reset") || temp[i].equals("tab") || temp[i].equals("set-recurring")
					|| temp[i].equals("remove-recurring") || temp[i].equals("create") || temp[i].equals("schedule")
					|| temp[i].equals("cancel") || temp[i].equals("remove") || temp[i].equals("modify") || temp[i].equals("change")
					|| temp[i].equals("replace") || temp[i].equals("archive") || temp[i].equals("complete") || temp[i].equals("finish")
					|| temp[i].equals("shelf") || temp[i].equals("unarchive") || temp[i].equals("incomplete")
					|| temp[i].equals("unfinish") || temp[i].equals("unshelf") || temp[i].equals("delay") || temp[i].equals("advance")
					|| temp[i].equals("categorize") || temp[i].equals("tag") || temp[i].equals("load")) {

				return new TokenizedCommand("invalid", temp);
			}
		}
		
		
		Parser parser = new Parser(TimeZone.getTimeZone(ZoneOffset.systemDefault()));
		List<DateGroup> groups = parser.parse(input);
		List<Date> dates = null;
		int[] column = new int[] {0, 0};
		int[] length = new int[] {0, 0};
		int counter = 0;
		for (DateGroup group : groups) {
			dates = group.getDates();
			// int line = group.getLine();
			// int column = group.getPosition();
			// String matchingValue = group.getText();
			// String syntaxTree = group.getSyntaxTree().toStringTree();
			// Map parseMap = group.getParseLocations();
			// boolean isRecurreing = group.isRecurring();
			// Date recursUntil = group.getRecursUntil();
			column[counter] = group.getPosition();
			length[counter] = group.getText().length();
			counter++;
		}
		
		System.out.println(dates);
		System.out.println(column[0] + " " + length[0]);
		System.out.println(column[1] + " " + length[1]);
		
		String result = null;
		
		
		if(dates == null || dates.size() == 0) {
			return new TokenizedCommand("add", new String[]{"task", input});
		} else {
			if(column[1] == 0) {
				System.out.println(input.substring(0, column[0] - 1));
				System.out.println(input.substring(column[0] + length[0] -1));
			
				
				result = input.substring(0, column[0] - 1) + input.substring(column[0] + length[0] -1);
			} else {
				result = input.substring(0, column[0] - 1) + input.substring(column[0] + length[0], column[1] - 1) + input.substring(column[1] + length[1] - 1);
			}
			
			if(dates.size() == 1) {
				Date deadline = dates.get(0);
				
				
				Instant instant = Instant.ofEpochMilli(deadline.getTime());
			    LocalDateTime end = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
			    
			    DecimalFormat decimalFormatter = new DecimalFormat("00");
			    String deadlineDate = end.getYear() + "-"
						+ decimalFormatter.format(end.getMonthValue()) + "-"
						+ decimalFormatter.format(end.getDayOfMonth());
			    
			    String deadlineTime = decimalFormatter.format(end.getHour()) + ":" + decimalFormatter.format(end.getMinute());
			    /*
				String endDate = deadline.getYear() + "-" + deadline.getMonth() + "-" + deadline.getDay();
				String endTime = deadline.getHours() + ":" + deadline.getMinutes();
				return new TokenizedCommand("add", new String[]{"deadline", input, endDate, endTime});
				*/
			    
				return new TokenizedCommand("add", new String[]{"deadline", result, deadlineDate, deadlineTime});

			} else {
			    DecimalFormat decimalFormatter = new DecimalFormat("00");

			    
				Date startTimeOriginal = dates.get(0);
				Date endTimeOriginal = dates.get(1);
				
				Instant startInstant = Instant.ofEpochMilli(startTimeOriginal.getTime());
			    LocalDateTime start = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
			    
			    //Instant endInstant = Instant.ofEpochMilli(endTimeOriginal.getTime());
			    //LocalDateTime end = LocalDateTime.ofInstant(endInstant, ZoneId.systemDefault());
			    
			    String startDate = start.getYear() + "-"
						+ decimalFormatter.format(start.getMonthValue()) + "-"
						+ decimalFormatter.format(start.getDayOfMonth());
			    
			    String startTime = decimalFormatter.format(start.getHour()) + ":" + decimalFormatter.format(start.getMinute());
			    
			    int interval = (int) getDateDiff(startTimeOriginal, endTimeOriginal)/1000/60;
			    
				/*
				String startDate = start.getYear() + "-" + start.getMonth() + "-" + start.getDay();
				String startTime = start.getHours() + ":" + start.getMinutes();
				
				String endDate = end.getYear() + "-" + end.getMonth() + "-" + end.getDay();
				String endTime = end.getHours() + ":" + end.getMinutes();
				*/
			    
				//return new TokenizedCommand("add", new String[]{"event", input, startDate, startTime, endDate, endTime});

				return new TokenizedCommand("add", new String[]{"event", result, startDate, startTime, Integer.toString(interval), "minute"});
			}
		}
	}

	private static long getDateDiff(Date date1, Date date2) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return diffInMillies;
	}
}