package hu.henry.sleepkeep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SleepActivity extends AppCompatActivity {

    boolean previousComplete = false;
    Button previousEntryButton;
    Button todayEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        previousEntryButton = (Button) findViewById(R.id.previousEntry);
        todayEntryButton = (Button) findViewById(R.id.todayEntry);

        // TODO: Load everything here and wire them
        if (previousComplete == true) {
            previousEntryButton.setEnabled(false);
        }

        previousEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = SleepPreviousActivity.newIntent(SleepActivity.this);
                startActivity(i);
            }
        });

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
                                    // TODO: Code to discard previous entry
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
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SleepActivity.class);
        return i;
    }
}
