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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepTodayActivity extends AppCompatActivity {

    private boolean isLocked;
    private Button lockInButton;
    private TextView todayTimeText;
    private TextView todayDateText;
    private CountDownTimer clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_today);

        lockInButton = (Button) findViewById(R.id.lockIn);
        todayTimeText = (TextView) findViewById(R.id.todayCurrentTime);
        todayDateText = (TextView) findViewById(R.id.todayCurrentDate);

        // TODO: Load saved data here
        isLocked = false;

        // Start the clock if today's entry is not done
        // Otherwise do not use the clock and disable the lock in button
        if (isLocked == false) {
            clock = getClock();
            clock.start();
        } else {
            // TODO: Load time / date here
            todayTimeText.setText("LOADED TIME");
            todayDateText.setText("LOADED DATE");
            lockInButton.setEnabled(false);
        }

        lockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb;
                final EditText editText = new EditText(SleepTodayActivity.this);
                adb = new AlertDialog.Builder(SleepTodayActivity.this);
                adb.setTitle("Confirmation");
                adb
                        .setMessage("Are you ready to lock in now?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                isLocked = true;
                                lockInButton.setEnabled(false);

                                //TODO: Save time and date data here
                                todayTimeText.setTextColor(Color.RED);
                                todayDateText.setTextColor(Color.RED);
                                clock.onFinish();

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

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SleepTodayActivity.class);
        return i;
    }
}
