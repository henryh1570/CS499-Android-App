package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DeleteDataActivity extends AppCompatActivity {

    SleepDataSharedPreferences SDFP = new SleepDataSharedPreferences(this);
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
                String username = SDFP.getString("username");
                int bedHour = SDFP.getInt("bedHour");
                int sleepHour = SDFP.getInt("sleepHour");
                SDFP.deleteAllData();
                SDFP.saveString("username", username);
                SDFP.saveInt("bedHour", bedHour);
                SDFP.saveInt("sleepHour", sleepHour);

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
