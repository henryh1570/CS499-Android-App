package hu.henry.sleepkeep;

import android.content.Context;
import android.content.SharedPreferences;

public class SleepDataSharedPrefs {

    private static SleepDataSharedPrefs INSTANCE;
    private static final String MyPREFERENCES = "SleepPreferences";

    private SleepDataSharedPrefs() {
    }

    public static SleepDataSharedPrefs getSleepDataSharedPreferences() {
        if (INSTANCE == null) {
            INSTANCE = new SleepDataSharedPrefs();
        }
        return INSTANCE;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveString(Context context, String key, String value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public void saveInt(Context context, String key, int value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(Context context, String key) {
        return getSharedPreferences(context).getInt(key, -1);
    }

    public void saveDay(Context context, DayEntry dayEntry) {
        saveString(context, dayEntry.getDate(), dayEntry.toJson());
    }

    public void deleteAllData(Context context) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    public void delete(Context context, String key) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }
}
