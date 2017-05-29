package hu.henry.sleepkeep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SleepPreviousActivity extends AppCompatActivity {

    Button doneButton;
    TextView timeSleptText;
    TextView timePassedText;
    long diff;
    long previousTimeValue;
    CountDownTimer clock;
    CheckBox checkBox;
    String data;
    DayEntry dayData;
    String message;
    String previousTimeString;
    Spinner manualMinSpinner;
    Spinner manualHourSpinner;
    ListView previousEntriesListView;
    private SleepPreviousEntryAdapter adapter;
    private ArrayList<SleepEntry> list = new ArrayList<SleepEntry>();
    SleepDataSharedPreferencesManager SDSP = SleepDataSharedPreferencesManager.getSleepDataSharedPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_previous);

        // Wiring views/spinners, setting default values
        doneButton = (Button) findViewById(R.id.done);
        timePassedText = (TextView) findViewById(R.id.timePassed);
        timeSleptText = (TextView) findViewById(R.id.timeSlept);
        checkBox = (CheckBox) findViewById(R.id.hourCheckBox);
        previousEntriesListView = (ListView) findViewById(R.id.previousEntriesList);

        manualHourSpinner = (Spinner) findViewById(R.id.manualHour);
        manualHourSpinner.setEnabled(false);
        manualHourSpinner.setVisibility(View.INVISIBLE);

        manualMinSpinner = (Spinner) findViewById(R.id.manualMin);
        manualMinSpinner.setEnabled(false);
        manualMinSpinner.setVisibility(View.INVISIBLE);

        // TODO: Load the locked date string here, and list
        Gson gson = new Gson();
        data = SDSP.getString(SleepPreviousActivity.this, getDay(-1));
        dayData = gson.fromJson(data, DayEntry.class);

        // Populate the list with existing entries delimited by '~'
        if (!data.equals("") && dayData.isFinished() == false) {
            list = dayData.getList();
        }

        // Load time date data
        if (!data.equals("")) {
            previousTimeString = (dayData.getDate() + " " + dayData.getLockInTime());
        } else {
            previousTimeString = "01/01/2017 16:55:03";
        }

        previousTimeValue = new Date(previousTimeString).getTime();

        clock = getClock();
        clock.start();

        // Apply updated list to the adapter and listview
        adapter = new SleepPreviousEntryAdapter(this, list);
        adapter.notifyDataSetChanged();
        previousEntriesListView.setAdapter(adapter);

        // OnclickListener for individual items in listview. Prompt for entry deletion.
        previousEntriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                AlertDialog.Builder adb;
                adb = new AlertDialog.Builder(SleepPreviousActivity.this);
                final String title = list.get(position).getTitle();
                adb.setTitle(title);
                adb
                        .setMessage("Is this entry completed?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(SleepPreviousActivity.this, "Updated status of " + title,
                                        Toast.LENGTH_SHORT).show();

                                list.get(position).setComplete(true);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                list.get(position).setComplete(false);
                                adapter.notifyDataSetChanged();
                            }
                        });

                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });

        // Dialog box for confirming a finished entry
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb;
                adb = new AlertDialog.Builder(SleepPreviousActivity.this);
                adb.setTitle("Confirmation");
                adb
                        .setMessage("Finish this sleep entry?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                boolean validInput = true;

                                // Using manual input over auto input
                                if (checkBox.isChecked()) {
                                    int hour = Integer.parseInt(manualHourSpinner.getSelectedItem().toString());
                                    int min = Integer.parseInt(manualMinSpinner.getSelectedItem().toString());
                                    long diffHours = (diff / (1000 * 60 * 60));
                                    long diffRemainingMins = (diff / (1000 * 60)) % 60;

                                    // Invalid time entered, give warning.
                                    if (((hour * 60) + min) > ((diffHours * 60) + diffRemainingMins)) {
                                        Toast.makeText(SleepPreviousActivity.this, "Invalid input",
                                                Toast.LENGTH_SHORT).show();
                                        validInput = false;
                                    } else {
                                        // Valid time entered, replace auto input with manual.
                                        message = hour + "h:" + min + "m";
                                    }
                                }

                                // Proceed with saving, terminating, and switching this activity.
                                if (validInput == true) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                    String date = sdf.format(new Date());
                                    Toast.makeText(SleepPreviousActivity.this, date + " Done! " + message,
                                            Toast.LENGTH_LONG).show();

                                    clock.onFinish();
                                    doneButton.setEnabled(false);
                                    checkBox.setEnabled(false);
                                    manualHourSpinner.setEnabled(false);
                                    manualMinSpinner.setEnabled(false);
                                    // TODO: Save completed Entries list to file here and Exit activity

                                    //Save time and date data here, and list data
                                    dayData.setList(list);
                                    dayData.setIsFinished(true);
                                    dayData.setHoursSlept(message);

                                    SDSP.saveDay(SleepPreviousActivity.this, dayData);

                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = adb.create();
                alertDialog.show();

            }
        });

        // Checkbox listener for toggling manual input and disabling auto tracked sleep hours
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timePassedText.setTextColor(Color.DKGRAY);
                    timeSleptText.setTextColor(Color.DKGRAY);
                    manualHourSpinner.setEnabled(true);
                    manualHourSpinner.setVisibility(View.VISIBLE);
                    manualMinSpinner.setEnabled(true);
                    manualMinSpinner.setVisibility(View.VISIBLE);
                } else {
                    timePassedText.setTextColor(Color.parseColor("#FFFFFF"));
                    timeSleptText.setTextColor(Color.parseColor("#FFFFFF"));
                    manualHourSpinner.setEnabled(false);
                    manualHourSpinner.setVisibility(View.INVISIBLE);
                    manualMinSpinner.setEnabled(false);
                    manualMinSpinner.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // Create a countdowntimer to constantly update the textview on offset time slept
    public CountDownTimer getClock() {
        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String date = sdf.format(new Date());

                diff = new Date(date).getTime() - previousTimeValue;
                long diffRemainingMins = ((diff / (1000 * 60)) % 60);
                long diffHours = (diff / (1000 * 60 * 60));
                message = diffHours + "h:" + diffRemainingMins + "m";
                timePassedText.setText(message);
            }

            public void onFinish() {
                cancel();
            }
        };
        return newtimer;
    }

    // Offset -1 for yesterday
    public String getDay(int offset) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(cal.getTime());
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SleepPreviousActivity.class);
        return i;
    }
}
