package net.desandoval.apps.shoppinglist.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import net.desandoval.apps.shoppinglist.ShoppingList;
import net.desandoval.apps.shoppinglist.data.DatabaseHelper;
import net.desandoval.apps.shoppinglist.data.Item;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.desandoval.apps.shoppinglist.R;

/**
 * Created by Daniel Sandoval on 2014.10.21
 * Modified from Peter's PlaceToVisit Example in class on 2014.10.08..
 */
public class ShoppingListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> ShoppingList;

    public ShoppingListAdapter(Context context, List<Item> ShoppingList) {
        this.context = context;
        this.ShoppingList = ShoppingList;
    }

    public void addItem(Item item) {
        ShoppingList.add(item);
    }

    public void removeItem(int index) {
        ShoppingList.remove(index);
    }

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
        CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_place, null);
            ViewHolder holder = new ViewHolder();
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            holder.checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            holder.tvPlace = (TextView) v.findViewById(R.id.tvItem);
            holder.tvDescriptionStore = (TextView) v.findViewById(R.id.tvDescriptionStore);
            v.setTag(holder);
        }

        final Item item = ShoppingList.get(position);

        if (item != null) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            holder.checkBox.setChecked(item.getChecked()); // retrieves item checked state from Item

            // Update checked state of current Item
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Item tempItem = ShoppingList.get(position);
                    tempItem.setChecked(holder.checkBox.isChecked());
                }
            });
            holder.tvPlace.setText(item.getItemName());
            holder.tvDescriptionStore.setText(Html.fromHtml(item.display()));

            holder.ivIcon.setImageResource(item.getItemType().getIconId());
        }
        return v;
    }

}
