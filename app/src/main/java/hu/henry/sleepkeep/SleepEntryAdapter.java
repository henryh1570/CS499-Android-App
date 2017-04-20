package hu.henry.sleepkeep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


// Class to handle sleepEntry items for a list view adapter
public class SleepEntryAdapter extends BaseAdapter {
    private ArrayList<SleepEntry> list;
    private LayoutInflater mInflater;

    public SleepEntryAdapter(Context context, ArrayList<SleepEntry> list) {
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
            convertView = mInflater.inflate(R.layout.activity_entrylistview, null);
            holder = new ViewHolder();

            // wire the attributes of sleep entry object to textviews
            holder.title = (TextView) convertView.findViewById(R.id.listViewEntryTitle);
            holder.description = (TextView) convertView.findViewById(R.id.listViewEntryDescription);
            holder.importance = (TextView) convertView.findViewById(R.id.listViewEntryImportance);
            holder.type = (TextView) convertView.findViewById(R.id.listViewEntryType);
            holder.isComplete = (TextView) convertView.findViewById(R.id.listViewEntryComplete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Retrieve object data and update the list view text
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
        holder.importance.setText("Importance: " + list.get(position).getImportance());
        holder.type.setText("Type: " + list.get(position).getTypeString());
        holder.isComplete.setText("Complete: " + list.get(position).getIsCompleteString());

        return convertView;
    }

    // Wire the TextViews of an entryListView layout to sleepEntry object attributes
    static class ViewHolder {
        TextView title;
        TextView description;
        TextView importance;
        TextView type;
        TextView isComplete;
    }
}
