package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DebugActivity extends AppCompatActivity {

    SleepDataSharedPrefs SDSP = SleepDataSharedPrefs.getSleepDataSharedPreferences();
    private Button addDummyButton;
    private Button clearTodayButton;
    private Button completeTodayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        addDummyButton = (Button) findViewById(R.id.debugAddButton);
        clearTodayButton = (Button) findViewById(R.id.debugClearTodayButton);
        completeTodayButton = (Button) findViewById(R.id.debugCompleteTodayButton);

        // Add a week's worth of dummy data
        addDummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String historyJSON = SDSP.getString(DebugActivity.this, "history");
                History history = new History(new ArrayList<String>());
                if (!historyJSON.equals("")) {
                    history = gson.fromJson(historyJSON, History.class);
                }

                String[] activities = {"bowling", "studying", "eat out", "boba shop", "exercise", "movie", "work", "apply for jobs"};
                String[] description = {"", "with kevin at 5", "later", "at night", "no breaks", "maybe", "stuff", "for 30 mins"};
                String[] type = {"WORK", "LEISURE", "OTHER"};

                for (int i = 0; i < 7; i++) {
                    int num = (int) (Math.random() * activities.length);
                    int num2 = (int) (Math.random() * description.length);
                    SleepEntry sleepEntry = new SleepEntry(activities[num], description[num2], getDay(-1 * i), ((num + num2) % 10), false, type[(num + num2) % 3]);
                    ArrayList<SleepEntry> list = new ArrayList<SleepEntry>();
                    list.add(sleepEntry);

                    String day = getDay(-1 * i);
                    DayEntry dayEntry = new DayEntry(day, "12:55:05", (int)(Math.random() * 12) + "h:53m", false, list);
                    SDSP.delete(DebugActivity.this, day);
                    SDSP.saveDay(DebugActivity.this, dayEntry);
                    history.addKey(day);
                }
                SDSP.saveString(DebugActivity.this, "history", history.toJson());

                Toast.makeText(DebugActivity.this, "Data added.", Toast.LENGTH_SHORT).show();
            }
        });

        clearTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = getDay(0);
                Gson gson = new Gson();
                String historyJSON = SDSP.getString(DebugActivity.this, "history");
                History history = new History(new ArrayList<String>());
                if (!historyJSON.equals("")) {
                    history = gson.fromJson(historyJSON, History.class);
                }

                SDSP.delete(DebugActivity.this, day);
                history.removeKey(day);

                SDSP.saveString(DebugActivity.this, "history", history.toJson());
                Toast.makeText(DebugActivity.this, "Today cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        completeTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = getDay(0);
                String data = SDSP.getString(DebugActivity.this, day);
                if (!data.equals("")) {
                    ArrayList<SleepEntry> list = new ArrayList<SleepEntry>();
                    Gson gson = new Gson();
                    DayEntry dayData = gson.fromJson(data, DayEntry.class);

                    if (!data.equals("") && dayData.isFinished() == false) {
                        list = dayData.getList();
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String date = sdf.format(new Date());

                    long previousTimeValue = new Date((day + " " + dayData.getLockInTime())).getTime();
                    long diff = new Date(date).getTime() - previousTimeValue;
                    long diffRemainingMins = ((diff / (1000 * 60)) % 60);
                    long diffHours = (diff / (1000 * 60 * 60));

                    String message = diffHours + "h:" + diffRemainingMins + "m";

                    //Save time and date data here, and list data
                    dayData.setList(list);
                    dayData.setIsFinished(true);
                    dayData.setHoursSlept(message);

                    // History already has this day keyed
                    SDSP.saveDay(DebugActivity.this, dayData);
                    Toast.makeText(DebugActivity.this, "Today's entry completed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DebugActivity.this, "Today's entry does not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, DebugActivity.class);
        return i;
    }

    // Offset -1 for yesterday
    public String getDay(int offset) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(cal.getTime());
    }

}
