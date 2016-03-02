package todolist.model;

public class Category implements Comparable<Category>{
    private String category;
	public Category(String category) {
    	this.category = category;
    }
	@Override
	public int compareTo(Name o) {
		if (this.getCategory().equals(o.getCategory()) {
		    return 0;
		} else {
		    this.getCategory().compareTo(o.getCategory() < 0) {
		        return -1;
		    } else {
		        return 1;
		    }
		}
	}
	
	public String getCategory() {
		return category;
	}
}
