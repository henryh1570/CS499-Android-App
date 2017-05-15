package hu.henry.sleepkeep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SleepActivity extends AppCompatActivity {

    SleepDataSharedPreferencesManager SDSP = SleepDataSharedPreferencesManager.getSleepDataSharedPreferences();
    boolean previousComplete = false;
    Button previousEntryButton;
    Button todayEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        previousEntryButton = (Button) findViewById(R.id.previousEntry);
        todayEntryButton = (Button) findViewById(R.id.todayEntry);

        // Load everything here and wire them
//        SDSP.saveDay(SleepActivity.this, getDay(-1), "12:55:05", "", "false", new SleepEntry("bowling", "go bowling with kevin", getDay(-1), 4, false, "WORK").toDelimitedString());
        // Check the previous date entry to see if still valid
        String data = SDSP.getString(SleepActivity.this, getDay(-1));
        if (data.equals("") || data.split("\\|")[3].toLowerCase().equals("true")) {
            previousComplete = true;
            previousEntryButton.setEnabled(false);
        }

        // Launch the today entry activity if previous entry is complete.
        // Otherwise send an alert dialog box to confirm discard of previous entry.
        todayEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousComplete == false) {
                    // Code to customize a dialog box.
                    AlertDialog.Builder adb;
                    adb = new AlertDialog.Builder(SleepActivity.this);
                    adb.setTitle("Warning");
                    adb
                            .setMessage("Previous entry is not complete. Do you wish to discard it?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Code to discard previous entry
                                    SDSP.delete(SleepActivity.this, getDay(-1));
                                    previousComplete = true;
                                    previousEntryButton.setEnabled(false);

                                    Toast.makeText(SleepActivity.this, "Previous Entry Discarded",
                                            Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();
                } else {
                    Intent i = SleepTodayActivity.newIntent(SleepActivity.this);
                    startActivity(i);
                }
            }
        });

        previousEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = SleepPreviousActivity.newIntent(SleepActivity.this);
                startActivity(i);
            }
        });
    }

    // Offset -1 for yesterday
    public String getDay(int offset) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(cal.getTime());
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SleepActivity.class);
        return i;
    }
}
