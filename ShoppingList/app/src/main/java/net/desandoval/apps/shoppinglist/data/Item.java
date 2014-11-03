package net.desandoval.apps.shoppinglist.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.desandoval.apps.shoppinglist.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel Sandoval on 2014.10.21
 * Modified from Peter's PlaceToVisit Example in class on 2014.10.08..
 */
public class Item implements Serializable, Comparable {

    public enum ItemType {
        HOUSEHOLD(0, R.drawable.dwelling1),
        BAKERY(1, R.drawable.bread8),
        DAIRY(2, R.drawable.box44),
        BULK_FOOD(3, R.drawable.rubbish1),
        MEAT_DELI(4, R.drawable.steak),
        COOKING(5, R.drawable.cooking16),
        PRODUCE(6, R.drawable.carrot8),
        CLEANING(7, R.drawable.cleaning14),
        PHARMACY(8, R.drawable.drugs5),
        ELECTRONICS(9, R.drawable.home115),
        LITERATURE(10, R.drawable.book126);

        private int value;
        private int iconId;

        private ItemType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public static ItemType fromInt(int value) {
            for (ItemType p : ItemType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return PRODUCE;
        }

        public int getIconId() {
            return iconId;
        }
    }

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private ItemType itemType;
    @DatabaseField
    private String itemName;
    @DatabaseField
    private String description;
    @DatabaseField
    private Date pickUpDate;
    @DatabaseField
    private String store;
    @DatabaseField
    private String price;
    @DatabaseField
    private boolean checked;


    Item() {
        // needed by ormlite
    }

    public Item(ItemType ItemType, String itemName, String description, Date pickUpDate, String store, String price, int id, boolean checked) {
        this.itemType = ItemType;
        this.itemName = itemName;
        this.description = description;
        this.store = store;
        this.pickUpDate = pickUpDate;
        this.price = price;
        if (id != -1) {
            this.id = id;
        }
        this.checked = checked;
    }

    @Override
    public int compareTo(Object o) {

        Item f = (Item)o;

        if(store.isEmpty()){
            return 1;
        }else if (f.store.isEmpty()){
            return -1;
        } else if (store.toLowerCase().charAt(0) > f.store.toLowerCase().charAt(0)) {
            return 1;
        }
        else if (store.toLowerCase().charAt(0) < f.store.toLowerCase().charAt(0)) {
            return -1;
        }
        else {
            return 0;
        }

    }


    /*
     * toString establishes storage schema for ORM database
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("itemType=").append(itemType);
        sb.append("itemName=").append(itemName);
        sb.append("description=").append(description);
        sb.append("store=").append(store);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S");
        sb.append(", ").append("pickUpDate=").append(dateFormatter.format(pickUpDate));
        sb.append("price=").append(price);
        sb.append("checked=").append(checked);
        return sb.toString();
    }

    /*
     * Returns a String representation of the item to display on the ShoppingList ListView
     */
    public String display() {
        if (this.price.isEmpty()) {
            return "<b>" + this.store + "  " + "</b>" + this.description;
        }else {
            return "<b>" + this.store + "  " + "</b>" + this.description + "  $<b>" + this.price + "  " + "</b>";
        }
    }
    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public String getStore() {
        return store;
    }

    public String getPrice() {
        return price;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getChecked() {return checked; }

    public void setChecked(boolean bool) {
        this.checked = bool;
    }

    /*
     * Returns ItemType of specified int. Useful for debugging
     */
    public static ItemType translateFromInt(int input){
        ItemType[] typeArray = {ItemType.HOUSEHOLD,
                                ItemType.BAKERY,
                                ItemType.DAIRY,
                                ItemType.BULK_FOOD,
                                ItemType.MEAT_DELI,
                                ItemType.COOKING,
                                ItemType.PRODUCE,
                                ItemType.CLEANING,
                                ItemType.PHARMACY,
                                ItemType.ELECTRONICS,
                                ItemType.LITERATURE};
        return typeArray[input];
    }
}
