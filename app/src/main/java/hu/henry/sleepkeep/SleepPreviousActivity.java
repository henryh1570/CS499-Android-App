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

public class SleepPreviousActivity extends AppCompatActivity {

    Button doneButton;
    TextView timePassedText;
    String previousTimeString;
    long previousTimeValue;
    CountDownTimer clock;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_previous);

        doneButton = (Button) findViewById(R.id.done);
        timePassedText = (TextView) findViewById(R.id.timePassed);

        // TODO: Load the locked date string here
        previousTimeString = "04/18/2017 16:55:03";
        previousTimeValue = new Date(previousTimeString).getTime();

        clock = getClock();
        clock.start();

        // Dialog box for confirming the finished entry
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
                                clock.onFinish();

                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                String date = sdf.format(new Date());
                                Toast.makeText(SleepPreviousActivity.this, date + " Done! " + message,
                                        Toast.LENGTH_LONG).show();

                                //TODO: Save completed Entry to file here
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

    // Create a countdowntimer to constantly update the textview on offset time slept
    public CountDownTimer getClock() {
        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String date = sdf.format(new Date());

                long diff = new Date(date.toString()).getTime() - previousTimeValue;
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
