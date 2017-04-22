package hu.henry.sleepkeep;

public class SleepEntry {

    private String title;
    private String description;
    private String date;
    private String type;
    private int importance;
    private boolean isComplete;

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

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getIsCompleteString() {
        if (isComplete) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public String getType() {
        return type;
    }

    public SleepEntry(String title, String description, String date, int importance, String type) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.importance = importance;
        isComplete = false;
        this.type = type;
    }
}
