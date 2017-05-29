package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatsActivity extends AppCompatActivity {

    SleepDataSharedPreferencesManager SDSP = SleepDataSharedPreferencesManager.getSleepDataSharedPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series;

        Gson gson = new Gson();

        // Get last week's hours slept
        ArrayList<DataPoint> list = new ArrayList<DataPoint>();
        for (int i = 0; i < 7; i++) {
            String data = SDSP.getString(StatsActivity.this, getDay(-1*i));
            DayEntry dayData = gson.fromJson(data, DayEntry.class);
            if (!data.equals("")) {
                String hours = dayData.getHoursSlept();
                if (!hours.equals("")) {
                    // Extract hours from #h:#m string
                    hours = hours.split(":")[0].replaceAll("[^\\d.]", "");
                    list.add(new DataPoint(i, Integer.parseInt(hours)));
                }
            }
        }

        series = new LineGraphSeries<>(list.toArray(new DataPoint[list.size()]));
        series.setDrawDataPoints(true);
        series.setDrawBackground(true);
        series.setAnimated(true);
        series.setThickness(5);
        series.setColor(Color.YELLOW);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Days Ago");
        gridLabel.setGridColor(Color.WHITE);
        gridLabel.setVerticalAxisTitle("Hours");
        gridLabel.setHorizontalAxisTitleColor(Color.WHITE);
        gridLabel.setVerticalAxisTitleColor(Color.WHITE);

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMaxY(23);
        viewport.setMaxX(7);

        graph.setTitle("Hours Slept in the Last 7 Days");
        graph.setTitleColor(Color.WHITE);
        graph.addSeries(series);
    }

    // Offset -1 for yesterday
    public String getDay(int offset) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(cal.getTime());
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, StatsActivity.class);
        return i;
    }
}
