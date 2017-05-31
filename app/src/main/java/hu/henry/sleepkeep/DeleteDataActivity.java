package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DeleteDataActivity extends AppCompatActivity {

    SleepDataSharedPrefs SDSP = SleepDataSharedPrefs.getSleepDataSharedPreferences();
    Button deleteScoreButton;
    Button deleteHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedata);

        deleteScoreButton = (Button) findViewById(R.id.deleteScore);
        deleteHistoryButton = (Button) findViewById(R.id.deleteHistory);

        //TODO: Code to delete saved data.
        deleteScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeleteDataActivity.this, "Score Reset",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Deletes everything
        deleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = SDSP.getString(DeleteDataActivity.this, "username");
                int bedHour = SDSP.getInt(DeleteDataActivity.this, "bedHour");
                int sleepHour = SDSP.getInt(DeleteDataActivity.this, "sleepHour");
                SDSP.deleteAllData(DeleteDataActivity.this);
                SDSP.saveString(DeleteDataActivity.this, "username", username);
                SDSP.saveInt(DeleteDataActivity.this, "bedHour", bedHour);
                SDSP.saveInt(DeleteDataActivity.this, "sleepHour", sleepHour);

                Toast.makeText(DeleteDataActivity.this, "History Deleted",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, DeleteDataActivity.class);
        return i;
    }
}
