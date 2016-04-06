package todolist.model;

import java.time.LocalDateTime;

//@@author A0130620B
public class Reminder {
    
    private Boolean switcher = null;
    private LocalDateTime time = null;
    
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
