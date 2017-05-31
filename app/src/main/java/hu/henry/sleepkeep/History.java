package hu.henry.sleepkeep;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

// A class to hold all days entered as keys.
public class History {

    // Keys are days in the format "MM/dd/yyyy"
    private ArrayList<Date> entryKeys = new ArrayList<Date>();

    public History(ArrayList<String> days) {
        for (String day : days) {
            entryKeys.add(new Date(day));
        }
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addKey(String key) {
        if (!entryKeys.contains(new Date(key))) {
            entryKeys.add(new Date(key));
        }
    }

    public void removeKey(String key) {
        entryKeys.remove(new Date(key));
    }

    public ArrayList<Date> getEntryKeys() {
        sortKeys();
        return entryKeys;
    }

    public void sortKeys() {
        Collections.sort(entryKeys, new Comparator<Date>() {
            @Override
            public int compare(Date d1, Date d2) {
                return d1.compareTo(d2);
            }
        });
    }
}
