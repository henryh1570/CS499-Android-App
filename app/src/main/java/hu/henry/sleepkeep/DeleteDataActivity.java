package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DeleteDataActivity extends AppCompatActivity {

    Button deleteScoreButton;
    Button deleteHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedata);

        deleteScoreButton = (Button) findViewById(R.id.deleteScore);
        deleteHistoryButton = (Button) findViewById(R.id.deleteHistory);

        deleteScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        deleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, DeleteDataActivity.class);
        return i;
    }
}
