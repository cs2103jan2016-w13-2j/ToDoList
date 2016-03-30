# yuxin
###### src/todolist/storage/FileHandler.java
``` java
/*
 * This class is to read and write from the file directly. It will be called by the database.
 * 
 * 
 * @author Xyx
 *
 */
public class FileHandler {
	private static String PATH_UPDATEDDIRECTORY = "updatedDirectory.txt";
	private String fileName = "taskStorage.txt";
    private String path = "";
    
    private Gson gson = new Gson();
    
	public FileHandler() {
		checkForUpdatedDirectory();
	}
    
	/**
	 * This method reads from the local file. It returns a ArrayList containing all the Strings in the file.
	 * @return taskList    the list of tasks stored in file
	 *                      if no such file or the file is empty, return an empty arraylist.
	 */
	public ArrayList<Task> read() {
		ArrayList<Task> taskList = new ArrayList<Task>();
		File filePath = new File(path + fileName);
		if(isFileReady(filePath) && !isFileEmpty(filePath)) {
			try {
				
				FileReader fr=new FileReader(path + fileName);
				BufferedReader br=new BufferedReader(fr);
				String input=null;
				input=br.readLine();
				System.out.println(input);
				
				while(input != null && !input.equals("null")){
					taskList.add(gson.fromJson(input, Task.class));
					input=br.readLine();
				}
				br.close();
			}catch (FileNotFoundException e) {
				return null;
			}catch (IOException e) {
				return null;
			}
		}	
		return taskList;		
	}
    
	/**
	 * This method write directly the list of tasks to the local file.
	 * 
	 * @param taskList    the list of tasks to be written
	 * @return            true if it is successfully written; false if the target file cannot be found
	 */
	public boolean write(ArrayList<Task> taskList) {
		    try{
				FileWriter fw=new FileWriter(path + fileName);
				BufferedWriter bw=new BufferedWriter(fw);
				for(Task eachTask: taskList) {	
					System.out.println("writing into file: " +  gson.toJson(eachTask));
					bw.write(gson.toJson(eachTask) + "\n");
				}
				bw.close();
			}catch (Exception e) {
				return false;
			}			
		return true;	
	}
	
	/**
	 * The method is to set the path to store the storage file.
	 * 
	 * @param path  the path that is going to store the file.
	 * @return      true if the path exists and is able to store the file
	 */
	/*
	public boolean setPath(String path) {
		if(path.equals("")) {
			return true;
		}else if(isPathCorrect(path+"/")) {
			this.path=path+"/";
		}		
		return true;
	}
	*/
	/**
	 * The method is to set a new direction to store the tasks in the defined path.
	 * 
	 * @param newFilePath   the path of the new file (the path + the file name); it is not null.
	 * @return              true if the file is set; false if the path is not a correct path  
	 */
	public boolean setFile(String newFilePath) {
		ArrayList<Task> existingTaskList = this.read();
		//set path 
		String newPath = getPathOfNewFile(newFilePath.trim());
		String newFileName = getNewFileName(newFilePath.trim());
		
		if(isPathCorrect(newFilePath)) {
			this.path = newPath;
			this.fileName=newFileName + ".txt";
			if(existingTaskList!=null) {
				this.write(existingTaskList);			
			}				
			try{
				FileWriter fw=new FileWriter(path + fileName);
				BufferedWriter bw=new BufferedWriter(fw);
				bw.write(path + "\n");
				bw.write(fileName + "\n");
				bw.close();
			}catch (Exception e) {
				return false;
			}
		    return true;
		}
		
	    return false;
		  	
		
	}
	
	public String getPath() {
		File pathOfFile = new File(path);
		return pathOfFile.getAbsolutePath();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	//helper methods
	private void checkForUpdatedDirectory() {
		File updatedDirectory = new File(PATH_UPDATEDDIRECTORY);	
		
		try {
			if(!isFileEmpty(updatedDirectory)) {
			    FileReader fr = new FileReader(updatedDirectory);
			    BufferedReader br = new BufferedReader(fr);		
			    path = br.readLine();
			    fileName = br.readLine();
			    br.close();
			}
		}catch (Exception e) {
			return;
		}
	}
	
	private boolean isFileReady(File filePath) {
		return filePath.exists();
	}
	
	private boolean isFileEmpty(File filePath) {
		if(isFileReady(filePath)) {
			return filePath.length() == 0;
		}
		return true;
	}
	
	private boolean isPathCorrect(String pathName) {
		if(pathName.equals("")) {
			return true;
		}
		File pathToCheck = new File(pathName);
		if(pathToCheck.isDirectory()) {
			return true;
		}
		return false;
	}
	
	private String getPathOfNewFile(String newFilePath) {
		String[] splitedPath = getSplitedPath(newFilePath);
		String newPath = "";
		
		if(splitedPath.length == 1) {
			return newPath;
		}
		
		for(int i = 0; i < splitedPath.length-1; i++) {
			newPath = newPath + splitedPath[i] + "/";
		}
		return newPath.substring(0, newPath.length()-1);
	}
	
	private String getNewFileName(String newFilePath) {
		String[] splitedPath = getSplitedPath(newFilePath);
		if(splitedPath.length == 1) {
			return newFilePath;
		}
		return splitedPath[splitedPath.length-1];
	}
	
	private String[] getSplitedPath(String newFilePath) {
		return newFilePath.split("/");
	}
}
```
###### src/todolist/common/tests/DataBaseTest.java
``` java
public class DataBaseTest {

	/**
	 * @throws java.lang.Exception
	 */

	private DataBase db;

	@Before
	public void setUp() throws Exception {
		db = new DataBase();
	}

	@Test
	/**
	 * test add an event to the database
	 * 
	 */
	public void testAdd1() {
		db.clear();
		// create a event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);

		// test whether can add an event to database
		boolean expected = true;
		assertEquals(db.add(newEvent), expected);

		// test whether it is really written into the file
		db.loadFromFile();
		boolean isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(isEqual, expected);
	}
    
	/**
	 * test delete an (existing) event from database
	 */
	@Test
	public void testDelete1() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// delete the task
		boolean expected = true;
		assertEquals(db.delete(newEvent), expected);

		// check whether it is really deleted from the file
		db.loadFromFile();
		assertEquals(db.taskList.isEmpty(), expected);
	}
	
	/**
	 * test delete an (not existing) event from database
	 */
	@Test
	public void testDelete2() {
		db.clear();
		//add an event to the database
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);
		
		//delete an event not in the database
		name = new Name("title2");
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		newEvent = new Task(name, start, end, null, null, false, false, null);
		
		boolean expected = false;
		assertEquals(db.delete(newEvent), expected);
	}
    /**
     * test check existence for an event (existing) in the database
     */
	@Test
	public void testCheckExistence1() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// check the existence
		boolean expected = true;
		assertEquals(db.checkExistence(newEvent), expected);
	}
	/**
     * test check existence for an event (not existing) in the database
     */
	@Test
	public void testCheckExistence2() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);
		
		//check the existence for another event
		name = new Name("title2");
        newEvent = new Task(name, start, end, null, null, false, false, null);
        boolean expected = false;
		assertEquals(db.checkExistence(newEvent), expected);
	}
    
	/**
	 * test retrieve by (existing) name
	 */
	@Test
	public void testRetrieve1() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the name of the (existing) task
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("name", "title"));
		boolean expected = true;
		//check the size of the resultant list
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
		// check the element is the task we add in
		isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);
	}
	
	/**
	 * test retrieve by (not existing) name
	 */
	@Test
	public void testRetrieve2() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the name of the ( not existing) task
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("name", "different-name"));
		boolean expected = false;
		//check the size of the resultant list
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
	}
	
	/**
	 * test retrieve by (existing) category
	 */
	@Test
	public void testRetrieve3() {
		db.clear();
		// add one event with category
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Category cat = new Category("cat1");
		Task newEvent = new Task(name, start, end, cat, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the specific (existing) category
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("category", "cat1"));
		boolean expected = true;
		//check the size of the resultant list
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
		//check the task in the resultant list 
		isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);
	}
	
	/**
	 * test retrieve by (not existing) category
	 */
	@Test
	public void testRetrieve4() {
		db.clear();
		// add one event with category
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Category cat = new Category("cat1");
		Task newEvent = new Task(name, start, end, cat, null, false, false, null);
		db.add(newEvent);

		// retrieve the task for the specific (existing) category
		ArrayList<Task> taskList = db.retrieve(new SearchCommand("category", "not_existing_cat"));
		boolean expected = false;
		//check the size of the resultant list
		boolean isEqual = taskList.size() >= 1;
		assertEquals(expected, isEqual);
	}
    
	/**
	 * test retrieveAll function
	 */
	@Test
	public void testRetrieveAll() {
		db.clear();
		// add one event
		Name name = new Name("title");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse("2017-01-01" + " " + "14:00", formatter);
		LocalDateTime end = start.plus(Long.parseLong("1"), ChronoUnit.DAYS);
		Task newEvent = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent);

		// check the arraylist only has one element
		ArrayList<Task> taskList = db.retrieveAll();
		boolean expected = true;
		boolean isEqual = taskList.size() == 1;
		assertEquals(expected, isEqual);
		// check the element is the task we add in
		isEqual = db.taskList.get(0).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);

		// add one more event
		name = new Name("title2");
		Task newEvent2 = new Task(name, start, end, null, null, false, false, null);
		db.add(newEvent2);

		// check the size the the taskList
		taskList = db.retrieveAll();
		isEqual = taskList.size() == 2;
		assertEquals(expected, isEqual);
		// check the two tasks really are the two tasks
		isEqual = db.taskList.get(1).getName().getName().equals(newEvent.getName().getName());
		assertEquals(expected, isEqual);

		isEqual = db.taskList.get(0).getName().getName().equals(newEvent2.getName().getName());
		assertEquals(expected, isEqual);
	}

}
```
