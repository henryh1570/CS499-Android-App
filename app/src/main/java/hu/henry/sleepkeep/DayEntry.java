package hu.henry.sleepkeep;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DayEntry {
    private String date;
    private String lockInTime;
    private String hoursSlept;
    private boolean isFinished;
    private ArrayList<SleepEntry> list;

    public DayEntry(String date, String lockInTime, String hoursSlept, boolean isFinished, ArrayList<SleepEntry> list) {
        this.date = date;
        this.lockInTime = lockInTime;
        this.hoursSlept = hoursSlept;
        this.isFinished = isFinished;
        this.list = list;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getDate() {
        return date;
    }

    public String getLockInTime() {
        return lockInTime;
    }

    public String getHoursSlept() {
        return hoursSlept;
    }

    public void setLockInTime(String str) {
        lockInTime = str;
    }

    public ArrayList<SleepEntry> getList() {
        return list;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setList(ArrayList<SleepEntry> list) {
        this.list = list;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsFinished(boolean b) {
        isFinished = b;
    }

    public void setHoursSlept(String i) {
        hoursSlept = i;
    }
}
