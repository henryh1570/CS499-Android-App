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

    Button saveProfileButton;
    EditText nameText;
    Spinner sleepHoursSpinner;
    Spinner bedHourSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // TODO: Link the edittexts / spinners to the saved data. And onclick functionality.

        // Wire the EditTexts and Spinners
        nameText = (EditText) findViewById(R.id.editTextName);
        nameText.setText("Doc", TextView.BufferType.EDITABLE);

        sleepHoursSpinner = (Spinner) findViewById(R.id.profileSleptHours);
        sleepHoursSpinner.setSelection(8);

        bedHourSpinner = (Spinner) findViewById(R.id.profileBedHour);
        bedHourSpinner.setSelection(18);

        // Save Changes Button, check if valid input.
        saveProfileButton = (Button) findViewById(R.id.saveProfile);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String sleepHours = sleepHoursSpinner.getSelectedItem().toString();
                String bedHour = bedHourSpinner.getSelectedItem().toString();

                if (name.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Invalid Name",
                            Toast.LENGTH_SHORT).show();
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
