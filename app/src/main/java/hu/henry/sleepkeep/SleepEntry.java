package hu.henry.sleepkeep;

public class SleepEntry {

    private String title;
    private String description;
    private String date;
    private String type;
    private int importance;
    private boolean isComplete;

    public SleepEntry(String title, String description, String date, int importance, boolean complete, String type) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.importance = importance;
        this.isComplete = complete;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getImportance() {
        return importance;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getIsCompleteString() {
        if (isComplete) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getType() {
        return type;
    }
}
