package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    SleepDataSharedPreferences SDSP = new SleepDataSharedPreferences(this);
    Button saveProfileButton;
    EditText nameText;
    Spinner sleepHoursSpinner;
    Spinner bedHourSpinner;
    String username;
    int defaultSleepHour;
    int defaultBedHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Wire the EditTexts and Spinners
        nameText = (EditText) findViewById(R.id.editTextName);
        sleepHoursSpinner = (Spinner) findViewById(R.id.profileSleptHours);
        bedHourSpinner = (Spinner) findViewById(R.id.profileBedHour);
        username = SDSP.getString("username");
        nameText.setText(username, TextView.BufferType.EDITABLE);

        // Get the shared preferences sleep hour
        defaultSleepHour = SDSP.getInt("sleepHour");
        if (defaultSleepHour == 0) {
            defaultSleepHour = 8;
        }
        sleepHoursSpinner.setSelection(defaultSleepHour);

        // Get the shared preferences bed hour
        defaultBedHour = SDSP.getInt("bedHour");
        if (defaultBedHour == 0) {
            defaultBedHour = 18;
        }
        bedHourSpinner.setSelection(defaultBedHour);

        // Save Changes Button, check if valid input.
        saveProfileButton = (Button) findViewById(R.id.saveProfile);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Invalid Name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Save the data
                    SDSP.saveString("username", nameText.getText().toString());
                    SDSP.saveInt("sleepHour", Integer.parseInt(sleepHoursSpinner.getSelectedItem().toString()));
                    SDSP.saveInt("bedHour", Integer.parseInt(bedHourSpinner.getSelectedItem().toString()));
                    Toast.makeText(ProfileActivity.this, "Data Saved!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, ProfileActivity.class);
        return i;
    }
}
