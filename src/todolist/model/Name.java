//@@author A0130620B
package todolist.model;

public class Name implements Comparable<Name> {

	private String name;

	public Name(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int compareTo(Name o) {
		return this.name.compareToIgnoreCase(o.getName());
	}
}
