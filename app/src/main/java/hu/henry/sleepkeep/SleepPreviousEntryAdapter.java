package hu.henry.sleepkeep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


// Class to handle sleepEntry items for a list view adapter
public class SleepPreviousEntryAdapter extends BaseAdapter {
    private ArrayList<SleepEntry> list;
    private LayoutInflater mInflater;

    public SleepPreviousEntryAdapter(Context context, ArrayList<SleepEntry> list) {
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_entrypreviouslistview, null);
            holder = new ViewHolder();

            // wire the attributes of sleep entry object to textviews
            holder.title = (TextView) convertView.findViewById(R.id.listViewPreviousEntryTitle);
            holder.description = (TextView) convertView.findViewById(R.id.listViewPreviousEntryDescription);
            holder.importance = (TextView) convertView.findViewById(R.id.listViewPreviousEntryImportance);
            holder.type = (TextView) convertView.findViewById(R.id.listViewPreviousEntryType);
            holder.complete = (TextView) convertView.findViewById(R.id.listViewPreviousEntryComplete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Retrieve object data and update the list view text
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
        holder.importance.setText("Importance: " + list.get(position).getImportance());
        holder.type.setText("Type: " + list.get(position).getType());
        holder.complete.setText("Complete: " + list.get(position).getIsCompleteString());

        return convertView;
    }

    // Wire the TextViews of an entryListView layout to sleepEntry object attributes
    static class ViewHolder {
        TextView title;
        TextView description;
        TextView importance;
        TextView type;
        TextView date;
        TextView complete;
    }
}
