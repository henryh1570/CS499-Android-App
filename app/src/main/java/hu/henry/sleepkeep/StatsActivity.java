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
import java.util.Date;

public class StatsActivity extends AppCompatActivity {

    private final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
    SleepDataSharedPrefs SDSP = SleepDataSharedPrefs.getSleepDataSharedPreferences();
    private Button toggleButton;
    private Button backwardButton;
    private Button forwardButton;
    private TextView dateView;
    private ListView previousEntriesListView;
    private SleepPreviousEntryAdapter adapter;
    private ArrayList<SleepEntry> list = new ArrayList<SleepEntry>();
    private GraphView graph;
    private ArrayList<String> keys;
    private String currentKey;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        gson = new Gson();
        toggleButton = (Button) findViewById(R.id.statsToggle);
        backwardButton = (Button) findViewById(R.id.statsBackward);
        forwardButton = (Button) findViewById(R.id.statsForward);
        dateView = (TextView) findViewById(R.id.statsDate);
        previousEntriesListView = (ListView) findViewById(R.id.statsEntriesList);
        // Apply updated list to the adapter and listview
        adapter = new SleepPreviousEntryAdapter(this, list);
        adapter.notifyDataSetChanged();

        // Hide secondary views
        previousEntriesListView.setAdapter(adapter);
        previousEntriesListView.setVisibility(View.GONE);
        dateView.setVisibility(View.GONE);
        backwardButton.setVisibility(View.GONE);
        forwardButton.setVisibility(View.GONE);

        // Load hisory
        String historyJSON = SDSP.getString(StatsActivity.this, "history");
        keys = new ArrayList<String>();
        currentKey = getDay(0);
        History history = null;
        if (!historyJSON.equals("")) {
            history = gson.fromJson(historyJSON, History.class);
            for (Date d: history.getEntryKeys()) {
                keys.add(SDF.format(d));
            }
        } else {
            backwardButton.setEnabled(false);
            forwardButton.setEnabled(false);
            dateView.setText("No entries have been made.");
        }

        String dataSaved = SDSP.getString(StatsActivity.this, getDay(0));
        if (!dataSaved.equals("")) {
            DayEntry dayData = gson.fromJson(dataSaved, DayEntry.class);
            list.addAll(dayData.getList());
            adapter.notifyDataSetChanged();
        }

        graph = (GraphView) findViewById(R.id.graph);
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
                toggleAction();
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backwardAction();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardAction();
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

    // Load Previous entry date data
    private void backwardAction() {
        if (!currentKey.equals(keys.get(0))) {
            currentKey = keys.get(keys.indexOf(currentKey) - 1);
            dateView.setText(currentKey);
            String dataSaved = SDSP.getString(StatsActivity.this, currentKey);
            DayEntry dayData = gson.fromJson(dataSaved, DayEntry.class);
            list.clear();
            list.addAll(dayData.getList());
            adapter.notifyDataSetChanged();
        }
    }

    // Load Next entry date data
    private void forwardAction() {
        if (!currentKey.equals(keys.get(keys.size() - 1))) {
            int index = keys.indexOf(currentKey);
            currentKey = keys.get(index + 1);
            dateView.setText(currentKey);
            String dataSaved = SDSP.getString(StatsActivity.this, currentKey);
            DayEntry dayData = gson.fromJson(dataSaved, DayEntry.class);
            list.clear();
            list.addAll(dayData.getList());
            adapter.notifyDataSetChanged();
        }
    }

    private void toggleAction() {
        // Hide graph, show history
        if (graph.getVisibility() == View.VISIBLE) {
            graph.setVisibility(View.GONE);
            dateView.setVisibility(View.VISIBLE);
            previousEntriesListView.setVisibility(View.VISIBLE);
            backwardButton.setVisibility(View.VISIBLE);
            forwardButton.setVisibility(View.VISIBLE);
            toggleButton.setText("View Graph");
            // If history exists
            if (forwardButton.isEnabled() == true) {
                // Make sure current key is valid.
                if (!keys.contains(currentKey)) {
                    currentKey = keys.get(keys.size() - 1);
                }
                dateView.setText(currentKey);
            }
        } else {
            // Hide History, show graph
            graph.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.GONE);
            previousEntriesListView.setVisibility(View.GONE);
            backwardButton.setVisibility(View.GONE);
            forwardButton.setVisibility(View.GONE);
            toggleButton.setText("View History");
        }
    }
}
