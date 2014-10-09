package net.desandoval.apps.placestovisitdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.desandoval.apps.placestovisitdemo.R;
import net.desandoval.apps.placestovisitdemo.data.Place;

import java.util.List;

/**
 * Created by Daniel on 9/10/2014.
 */
public class PlacesAdapter extends BaseAdapter{

    private Context context;
    private List<Place> placesList;

    public PlacesAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placesList = placeList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public void addPlace(Place p) {
        placesList.add(p);
    }

    public static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvDate;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.row_place, null);
            ViewHolder holder = new ViewHolder();
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            holder.tvDate = (TextView) v.findViewById(R.id.tvDate);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            v.setTag(holder);
        }

        Place place = placesList.get(i);

        if (place != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.tvName.setText(place.getName());
            holder.tvDate.setText(place.getDate().toString());

            switch (place.getCategory()) {
                case LANDSCAPE:
                    holder.ivIcon.setImageResource(R.drawable.landscape);
                    break;
                case CITY:
                    holder.ivIcon.setImageResource(R.drawable.city);
                    break;
                case BUILDING:
                    holder.ivIcon.setImageResource(R.drawable.building);
                    break;
            }

        }
        return v;
    }
}
