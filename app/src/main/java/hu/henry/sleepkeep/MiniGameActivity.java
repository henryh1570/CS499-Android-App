package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MiniGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MiniGameActivity.class);
        return i;
    }
}
