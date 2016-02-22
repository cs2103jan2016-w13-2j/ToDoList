package todolist.model;

public class Name implements Comparable<Name>{

	String name;
	
	public Name(String name) {
	    this.name = name;
	}
	
	@Override
	public int compareTo(Name o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public getName() {
	    return name;
	}

}
