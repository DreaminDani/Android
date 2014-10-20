package demo.android.ait.hu.placestovisit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import demo.android.ait.hu.placestovisit.R;
import demo.android.ait.hu.placestovisit.data.Place;

/**
 * Created by Peter on 2014.10.08..
 */
public class PlacesToVisitAdapter extends BaseAdapter {

    private Context context;
    private List<Place> placesToVisit;

    public PlacesToVisitAdapter(Context context, List<Place> placesToVisit) {
        this.context = context;
        this.placesToVisit = placesToVisit;
    }

    public void addPlace(Place place) {
        placesToVisit.add(place);
    }

    public void removeItem(int index) {
        placesToVisit.remove(index);
    }

    @Override
    public int getCount() {
        return placesToVisit.size();
    }

    @Override
    public Object getItem(int i) {
        return placesToVisit.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    static class ViewHolder {
        ImageView ivIcon;
        TextView tvPlace;
        TextView tvDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_place, null);
            ViewHolder holder = new ViewHolder();
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            holder.tvPlace = (TextView) v.findViewById(R.id.tvPlace);
            holder.tvDate = (TextView) v.findViewById(R.id.tvDate);
            v.setTag(holder);
        }

        Place place = placesToVisit.get(position);
        if (place != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.tvPlace.setText(place.getPlaceName());
            holder.tvDate.setText(place.getPickUpDate().toString());

            holder.ivIcon.setImageResource(place.getPlaceType().getIconId());
        }
        return v;
    }

}
