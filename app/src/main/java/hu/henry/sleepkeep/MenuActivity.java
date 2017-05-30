package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    TextView welcomeText;
    SleepDataSharedPrefs SDSP = SleepDataSharedPrefs.getSleepDataSharedPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        welcomeText = (TextView) findViewById(R.id.welcome);
        int sleepHour = SDSP.getInt(MenuActivity.this, "sleepHour");
        int bedHour = SDSP.getInt(MenuActivity.this, "bedHour");
        String name = SDSP.getString(MenuActivity.this, "username");

        if (sleepHour < 0 ) {
            SDSP.saveInt(MenuActivity.this, "sleepHour", 8);
        }

        if (bedHour < 0 ) {
            SDSP.saveInt(MenuActivity.this, "bedHour", 18);
        }

        if (name.equals("")) {
            name = "nameless user";
            SDSP.saveString(MenuActivity.this, "username", name);
        }
        welcomeText.setText("Hello, " + name + "!");
    }

    // OnClickListeners to change to the selected activity
    public void boxOnClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.BoxSleepEntry:
                i = SleepActivity.newIntent(MenuActivity.this);
                break;
            case R.id.BoxStats:
                i = StatsActivity.newIntent(MenuActivity.this);
                break;
            case R.id.BoxProfile:
                i = ProfileActivity.newIntent(MenuActivity.this);
                break;
            case R.id.BoxDeleteData:
                i = DeleteDataActivity.newIntent(MenuActivity.this);
                break;
            case R.id.BoxExtra:
                i = DebugActivity.newIntent(MenuActivity.this);
                break;
        }
        startActivity(i);
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MenuActivity.class);
        return i;
    }
}
