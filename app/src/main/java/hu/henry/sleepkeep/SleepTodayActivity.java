package hu.henry.sleepkeep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Time;

public class SleepTodayActivity extends AppCompatActivity {

    boolean isLocked = false;
    Spinner todayBedHourSpinner;
    Button lockInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_today);

        // TODO: Load saved data here
        todayBedHourSpinner = (Spinner) findViewById(R.id.sleepTodayBedHour);
        lockInButton = (Button) findViewById(R.id.lockIn);

        todayBedHourSpinner.setSelection(8);

        lockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = todayBedHourSpinner.getSelectedItem().toString();
                int currentHour = new Time(System.currentTimeMillis()).getHours();
                // TODO: Cross check current day's hour with the set hour for accuracy.
                if (currentHour > Integer.parseInt(hour)) {
                    Toast.makeText(SleepTodayActivity.this, "Invalid Hour",
                            Toast.LENGTH_SHORT).show();
                } else {
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

                                    //TODO: Save data here

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
            }
        });

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SleepTodayActivity.class);
        return i;
    }
}
