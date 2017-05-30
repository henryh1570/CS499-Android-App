package hu.henry.sleepkeep;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    SleepDataSharedPrefs SDSP = SleepDataSharedPrefs.getSleepDataSharedPreferences();
    Button toggleButton;
    TextView dateView;
    ListView previousEntriesListView;
    private SleepPreviousEntryAdapter adapter;
    ArrayList<SleepEntry> list = new ArrayList<SleepEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Gson gson = new Gson();
        toggleButton = (Button) findViewById(R.id.statsToggle);
        dateView = (TextView) findViewById(R.id.debugDate);
        previousEntriesListView = (ListView) findViewById(R.id.statsEntriesList);
        // Apply updated list to the adapter and listview
        adapter = new SleepPreviousEntryAdapter(this, list);
        adapter.notifyDataSetChanged();
        previousEntriesListView.setAdapter(adapter);
        previousEntriesListView.setVisibility(View.GONE);
        dateView.setVisibility(View.GONE);


        String dataSaved = SDSP.getString(StatsActivity.this, getDay(0));
        if (!dataSaved.equals("")) {
            DayEntry dayData = gson.fromJson(dataSaved, DayEntry.class);
            list.addAll(dayData.getList());
            adapter.notifyDataSetChanged();
        }


        final GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series;

        // Get last week's hours slept
        ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();
        for (int i = 0; i < 7; i++) {
            String data = SDSP.getString(StatsActivity.this, getDay(-1*i));
            if (!data.equals("")) {
                DayEntry dayData = gson.fromJson(data, DayEntry.class);
                String hours = dayData.getHoursSlept();
                if (!hours.equals("")) {
                    // Extract hours from #h:#m string
                    hours = hours.split(":")[0].replaceAll("[^\\d.]", "");
                    dataPoints.add(new DataPoint(i, Integer.parseInt(hours)));
                }
            }
        }

        series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
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

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (graph.getVisibility() == View.VISIBLE) {
                    graph.setVisibility(View.GONE);
                    dateView.setVisibility(View.VISIBLE);
                    previousEntriesListView.setVisibility(View.VISIBLE);
                    toggleButton.setText("View Graph");
                } else {
                    graph.setVisibility(View.VISIBLE);
                    dateView.setVisibility(View.GONE);
                    previousEntriesListView.setVisibility(View.GONE);
                    toggleButton.setText("View History");
                }
            }
        });
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
