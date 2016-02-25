package todolist.model;

public class Reminder {
    
    public Boolean switcher = null;
    public LocalDateTime time = null;
    
    public Reminder(Boolean switcher, LocalDateTime time) {
        this.switcher = switcher;
        this.time = time;
    }
        
    public Boolean getStatus() {
        return switcher;
    }
    
    public LocalDateTime getTime() {
        return time;
    }
}
