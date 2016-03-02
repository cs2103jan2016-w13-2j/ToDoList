package todolist.model;

public class Name implements Comparable<Name>{

	private String name;
	
	public Name(String name) {
	    this.name = name;
	}
	
	@Override
	public int compareTo(Name o) {
		if (this.getName().equals(o.getName()) {
		    return 0;
		} else {
		    this.getName.compareTo(o.getName() < 0) {
		        return -1;
		    } else {
		        return 1;
		    }
		}
	}
	
	public String getName() {
	    return name;
	}
}
