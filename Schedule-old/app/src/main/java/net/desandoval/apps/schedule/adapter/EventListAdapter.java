package net.desandoval.apps.schedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.desandoval.apps.schedule.data.DayEvent;
import net.desandoval.apps.schedule.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel Sandoval on 2014.10.21
 * Modified from Peter's PlaceToVisit Example in class on 2014.10.08..
 */
public class EventListAdapter extends BaseAdapter {

    private Context context;
    public List<DayEvent> EventList;

    public EventListAdapter(List<DayEvent> eventList) {
        this.EventList = eventList;
    }

    @Override
    public int getCount() {
        return EventList.size();
    }

    @Override
    public Object getItem(int i) {
        return EventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    static class ViewHolder {
        TextView startTime;
        TextView eventName;
        TextView eventLocation;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_place, null);
            ViewHolder holder = new ViewHolder();

            holder.startTime = (TextView) v.findViewById(R.id.startTime);
            holder.eventName = (TextView) v.findViewById(R.id.eventName);
            holder.eventLocation = (TextView) v.findViewById(R.id.eventLocation);
            v.setTag(holder);
        }

        final DayEvent item = EventList.get(position);

        if (item != null) {
            final ViewHolder holder = (ViewHolder) v.getTag();

            String timeHolder = Integer.toString(item.getStartHour())
                    + ":" + Integer.toString(item.getStartMinute());
            holder.startTime.setText(timeHolder);

            holder.eventName.setText(item.getTitle());
            holder.eventLocation.setText(item.getLocation());
        }
        return v;
    }

}
