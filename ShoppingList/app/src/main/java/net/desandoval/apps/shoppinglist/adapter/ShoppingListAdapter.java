package net.desandoval.apps.shoppinglist.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.desandoval.apps.shoppinglist.data.Item;

import java.util.List;

import net.desandoval.apps.shoppinglist.R;

/**
 * Created by Peter on 2014.10.08..
 */
public class ShoppingListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> ShoppingList;

    public ShoppingListAdapter(Context context, List<Item> ShoppingList) {
        this.context = context;
        this.ShoppingList = ShoppingList;
    }

    public void addPlace(Item item) {
        ShoppingList.add(item);
    }

    public void removeItem(int index) { ShoppingList.remove(index); }

    @Override
    public int getCount() {
        return ShoppingList.size();
    }

    @Override
    public Object getItem(int i) {
        return ShoppingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    static class ViewHolder {
        ImageView ivIcon;
        TextView tvPlace;
        TextView tvDescriptionStore;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_place, null);
            ViewHolder holder = new ViewHolder();
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            holder.tvPlace = (TextView) v.findViewById(R.id.tvItem);
            holder.tvDescriptionStore = (TextView) v.findViewById(R.id.tvDescriptionStore);
            v.setTag(holder);
        }

        Item item = ShoppingList.get(position);
        if (item != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.tvPlace.setText(item.getItemName());
            holder.tvDescriptionStore.setText(Html.fromHtml(item.toString()));

            holder.ivIcon.setImageResource(item.getItemType().getIconId());
        }
        return v;
    }

}
