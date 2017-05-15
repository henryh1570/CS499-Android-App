package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DeleteDataActivity extends AppCompatActivity {

    SleepDataSharedPreferencesManager SDFP = SleepDataSharedPreferencesManager.getSleepDataSharedPreferences();
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

        deleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = SDFP.getString(DeleteDataActivity.this, "username");
                int bedHour = SDFP.getInt(DeleteDataActivity.this, "bedHour");
                int sleepHour = SDFP.getInt(DeleteDataActivity.this, "sleepHour");
                SDFP.deleteAllData(DeleteDataActivity.this);
                SDFP.saveString(DeleteDataActivity.this, "username", username);
                SDFP.saveInt(DeleteDataActivity.this, "bedHour", bedHour);
                SDFP.saveInt(DeleteDataActivity.this, "sleepHour", sleepHour);

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
