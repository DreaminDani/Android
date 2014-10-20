package net.desandoval.apps.shoppinglist.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.desandoval.apps.shoppinglist.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Stolen from Peter's Example in class on 2014.10.08..
 */
public class Item implements Serializable {

    public enum ItemType {
        HOUSEHOLD(0, R.drawable.landscape),
        BAKERY(1, R.drawable.landscape),
        DAIRY(2, R.drawable.landscape),
        BULK_FOOD(3, R.drawable.landscape),
        MEAT_DELI(4, R.drawable.landscape),
        COOKING(5, R.drawable.landscape),
        PRODUCE(6, R.drawable.landscape),
        CLEANING(7, R.drawable.landscape),
        PHARMACY(8, R.drawable.landscape),
        ELECTRONICS(9, R.drawable.landscape),
        LITERATURE(10, R.drawable.landscape);

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

    public ItemType itemType;
    private String itemName;
    private String description;
    private Date pickUpDate;
    private String store;
    private String price;
    private long id;

    public Item(ItemType ItemType, String itemName, String description, Date pickUpDate, String store, String price) {
        this.itemType = ItemType;
        this.itemName = itemName;
        this.description = description;
        this.store = store;
        this.pickUpDate = pickUpDate;
        this.price = price;
    }

    @Override
    public String toString() {
        if (this.price.isEmpty()) {
            return "<b>" + this.store + "  " + "</b>" + this.description;
        }else {
            return "<b>" + this.store + "  " + "</b>" + this.description + "$<b>" + this.price + "  " + "</b>";
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

    public ItemType getItemType() {
        return itemType;
    }

    public ItemType translateFromInt(int input){

    }

    public String toJSON() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("itemType", this.itemType);
            jsonObj.put("itemName", this.itemName);
            jsonObj.put("description", this.description);
            jsonObj.put("store", this.store);
            jsonObj.put("pickUpDate", this.pickUpDate);

            return jsonObj.toString();
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
