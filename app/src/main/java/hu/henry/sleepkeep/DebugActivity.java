package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DebugActivity extends AppCompatActivity {

    SleepDataSharedPreferencesManager SDSP = SleepDataSharedPreferencesManager.getSleepDataSharedPreferences();
    Button addDummyButton;
    Button clearTodayButton;
    Button completeTodayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        addDummyButton = (Button) findViewById(R.id.debugAddButton);
        clearTodayButton = (Button) findViewById(R.id.debugClearTodayButton);
        completeTodayButton = (Button) findViewById(R.id.debugCompleteTodayButton);

        addDummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 7; i++) {
                    SDSP.saveDay(DebugActivity.this, getDay(-1*i), "12:55:05", i*i%10 + "h:53m", "false", new SleepEntry("bowling", "go bowling with kevin", getDay(-1), 4, false, "WORK").toDelimitedString());
                }
                Toast.makeText(DebugActivity.this, "Data added.", Toast.LENGTH_SHORT).show();
            }
        });

        clearTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 7; i++) {
                    SDSP.delete(DebugActivity.this, getDay(0));
                }
                Toast.makeText(DebugActivity.this, "Today cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        completeTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = SDSP.getString(DebugActivity.this, getDay(0));
                String[] dayData = data.split("\\|");
                ArrayList<SleepEntry> list = new ArrayList<SleepEntry>();

                if (!data.equals("") && dayData.length == 5) {
                    // Further delimit the entry strings to get their attributes
                    for (String eStr : dayData[4].split("~")) {
                        String[] att = eStr.split("`");
                        SleepEntry entry = new SleepEntry(att[0], att[1], att[2], Integer.parseInt(att[3]), Boolean.parseBoolean(att[4]), att[5]);
                        list.add(entry);
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String date = sdf.format(new Date());

                long previousTimeValue = new Date((dayData[0] + " " + dayData[1])).getTime();
                long diff = new Date(date).getTime() - previousTimeValue;
                long diffRemainingMins = ((diff / (1000 * 60)) % 60);
                long diffHours = (diff / (1000 * 60 * 60));

                String message = diffHours + "h:" + diffRemainingMins + "m";

                SDSP.saveDay(DebugActivity.this, dayData[0], dayData[1], message, "true", SDSP.combineEntries(list));
                Toast.makeText(DebugActivity.this, "Today's entry completed.", Toast.LENGTH_SHORT).show();
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
