package hu.henry.sleepkeep;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SleepDataSharedPreferences {

    public static final String MyPREFERENCES = "SleepPreferences";
    public Context context;

    public SleepDataSharedPreferences(Context context) {
        this.context = context;
    }

    public void saveString(String key, String value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "");
    }

    public void saveInt(String key, int value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(key, 0);
    }

    // Combine entry strings with '~' delimiter
    public String combineEntries(ArrayList<SleepEntry> entries) {
        String str = "";
        for (SleepEntry entry : entries) {
            str += (entry.toDelimitedString() + "~");
        }
        return str.substring(0, str.length() - 1);
    }

    public void saveDay(String date, String lockInTime, String hoursSlept, String finished,
                        String entries) {
        String str = date + "|" + lockInTime + "|" + hoursSlept + "|" + finished + "|"
                + entries;
        saveString(date, str);
    }

    public void deleteAllData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
