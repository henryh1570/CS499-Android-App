package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    Button saveProfileButton;
    EditText nameText;
    EditText desiredSleepHoursText;
    EditText desiredBedHourText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // TODO: Link the edittexts to the saved data. And onclick functionality.

        // Wire the TextViews and EditTexts
        nameText = (EditText) findViewById(R.id.editTextName);
        nameText.setText("Doc", TextView.BufferType.EDITABLE);

        desiredSleepHoursText = (EditText) findViewById(R.id.editTextSleepHour);
        desiredSleepHoursText.setText("8", TextView.BufferType.EDITABLE);

        desiredBedHourText = (EditText) findViewById(R.id.editTextBedHour);
        desiredBedHourText.setText("18", TextView.BufferType.EDITABLE);

        // Save Changes Button, check if valid input.
        saveProfileButton = (Button) findViewById(R.id.saveProfile);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String sleepHours = desiredSleepHoursText.getText().toString();
                String bedHour = desiredBedHourText.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Invalid Name",
                            Toast.LENGTH_SHORT).show();
                } else if (sleepHours.equals("") || Integer.parseInt(sleepHours) > 24) {
                    Toast.makeText(ProfileActivity.this, "Invalid Hours",
                            Toast.LENGTH_LONG).show();
                } else if (bedHour.equals("") || Integer.parseInt(bedHour) > 23) {
                    Toast.makeText(ProfileActivity.this, "Invalid Hour",
                            Toast.LENGTH_LONG).show();
                } else {
                    // TODO: Code to save data here
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
