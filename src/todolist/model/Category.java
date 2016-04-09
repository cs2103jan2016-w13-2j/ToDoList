//@@author A0130620B
package todolist.model;

public class Category implements Comparable<Category>{
    private String category;
	public Category(String category) {
    	this.category = category;
    }
	@Override
	public int compareTo(Category o) {
		return this.category.compareToIgnoreCase(o.getCategory());
	}
	
	public String getCategory() {
		return category;
	}
}
