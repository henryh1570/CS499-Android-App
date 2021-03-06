package hu.henry.sleepkeep;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SleepTodayActivity extends AppCompatActivity {

    SleepDataSharedPrefs SDSP = SleepDataSharedPrefs.getSleepDataSharedPreferences();
    private boolean isLocked;
    private Button lockInButton;
    private Button addNewEntryButton;
    private TextView todayTimeText;
    private TextView todayDateText;
    private CountDownTimer clock;
    private ArrayList<SleepEntry> list = new ArrayList<SleepEntry>();
    private SleepEntryAdapter adapter;
    private ListView entriesListView;
    private String data;
    private DayEntry dayData = new DayEntry("", "", "", false, list);
    private String today;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_today);

        lockInButton = (Button) findViewById(R.id.lockIn);
        addNewEntryButton = (Button) findViewById(R.id.addNewEntry);
        todayTimeText = (TextView) findViewById(R.id.todayCurrentTime);
        todayDateText = (TextView) findViewById(R.id.todayCurrentDate);
        entriesListView = (ListView) findViewById(R.id.todayEntriesList);

        // Get the current date + data
        SimpleDateFormat sdfdate = new SimpleDateFormat("MM/dd/yyyy");
        today = sdfdate.format(new Date());
        data = SDSP.getString(SleepTodayActivity.this, today);

        if (!data.equals("")) {
            dayData = gson.fromJson(data, DayEntry.class);
            list = dayData.getList();
        }

        // Start the clock if today's entry is not done
        // Otherwise do not use the clock and disable the lock in button
        if (data.equals("") || dayData.getLockInTime() == "") {
            isLocked = false;
            clock = getClock();
            clock.start();
        } else {
            lockedDown();
            todayDateText.setText(dayData.getDate());
            todayTimeText.setText(dayData.getLockInTime());
        }

        // Attach adapter and wire to listview
        adapter = new SleepEntryAdapter(this, list);
        adapter.notifyDataSetChanged();
        entriesListView.setAdapter(adapter);

        // OnclickListener for individual items in listview. Prompt for entry deletion.
        entriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                if (isLocked == false) {
                    AlertDialog.Builder adb;
                    adb = new AlertDialog.Builder(SleepTodayActivity.this);
                    final String title = list.get(position).getTitle();
                    adb.setTitle(title);
                    adb
                            .setMessage("Delete this entry?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(SleepTodayActivity.this, "Deleted " + title,
                                            Toast.LENGTH_SHORT).show();

                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
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
            }
        });

        // Lockin the data to freeze timer and other features
        lockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb;
                adb = new AlertDialog.Builder(SleepTodayActivity.this);

                adb.setTitle("Confirmation");
                adb
                        .setMessage("Are you ready to lock in now?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                lockedDown();
                                SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                String time = sdftime.format(new Date());

                                //Save time and date data here, and list data
                                dayData.setList(list);
                                dayData.setLockInTime(time);
                                dayData.setDate(today);
                                dayData.setIsFinished(false);
                                dayData.setHoursSlept("");

                                // Save today's data and to history key
                                SDSP.saveDay(SleepTodayActivity.this, dayData);
                                String historyJSON = SDSP.getString(SleepTodayActivity.this, "history");
                                History history = new History(new ArrayList<String>());
                                if (!historyJSON.equals("")) {
                                    history = gson.fromJson(historyJSON, History.class);
                                }
                                history.addKey(today);
                                SDSP.saveString(SleepTodayActivity.this, "history", history.toJson());

                                Toast.makeText(SleepTodayActivity.this, "Locked In!",
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
            }
        });

        // Allow user to create a new entry object and display it in listview
        addNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb;
                adb = new AlertDialog.Builder(SleepTodayActivity.this);
                LayoutInflater inflater = SleepTodayActivity.this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                adb.setView(inflater.inflate(R.layout.dialog_entryform, null));
                adb.setTitle("New Sleep Entry");
                adb
                        .setMessage("Fill in the details")
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Get this dialog's edittext values and make a SleepEntry object.
                                Dialog d = (Dialog) dialog;
                                String title = ((EditText) d.findViewById(R.id.dialog_title)).getText().toString().replaceAll("[~`\\|]", "");
                                String description = ((EditText) d.findViewById(R.id.dialog_description)).getText().toString().replaceAll("[~`\\|]", "");
                                String importance = ((Spinner) d.findViewById(R.id.dialog_importance)).getSelectedItem().toString().replaceAll("[~`\\|]", "");
                                String type = ((Spinner) d.findViewById(R.id.dialog_type)).getSelectedItem().toString().replaceAll("[~`\\|]", "");
                                String date = todayDateText.getText().toString().replaceAll("[~`\\|]", "");

                                if (title.equals("")) {
                                    title = "Nameless Entry";
                                }

                                if (description.equals("")) {
                                    description = "No description.";
                                }

                                list.add(new SleepEntry(title, description, date, Integer.parseInt(importance), false, type));
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });
    }

    // Create a countdowntimer to constantly update the textview like a clock
    public CountDownTimer getClock() {
        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat sdfdate = new SimpleDateFormat("MM/dd/yyyy");
                String time = sdftime.format(new Date());
                String date = sdfdate.format(new Date());

                todayTimeText.setText(time);
                todayDateText.setText(date);
            }

            public void onFinish() {
                cancel();
            }
        };
        return newtimer;
    }

    // Prevent ui features when the entry is locked down
    public void lockedDown() {
        isLocked = true;
        lockInButton.setEnabled(false);
        addNewEntryButton.setEnabled(false);
        todayTimeText.setTextColor(Color.RED);
        todayDateText.setTextColor(Color.RED);
        if (clock != null) {
            clock.onFinish();
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SleepTodayActivity.class);
        return i;
    }
}
