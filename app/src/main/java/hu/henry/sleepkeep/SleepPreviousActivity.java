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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepPreviousActivity extends AppCompatActivity {

    Button doneButton;
    TextView timePassedText;
    TextView timeSleptText;
    String previousTimeString;
    long previousTimeValue;
    long diff;
    CountDownTimer clock;
    String message;
    CheckBox checkBox;
    Spinner manualHourSpinner;
    Spinner manualMinSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_previous);

        doneButton = (Button) findViewById(R.id.done);
        timePassedText = (TextView) findViewById(R.id.timePassed);
        timeSleptText = (TextView) findViewById(R.id.timeSlept);
        checkBox = (CheckBox) findViewById(R.id.hourCheckBox);
        manualHourSpinner = (Spinner) findViewById(R.id.manualHour);
        manualHourSpinner.setEnabled(false);
        manualHourSpinner.setVisibility(View.INVISIBLE);
        manualMinSpinner = (Spinner) findViewById(R.id.manualMin);
        manualMinSpinner.setEnabled(false);
        manualMinSpinner.setVisibility(View.INVISIBLE);

        // TODO: Load the locked date string here
        previousTimeString = "04/18/2017 16:55:03";
        previousTimeValue = new Date(previousTimeString).getTime();

        clock = getClock();
        clock.start();

        // Dialog box for confirming a finished entry
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb;
                final EditText editText = new EditText(SleepPreviousActivity.this);
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
                                    //TODO: Save completed Entry to file here and Exit activity
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

                diff = new Date(date.toString()).getTime() - previousTimeValue;
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

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SleepPreviousActivity.class);
        return i;
    }
}
