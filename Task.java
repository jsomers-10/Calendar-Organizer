package util;

public class Task extends CalendarItem{
    private int priority;
    private boolean completed;

    public Task(String title, DateTime start, DateTime end, String owner, String location,
                       int priority, boolean completed) {
        super(title, start, end, owner, location);
        if (priority >= 1 && priority <= 5)
            this.priority = priority;
        this.completed = completed;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public String getType() {
        return "Task";
    }
    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("TASK, %s, %s, %s, %s, %s, %d, %b",
                title, start.toString(), end.toString(), owner, location, priority, completed);
    }


}
